package com.shanzhu.em.controller;

import cn.hutool.core.date.DateUtil;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Cart;
import com.shanzhu.em.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车 控制层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 查询购物车信息
     *
     * @param id 购物车id
     * @return 购物车
     */
    @GetMapping("/{id}")
    public R<Cart> findUserCart(@PathVariable Long id) {
        return R.success(cartService.getById(id));
    }

    /**
     * 查询所有购物车
     *
     * @return 购物车
     */
    @GetMapping
    public R<Cart> findAllUserCart() {
        return R.success(cartService.list());
    }

    /**
     * 查询用户的购物车
     *
     * @param userId 用户id
     * @return 购物车信息
     */
    @GetMapping("/userid/{userId}")
    public R<List<Cart>> selectByUserId(@PathVariable Long userId) {
        return R.success(cartService.selectByUserId(userId));
    }

    /**
     * 保存用户购物车信息
     *
     * @param cart 购物车信息
     * @return 结果
     */
    @PostMapping
    public R<Void> save(@RequestBody Cart cart) {
        cart.setCreateTime(DateUtil.now());
        cartService.save(cart);
        return R.success();
    }

    /**
     * 更新购物车
     *
     * @param cart 购物车信息
     * @return 结果
     */
    @PutMapping
    public R<Void> update(@RequestBody Cart cart) {
        cartService.updateById(cart);
        return R.success();
    }

    /**
     * 删除购物车
     *
     * @param id 购物车信息
     * @return 结果
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        cartService.removeById(id);
        return R.success();
    }

}
