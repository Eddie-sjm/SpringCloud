package com.sjm.wlkg.controller;

import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import com.sjm.wlkg.pojo.SpecGroup;
import com.sjm.wlkg.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpecGroupController {

    @Autowired
    private SpecGroupService specGroupService;

    @GetMapping("/spec/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> getAll(@PathVariable("cid")Integer cid){
        List<SpecGroup> SpecGroups = specGroupService.getAll(cid);
        if(SpecGroups == null || SpecGroups.size() == 0){
            throw  new WlkgException(ExceptionEnums.LIST_NULL);
        }
        return ResponseEntity.ok(SpecGroups);
    }

    @PostMapping("/spec/group")
    public ResponseEntity<Integer> addSpecGroup(@RequestBody SpecGroup specGroup){
        int result = specGroupService.addSpecGroup(specGroup);
        if(result > 0){
            return ResponseEntity.ok(result);
        }
        throw new WlkgException(ExceptionEnums.Object_Null);
    }

    @PutMapping("/spec/group")
    public ResponseEntity<Integer> updateSpecGroup(@RequestBody SpecGroup specGroup){
        int result = specGroupService.updateSpecGroup(specGroup);
        if(result > 0){
            return ResponseEntity.ok(result);
        }
        throw new WlkgException(ExceptionEnums.Object_Null);
    }

    @DeleteMapping("/spec/group/{id}")
    public ResponseEntity<Integer> deleteSpecGroup(@PathVariable("id") Integer id){
        int result = specGroupService.deleteSpecGroup(id);
        if(result > 0){
            return ResponseEntity.ok(result);
        }
        throw new WlkgException(ExceptionEnums.Object_Null);
    }
}
