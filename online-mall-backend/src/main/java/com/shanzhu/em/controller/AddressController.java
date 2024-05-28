package com.shanzhu.em.controller;

import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Address;
import com.shanzhu.em.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户地址 控制层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * 查询用户的所有地址
     *
     * @param userId 用户id
     * @return 用户地址
     */
    @GetMapping("/{userId}")
    public R<Address> findUserAddress(@PathVariable Long userId) {
        return R.success(addressService.selectById(userId));
    }

    /**
     * 查询所有地址
     *
     * @return 地址
     */
    @GetMapping
    public R<List<Address>> findAllUserAddress() {
        return R.success(addressService.list());
    }

    /**
     * 保存地址
     *
     * @param address 地址
     * @return 结果
     */
    @PostMapping
    public R<Void> save(@RequestBody Address address) {
        addressService.save(address);
        return R.success();
    }

    /**
     * 更新地址
     *
     * @param address 地址
     * @return 结果
     */
    @PutMapping
    public R<Void> update(@RequestBody Address address) {
        addressService.updateById(address);
        return R.success();
    }

    /**
     * 删除地址
     *
     * @param id 用户id
     * @return 结果
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        addressService.removeById(id);
        return R.success();
    }

}
