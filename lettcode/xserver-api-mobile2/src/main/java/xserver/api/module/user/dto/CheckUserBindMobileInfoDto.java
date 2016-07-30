package xserver.api.module.user.dto;

import xserver.api.dto.BaseDto;
import xserver.api.module.vip.dto.VipInfoDto;

/**
 * 鉴定用户是否绑定手机
 * 如果未绑定则返回绑定手机的url
 */
public class CheckUserBindMobileInfoDto {
    private String userid;
    private String username;
    private Boolean isBind;// 是否绑定
    private String mobile;
    private String bindMobileUrl;// 如果未绑定，返回的url
    
    
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Boolean getIsBind() {
        return isBind;
    }
    public void setIsBind(Boolean isBind) {
        this.isBind = isBind;
    }
    public String getBindMobileUrl() {
        return bindMobileUrl;
    }
    public void setBindMobileUrl(String bindMobileUrl) {
        this.bindMobileUrl = bindMobileUrl;
    }
    
}
