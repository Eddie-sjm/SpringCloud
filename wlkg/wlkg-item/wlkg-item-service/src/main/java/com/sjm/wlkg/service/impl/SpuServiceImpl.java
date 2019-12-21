package com.sjm.wlkg.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import com.sjm.common.vo.PageResult;
import com.sjm.wlkg.mapper.SkuMapper;
import com.sjm.wlkg.mapper.SpuDetailMapper;
import com.sjm.wlkg.mapper.SpuMapper;
import com.sjm.wlkg.pojo.Brand;
import com.sjm.wlkg.pojo.Sku;
import com.sjm.wlkg.pojo.Spu;
import com.sjm.wlkg.pojo.Stock;
import com.sjm.wlkg.service.BrandService;
import com.sjm.wlkg.service.CategoryService;
import com.sjm.wlkg.service.SkuService;
import com.sjm.wlkg.service.SpuDetailService;
import com.sjm.wlkg.service.SpuService;
import com.sjm.wlkg.service.StockService;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuDetailService spuDetailService;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StockService stockService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public PageResult<Spu> getAll(String key, Boolean saleable, Integer page, Integer rows) {
        PageHelper.startPage(page,Math.min(rows,100));
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        if(saleable != null){
            criteria.orEqualTo("saleable",saleable);
        }
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        example.setOrderByClause("last_update_time desc");
        List<Spu> spus = this.spuMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(spus)){
            throw new WlkgException(ExceptionEnums.LIST_NULL);
        }

        PageInfo<Spu> pageInfo = new PageInfo<>(spus);
        PageResult<Spu> result = new PageResult<>();

        for (Spu spu : spus) {
            // 2、查询spu的商品分类名称,要查三级分类
            List<String> names = this.categoryService.queryNameByIds(
                    Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            // 将分类名称拼接后存入
            spu.setCname(StringUtils.join(names, "/"));

            // 3、查询spu的品牌名称
            Brand brand = this.brandService.selectByPrimaryKey(spu.getBrandId());
            spu.setBname(brand.getName());
        }

        result.setItems(spus);
        result.setTotal(pageInfo.getTotal());
        result.setTotalPage(Long.valueOf(pageInfo.getPages()));

//        List<Spu> spus1 = this.spuMapper.selectBySth();


        return result;
    }

    @Override
    public Integer changeSaleable(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu.getSaleable()==true){
            spu.setSaleable(false);
        }else {
            spu.setSaleable(true);
        }
        return spuMapper.updateByPrimaryKey(spu);
    }

    @Override
    @Transactional
    public void addGoods(Spu spu) {
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        this.spuMapper.insert(spu);

        //保存spu详情
        spu.getSpuDetail().setSpuId(spu.getId());
        this.spuDetailService.addSpuDetail(spu.getSpuDetail());

        saveSkuAndStock(spu.getSkus(),spu.getId());
        this.sendMessage(spu.getId(),"insert");
    }

    public void sendMessage(Long id,String type){
        try{
            amqpTemplate.convertAndSend("item."+type,id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void updateGoods(Spu spu) {
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skus = skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skus)){
            skuMapper.delete(sku);
            List<Long> ids = skus.stream().map(Sku::getId).collect(Collectors.toList());
            System.out.println(ids);
            stockService.deleteAll(ids);
        }

        spu.setCreateTime(null);
        spu.setLastUpdateTime(new Date());
        spu.setValid(null);
        spu.setSaleable(null);
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if(count!=1){
            throw new WlkgException(ExceptionEnums.Object_Null);
        }

        count = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if(count!=1){
            throw new WlkgException(ExceptionEnums.Object_Null);
        }

        saveSkuAndStock(spu.getSkus(),spu.getId());

        this.sendMessage(spu.getId(),"update");
    }

    @Override
    public Spu querySpuById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu == null){
            throw new WlkgException(ExceptionEnums.Object_Null);
        }
        spu.setSkus(skuService.querySkusById(id));
        spu.setSpuDetail(spuDetailService.querySpuDetailById(id));
        return spu;
    }

    public void saveSkuAndStock(List<Sku> skus, Long spuId){
        List<Stock> stocks = new ArrayList<>();
        for(Sku sku:skus){
            if(!sku.getEnable()){
                continue;
            }

            sku.setSpuId(spuId);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());

            this.skuMapper.insert(sku);

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock().intValue());
            stocks.add(stock);
        }
        stockService.addStocks(stocks);
    }
}
