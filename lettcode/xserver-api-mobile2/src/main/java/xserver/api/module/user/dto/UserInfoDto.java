package xserver.api.module.user.dto;

import xserver.api.dto.BaseDto;
import xserver.api.module.vip.dto.VipInfoDto;

/**
 * 用户信息
 * 关联其会员信息
 */
public class UserInfoDto extends BaseDto {
    private static final long serialVersionUID = -3153136660099880535L;
    private Long uid;// 用户 id
    private String userName;// 用户名
    private String status;// 用户状态
    private String nickName;// 用户昵称
    private String picture;// 用户头像
    private String ssouid;// 等于uid
    private String isVip;// 是否为vip
    private VipInfoDto vipInfo;// vip信息
    private Integer hotWordNum;// 乐词关注度
    private Integer starNum;// 明星关注数
    private String identify; // 是否进行了实名认证，认证："1"，未认证："0"
    

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

    public VipInfoDto getVipInfo() {
        return vipInfo;
    }

    public void setVipInfo(VipInfoDto vipInfo) {
        this.vipInfo = vipInfo;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSsouid() {
        return ssouid;
    }

    public void setSsouid(String ssouid) {
        this.ssouid = ssouid;
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public Integer getHotWordNum() {
        return hotWordNum;
    }

    public void setHotWordNum(Integer hotWordNum) {
        this.hotWordNum = hotWordNum;
    }

    public Integer getStarNum() {
        return starNum;
    }

    public void setStarNum(Integer starNum) {
        this.starNum = starNum;
    }

}
