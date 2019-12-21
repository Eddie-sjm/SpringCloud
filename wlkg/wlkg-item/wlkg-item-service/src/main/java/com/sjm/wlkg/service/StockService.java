package com.sjm.wlkg.service;

import com.sjm.wlkg.pojo.Stock;

import java.util.List;

public interface StockService {
    void addStocks(List<Stock> stocks);

    void deleteAll(List<Long> ids);
}
