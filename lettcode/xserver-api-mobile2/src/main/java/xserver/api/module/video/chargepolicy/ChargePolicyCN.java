package xserver.api.module.video.chargepolicy;

import xserver.lib.constant.StreamConstants;
import xserver.lib.constant.VideoConstants;

public class ChargePolicyCN implements ChargePolicy {

    private String playStream;

    private Boolean positive;

    private String payPlatform;

    private Integer isPay;

    private Integer categoryId;

    private Boolean isChargeVideo;

    public ChargePolicyCN(String playStream, String payPlatform, Integer isPay, Boolean positive,
            Boolean isChargeVideo, Integer categoryId) {
        this.isPay = isPay;
        this.playStream = playStream;
        this.payPlatform = payPlatform;
        this.positive = positive;
        this.isChargeVideo = isChargeVideo;
        this.categoryId = categoryId;
    }

    @Override
    public Integer getChargePolicy() {
        Integer chargeType = ChargeTypeConstants.chargePolicy.FREE;

        if ((positive && isMobPay())) {// 1.收费专辑下所有正片收费
            // 电视剧频道 只判断视频是否付费
            if (categoryId.intValue() == VideoConstants.Category.TV) {
                if (isChargeVideo) {
                    chargeType = ChargeTypeConstants.chargePolicy.CHARGE_YUAN_XIAN;
                }
            } else {
                chargeType = ChargeTypeConstants.chargePolicy.CHARGE_YUAN_XIAN;
            }

        } else if (playStream.startsWith("3d") || playStream.endsWith("_db") || playStream.endsWith("_dts")
                || StreamConstants.CHARGE_STREAM_SET.contains(playStream)) {// 2.收费码流都收费
            chargeType = ChargeTypeConstants.chargePolicy.CHARGE_BY_STREAM;
        }

        return chargeType;
    }

    @Override
    public boolean isMobPay() {
        if (this.isPay != null && this.isPay == 1 && this.payPlatform != null && payPlatform.contains("141003")) {
            return true;
        }
        return false;
    }
}
