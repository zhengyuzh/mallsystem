package com.shanzhu.em.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 轮播图
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Data
@TableName("carousel")
public class Carousel extends Model<Carousel> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 对应的商品
     */
    private Long goodId;

    /**
     * 轮播顺序
     */
    private Integer showOrder;

    /**
     * 商品名
     */
    @TableField(exist = false)
    private String goodName;

    /**
     * 图片
     */
    @TableField(exist = false)
    private String img;

}