package com.sjm.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(indexName = "goods",type = "docs",shards = 1,replicas = 0)
public class Goods {
    @Id
    private Long id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String all;

    @Field(type = FieldType.Keyword,index = false)
    private String subTitle;

    private Long brandId;
    private Long cid1;
    private Long cid2;// 2级分类id
    private Long cid3;// 3级分类id
    private Date createTime;// 创建时间
    private List<Long> price;// 价格

    @Field(type = FieldType.Keyword,index = false)
    private String skus;

    private Map<String,Object> specs;

}
