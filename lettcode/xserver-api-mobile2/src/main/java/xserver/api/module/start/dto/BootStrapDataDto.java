package xserver.api.module.start.dto;

import org.apache.commons.lang.StringUtils;

import xserver.api.constant.TerminalConstants;

public class BootStrapDataDto {

    private String status = TerminalConstants.RESPONSE_STATUS_FAILURE;
    private String message = TerminalConstants.RESPONSE_MESSAGE_FAILURE;
    private String terminalUuid = "";
    private String versionUrl = "";
    private String description = "";
    private String publishTime = "";
    private String versionName = "";
    private Integer cityLevel;// 一线城市、二线城市。。。
    private String sKey = "";

    public BootStrapDataDto(String status, String message, String terminalUuid, String versionUrl, String description,
            String publishTime, String versionName) {
        this.status = status;
        this.message = message;
        this.terminalUuid = terminalUuid;
        this.versionUrl = versionUrl;
        this.description = description;
        this.publishTime = publishTime;
        this.versionName = versionName;
    }

    public BootStrapDataDto() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTerminalUuid() {
        return terminalUuid;
    }

    public void setTerminalUuid(String terminalUuid) {
        this.terminalUuid = terminalUuid;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(Integer cityLevel) {
        this.cityLevel = cityLevel;
    }

    public String getsKey() {
        return sKey;
    }

    public void setsKey(String sKey) {
        this.sKey = sKey;
    }

    @Override
    public String toString() {
        return "NewVersion=" + (StringUtils.isEmpty(this.versionName) ? "nohigher" : this.versionName) + ",Status="
                + this.status;
    }
}
