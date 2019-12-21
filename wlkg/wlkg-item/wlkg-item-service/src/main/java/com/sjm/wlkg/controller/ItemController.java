package com.sjm.wlkg.controller;

import com.sjm.wlkg.pojo.Item;
import com.sjm.wlkg.service.ItemService;
import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<Item> saveItem(Item item) throws WlkgException{
        if (item.getPrice() == null) {
            throw new WlkgException(ExceptionEnums.PRICE_CANNOT_BE_NULL);
        }
        item = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.OK).body(item);
    }

}
