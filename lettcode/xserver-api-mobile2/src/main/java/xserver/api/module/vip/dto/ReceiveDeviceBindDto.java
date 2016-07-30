package xserver.api.module.vip.dto;

import xserver.api.dto.BaseDto;

/**
 * 领取（自带或赠送的）机卡时长请求结果封装类
 * @author
 */
public class ReceiveDeviceBindDto extends BaseDto {

    /**
     * 领取的赠送机卡时长对应会员有效期的到期时间
     */
    private String endTime;

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
