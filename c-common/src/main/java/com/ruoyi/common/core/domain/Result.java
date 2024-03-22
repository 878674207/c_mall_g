package com.ruoyi.common.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.enums.CustomResultErrorEnum;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description TOD0
 * author axx
 * Date 2023/8/15 9:35
 * Version 1.0
 **/
public class Result<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result() {
        this.code = ResultStatusCode.OK.getErrorCode();
        this.message = ResultStatusCode.OK.getErrorMsg();
    }


    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(ResultStatusCode status, T data) {
        this(status.getErrorCode(), status.getErrorMsg(), data);
    }

    public Result(ResultStatusCode status, String message, T data) {
        this(status.getErrorCode(), message,  data);
    }

    public Result(T data) {
        this(ResultStatusCode.OK, data);
    }

    public static Result sucessResult() {
        return sucessResult(null);
    }

    public static <T> Result<T> sucessResult(T data) {
        return new Result(ResultStatusCode.OK, data);
    }

    public static Result failResult() {
        return new Result(ResultStatusCode.SYSTEM_ERR, null);
    }

    public static Result failResult(int errorCode, String errorMsg) {
        return new Result(errorCode, errorMsg, null);
    }

    /**
     * 自定义异常回执信息
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> exceptionReturn(CustomResultErrorEnum xwlResultErrorEnum) {
        return new Result<>(xwlResultErrorEnum.getCode(), xwlResultErrorEnum.getMsg(), null);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == 0;
    }
}
