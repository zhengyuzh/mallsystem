package com.shanzhu.em.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.em.common.R;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.Order;
import com.shanzhu.em.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单 控制层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 查询用户订单 通过用户id
     *
     * @param userid 用户id
     * @return 订单信息
     */
    @GetMapping("/userid/{userid}")
    public R<List<Map<String, Object>>> findOrder(@PathVariable Integer userid) {
        return R.success(orderService.selectByUserId(userid));
    }

    /**
     * 查询用户订单 通过订单编号
     *
     * @param orderNo 订单编号
     * @return 订单信息
     */
    @GetMapping("/orderNo/{orderNo}")
    public R<Map<String, Object>> selectByOrderNo(@PathVariable String orderNo) {
        return R.success(orderService.selectByOrderNo(orderNo));
    }

    /**
     * 查询所有订单
     *
     * @return
     */
    @GetMapping
    public R<List<Order>> findAllOrder() {
        return R.success(orderService.list());
    }

    /**
     * 分页查询订单
     *
     * @param pageNum  页数
     * @param pageSize 页大小
     * @param orderNo  订单编号
     * @param state    状态
     * @return 订单
     */
    @GetMapping("/page")
    public R<IPage<Order>> findPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            String orderNo,
            String state
    ) {
        return R.success(orderService.selectByPage(pageNum, pageSize, orderNo, state));
    }

    /**
     * 保存订单
     *
     * @param order 订单信息
     * @return 订单编号
     */
    @PostMapping
    public R<String> save(@RequestBody Order order) {
        return R.success(orderService.saveOrder(order));
    }

    /**
     * 支付订单
     *
     * @param orderNo 订单编号
     * @return 支付结果
     */
    @GetMapping("/paid/{orderNo}")
    public R<Void> payOrder(@PathVariable String orderNo) {
        orderService.payOrder(orderNo);
        return R.success();
    }

    /**
     * 发货
     *
     * @param orderNo 订单编号
     * @return 结果
     */
    @GetMapping("/delivery/{orderNo}")
    public R<Void> delivery(@PathVariable String orderNo) {
        orderService.delivery(orderNo);
        return R.success();
    }

    /**
     * 确认收货
     *
     * @param orderNo 订单编号
     * @return 结果
     */
    @GetMapping("/received/{orderNo}")
    public R<Void> receiveOrder(@PathVariable String orderNo) {
        if (orderService.receiveOrder(orderNo)) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "确认收货失败");
        }
    }

    /**
     * 更新订单
     *
     * @param order 订单信息
     * @return 结果
     */
    @PutMapping
    public R<Void> update(@RequestBody Order order) {
        orderService.updateById(order);
        return R.success();
    }

    /**
     * 删除订单
     *
     * @param id 订单id
     * @return 结果
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        orderService.removeById(id);
        return R.success();
    }


}
