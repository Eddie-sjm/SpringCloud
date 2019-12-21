package com.sjm.search.test;

import com.sjm.common.vo.PageResult;
import com.sjm.search.WlkgSearchService;
import com.sjm.search.client.GoodsClient;
import com.sjm.search.pojo.Goods;
import com.sjm.search.repository.GoodsRepository;
import com.sjm.search.service.GoodsService;
import com.sjm.wlkg.pojo.Spu;
import org.elasticsearch.search.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WlkgSearchService.class)
public class GoodsRepositoryTest {
    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Test
    public void createIndex(){
        // 创建索引
        this.esTemplate.createIndex(Goods.class);
        // 配置映射
        this.esTemplate.putMapping(Goods.class);
    }


}
