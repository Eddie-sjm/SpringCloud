package com.sjm.wlkg.mapper;

import com.sjm.wlkg.pojo.Spu;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuMapper extends Mapper<Spu>, IdListMapper<Spu,Long> {

    @Select("SELECT s.*, " +
            "CONCAT(c1.`name`,'/',c2.name,'/',c3.name) cname , b.`name` bname " +
            "FROM tb_spu s " +
            "JOIN tb_category c1 ON s.cid1 = c1.id " +
            "JOIN tb_category c2 ON s.cid2 = c2.id " +
            "JOIN tb_category c3 ON s.cid3 = c3.id " +
            "JOIN tb_brand b ON s.brand_id = b.id")
    List<Spu> selectBySth();
}
