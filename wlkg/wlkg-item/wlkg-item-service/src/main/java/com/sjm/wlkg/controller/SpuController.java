package com.sjm.wlkg.controller;

import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import com.sjm.common.vo.PageResult;
import com.sjm.wlkg.pojo.Spu;
import com.sjm.wlkg.pojo.SpuDetail;
import com.sjm.wlkg.service.SpuDetailService;
import com.sjm.wlkg.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpuController {

    @Autowired
    private SpuService spuService;

    @Autowired
    private SpuDetailService spuDetailService;

    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> getAll( @RequestParam(value = "key",required = false)String key,
                                             @RequestParam(value = "saleable",required = false)Boolean saleable,
                                             @RequestParam(value = "page",defaultValue = "1")Integer page,
                                             @RequestParam(value = "rows",defaultValue = "5")Integer rows){
        PageResult<Spu> spus = spuService.getAll(key,saleable,page,rows);
        return ResponseEntity.ok(spus);
    }

    @PutMapping("/spu/{id}")
    public ResponseEntity<Integer> changeSaleable(@PathVariable("id")Long id){
        System.out.println(id);
        Integer result = spuService.changeSaleable(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/goods")
    public ResponseEntity<Void> addGoods(@RequestBody Spu spu){
        spuService.addGoods(spu);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id){
        SpuDetail spuDetail = spuDetailService.querySpuDetailById(id);
        if(spuDetail == null){
            throw new WlkgException(ExceptionEnums.Object_Null);
        }
        return ResponseEntity.ok(spuDetail);
    }

    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu){
        System.out.println(spu);
        spuService.updateGoods(spu);
        return ResponseEntity.ok(null);
    }

    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = this.spuService.querySpuById(id);
        return ResponseEntity.ok(spu);
    }

}
