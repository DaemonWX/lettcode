package xserver.api.module.vip.dto;

import xserver.api.dto.BaseDto;

/**
 * 手机校验DTO
 * @author
 */
public class CheckPhoneDto extends BaseDto {

    /**
     * 支付通道类型，31代表联通，34代表联通，35代表电信
     */
    private String paymentChannel;

    /**
     * 通用（普通）VIP包月价格
     */
    private String commonVipPrice;

    /**
     * 高级VIP包月价格
     */
    private String seniorVipPrice;

    public String getPaymentChannel() {
        return this.paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getCommonVipPrice() {
        return this.commonVipPrice;
    }

    public void setCommonVipPrice(String commonVipPrice) {
        this.commonVipPrice = commonVipPrice;
    }

    public String getSeniorVipPrice() {
        return this.seniorVipPrice;
    }

    public void setSeniorVipPrice(String seniorVipPrice) {
        this.seniorVipPrice = seniorVipPrice;
    }

}
