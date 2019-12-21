package com.sjm.wlkg.service;

import com.sjm.common.vo.PageResult;
import com.sjm.wlkg.pojo.Brand;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BrandService {
    List<Brand> getAll();

    PageResult<Brand> getAllBySth(Integer page, Integer rows, String sortBy, boolean desc, String key);

    void saveBrand(Brand brand, List<Long> cids);

    void updataBrand(Brand brand);

    int deleteBrand(Integer id);

    Brand selectByPrimaryKey(Long brandId);

    List<Brand> getBrandByCid(Long cid);

    Brand queryById(Long id);
}
