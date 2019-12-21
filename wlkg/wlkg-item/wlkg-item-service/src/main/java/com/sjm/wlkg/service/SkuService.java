package com.sjm.wlkg.service;

import com.sjm.wlkg.pojo.Sku;

import java.util.List;

public interface SkuService {
    void addSku(Sku sku);

    List<Sku> querySkusById(Long id);
}
