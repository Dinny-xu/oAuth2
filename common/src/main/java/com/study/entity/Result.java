package com.study.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
* @description 通用返回接口
* @author Xu·yan
* @date 2021/1/13 10:23 上午
*/
@Getter
@Setter
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 687732856090568050L;
    /**
     * 返回代码，200为成功
     */
    private String code;
    /**
     * 执行成功或失败
     */
    private Boolean success;
    /**
     * 返回的消息
     */
    private String msg;
    /**
     * 返回的数据
     */
    private T data;

    public Result(String code, String msg, Boolean success) {
        this.code = code;
        this.msg = msg;
        this.success = success;
    }

    public static Result ok() {
        return new Result(ResultEnum.OK.getCode(), ResultEnum.OK.getMsg(), true);
    }

    public static <T> Result<T> ok(T data) {
        return ok(data, ResultEnum.OK.getMsg());
    }

    public static <T> Result<T> ok(T data, String msg) {
        Result<T> result = new Result<>(ResultEnum.OK.getCode(), msg, true);
        result.setData(data);
        return result;
    }

    public static Result error() {
        return new Result(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg(), false);
    }

    public static Result error(String msg) {
        return new Result(ResultEnum.ERROR.getCode(), msg, false);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, false);
    }

}
