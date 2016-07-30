package xserver.api.module.play.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xserver.api.constant.DataConstants;
import xserver.lib.util.MessageUtils;

public class LiveUtil {

    public static final Map<String, String> liveStreamMap = new HashMap<String, String>();
    private static final Map<String, String> liveStreamCode2NameMap = new HashMap<String, String>();// TODO
                                                                                                    // 码流先写死
    public static final Map<String, String> clientIdMap = new HashMap<String, String>();

    public static final Map<String, Integer> liveTypeMap = new HashMap<String, Integer>();
    public static final Map<String, String> business2SplatidMap = new HashMap<String, String>();

    static {
        liveStreamMap.put("flv_350", "350");
        liveStreamMap.put("flv_1000", "1000");
        liveStreamMap.put("flv_1300", "1300");
        liveStreamMap.put("flv_720p", "720p");
        liveStreamMap.put("flv_1080p3m", "1080p");
        liveStreamMap.put("flv_1080p", "1080p");
        liveStreamMap.put("flv_4k", "4k");

        liveStreamCode2NameMap.put("350", "xserver.api.module.play.util.LiveUtil.stream.350");
        liveStreamCode2NameMap.put("1000", "xserver.api.module.play.util.LiveUtil.stream.1000");
        liveStreamCode2NameMap.put("1300", "xserver.api.module.play.util.LiveUtil.stream.1300");
        liveStreamCode2NameMap.put("720p", "xserver.api.module.play.util.LiveUtil.stream.720p");
        liveStreamCode2NameMap.put("1080p", "xserver.api.module.play.util.LiveUtil.stream.1080p");
        liveStreamCode2NameMap.put("4k", "xserver.api.module.play.util.LiveUtil.stream.4k");

        liveTypeMap.put("sports", 0);
        liveTypeMap.put("entertainment", 1);
        liveTypeMap.put("music", 2);
        liveTypeMap.put("other", 3);

        business2SplatidMap.put("3004", "1027");// 乐视音乐app Android - 超级手机音乐
        business2SplatidMap.put("101", "1036");// super live、领先版
        business2SplatidMap.put("3002", "1050918002");// 乐视体育APP内置版
    }

    public static String transTpLiveStream2PlayStream(String tpLiveStream) {
        return liveStreamMap.get(tpLiveStream);
    }

    public static String getLiveStreamName(String streamCode, String langcode) {
        if (StringUtils.isNotBlank(streamCode)) {
            String key = liveStreamCode2NameMap.get(streamCode);
            String streamName = MessageUtils.getMessage(key, langcode);
            if (StringUtils.isBlank(streamName)) {
                streamName = MessageUtils.getMessage(key, DataConstants.LANGCODE_CN);
            }
            return streamName;
        }
        return null;
    }
}
