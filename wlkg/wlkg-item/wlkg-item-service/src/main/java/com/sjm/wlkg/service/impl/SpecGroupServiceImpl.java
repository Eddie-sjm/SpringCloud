package com.sjm.wlkg.service.impl;

import com.sjm.wlkg.mapper.SpecGroupMapper;
import com.sjm.wlkg.pojo.SpecGroup;
import com.sjm.wlkg.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SpecGroupServiceImpl implements SpecGroupService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Override
    public List<SpecGroup> getAll(Integer cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return specGroupMapper.select(specGroup);
    }

    @Override
    public int addSpecGroup(SpecGroup specGroup) {
        specGroup.setName(specGroup.getName());
        return specGroupMapper.insert(specGroup);
    }

    @Override
    public int updateSpecGroup(SpecGroup specGroup) {
        specGroup.setName(specGroup.getName());
        return specGroupMapper.updateByPrimaryKey(specGroup);
    }

    @Override
    public int deleteSpecGroup(Integer id) {
        return specGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<SpecGroup> querySpecGroups(Long cid) {
        Example example = new Example(SpecGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cid",cid);
        return specGroupMapper.selectByExample(example);
    }
}
