package com.sjm.wlkg.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sjm.common.vo.PageResult;
import com.sjm.wlkg.mapper.BrandMapper;
import com.sjm.wlkg.pojo.Brand;
import com.sjm.wlkg.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public List<Brand> getAll() {
        return  brandMapper.selectAll();
    }

    @Override
    public PageResult<Brand> getAllBySth(Integer page, Integer rows, String sortBy, boolean desc, String key) {
        PageHelper.startPage(page,rows);

        Example example = new Example(Brand.class);
        if(StringUtils.isNotBlank(key)){
            Example.Criteria criteria = example.createCriteria();
             criteria.andLike("name","%"+key+"%");
             criteria.orLike("letter","%"+key+"%");

        }
        example.setOrderByClause(sortBy + " " + (desc?"desc":"asc"));

        List<Brand> brands = brandMapper.selectByExample(example);

        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        PageResult<Brand> result = new PageResult<>();

        result.setItems(brands);
        result.setTotal(pageInfo.getTotal());
        result.setTotalPage(Long.valueOf(pageInfo.getPages()));
        return result;
    }

    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        this.brandMapper.insertSelective(brand);
        for (Long cid : cids){
            this.brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    @Override
    public void updataBrand(Brand brand) {
        this.brandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public int deleteBrand(Integer id) {
        brandMapper.deleteBidById(id);
        brandMapper.deleteByPrimaryKey(id);
        return 0;
    }

    @Override
    public Brand selectByPrimaryKey(Long brandId) {
        return brandMapper.selectByPrimaryKey(brandId);
    }

    @Override
    public List<Brand> getBrandByCid(Long cid) {
        return brandMapper.getBrandByCid(cid);
    }

    @Override
    public Brand queryById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }


}
