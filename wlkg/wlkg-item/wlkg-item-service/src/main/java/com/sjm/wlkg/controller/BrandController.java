package com.sjm.wlkg.controller;

import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import com.sjm.common.vo.PageResult;
import com.sjm.wlkg.pojo.Brand;
import com.sjm.wlkg.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/brand/list")
    public List<Brand> getAll(){
        return brandService.getAll();
    }

    @GetMapping("/brand/page")
    public ResponseEntity<PageResult<Brand>> getAllBySth(@RequestParam(value = "page",defaultValue = "1")Integer page
                                                        ,@RequestParam(value = "rows",defaultValue = "5")Integer rows
                                                        ,@RequestParam(value = "sortBy",required = false)String sortBy
                                                        ,@RequestParam(value = "desc",defaultValue = "false")boolean desc
                                                        ,@RequestParam(value = "key",required = false)String key){
        PageResult<Brand> result = brandService.getAllBySth(page, rows, sortBy, desc, key);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/brand")
    public ResponseEntity<Void> saveBrand(Brand brand , @RequestParam("cids") List<Long> cids){
        System.out.println("我是新增");
        this.brandService.saveBrand(brand,cids);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/brand")
    public ResponseEntity<Void> updataBrand(Brand brand){
        System.out.println("我是修改");
        this.brandService.updataBrand(brand);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/brand/{id}")
    public ResponseEntity<Integer> deleteBrand(@PathVariable("id") Integer id ){
        System.out.println("我是修改");
        int result = this.brandService.deleteBrand(id);
        if(result >0){
            return ResponseEntity.ok(result);
        }
        throw new WlkgException(ExceptionEnums.Object_Null);
    }

    @GetMapping("/brand/cid/{cid}")
    public ResponseEntity<List<Brand>> getBrandByCid(@PathVariable("cid")Long cid){
        List<Brand> brands = brandService.getBrandByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            throw new WlkgException(ExceptionEnums.LIST_NULL);
        }
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/brand/{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(brandService.queryById(id));
    }
}
