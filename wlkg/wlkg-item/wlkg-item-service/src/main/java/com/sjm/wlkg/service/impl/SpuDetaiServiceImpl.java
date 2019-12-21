package com.sjm.wlkg.service.impl;

import com.sjm.wlkg.mapper.SpuDetailMapper;
import com.sjm.wlkg.pojo.SpuDetail;
import com.sjm.wlkg.service.SpuDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpuDetaiServiceImpl implements SpuDetailService {

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Override
    public void addSpuDetail(SpuDetail spuDetail) {
        this.spuDetailMapper.insert(spuDetail);
    }

    @Override
    public SpuDetail querySpuDetailById(Long id) {
        return spuDetailMapper.selectByPrimaryKey(id);
    }
}
