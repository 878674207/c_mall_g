package com.ruoyi.common.core.domain;

/**
 * @ClassName CommonException
 * @Description TOD0
 * author axx
 * Date 2023/9/1 10:14
 * Version 1.0
 **/
public class CommonException extends RuntimeException {
    private int resultCode;
    private String detailMessage;

    public CommonException() {
    }

    public CommonException(String detailMessage) {
        super(detailMessage);
        this.detailMessage = detailMessage;
    }

    public CommonException(ResultStatusCode resultStatusCode) {
        super(resultStatusCode.getErrorMsg());
        this.resultCode = resultStatusCode.getErrorCode();
        this.detailMessage = resultStatusCode.getErrorMsg();
    }

    public CommonException(String detailMessage, ResultStatusCode resultStatusCode) {
        this.resultCode = resultStatusCode.getErrorCode();
        this.detailMessage = detailMessage;
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getDetailMessage() {
        return this.detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}

