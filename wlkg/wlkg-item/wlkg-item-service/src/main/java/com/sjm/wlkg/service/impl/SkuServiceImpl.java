package com.sjm.wlkg.service.impl;

import com.sjm.wlkg.mapper.SkuMapper;
import com.sjm.wlkg.mapper.StockMapper;
import com.sjm.wlkg.pojo.Sku;
import com.sjm.wlkg.service.SkuService;
import com.sjm.wlkg.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Override
    public void addSku(Sku sku) {
        skuMapper.insert(sku);
    }

    @Override
    public List<Sku> querySkusById(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = skuMapper.select(sku);
        for(Sku sk : skus){
            sk.setStock(this.stockMapper.selectByPrimaryKey(sk.getId()).getStock());
        }
        return skus;
    }
}
