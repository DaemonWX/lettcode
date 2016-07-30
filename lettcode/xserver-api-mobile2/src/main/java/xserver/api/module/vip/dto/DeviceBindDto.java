package xserver.api.module.vip.dto;

import java.util.List;

import xserver.api.dto.BaseDto;

/**
 * 机卡绑定需求，鉴别该机器是否有未领取的机卡绑定套餐及套餐时长的结果封装，返回给客户端。
 * @author yikun
 * @since 2014-08-07
 */
public class DeviceBindDto extends BaseDto {

    /**
     * 机卡绑定套餐是否已激活，0--数据不可用，1--未激活，可领取；2--已激活，不可领取
     */
    private Integer isDeviceActive;

    /**
     * 绑定时长，单位：月，仅在isDeviceActive为1时有效
     */
    private Integer bindMonths;

    /**
     * 机卡绑定显示优先级，1--优先显示自带机卡绑定，2--优先显示赠送机卡绑定
     */
    private String priority;

    /**
     * 机卡赠送时长列表
     */
    private List<PresentDeviceBindDto> presentDeviceBinds;

    /**
     * 自带机卡绑定文案
     */
    private DeviceBindTextDto deviceBindText;

    /**
     * 赠送机卡绑定文案
     */
    private DeviceBindTextDto presentDeviceBindText;

    public Integer getIsDeviceActive() {
        return this.isDeviceActive;
    }

    public void setIsDeviceActive(Integer isDeviceActive) {
        this.isDeviceActive = isDeviceActive;
    }

    public Integer getBindMonths() {
        return this.bindMonths;
    }

    public void setBindMonths(Integer bindMonths) {
        this.bindMonths = bindMonths;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<PresentDeviceBindDto> getPresentDeviceBinds() {
        return this.presentDeviceBinds;
    }

    public void setPresentDeviceBinds(List<PresentDeviceBindDto> presentDeviceBinds) {
        this.presentDeviceBinds = presentDeviceBinds;
    }

    public DeviceBindTextDto getDeviceBindText() {
        return this.deviceBindText;
    }

    public void setDeviceBindText(DeviceBindTextDto deviceBindText) {
        this.deviceBindText = deviceBindText;
    }

    public DeviceBindTextDto getPresentDeviceBindText() {
        return this.presentDeviceBindText;
    }

    public void setPresentDeviceBindText(DeviceBindTextDto presentDeviceBindText) {
        this.presentDeviceBindText = presentDeviceBindText;
    }

}
