package com.ruoyi.common.core.domain;

import java.io.Serializable;
import com.ruoyi.common.constant.HttpStatus;

/**
 * 响应信息主体
 *
 * @author ruoyi
 */
public class ResponseResult<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 成功 */
    public static final int SUCCESS = HttpStatus.SUCCESS;

    /** 失败 */
    public static final int FAIL = HttpStatus.ERROR;

    private int code;

    private String msg;

    private T data;

    public static <T> ResponseResult<T> sucessResult()
    {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> ResponseResult<T> sucessResult(T data)
    {
        return restResult(data, SUCCESS, "操作成功");
    }

    public static <T> ResponseResult<T> sucessResult(T data, String msg)
    {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> ResponseResult<T> failResult()
    {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> ResponseResult<T> failResult(String msg)
    {
        return restResult(null, FAIL, msg);
    }

    public static <T> ResponseResult<T> failResult(T data)
    {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> ResponseResult<T> failResult(T data, String msg)
    {
        return restResult(data, FAIL, msg);
    }

    public static <T> ResponseResult<T> failResult(int code, String msg)
    {
        return restResult(null, code, msg);
    }

    private static <T> ResponseResult<T> restResult(T data, int code, String msg)
    {
        ResponseResult<T> apiResult = new ResponseResult<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public static <T> Boolean isError(ResponseResult<T> ret)
    {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(ResponseResult<T> ret)
    {
        return ResponseResult.SUCCESS == ret.getCode();
    }
}
