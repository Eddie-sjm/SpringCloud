package com.sjm.wlkg.api;

import com.sjm.common.vo.PageResult;
import com.sjm.wlkg.pojo.Sku;
import com.sjm.wlkg.pojo.Spu;
import com.sjm.wlkg.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {

    @GetMapping("/spu/page")
    PageResult<Spu> getAll(@RequestParam(value = "key",required = false)String key,
                           @RequestParam(value = "saleable",required = false)Boolean saleable,
                           @RequestParam(value = "page",defaultValue = "1")Integer page,
                           @RequestParam(value = "rows",defaultValue = "5")Integer rows);

    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable("id") Long id);

    @GetMapping("/sku/list")
    List<Sku> querySkusById(@RequestParam("id") Long id);

    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);
}
