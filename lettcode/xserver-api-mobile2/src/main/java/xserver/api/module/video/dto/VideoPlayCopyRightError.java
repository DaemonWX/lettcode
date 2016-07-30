package xserver.api.module.video.dto;

public class VideoPlayCopyRightError {
    private String errorCode;
    private String errorMsg;
    private Integer canFeedBack;// 是否提交反馈

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getCanFeedBack() {
        return canFeedBack;
    }

    public void setCanFeedBack(Integer canFeedBack) {
        this.canFeedBack = canFeedBack;
    }

}
