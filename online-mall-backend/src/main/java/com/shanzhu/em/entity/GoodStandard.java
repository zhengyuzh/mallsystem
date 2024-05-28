package com.shanzhu.em.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 商品规格
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Data
@TableName("good_standard")
public class GoodStandard extends Model<GoodStandard> {

    /**
     * 商品id
     */
    private Long goodId;

    /**
     * 商品规格
     */
    private String value;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 库存
     */
    private Integer store;

}
