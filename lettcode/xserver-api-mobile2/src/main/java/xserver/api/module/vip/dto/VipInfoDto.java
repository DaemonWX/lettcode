package xserver.api.module.vip.dto;

import xserver.api.dto.BaseDto;

/**
 * 用户会员账户相关信息
 */
public class VipInfoDto extends BaseDto {

    /**
     * 普通vip服务到期时间，格式"yyyy-MM-dd"
     */
    private String cancelTime;

    /**
     * 高级vip服务到期时间，格式"yyyy-MM-dd"
     */
    private String seniorCancelTime;

    /**
     * vip类型，0--非vip，1--普通vip，2--高级vip
     */
    private Integer vipType;

    /**
     * 会员类型文案
     */
    private String vipTypeName;

    /**
     * 状态信息 三种状态： 移动会员： 0：已开通连续包月 1： 已暂停连续包月 2：未开通连续包月，有签约 3：未开通连续包月，没有签约
     * 
     */
    private String mobileVipStatus;

    /**
     * 状态信息 三种状态： 全屏： 0：已开通连续包月 1： 已暂停连续包月 2：未开通连续包月，有签约 3：未开通连续包月，没有签约
     * 
     */
    private String screenVipStatus;

    
    public String getCancelTime() {
        return this.cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getSeniorCancelTime() {
        return this.seniorCancelTime;
    }

    public void setSeniorCancelTime(String seniorCancelTime) {
        this.seniorCancelTime = seniorCancelTime;
    }

    public Integer getVipType() {
        return this.vipType;
    }

    public void setVipType(Integer vipType) {
        this.vipType = vipType;
    }

    public String getVipTypeName() {
        return this.vipTypeName;
    }

    public void setVipTypeName(String vipTypeName) {
        this.vipTypeName = vipTypeName;
    }

	public String getMobileVipStatus() {
		return mobileVipStatus;
	}

	public void setMobileVipStatus(String mobileVipStatus) {
		this.mobileVipStatus = mobileVipStatus;
	}

	public String getScreenVipStatus() {
		return screenVipStatus;
	}

	public void setScreenVipStatus(String screenVipStatus) {
		this.screenVipStatus = screenVipStatus;
	}

}
