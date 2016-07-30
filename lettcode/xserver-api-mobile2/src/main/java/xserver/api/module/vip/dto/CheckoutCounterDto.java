package xserver.api.module.vip.dto;

import java.util.Collection;
import java.util.Map;

import xserver.api.dto.BaseDto;

/**
 * 收银台信息封装类，包含套餐信息（含套餐基本信息，套餐下优惠活动，套餐下支付渠道活动），收银台焦点图，收银台会员权益文案。
 * @author
 */
public class CheckoutCounterDto extends BaseDto {

    private Collection<VipPackageDto> vipPackages;

    /**
     * 优惠活动，key--活动id，value--活动信息
     */
    private Map<String, PaymentActivityDto> activities;

    /**
     * 焦点图信息
     */
    private CheckoutFocusDto focusInfo;

    /**
     * 会员权益文案，
     */
    private VipPrivilegeDto vipPrivilege;

    public CheckoutCounterDto() {
        super();
    }

    public Collection<VipPackageDto> getVipPackages() {
        return this.vipPackages;
    }

    public void setVipPackages(Collection<VipPackageDto> vipPackages) {
        this.vipPackages = vipPackages;
    }

    public Map<String, PaymentActivityDto> getActivities() {
        return this.activities;
    }

    public void setActivities(Map<String, PaymentActivityDto> activities) {
        this.activities = activities;
    }

    public VipPrivilegeDto getVipPrivilege() {
        return this.vipPrivilege;
    }

    public void setVipPrivilege(VipPrivilegeDto vipPrivilege) {
        this.vipPrivilege = vipPrivilege;
    }

    public CheckoutFocusDto getFocusInfo() {
        return this.focusInfo;
    }

    public void setFocusInfo(CheckoutFocusDto focusInfo) {
        this.focusInfo = focusInfo;
    }

}
