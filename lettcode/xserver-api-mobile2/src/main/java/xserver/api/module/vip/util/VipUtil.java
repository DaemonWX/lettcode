package xserver.api.module.vip.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import xserver.api.constant.DataConstants;

public class VipUtil {

    /**
     * 以序列号开头的字符串正则表达式，如“1.xxxx”
     */
    private static final String SERIAL_NUMBER_PREFIX_REGEX = "^[\\d*\\.]+";

    /**
     * 全部数字类型的正则表达式编译形式
     */
    public static final Pattern SERIAL_NUMBER_PREFIX_REGEX_PATTERN = Pattern.compile(SERIAL_NUMBER_PREFIX_REGEX);

    /**
     * 将天数转换为月数，按每月conversionFactor天计算，并四舍五入
     * @param day
     * @param conversionFactor
     * @return
     *         conversionFactor为0时返回0
     */
    public static int parseDayToMonth(int days, int daysOfPerMonth) {
        if (daysOfPerMonth == 0) {
            return 0;
        }
        BigDecimal day = new BigDecimal(String.valueOf(days));
        BigDecimal divisor = new BigDecimal(String.valueOf(daysOfPerMonth));
        BigDecimal month = day.divide(divisor, 0, BigDecimal.ROUND_HALF_UP);

        return month.intValue();
    }

    /**
     * 根据业务码和参数校验返回码，构造参数校验的详细错误码后缀，约定
     * <li>errorMsgCode在[1000,8999]为一般参数校验错误，在[9000,9999]为公共参数校验错误
     * <li>errorMsgCode在[9000,9999]时，businessCode修改为0，作为一个标志位，表示是公共参数校验错误
     * <p>
     * Examples:
     * <blockquote><pre>
     * VipUtil.parseErrorMsgCode(1,1000) returns "_1_1000"
     * VipUtil.parseErrorMsgCode(1,9000) returns "_0_9000"
     * </pre></blockquote>
     * @param businessCode
     *            业务码
     * @param errorMsgCode
     *            参数校验返回码
     * @return
     */
    public static String parseErrorMsgCode(int businessCode, int errorMsgCode) {
        // 业务码或者标志位非负，错误消息码区间在[1000,9999]
        if (businessCode < 0 || errorMsgCode < 1000 || errorMsgCode > 9999) {
            return "";// 返回空字符串，即使跟未细分前的的错误码拼接，也不会出现找不到错误消息的情况
        }
        if (errorMsgCode > 8999) {// 参数校验码大于8999时表示是公共参数校验错误
            businessCode = 0;// 此时0作为一个标志位，表示是公共参数校验问题
        }
        return DataConstants.COMMON_HYPHEN_UNDERLINE + businessCode + DataConstants.COMMON_HYPHEN_UNDERLINE
                + errorMsgCode;
    }
}
