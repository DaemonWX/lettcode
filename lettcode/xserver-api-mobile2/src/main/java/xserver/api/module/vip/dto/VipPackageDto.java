package xserver.api.module.vip.dto;

import java.util.Map;

import xserver.api.dto.BaseDto;

/**
 * 会员套餐包信息封装类
 * @author
 */
public class VipPackageDto extends BaseDto implements Comparable<VipPackageDto> {

    /**
     * 套餐id
     */
    private String id;

    /**
     * 产品包适用的终端类型，也即适用的会员类型
     */
    private String vipType;

    /**
     * 产品包名称
     */
    private String packageName;

    private String packageText;

    /**
     * 现价，单位：元，支持小数
     */
    private String currentPrice;

    /**
     * 原价，单位：元
     */
    private String originPrice;

    /**
     * 套餐时长，单位：月
     */
    private String duration;

    /**
     * 折扣信息，如“8.5”
     */
    private String discount;

    /**
     * 打折开始时间，格式为“yyyy-MM-dd HH:mm:ss”
     */
    private String discountStart;

    /**
     * 打折结束时间，格式为“yyyy-MM-dd HH:mm:ss”
     */
    private String discountEnd;

    /**
     * 是否使用当前价格（该字段仅在乐点支付时有效）
     */
    private Boolean useCurrentPrice;

    /**
     * 套餐描述
     */
    private String vipDesc;

    /**
     * 移动端图片
     */
    private String mobileImg;

    /**
     * 超级手机端图片
     */
    private String superMobileImg;

    /**
     * 套餐包上的活动id
     */
    private String packageActivityId;

    /**
     * 支付渠道上设置的针对房钱套餐的活动，key--paymentChannel，value--活动id
     */
    private Map<String, String> paymentChannelActivitys;

    /**
     * 连续包月文案信息：会员到期自动扣费
     */
    private String autoPayInfo;

    /**
     * 连续包月不同状态下的提示信息
     */
    private String autoPayMsg;

    @Override
    public int compareTo(VipPackageDto obj) {
        // 采用null在前策略
        if (obj == null || obj.id == null) {
            return 1;
        }
        return this.id.compareTo(obj.id);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVipType() {
        return this.vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCurrentPrice() {
        return this.currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getOriginPrice() {
        return this.originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDiscount() {
        return this.discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountStart() {
        return this.discountStart;
    }

    public void setDiscountStart(String discountStart) {
        this.discountStart = discountStart;
    }

    public String getDiscountEnd() {
        return this.discountEnd;
    }

    public void setDiscountEnd(String discountEnd) {
        this.discountEnd = discountEnd;
    }

    public Boolean getUseCurrentPrice() {
        return this.useCurrentPrice;
    }

    public void setUseCurrentPrice(Boolean useCurrentPrice) {
        this.useCurrentPrice = useCurrentPrice;
    }

    public String getVipDesc() {
        return this.vipDesc;
    }

    public void setVipDesc(String vipDesc) {
        this.vipDesc = vipDesc;
    }

    public String getMobileImg() {
        return this.mobileImg;
    }

    public void setMobileImg(String mobileImg) {
        this.mobileImg = mobileImg;
    }

    public String getSuperMobileImg() {
        return this.superMobileImg;
    }

    public void setSuperMobileImg(String superMobileImg) {
        this.superMobileImg = superMobileImg;
    }

    public String getPackageActivityId() {
        return this.packageActivityId;
    }

    public void setPackageActivityId(String packageActivityId) {
        this.packageActivityId = packageActivityId;
    }

    public Map<String, String> getPaymentChannelActivitys() {
        return this.paymentChannelActivitys;
    }

    public void setPaymentChannelActivitys(Map<String, String> paymentChannelActivitys) {
        this.paymentChannelActivitys = paymentChannelActivitys;
    }

    public String getPackageText() {
        return packageText;
    }

    public void setPackageText(String packageText) {
        this.packageText = packageText;
    }

    public String getAutoPayInfo() {
        return autoPayInfo;
    }

    public void setAutoPayInfo(String autoPayInfo) {
        this.autoPayInfo = autoPayInfo;
    }

    public String getAutoPayMsg() {
        return autoPayMsg;
    }

    public void setAutoPayMsg(String autoPayMsg) {
        this.autoPayMsg = autoPayMsg;
    }

}
