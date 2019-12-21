package com.sjm.wlkg.service.impl;

import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import com.sjm.wlkg.mapper.SpecParamMapper;
import com.sjm.wlkg.pojo.SpecGroup;
import com.sjm.wlkg.pojo.SpecParam;
import com.sjm.wlkg.service.SpecGroupService;
import com.sjm.wlkg.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SpecParamServiceImpl implements SpecParamService {

    @Autowired
    private SpecParamMapper specParamMapper;

    @Autowired
    private SpecGroupService specGroupService;

    @Override
    public int addSpecParam(SpecParam specParam) {
        return specParamMapper.insert(specParam);
    }

    @Override
    public int updateSpecParam(SpecParam specParam) {
        return specParamMapper.updateByPrimaryKey(specParam);
    }

    @Override
    public int deleteSpecParam(Long id) {
        return specParamMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam t = new SpecParam();
        t.setGroupId(gid);
        t.setCid(cid);
        t.setSearching(searching);
        t.setGeneric(generic);
        List<SpecParam> params = this.specParamMapper.select(t);
        if (CollectionUtils.isEmpty(params)) {
            throw new WlkgException(ExceptionEnums.LIST_NULL);
        }
        return params;


    }

    @Override
    public List<SpecGroup> querySpecsByCid(Long cid) {
        List<SpecGroup> groups = this.specGroupService.querySpecGroups(cid);

        System.out.println(groups);

        List<SpecParam> specParams = querySpecParams(null, cid, null, null);

        Map<Long, List<SpecParam>> map  = new HashMap<>();

        for(SpecParam param : specParams){

            System.out.println("param---:"+param);

            if(!map.containsKey(param.getGroupId())){

//                System.out.println("param---groupId:"+param.getGroupId());

                map.put(param.getGroupId(),new ArrayList<>());

            }
            map.get(param.getGroupId()).add(param);
//            System.out.println("map---groupId"+param.getGroupId()+":"+map.get(param.getGroupId()));
        }

//        System.out.println("map:"+map);

        for (SpecGroup specGroup : groups){

            for(Long key : map.keySet()){
                if(key == Long.valueOf(specGroup.getId())){
                    System.out.println("key"+key+"=="+"groupId"+specGroup.getId());
                    specGroup.setParams(map.get(key));
                }
            }
            System.out.println("map---groupId"+specGroup.getParams());


            System.out.println("specGroup中的params:"+specGroup.getParams());

        }
        return groups;
    }
}
