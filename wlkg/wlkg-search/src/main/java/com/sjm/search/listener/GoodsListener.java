package com.sjm.search.listener;

import com.sjm.search.service.GoodsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {

    @Autowired
    private GoodsService goodsService;

    @RabbitListener(
            bindings = @QueueBinding(
                   value = @Queue(
                           value = "wlg.create.index.queue",
                           durable = "true"
                   ),
                   exchange = @Exchange(
                           value = "wlkg.item.exchange",
                           ignoreDeclarationExceptions = "true",
                           type = ExchangeTypes.TOPIC
                   ),
                   key ={"item.insert","item.update"}
    ))

    public void listenCreate(Long id){
        if(id == null){
            return;
        }
        this.goodsService.createIndex(id);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    value = "wlkg.delete.index.queue",
                    durable = "true"
                    ),
                    exchange = @Exchange(
                            value = "wlkg.item.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = "item.delete"))
    public void listenDelete(Long id) {
        if (id == null) {
            return;
        }
        // 删除索引
        this.goodsService.deleteIndex(id);
    }

}