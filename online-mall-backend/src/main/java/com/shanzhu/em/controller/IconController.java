package com.shanzhu.em.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Icon;
import com.shanzhu.em.entity.User;
import com.shanzhu.em.service.IconService;
import com.shanzhu.em.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 图标 控制层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@RestController
@RequestMapping("/api/icon")
@RequiredArgsConstructor
public class IconController {

    private final IconService iconService;

    private final HttpServletRequest request;

    private final UserService userService;

    /**
     * 查询图标
     *
     * @param id 图标id
     * @return 图标
     */
    @GetMapping("/{id}")
    public R<Icon> findIcon(@PathVariable Long id) {
        return R.success(iconService.getById(id));
    }

    /**
     * 查询所有图标
     *
     * @return 图标
     */
    @GetMapping
    public R<List<Icon>> findAllIcon() {
        return R.success(iconService.getIconCategoryMapList());
    }

    /**
     * 保存图标
     *
     * @param icon 图标信息
     */
    @PostMapping
    public R<Void> save(@RequestBody Icon icon) {
        iconService.saveOrUpdate(icon);
        return R.success();
    }

    /**
     * 更新图标
     *
     * @param icon 图标信息
     */
    @PutMapping
    public R<Void> update(@RequestBody Icon icon) {
        iconService.updateById(icon);
        return R.success();
    }

    /**
     * 删除图标
     *
     * @param id 图标id
     * @return 结果
     */
    @GetMapping("/delete")
    public Map<String, Object> delete(@RequestParam("id") Long id) {
        return iconService.deleteById(id);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

}
