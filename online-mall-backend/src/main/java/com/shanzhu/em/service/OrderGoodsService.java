package com.shanzhu.em.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.entity.OrderGoods;
import com.shanzhu.em.mapper.OrderGoodsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单商品 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Service
public class OrderGoodsService extends ServiceImpl<OrderGoodsMapper, OrderGoods> {

}
