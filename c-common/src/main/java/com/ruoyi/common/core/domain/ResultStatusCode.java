package com.ruoyi.common.core.domain;

public enum ResultStatusCode {
    OK(200, "OK"),
    SYSTEM_ERR(30001, "System Error"),
    PERMISSION_DENIED(30002, "Permission Denied"),
    INVALID_CLIENT_ID(30003, "Invalid clientid"),
    INVALID_PASSWORD(30004, "User name or password is incorrect"),
    INVALID_CAPTCHA(30005, "Invalid captcha or captcha overdue"),
    INVALID_TOKEN(30006, "Invalid token"),
    UNEXPIRED_TOKEN(30007, "Token expired"),
    REGISTER_FAILED(30008, "User already exist"),
    RECORD_EXIST(30009, "Record exits"),
    NOTIFY_FAILED(30010, "Send failed"),
    PARAM_EMPTY(30011, "Param cannot be empty"),
    EMPTY_ROW(30012, "No records"),
    TIME_OUT(30013, "Connect time out"),
    SERVICE_NOT_FOUND(30014, "Service not found"),
    INVALID_OLD_PASSWORD(30015, "Invalid old password"),
    PASSWORD_NOT_INIT(30020, "Password not init"),
    PASSWORD_TOO_SIMPLE(30021, "Password is too simple"),
    INFO_NOT_COMPLETE(30022, "Information is not complete"),
    INVALID_REQUEST(30023, "Invalid request"),
    SMS_SEND_FAILED(30030, "Sms send failed"),
    CODE_NOT_MATCH(30031, "Verification code does not match"),
    SMS_USEAGE_LIMITED(30032, "Times of sms usage is limite（10）"),
    FILE_CONTENT_INCOMPLETE(30100, "File content is incomplete"),
    WRONG_DATA_FORMAT(30200, "Wrong data format"),
    WEBSERVICE_WRONG_RESPONSE(30300, "WebService request exception"),
    NEW_DEVICE(30400, "new device"),
    DEVICE_FIRST_LOGIN(30401, "device first login"),
    PICTURE_VERIFY_FAILED(30777, "picture verify failed"),
    AUTO_REGISTER(30778, "picture verify failed"),
    SIGNED_VERIFY(30779, "signed verify failed"),
    PHONE_VERIFY_FAILED(30780, "手机号格式错误"),
    CODE_VERIFY_FAILED(30781, "验证码错误"),
    CODE_COUNT_LIMIT(30782, "今天已达到发送短信验证码上限，请明天再试！");


    private int errorCode;
    private String errorMsg;

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private ResultStatusCode(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
