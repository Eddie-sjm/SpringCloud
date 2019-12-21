package com.sjm.wlkg.service;

import com.sjm.wlkg.pojo.SpuDetail;

public interface SpuDetailService {

    void addSpuDetail(SpuDetail spuDetail);

    SpuDetail querySpuDetailById(Long id);
}
