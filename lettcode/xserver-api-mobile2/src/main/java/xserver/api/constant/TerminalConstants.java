package xserver.api.constant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TerminalConstants {

    // ==================terminal的返回值================
    // 响应返回状态
    public static final String RESPONSE_STATUS_SUCCESS = "0";// 成功
    public static final String RESPONSE_STATUS_FAILURE = "1";// 失败
    public static final String RESPONSE_MESSAGE_SUCCESS = "业务正常";// 业务正常
    public static final String RESPONSE_MESSAGE_FAILURE = "业务异常";// 业务异常

    // Response Map key（向用户返回的response的key值）
    public static final String RESPONSE_KEY_STATUS = "status";
    public static final String RESPONSE_KEY_USERNAME = "username";
    public static final String RESPONSE_KEY_TERMINALUUID = "terminalUuid";
    public static final String RESPONSE_KEY_IDENTIFYCODE = "identifyCode";
    public static final String RESPONSE_KEY_MESSAGE = "message";
    public static final String RESPONSE_KEY_VERSIONURL = "versionUrl";
    public static final String RESPONSE_KEY_VERSIONID = "versionId";
    public static final String RESPONSE_KEY_OTHER = "other";
    public static final String RESPONSE_KEY_DESCRIPTION = "description";
    public static final String RESPONSE_KEY_VERSIONNAME = "versionName";
    public static final String RESPONSE_KEY_PLAYFORMATISTS = "playFormatIsTs";
    public static final String RESPONSE_KEY_BROADCASTID = "broadcastId";
    public static final String RESPONSE_KEY_CONFIG = "config";
    public static final String RESPONSE_KEY_ROM_MINIMUM = "romMinimum";
    public static final String RESPONSE_KEY_PUBLISH_TIME = "publishTime";
    public static final String RESPONSE_KEY_CURROM_MINIMUM = "CurRomMinimum";
    public static final String RESPONSE_KEY_POJIESTATUS = "pojieVersion";
    public static final String RESPONSE_KEY_FREE_VIP = "freeVIP"; // 终端可以领取会员试用的时长
    public static final String RESPONSE_KEY_BROADCAST_STATUS = "broadcastStatus";
    public static final String RESPONSE_KEY_BROADCAST_MESSAGE = "broadcastMessage";

    // 播放验证以及升级相关
    public static final String TERMINAL_VERSION_HAS_NEW = "TERMINAL_VERSION_HAS_NEW";
    public static final String TERMINAL_VERSION_UNNEED_UPGRADE = "TERMINAL_VERSION_UNNEED_UPGRADE";
    public static final String TERMINAL_AUTH_PASS = "TERMINAL_AUTH_PASS";
    public static final String TERMINAL_AUTH_UNPASS = "TERMINAL_AUTH_UNPASS";

    // 升级状态相关
    public static final String STATUS_FORCE = "5";
    public static final String STATUS_RECOMMEND = "6";
    public static final String STATUS_NORMAL = "7";

    public static final Map<String, String> MSG = new HashMap<String, String>();

    public static final Set<String> NOT_SUPPORT_DOLBY_DTS_SERIES = new HashSet<String>();
    static {
        MSG.put("TERMINAL_VERSION_HAS_NEW", "亲，有新版本了哦!");
        MSG.put("TERMINAL_VERSION_UNNEED_UPGRADE", "不用升级!");
        MSG.put("TERMINAL_AUTH_PASS", "业务正常");
        MSG.put("TERMINAL_AUTH_UNPASS", "业务异常");

        NOT_SUPPORT_DOLBY_DTS_SERIES.add("X800");// 超级手机x1(公开&联通)
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("X806");// 超级手机x1-电信版
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("X808");// 超级手机x1-移动版
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("X900");// 超级手机Max1(公开&联通)
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("X906");// 超级手机Max1-电信版
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("X908");// 超级手机Max1-移动版

        NOT_SUPPORT_DOLBY_DTS_SERIES.add("Le_1_Pro_default");
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("Le_1_Pro_china-telecom");
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("Le_1_Pro_china-mobile");
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("Le_1_Pro_whole-netcom");
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("Le_Max_default");
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("Le_Max_china-telecom");
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("Le_Max_china-mobile");
        NOT_SUPPORT_DOLBY_DTS_SERIES.add("Le_Max_whole-netcom");
    }
}
