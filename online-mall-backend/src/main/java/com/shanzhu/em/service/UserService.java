package com.shanzhu.em.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.constants.RedisConstants;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.form.LoginForm;
import com.shanzhu.em.entity.User;
import com.shanzhu.em.entity.vo.UserVo;
import com.shanzhu.em.exception.BizException;
import com.shanzhu.em.mapper.UserMapper;
import com.shanzhu.em.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 用户 服务层
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    RedisTemplate<String, User> redisTemplate;

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    public User selectByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
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
    public IPage<User> selectUserPage(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            String id,
            String username,
            String nickname
    ) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(id)) {
            userQueryWrapper.like("id", id);
        }
        if (StrUtil.isNotBlank(username)) {
            userQueryWrapper.like("username", username);
        }
        if (StrUtil.isNotBlank(nickname)) {
            userQueryWrapper.like("nickname", nickname);
        }
        userQueryWrapper.orderByDesc("id");

        return this.page(new Page<>(pageNum, pageSize), userQueryWrapper);
    }

    /**
     * 登录
     *
     * @param loginForm 登录表单
     * @return 用户信息
     */
    public UserVo login(LoginForm loginForm) {
        //查询用户
        User user = lambdaQuery()
                .eq(User::getUsername, loginForm.getUsername())
                .eq(User::getPassword, loginForm.getPassword())
                .one();

        //用户不存在
        if (user == null) {
            throw new BizException(Status.CODE_403, "用户名或密码错误");
        }

        //生成token
        String token = TokenUtils.genToken(user.getId().toString(), user.getUsername());

        //把用户存到redis中
        redisTemplate.opsForValue().set(RedisConstants.USER_TOKEN_KEY + token, user);

        //jwt不设置过期时间，只设置redis过期时间。
        redisTemplate.expire(RedisConstants.USER_TOKEN_KEY + token, RedisConstants.USER_TOKEN_TTL, TimeUnit.MINUTES);

        //把查到的user的一些属性赋值给userVo
        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
        userVo.setToken(token);

        return userVo;
    }

    /**
     * 注册
     *
     * @param loginForm 注册信息表单
     * @return 用户信息
     */
    public User register(LoginForm loginForm) {
        //查询用户
        User user = lambdaQuery()
                .eq(User::getUsername, loginForm.getUsername())
                .one();

        //用户已存在
        if (user != null) {
            throw new BizException(Status.CODE_403, "用户名已被使用");
        } else {
            user = new User();
            BeanUtils.copyProperties(loginForm, user);
            user.setNickname("新用户");
            user.setRole("user");
            //保存
            this.save(user);
            return user;
        }
    }

}
