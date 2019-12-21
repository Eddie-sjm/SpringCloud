package com.sjm.search.test;

import com.sjm.common.vo.PageResult;
import com.sjm.search.WlkgSearchService;
import com.sjm.search.client.GoodsClient;
import com.sjm.search.pojo.Goods;
import com.sjm.search.repository.GoodsRepository;
import com.sjm.search.service.GoodsService;
import com.sjm.wlkg.pojo.Spu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WlkgSearchService.class)
public class loadDataTest {
    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsService goodsService;

    @Test
    public void loadData(){
//        int size = 0;
//        int page = 1;
//        do {
//            //1.查询数据
//            PageResult<Spu> pages = goodsClient.getAll(null,true, page, 50);
//
//            size = pages.getItems().size();
//            //3.导入索引
//            List<Spu> list = pages.getItems();
//            List<Goods> goodsList = new ArrayList<>();
//            for (Spu spu : list) {
//                System.out.println(spu);
//                goodsList.add(goodsService.buildGoods(spu));
//            }
//            goodsRepository.saveAll(goodsList);
//            page ++;
//        } while (size == 50 );

        int page = 1;
        int rows = 50;
        int size = 0;
        do {
            // 查询分页数据
            PageResult<Spu> result = this.goodsClient.getAll(null,true,page,rows);
            List<Spu> spus = result.getItems();
            size = spus.size();
            // 创建Goods集合
            List<Goods> goodsList = new ArrayList<>();
            // 遍历spus
            for (Spu spu : spus) {
                try {
                    Goods goods = this.goodsService.buildGoods(spu);
                    System.out.println(goods);
                    goodsList.add(goods);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            System.out.println(goodsList);
            if (goodsList != null && goodsList.size() > 0){
                this.goodsRepository.saveAll(goodsList);
            }
            page++;
        } while (size == 100);
    }

}
