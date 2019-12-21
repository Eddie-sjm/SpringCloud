package com.sjm.wlkg.service;

import com.sjm.wlkg.pojo.SpecGroup;

import java.util.List;

public interface SpecGroupService {

    List<SpecGroup> getAll(Integer cid);

    int addSpecGroup(SpecGroup specGroup);

    int updateSpecGroup(SpecGroup specGroup);

    int deleteSpecGroup(Integer id);

    List<SpecGroup> querySpecGroups(Long cid);
}
