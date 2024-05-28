package com.shanzhu.em.controller;


import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shanzhu.em.common.R;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.form.LoginForm;
import com.shanzhu.em.entity.User;
import com.shanzhu.em.entity.vo.UserVo;
import com.shanzhu.em.service.UserService;
import com.shanzhu.em.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 控制层
 * <p>
 * '@CrossOrigin' 注解允许接口跨域
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/userinfo/{username}")
    public R<User> getUserInfoByName(@PathVariable String username) {
        return R.success(userService.selectByUsername(username));
    }

    /**
     * 获取用户id
     *
     * @return 用户id
     */
    @GetMapping("/userid")
    public long getUserId() {
        return TokenUtils.getCurrentUser().getId();
    }

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    @GetMapping("/user/")
    public R<List<User>> findAllUser() {
        return R.success(userService.list());
    }

    /**
     * 登录
     *
     * @param loginForm 登录表单
     * @return 用户信息
     */
    @PostMapping("/login")
    public R<UserVo> login(@RequestBody LoginForm loginForm) {
        return R.success(userService.login(loginForm));
    }

    /**
     * 注册
     *
     * @param loginForm 注册信息表单
     * @return 用户信息
     */
    @PostMapping("/register")
    public R<User> register(@RequestBody LoginForm loginForm) {
        return R.success(userService.register(loginForm));
    }

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @PostMapping("/user")
    public R<Void> save(@RequestBody User user) {
        if (BooleanUtil.isTrue(userService.saveOrUpdate(user))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "保存失败");
        }
    }

    /**
     * 删除用户信息
     *
     * @param id 用户id
     * @return 结果
     */
    @DeleteMapping("/user/{id}")
    public R<Void> deleteById(@PathVariable int id) {
        if (BooleanUtil.isTrue(userService.removeById(id))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "删除失败");
        }
    }

    /**
     * 批量删除用户
     *
     * @param ids 用户id列表
     * @return 结果
     */
    @PostMapping("/user/del/batch")
    public R deleteBatch(@RequestBody List<Integer> ids) {
        if (BooleanUtil.isTrue(userService.removeBatchByIds(ids))) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "删除失败");
        }
    }

    /**
     * 分页查询用户
     *
     * @param pageNum  页数
     * @param pageSize 页大学
     * @param id       用户id
     * @param username 用户名
     * @param nickname 用户昵称
     * @return 用户
     */
    @GetMapping("/user/page")
    public R<IPage<User>> findUserPage(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            String id,
            String username,
            String nickname
    ) {
        return R.success(userService.selectUserPage(pageNum, pageSize, id, username, nickname));
    }

}
