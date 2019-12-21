package com.sjm.wlkg.service.impl;

import com.sjm.wlkg.pojo.Item;
import com.sjm.wlkg.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ItemServiceImpl implements ItemService {
    @Override
    public Item saveItem(Item item) {
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}
