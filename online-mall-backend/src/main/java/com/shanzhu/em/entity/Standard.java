package com.shanzhu.em.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;

@TableName("good_standard")
public class Standard extends Model<Standard> {

    /**
      * 商品id 
      */
    private Integer goodId;

    /**
      * 商品规格 
      */
    private String value;

    /**
      * 该规格的价格 
      */
    private BigDecimal price;

    /**
      * 该规格的库存 
      */
    private Integer store;

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "Standard{" +
                "goodId=" + goodId +
                ", value='" + value + '\'' +
                ", price=" + price +
                ", store=" + store +
                '}';
    }
}