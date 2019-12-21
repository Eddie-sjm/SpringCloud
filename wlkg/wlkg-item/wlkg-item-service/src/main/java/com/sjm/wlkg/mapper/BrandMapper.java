package com.sjm.wlkg.mapper;

import com.sjm.wlkg.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    /**
     *
     * @param cid
     * @param bid
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    void insertCategoryBrand(@Param("cid") Long cid,@Param("bid") Integer bid);

//    @Update("update tb_category_brand set bid = #{bid} where cid = #{cid} ")
//    void updateCategoryBrand(@Param("cid")Long cid,@Param("bid") Integer bid);

    @Delete("delete from tb_category_brand where brand_id = #{id}")
    void deleteBidById(@Param("id")Integer id);

    @Select("SELECT b.* FROM tb_brand b " +
            "LEFT JOIN tb_category_brand cb " +
            "ON cb.brand_id = b.id " +
            "WHERE cb.category_id = #{cid} ")
    List<Brand> getBrandByCid(@Param("cid") Long cid);
}
