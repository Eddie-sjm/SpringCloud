package com.sjm.search.controller;

import com.sjm.common.vo.PageResult;
import com.sjm.search.pojo.Goods;
import com.sjm.search.service.GoodsService;
import com.sjm.search.vo.SearchRequest;
import org.elasticsearch.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SearchController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(this.goodsService.search(request));
    }

}
