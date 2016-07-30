package xserver.api.response;

import xserver.api.constant.ErrorCodeConstants;

/**
 * Response基类
 */
public abstract class BaseResponse {
    private Integer status = ErrorCodeConstants.RESPONSE_SUC_CODE;
    private String errorCode;

    private String errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
