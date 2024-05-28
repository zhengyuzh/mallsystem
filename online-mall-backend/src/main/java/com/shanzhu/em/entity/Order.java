package com.shanzhu.em.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Data
@TableName("t_order")
public class Order extends Model<Order> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 下单人id
     */
    private int userId;

    /**
     * 联系人
     */
    private String linkUser;

    /**
     * 联系电话
     */
    private String linkPhone;

    /**
     * 送货地址
     */
    private String linkAddress;

    /**
     * 状态
     */
    private String state;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 该订单包含的商品信息
     */
    @TableField(exist = false)
    private String goods;

    /**
     * 对应购物车id
     */
    @TableField(exist = false)
    private Long cartId;

}