package com.sjm.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import com.sjm.common.vo.PageResult;
import com.sjm.search.client.BrandClient;
import com.sjm.search.client.CategoryClient;
import com.sjm.search.client.GoodsClient;
import com.sjm.search.client.SpecificationClient;
import com.sjm.search.pojo.Goods;
import com.sjm.search.repository.GoodsRepository;
import com.sjm.search.utils.JsonUtils;
import com.sjm.search.vo.SearchRequest;
import com.sjm.wlkg.pojo.Brand;
import com.sjm.wlkg.pojo.Category;
import com.sjm.wlkg.pojo.Sku;
import com.sjm.wlkg.pojo.SpecParam;
import com.sjm.wlkg.pojo.Spu;
import com.sjm.wlkg.pojo.SpuDetail;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specClient;

    @Autowired
    private GoodsRepository goodsRepository;

    public Goods buildGoods(Spu spu){
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if(CollectionUtils.isEmpty(categories)) {
            throw new WlkgException(ExceptionEnums.LIST_NULL);
        }
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());

        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if(brand==null){
            throw new WlkgException(ExceptionEnums.Object_Null);
        }
        //搜素字段
        String all = spu.getTitle()+ StringUtils.join(names, " ")+brand.getName();

        //查询sku
        List<Sku> skus = goodsClient.querySkusById(spu.getId());
        if(CollectionUtils.isEmpty(skus)) {
            throw new WlkgException(ExceptionEnums.LIST_NULL);
        }
        //Set<Long> priceSet = skus.stream().map(Sku::getPrice).collect(Collectors.toSet());
        // 处理sku，仅封装id、价格、标题、图片，并获得价格集合

        List<Long> prices = new ArrayList<>();

        List<Map<String, Object>> skuList = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            //skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            skuMap.put("image", StringUtils.substringBefore(sku.getImages(), ","));
            skuList.add(skuMap);
        });

        //查询规格参数
        List<SpecParam> params = specClient.querySpecParam(null, spu.getCid3(), true, null);
        //查询商品详情
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spu.getId());

        //获取通用规格参数
        Map<Long, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        //获取特有规格参数
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        //定义spec对应的map
        HashMap<String, Object> specs = new HashMap<>();
        //对规格进行遍历，并封装spec，其中spec的key是规格参数的名称，值是商品详情中的值
        for (SpecParam param : params) {
            //key是规格参数的名称
            String key = param.getName();
            Object value = "";
            if (param.getGeneric()) {
                //参数是通用属性，通过规格参数的ID从商品详情存储的规格参数中查出值
                value = genericSpec.get(param.getId());
                if (param.getNumeric()) {
                    //参数是数值类型，处理成段，方便后期对数值类型进行范围过滤
                    value = chooseSegment(value.toString(), param);
                }
            } else {
                //参数不是通用类型
                value = specialSpec.get(param.getId());
            }
            value = (value == null ? "其他" : value);
            //存入map
            specs.put(key, value);
        }
        Goods goods = new Goods();

        goods.setId(spu.getId());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());

        goods.setAll(all);//
        goods.setPrice(prices);// 设置价格
        goods.setSkus(JsonUtils.serialize(skuList));//设置sku
        goods.setSpecs(specs);//设置规格
        return goods;
    }


    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult<Goods> search(SearchRequest request) {
        String key = request.getKey();
        if(StringUtils.isBlank(key)){
            return null;
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        queryBuilder.withQuery(QueryBuilders.matchQuery("all",key).operator(Operator.AND));

        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));

        Integer page = request.getPage();
        Integer size = request.getSize();
        queryBuilder.withPageable(PageRequest.of(page - 1,size));

        Page<Goods> pageInfo = this.goodsRepository.search(queryBuilder.build());

        List<Goods> goods = pageInfo.getContent();

        long total = pageInfo.getTotalElements();

        long totalPages = pageInfo.getTotalPages();

        return new PageResult<>(total,totalPages,goods);
    }

    public void createIndex(Long id) {
        Spu spu = goodsClient.querySpuById(id);


        Goods goods = buildGoods(spu);
        goodsRepository.save(goods);
    }

    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }

}
