package com.sjm.wlkg.controller;

import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import com.sjm.wlkg.pojo.SpecGroup;
import com.sjm.wlkg.pojo.SpecParam;
import com.sjm.wlkg.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpecParamContoller {

    @Autowired
    private SpecParamService specParamService;

    @PostMapping("/spec/param")
    public ResponseEntity<Integer> addSpecParam(@RequestBody SpecParam specParam){
        int result = specParamService.addSpecParam(specParam);
        if(result > 0){
            return ResponseEntity.ok(result);
        }
        throw new WlkgException(ExceptionEnums.Object_Null);
    }

    @PutMapping("/spec/param")
    public ResponseEntity<Integer> updateSpecParam(@RequestBody SpecParam specParam){
        int result = specParamService.updateSpecParam(specParam);
        if(result > 0){
            return ResponseEntity.ok(result);
        }
        throw new WlkgException(ExceptionEnums.Object_Null);
    }

    @DeleteMapping("/spec/param/{id}")
    public ResponseEntity<Integer> deleteSpecParam(@PathVariable("id") Long id){
        int result = specParamService.deleteSpecParam(id);
        if(result > 0){
            return ResponseEntity.ok(result);
        }
        throw new WlkgException(ExceptionEnums.Object_Null);
    }

    @GetMapping("/spec/params")
    public ResponseEntity<List<SpecParam>> querySpecParam(
            @RequestParam(value="gid", required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching,
            @RequestParam(value="generic", required = false) Boolean generic
    ){
        System.out.println("gid："+gid);
        System.out.println("cid："+cid);
        System.out.println("searching："+searching);
        System.out.println("generic："+generic);
        List<SpecParam> params = this.specParamService.querySpecParams(gid,cid,searching,generic);
        if(params == null || params.size() == 0){
            throw new WlkgException(ExceptionEnums.LIST_NULL);
        }
        return ResponseEntity.ok(params);
    }

    @GetMapping("/group")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@RequestParam("cid") Long cid){
        List<SpecGroup> specGroups = specParamService.querySpecsByCid(cid);
        return ResponseEntity.ok(specGroups);
    }


}
