package com.shanzhu.em.constants;

/**
 * Redis 常量
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
public class RedisConstants {

    /**
     * 用户token键值
     */
    public static final String USER_TOKEN_KEY = "user:token:";
    public static final Integer USER_TOKEN_TTL = 180;

    /**
     * 商品id键值
     */
    public static final String GOOD_ID_KEY = "good:id:";
    public static final Integer GOOD_ID_TTL = 30;
}
