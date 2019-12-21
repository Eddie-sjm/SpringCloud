package com.sjm.wlkg.service.impl;

import com.sjm.wlkg.mapper.StockMapper;
import com.sjm.wlkg.pojo.Stock;
import com.sjm.wlkg.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockMapper stockMapper;

    @Override
    public void addStocks(List<Stock> stocks) {
        stockMapper.insertList(stocks);
    }

    @Override
    public void deleteAll(List<Long> ids) {
        stockMapper.deleteByIdList(ids);
    }
}
