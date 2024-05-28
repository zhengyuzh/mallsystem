package com.shanzhu.em.utils;

import com.shanzhu.em.entity.User;

/**
 * 用户信息
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
public class UserHolder {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void saveUser(User user){
        userThreadLocal.set(user);
    }

    public static User getUser(){
        return userThreadLocal.get();
    }

    public static void removeUser(){
        userThreadLocal.remove();
    }

}
