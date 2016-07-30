package xserver.api.module;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import xserver.lib.util.RequestUtil;

/**
 * 接口通用参数
 */
public class CommonParam {
    private String pcode; // 产品编码
    private String devId; // 客户端设备唯一id
    private String terminalSeries; // 设备型号
    private String appVersion; // 版本号
    private String uid; // 用户id
    private String bsChannel; // 渠道号
    private String terminalApplication; // 应用
    private String langcode = "zh_cn"; // 语言代码
    private String wcode = "cn"; // 区域代码
    private String terminalBrand;// 品牌

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getTerminalSeries() {
        return terminalSeries;
    }

    public void setTerminalSeries(String terminalSeries) {
        this.terminalSeries = terminalSeries;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBsChannel() {
        return bsChannel;
    }

    public void setBsChannel(String bsChannel) {
        this.bsChannel = bsChannel;
    }

    public String getTerminalApplication() {
        return terminalApplication;
    }

    public void setTerminalApplication(String terminalApplication) {
        this.terminalApplication = terminalApplication;
    }

    public String getLangcode() {
        return langcode;
    }

    public void setLangcode(String langcode) {
        this.langcode = langcode;
    }

    public String getWcode() {
        return wcode;
    }

    public void setWcode(String wcode) {
        this.wcode = wcode;
    }

    public String getTerminalBrand() {
        return terminalBrand;
    }

    public void setTerminalBrand(String terminalBrand) {
        this.terminalBrand = terminalBrand;
    }

    public String getIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return RequestUtil.getClientIp(request);
    }

    @Override
    public String toString() {
        return "CommonParam [devId=" + devId + "]";
    }

}
