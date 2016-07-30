package xserver.api.module.vip;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xserver.lib.constant.ApplicationConstants;
import xserver.lib.tp.vip.VipTpConstant;
import xserver.lib.util.ApplicationUtils;

public class VipConstants {

    /**
     * 会员模块业务编码，1--用户，2--消费记录，3--观影券，4--手机校验，5--收银台，
     * 6--机卡绑定，7--领取机卡绑定，8--购买会员，9--用户订单，10--账户信息，11--用户直播券，12--恢复或者暂停连续包月
     */
    public static final int COMMON = 0;
    public static final int USERINFO = 1;
    public static final int CONSUMPTION_RECORD = 2;
    public static final int MOVIE_TICKET = 3;
    public static final int CHECK_PHONE = 4;
    public static final int CHECKOUT_COUNTER = 5;
    public static final int DEVICE_BIND = 6;
    public static final int RECEIVE_DEVICE_BIND = 7;
    public static final int PURCHASE_VIP = 8;
    public static final int CHECK_ORDER_STATUS = 9;
    public static final int USER_ACCOUNT = 10;
    public static final int USER_LIVE_TICKET = 11;
    public static final int ONEKEY_PAY_SWITCH_STATUS = 12;

    /**
     * 机卡绑定套餐是否已激活，0--数据不可用，1--未激活，可领取；2--已激活，不可领取
     */
    public static final int DEVICE_BIND_STATUS_ILLEGAL = 0;
    public static final int DEVICE_BIND_STATUS_UNACTIVATED = 1;
    public static final int DEVICE_BIND_STATUS_ACTIVATED = 2;

    public final static String VIP_PACKAGE_TYPE_NAME_PREFIX = "VIP_PACKAGE_TYPE_NAME_"; // 获取产品包列表名称

    /**
     * 自定义订单支付状态，0--未支付，1--支付成功，2--支付失败，3--订单失效或异常（如已退款等，不表示订单不存在）
     */
    public static final String ORDER_PAY_STATUS_NOT_PAY = "0";
    public static final String ORDER_PAY_STATUS_SUCCESS = "1";

    /**
     * 机卡绑定查询类型，0--查询所有（本机机卡和赠送机卡），1--本机机卡，2--赠送机卡
     */
    public static final String DEVICE_BIND_QUERY_TYPE_0 = "0";
    public static final String DEVICE_BIND_QUERY_TYPE_1 = "1";
    public static final String DEVICE_BIND_QUERY_TYPE_2 = "2";

    /**
     * 机卡绑定领取类型，1--领取自带机卡，2--领取赠送机卡
     */
    public static final int DEVICE_BIND_RECEIVE_TYPE_1 = 1;
    public static final int DEVICE_BIND_RECEIVE_TYPE_2 = 2;

    /**
     * 会员模块收银台权益文件Key
     */
    public final static String VIP_CHECKOUT_PRIVILEGE_BLOCK_USER_AGGREMENT_TEXT = "VIP_CHECKOUT_PRIVILEGE_BLOCK_USER_AGGREMENT_TEXT";

    /**
     * 会员模块机卡绑定Title
     */
    public final static String VIP_DEVICE_BIND_TEXT_TITLE = "VIP_DEVICE_BIND_TEXT_TITLE";
    public final static String VIP_DEVICE_BIND_TEXT_TITLE_PRESENT = "VIP_DEVICE_BIND_TEXT_TITLE_PRESENT";

    /**
     * 会员模块机卡绑定Content
     */
    public final static String VIP_DEVICE_BIND_TEXT_CONTENT = "VIP_DEVICE_BIND_TEXT_CONTENT";
    public final static String VIP_DEVICE_BIND_TEXT_CONTENT_PRESENT = "VIP_DEVICE_BIND_TEXT_CONTENT_PRESENT";

    /**
     * 会员模块机卡绑定Content
     */
    public final static String VIP_DEVICE_BIND_TEXT_TIPS = "VIP_DEVICE_BIND_TEXT_TIPS";
    public final static String VIP_DEVICE_BIND_TEXT_TIPS_PRESENT = "VIP_DEVICE_BIND_TEXT_TIPS_PRESENT";

    /**
     * 会员模块超级手机业务中，赠送给手机端适用的普通会员机卡时长PresentProductName
     */
    public final static String VIP_PRESENT_DEVICE_BIND_PRESENTPRODUCTNAME = "VIP_PRESENT_DEVICE_BIND_PRESENTPRODUCTNAME";

    /**
     * 会员模块
     */
    public final static String VIP_PAYMENTACTIVITY_LETVPOINTPAYTEXT = "VIP_PAYMENTACTIVITY_LETVPOINTPAYTEXT";
    public final static String VIP_COMMON_NULLTIME = "VIP_COMMON_NULLTIME";

    /**
     * 是否显示套餐包订单开关
     */
    public final static String VIP_SHOW_PACKAGE_ORDE_CANCEL_TIME_SWITCH_ON = "1";
    public final static boolean VIP_SHOW_PACKAGE_ORDER_CANCEL_TIME = StringUtils.equals(
            VIP_SHOW_PACKAGE_ORDE_CANCEL_TIME_SWITCH_ON,
            ApplicationUtils.get(ApplicationConstants.VIP_SHOW_PACKAGE_ORDER_CANCEL_TIME_SWITCH));

    /**
     * 是否显示赠送机卡时长开关
     */
    public final static String VIP_SHOW_PRESENT_DEVICE_BIND_SWITCH_ON = "1";
    public final static boolean VIP_SHOW_PRESENT_DEVICE_BIND = StringUtils.equals(
            VIP_SHOW_PRESENT_DEVICE_BIND_SWITCH_ON,
            ApplicationUtils.get(ApplicationConstants.VIP_DEVICE_BIND_SHOW_PRESENT_DEVICE_BIND_SWITCH));

    /**
     * 是否显示已过期的直播券开关，1--显示，0--不显示
     */
    public final static String VIP_SHOW_EXPRIED_LIVE_TICKET_SWITCH_ON = "1";
    public final static boolean VIP_FILTER_EXPRIED_LIVE_TICKET = !StringUtils.equals(
            VIP_SHOW_EXPRIED_LIVE_TICKET_SWITCH_ON,
            ApplicationUtils.get(ApplicationConstants.VIP_SHOW_EXPRIED_LIVE_TICKET_SWITCH));

    /**
     * 订单来源Map，key--订单来源，也即终端类型，value--国际化文本
     */
    private final static Map<String, String> orderFromMap = new HashMap<String, String>();

    /**
     * 订单状态Map，key--订单状态码，value--国际化文本
     */
    private final static Map<String, String> orderStatusMap = new HashMap<String, String>();

    public static final Map<String, String> PACKAGENAME_MAP = new HashMap<String, String>();

    static {
        orderFromMap.put(VipTpConstant.ORDER_FROM_PC_VIP, "VIP_ORDER_FROM_1");
        orderFromMap.put(VipTpConstant.ORDER_FROM_PC, "VIP_ORDER_FROM_2");
        orderFromMap.put(VipTpConstant.ORDER_FROM_TV, "VIP_ORDER_FROM_3");
        orderFromMap.put(VipTpConstant.ORDER_FROM_MOBILE, "VIP_ORDER_FROM_4");
        orderFromMap.put(VipTpConstant.ORDER_FROM_NETWORK_Cinemas, "VIP_ORDER_FROM_5");
        orderFromMap.put(VipTpConstant.ORDER_FROM_TV_48, "VIP_ORDER_FROM_6");
        orderFromMap.put(VipTpConstant.ORDER_FROM_CNTV, "VIP_ORDER_FROM_7");
        orderFromMap.put(VipTpConstant.ORDER_FROM_TV2, "VIP_ORDER_FROM_9");
        orderFromMap.put(VipTpConstant.ORDER_FROM_DEVICEBIND, "VIP_ORDER_FROM_13");
    }

    static {
        orderStatusMap.put(VipTpConstant.ORDER_STATUS_NOT_PAY, "VIP_ORDER_STATUS_0");
        orderStatusMap.put(VipTpConstant.ORDER_STATUS_VIDEO_PASTDUE, "VIP_ORDER_STATUS_-1");
        orderStatusMap.put(VipTpConstant.ORDER_STATUS_VIDEO_SUCCEED, "VIP_ORDER_STATUS_1");
        orderStatusMap.put(VipTpConstant.ORDER_STATUS_PACKAGE_PASTDUE, "VIP_ORDER_STATUS_-2");
        orderStatusMap.put(VipTpConstant.ORDER_STATUS_PACKAGE_SUCCEED, "VIP_ORDER_STATUS_2");
        orderStatusMap.put(VipTpConstant.ORDER_STATUS_LIVE_SUCCEED, "VIP_ORDER_STATUS_3");

        PACKAGENAME_MAP.put("1", "包月");
        PACKAGENAME_MAP.put("3", "包季");
        PACKAGENAME_MAP.put("6", "包半年");
        PACKAGENAME_MAP.put("12", "包年");
        PACKAGENAME_MAP.put("36", "包三年");
    }

    /**
     * 获取订单来源文案
     * @param orderFromCode
     * @return
     */
    public static String getOrderFromByCode(String orderFromCode) {
        if (orderFromMap.get(orderFromCode) != null) {
            return orderFromMap.get(orderFromCode);
        } else {
            return "未知";
        }
    }

    /**
     * 获取订单状态文案
     * @param orderStatusCode
     * @return
     */
    public static String getOrderStatusByCode(String orderStatusCode) {
        return orderStatusMap.get(orderStatusCode);
    }
    
    
    /**
     * 用户开通或暂停连续包月
     */
    public static final String AUTO_PAY_OPEN = "3";
    public static final String AUTO_PAY_CLOSE = "2";
    
    /**
     * 用户状态 开通、未开通、已暂停
     */
    public static final String AUTO_PAY_USER_OPEN = "0";
    public static final String AUTO_PAY_USER_PAUSE = "1";
    public static final String AUTO_PAY_USER_UNOPEN_SINGED = "2";
    public static final String AUTO_PAY_USER_UNOPEN_UNSINGED = "3";
    
    /**
     * 连续包月会员文案信息
     */
    public static final String AUTO_PAY_MONTHLY_INFO = "会员到期自动扣费";

    /**
     * 连续包月套餐包文案信息
     */
    public static final String MONTHLY_PACKAGE_INFO = "连续包月";

    /**
     * 控制连续包月是否在收银台展示
     */
    public static final Boolean MONTHLY_PACKAGE_CLOSE = true;
    
    /**
     * 连续包月一键签约错误码
     */
    public static final String AUTO_PAY_ONKEY_SIGN_FAILURE = "APOS001"; // 一键签约失败
    public static final String AUTO_PAY_USERS_INFO_FAILURE = "APUI001"; // 获取用户签约续费信息失败
    public static final String AUTO_PAY_SWITCH_INFO_FAILURE = "APSF001"; // 获取连续包月状态失败

}
