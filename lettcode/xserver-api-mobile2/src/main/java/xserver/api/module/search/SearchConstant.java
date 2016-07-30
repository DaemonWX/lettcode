package xserver.api.module.search;

import org.apache.commons.lang.StringUtils;

import xserver.api.constant.DataConstants;
import xserver.lib.util.MessageUtils;

public class SearchConstant {
    public final static String ERROR_CODE_SEARCH_PARAM_ERROR = "SSC0001"; // 错误码，参数不合法

    public final static Integer DATA_TYPE_ALUM = 1; // 搜索数据格式--专辑
    public final static Integer DATA_TYPE_VIDEO = 2; // 搜索数据格式--视频
    public final static Integer DATA_TYPE_STAR = 3; // 搜索数据格式--明星
    public final static Integer DATA_TYPE_SUBJECT = 4; // 搜索数据格式--专题

    private final static String LIVE_HOT_WORDS = "xserver.api.module.search.SearchConstant.LIVE_HOT_WORDS";
    // 直播默认搜索热词，目前无运营后台，暂定死
    // 填充角标时，保存模块数据时的通用key
    public static String PID_VID_ZID_KEY = "pid:{pid}_vid:{vid}_zid:{zid}";

    public static String getLiveHotWords(String langcode) {
        String hotWords = MessageUtils.getMessage(LIVE_HOT_WORDS, langcode);
        if (StringUtils.isBlank(hotWords)) {
            hotWords = MessageUtils.getMessage(LIVE_HOT_WORDS, DataConstants.LANGCODE_CN);
        }

        return hotWords;
    }
}
