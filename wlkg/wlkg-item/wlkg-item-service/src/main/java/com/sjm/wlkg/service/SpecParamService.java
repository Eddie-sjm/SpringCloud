package com.sjm.wlkg.service;

import com.sjm.wlkg.pojo.SpecGroup;
import com.sjm.wlkg.pojo.SpecParam;

import java.util.List;

public interface SpecParamService {
    int addSpecParam(SpecParam specParam);

    int updateSpecParam(SpecParam specParam);

    int deleteSpecParam(Long id);

    List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic);

    List<SpecGroup> querySpecsByCid(Long cid);
}
