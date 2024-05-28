package com.shanzhu.em.common;

import com.shanzhu.em.constants.Status;
import lombok.Data;

/**
 * 接口返回对象封装 - Result
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Data
public class R<T> {

    /**
     * 状态码
     */
    private String code;

    /**
     * 状态消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public static R success(){
        return new R(Status.CODE_200,null,null);
    }

    public static R success(Object data){
        return new R(Status.CODE_200,null,data);
    }

    public static R error(){
        return new R(Status.CODE_500,null,null);
    }

    public static R error(String code, String msg){
        return new R(code,msg,null);
    }

    public R code(String code){
        this.code = code;
        return this;
    }

    public R() {
    }

    public R(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
