package xserver.api.module.vip.builder;

import java.util.Locale;

import org.springframework.util.CollectionUtils;

import xserver.api.module.vip.VipConstants;
import xserver.api.module.vip.dto.PaymentActivityDto;
import xserver.lib.tp.vip.VipTpConstant;
import xserver.lib.tp.vip.response.PaymentActivityTpResponse.PaymentActivityInfo;
import xserver.lib.util.CalendarUtil;
import xserver.lib.util.MessageUtils;

/**
 * 付费活动转换器，将PaymentActivity转换为PaymentActivitypaymentActivity
 * @author yikun
 */
public class PaymentActivityBuilder {

    public static PaymentActivityDto build(PaymentActivityInfo paymentActivityInfo, Integer vipType, Locale locale) {
        PaymentActivityDto dto = null;
        if (isPaymentActivityAvailable(paymentActivityInfo, vipType)) {

            dto = new PaymentActivityDto();
            dto.setId(paymentActivityInfo.getActivityId());
            dto.setType(paymentActivityInfo.getType());
            dto.setMonthType(String.valueOf(paymentActivityInfo.getMonthType()));
            dto.setPayTypeList(paymentActivityInfo.getPayTypeList());
            dto.setDiscount(paymentActivityInfo.getDiscount());
            dto.setProlongDays(paymentActivityInfo.getProlongDays());
            dto.setIconText(paymentActivityInfo.getIconText());
            dto.setLableText(paymentActivityInfo.getLableText());
            dto.setBodyText(paymentActivityInfo.getBodyText());
            dto.setNeedLogin(paymentActivityInfo.getNeedLogin());
            dto.setAllowPayLepoint(paymentActivityInfo.getAllowPayLepoint());
            dto.setHasUserQuota(paymentActivityInfo.getHasUserQuota());
            dto.setQuota(paymentActivityInfo.getQuota());
            dto.setLeftQuota(paymentActivityInfo.getLeftQuota());
            dto.setUserQuota(paymentActivityInfo.getUserQuota());
            if (dto.getAllowPayLepoint() == null || Boolean.FALSE == dto.getAllowPayLepoint()) {
                dto.setLetvPointPayText(MessageUtils.getMessage(VipConstants.VIP_PAYMENTACTIVITY_LETVPOINTPAYTEXT,
                        locale));
            }
        }
        return dto;
    }

    /**
     * 判断PaymentActivity是否可用;
     * 活动必须适用TV端；必须说明活动总数限制，必须说明用户参与活动次数限制；时间限制必须合法
     * @param paymentActivity
     * @return boolean true--可用，false--不可用
     */
    private static boolean isPaymentActivityAvailable(PaymentActivityInfo paymentActivityInfo, Integer vipType) {

        return paymentActivityInfo != null && vipType != null
                && isPaymentActivityApplyToTerminal(paymentActivityInfo, vipType)
                && isPaymentActivityQuotaLegal(paymentActivityInfo)
                && isPaymentActivityUserQuotaLegal(paymentActivityInfo)
                && isPaymentActivityTimeLegal(paymentActivityInfo);
    }

    /**
     * 判断活动信息是否适用TV端；
     * terminals字符串列表中必须包含“141007”
     * @return
     */
    private static boolean isPaymentActivityApplyToTerminal(PaymentActivityInfo paymentActivityInfo, Integer vipType) {
        // String terminal = VipTpConstantUtils.getTermialByVipType(vipType);

        return !CollectionUtils.isEmpty(paymentActivityInfo.getTerminals())
                && paymentActivityInfo.getTerminals().contains(VipTpConstant.PRICE_ZHIFU_TERMINAL_MOBILE);
    }

    /**
     * 判断付费活动总数限制是否合法；
     * hasUserQuota字段必须给出；为true时userQuota必须给出且大于0
     * @param paymentActivity
     * @return
     */
    private static boolean isPaymentActivityQuotaLegal(PaymentActivityInfo paymentActivityInfo) {
        if (paymentActivityInfo.getQuota() == null || 0 == paymentActivityInfo.getQuota()
                || paymentActivityInfo.getQuota() < -1) {
            return false;
        }

        if (paymentActivityInfo.getQuota() > 0) {
            return (paymentActivityInfo.getLeftQuota() != null && paymentActivityInfo.getLeftQuota() > 0);
        }

        return true;
    }

    /**
     * 判断付费活动用户参数次数限制是否合法；
     * quota必须给出，只能为-1或其他正数值；大于0时，leftQuota必须给出，且大于0
     * @param paymentActivity
     * @return
     */
    private static boolean isPaymentActivityUserQuotaLegal(PaymentActivityInfo paymentActivityInfo) {
        if (paymentActivityInfo.getHasUserQuota() == null) {
            return false;
        }

        if (Boolean.TRUE == paymentActivityInfo.getHasUserQuota()) {
            return (paymentActivityInfo.getUserQuota() != null && paymentActivityInfo.getUserQuota() > 0);
        }

        return true;
    }

    /**
     * 判断付费活动时间限制是否合法
     * @param paymentActivity
     * @return
     */
    private static boolean isPaymentActivityTimeLegal(PaymentActivityInfo paymentActivityInfo) {
        if (paymentActivityInfo == null || paymentActivityInfo.getBeginTime() == null
                || paymentActivityInfo.getEndTime() == null || paymentActivityInfo.getIntervalBegin() == null
                || paymentActivityInfo.getIntervalEnd() == null) {
            return false;
        }

        // 活动时间、日期均需合法
        long now = System.currentTimeMillis();
        String hourMinuteStr = CalendarUtil.getDateStringFromLong(now, CalendarUtil.TIME_FORMAT);

        return (now >= paymentActivityInfo.getBeginTime() && now < paymentActivityInfo.getEndTime())
                && (hourMinuteStr.compareTo(paymentActivityInfo.getIntervalBegin()) >= 0 && hourMinuteStr
                        .compareTo(paymentActivityInfo.getIntervalEnd()) < 0);
    }
}
