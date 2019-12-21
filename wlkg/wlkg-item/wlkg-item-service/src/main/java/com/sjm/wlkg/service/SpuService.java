package com.sjm.wlkg.service;

import com.sjm.common.vo.PageResult;
import com.sjm.wlkg.pojo.Spu;

public interface SpuService {
    PageResult<Spu> getAll(String key, Boolean saleable, Integer page, Integer rows);

    Integer changeSaleable(Long id);

    void addGoods(Spu spu);

    void updateGoods(Spu spu);

    Spu querySpuById(Long id);
}
