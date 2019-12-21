package com.sjm.wlkg.controller;

import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import com.sjm.wlkg.pojo.Sku;
import com.sjm.wlkg.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SkuController {

    @Autowired
    private SkuService skuService;

    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkusById(@RequestParam("id") Long id){
        List<Sku> skus = skuService.querySkusById(id);
        if(CollectionUtils.isEmpty(skus)){
            throw new WlkgException(ExceptionEnums.LIST_NULL);
        }
        return ResponseEntity.ok(skus);
    }

}
