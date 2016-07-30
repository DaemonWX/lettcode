package xserver.api.module.vip.builder;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import xserver.api.module.vip.VipConstants;
import xserver.api.module.vip.dto.OrderInfoDto;
import xserver.lib.tp.vip.VipTpConstant;
import xserver.lib.tp.vip.VipTpConstantUtils;
import xserver.lib.tp.vip.response.UserOrderInfoTpResponse;
import xserver.lib.tp.vip.response.UserOrderSimpleInfoTpResponse;
import xserver.lib.util.CalendarUtil;
import xserver.lib.util.MessageUtils;
import xserver.lib.util.NumberFormatUtil;

/**
 * ConsumptionRecord转换器，将ConsumptionRecord转换为ConsumptionRecordDto
 * @author yikun
 */
public class ConsumptionRecordBuilder {

    public static OrderInfoDto build(final UserOrderInfoTpResponse consumptionRecord, final Locale locale) {
        // FacadeService facadeService
        if (consumptionRecord == null || locale == null) {
            return null;
        }

        OrderInfoDto dto = new OrderInfoDto();
        dto.setOrderId(consumptionRecord.getOrderId());
        dto.setOrderName(consumptionRecord.getOrderName());

        if (StringUtils.isNotBlank(consumptionRecord.getMoney())) {
            dto.setMoney(consumptionRecord.getMoney());
            dto.setMoneyDes("元");
        } else {
            dto.setMoney(consumptionRecord.getMoneyName());
            dto.setMoneyDes("乐点");
        }

        dto.setStatusName(consumptionRecord.getStatusName());
        dto.setStatusNameText(MessageUtils.getMessage(
                VipConstants.getOrderStatusByCode(consumptionRecord.getStatusName()), locale));
        dto.setCancelTime(getCancelTime(consumptionRecord));
        dto.setPayTime(CalendarUtil.getDateStringFromLong(consumptionRecord.getPayTime(),
                CalendarUtil.SHORT_DATE_FORMAT));

        return dto;
    }

    public static OrderInfoDto build(final UserOrderSimpleInfoTpResponse simpleConsumptionRecord, final Locale locale) {
        if (simpleConsumptionRecord == null || locale == null) {
            return null;
        }

        OrderInfoDto dto = new OrderInfoDto();
        dto.setOrderId(simpleConsumptionRecord.getId());
        dto.setOrderName(simpleConsumptionRecord.getOrderName());
        dto.setMoney(simpleConsumptionRecord.getMoney());
        dto.setMoneyDes(simpleConsumptionRecord.getMoneyDes());

        // 判断yuanxian返回值中订单失效时间是否合法
        boolean isCancelTimeValid = NumberFormatUtil.isNumeric(simpleConsumptionRecord.getCancelTime())
                && Long.parseLong(simpleConsumptionRecord.getCancelTime()) > 0;
        if (isCancelTimeValid) {
            // 单点支付新增逻辑
            String cancelTime = CalendarUtil.getDateStringFromLong(
                    Long.valueOf(simpleConsumptionRecord.getCancelTime()), CalendarUtil.SHORT_DATE_FORMAT);
            dto.setCancelTime(cancelTime);
        }

        boolean isPayTimeValid = NumberFormatUtil.isNumeric(simpleConsumptionRecord.getAddTime())
                && Long.parseLong(simpleConsumptionRecord.getAddTime()) > 0;
        if (isPayTimeValid) {
            // 单点支付新增逻辑
            String payTime = CalendarUtil.getDateStringFromLong(Long.valueOf(simpleConsumptionRecord.getAddTime()),
                    CalendarUtil.SHORT_DATE_FORMAT);
            dto.setPayTime(payTime);
        }

        if (VipTpConstant.ORDER_AVAILABLE_STATUS_EFFECTIVE.equals(simpleConsumptionRecord.getStatus())) {
            dto.setStatusNameText(MessageUtils.getMessage(
                    VipConstants.getOrderStatusByCode(VipTpConstant.ORDER_STATUS_PACKAGE_SUCCEED), locale));
        } else {
            dto.setStatusNameText(MessageUtils.getMessage(
                    VipConstants.getOrderStatusByCode(VipTpConstant.ORDER_STATUS_NOT_PAY), locale));
        }

        return dto;
    }

    /**
     * 获取还在有效期内的单点影片剩余有效时间，格式为"天:时:分"，使用英文冒号（:）分割
     * @param leftEffectiveTimeMillis
     * @return
     */
    private static String getFilmLeftEffectiveTime(long leftEffectiveTimeMillis) {
        long leftEffectiveTime = leftEffectiveTimeMillis / 1000;
        // int days = (int) (leftEffectiveTime /
        // CalendarUtil.SECONDS_OF_PER_DAY);
        int days = 0;
        // int minutes = (int) ((leftEffectiveTime %
        // CalendarUtil.SECONDS_OF_PER_DAY) / CalendarUtil.SECONDS_OF_PER_HOUR);
        int minutes = (int) (leftEffectiveTime / CalendarUtil.SECONDS_OF_PER_HOUR);
        int seconds = (int) ((leftEffectiveTime % CalendarUtil.SECONDS_OF_PER_HOUR) / CalendarUtil.SECONDS_OF_PER_MINUTE);
        StringBuffer timeBuffer = new StringBuffer();

        timeBuffer.append(NumberFormatUtil.getDecimalNubmerFromInt(days, NumberFormatUtil.COMMON_DATE_AND_TIME_FORMAT))
                .append(CalendarUtil.TIME_FORMAT_SEPARATOR);
        timeBuffer.append(
                NumberFormatUtil.getDecimalNubmerFromInt(minutes, NumberFormatUtil.COMMON_DATE_AND_TIME_FORMAT))
                .append(CalendarUtil.TIME_FORMAT_SEPARATOR);
        timeBuffer.append(NumberFormatUtil.getDecimalNubmerFromInt(seconds,
                NumberFormatUtil.COMMON_DATE_AND_TIME_FORMAT));

        return timeBuffer.toString();
    }

    /**
     * 获取订单有效期截至时间
     * @param consumptionRecord
     * @return
     */
    private static String getCancelTime(UserOrderInfoTpResponse consumptionRecord) {
        String cancelTimeStr = null;
        boolean isPackageOrder = VipTpConstantUtils.isPackageOrder(consumptionRecord.getOrderType());
        Long cancelTime = consumptionRecord.getCancelTime();
        boolean isCancelTimeValid = cancelTime != null && cancelTime > 0;

        // 截至时间有效 并且 （不是套餐订单 或者 是套餐订单但是要显示截至时间）
        if (isCancelTimeValid && (!isPackageOrder || VipConstants.VIP_SHOW_PACKAGE_ORDER_CANCEL_TIME)) {
            cancelTimeStr = CalendarUtil.getDateStringFromLong(cancelTime, CalendarUtil.SHORT_DATE_FORMAT);
        }
        return cancelTimeStr;
    }
}
