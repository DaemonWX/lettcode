package xserver.api.module.channel.dto;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xserver.api.module.channel.ChannelConstant;
import xserver.lib.constant.ApplicationConstants;
import xserver.lib.util.ApplicationUtils;

public class ChannelSkip {
    private final static String SEARCH_URL = ApplicationUtils.get(ApplicationConstants.MOBILE_LEAD_SEARCH_URL);
    private final static String REC_URL = ApplicationUtils.get(ApplicationConstants.MOBILE_LEAD_REC_URL);
    private final static String TOP_URL = ApplicationUtils.get(ApplicationConstants.MOBILE_LEAD_TOP_URL);
    public static Map<Integer, String> CHANNEL_SKIP_SEARCH = new HashMap<Integer, String>(); // 跳搜索的频道
    public static Map<String, String> CHANNEL_TOP_PAGEID = new HashMap<String, String>(); // 各频道排行的pageid
    public static Map<String, String> CHANNEL_TOP_DATA_TYPE = new HashMap<String, String>(); // 各频道排行的pageid
    public static Map<Integer, String> CHANNEL_MAP = new HashMap<Integer, String>();
    public static Map<String, String> CHANNEL_CMS_FILTER_MAP = new HashMap<String, String>(); // cms筛选跳转配置条件与检索的映射

    static {
        CHANNEL_SKIP_SEARCH.put(1012,
                "filter=playStreamFeatures:4k;dt:1;cg:1;or:1;vt=180001&channelid=1012&page=1&pageSize=50"); // 4K频道
        CHANNEL_SKIP_SEARCH.put(1013,
                "filter=playStreamFeatures:1080p;dt:1;cg:1;or:1;vt=180001&channelid=1013&page=1&pageSize=50"); // 1080p
        CHANNEL_SKIP_SEARCH.put(1014,
                "filter=playStreamFeatures:3d;dt:1;cg:1;or:1;vt=180001&channelid=1014&page=1&pageSize=50"); // 3D频道
        // CHANNEL_SKIP_SEARCH.put(1023,
        // "filter=playStreamFeatures:db;dt:1;cg:1;or:1;vt=180001&channelid=1023&page=1&pageSize=50");//
        // 影院声
        CHANNEL_SKIP_SEARCH.put(1024,
                "filter=playStreamFeatures:2k;dt:1;cg:1;or:1;vt=180001&channelid=1024&page=1&pageSize=50"); // 2k频道

        CHANNEL_TOP_PAGEID.put("1002921117", "dayTVPlay.jsn"); // 电视剧排行
        CHANNEL_TOP_PAGEID.put("1002921172", "dayFilmPlay.jsn"); // 电影排行
        CHANNEL_TOP_PAGEID.put("1002921160", "dayVarPlay.jsn"); // 综艺排行
        CHANNEL_TOP_PAGEID.put("1002921094", "dayEntPlay.jsn"); // 娱乐排行
        CHANNEL_TOP_PAGEID.put("1002920002", "dayMusicPlay.jsn"); // 音乐排行
        CHANNEL_TOP_PAGEID.put("1002948836", "daySportPlay.jsn"); // 体育排行
        CHANNEL_TOP_PAGEID.put("1002921053", "dayDocPlay.jsn"); // 记录片排行
        CHANNEL_TOP_PAGEID.put("1002950208", "dayComicPlay.jsn"); // 动漫排行
        CHANNEL_TOP_PAGEID.put("1002921096", "dayFinancePlay.jsn"); // 财经排行
        CHANNEL_TOP_PAGEID.put("1002949323", "dayCarPlay.jsn"); // 汽车排行
        CHANNEL_TOP_PAGEID.put("1002949324", "dayFashionPlay.jsn"); // 风尚排行
        CHANNEL_TOP_PAGEID.put("1002949326", "dayTravelPlay.jsn"); // 旅游排行

        CHANNEL_TOP_DATA_TYPE.put("dayTVPlay.jsn", ChannelConstant.DATA_TYPE_ALBUM); // 电视剧排行
        CHANNEL_TOP_DATA_TYPE.put("dayFilmPlay.jsn", ChannelConstant.DATA_TYPE_ALBUM); // 电影排行
        CHANNEL_TOP_DATA_TYPE.put("dayVarPlay.jsn", ChannelConstant.DATA_TYPE_VIDEO); // 综艺排行
        CHANNEL_TOP_DATA_TYPE.put("dayEntPlay.jsn", ChannelConstant.DATA_TYPE_VIDEO); // 娱乐排行
        CHANNEL_TOP_DATA_TYPE.put("dayMusicPlay.jsn", ChannelConstant.DATA_TYPE_VIDEO); // 音乐排行
        CHANNEL_TOP_DATA_TYPE.put("daySportPlay.jsn", ChannelConstant.DATA_TYPE_VIDEO); // 体育排行
        CHANNEL_TOP_DATA_TYPE.put("dayDocPlay.jsn", ChannelConstant.DATA_TYPE_VIDEO); // 纪录片排行
        CHANNEL_TOP_DATA_TYPE.put("dayComicPlay.jsn", ChannelConstant.DATA_TYPE_ALBUM); // 动漫排行
        CHANNEL_TOP_DATA_TYPE.put("dayFinancePlay.jsn", ChannelConstant.DATA_TYPE_VIDEO); // 财经排行
        CHANNEL_TOP_DATA_TYPE.put("dayCarPlay.jsn", ChannelConstant.DATA_TYPE_VIDEO); // 汽车排行
        CHANNEL_TOP_DATA_TYPE.put("dayFashionPlay.jsn", ChannelConstant.DATA_TYPE_VIDEO); // 风尚排行
        CHANNEL_TOP_DATA_TYPE.put("dayTravelPlay.jsn", ChannelConstant.DATA_TYPE_VIDEO); // 旅游排行

        CHANNEL_MAP.put(1, "CHANNEL.NAME.FILM");
        CHANNEL_MAP.put(2, "CHANNEL.NAME.TV");
        CHANNEL_MAP.put(3, "CHANNEL.NAME.ENT");
        CHANNEL_MAP.put(4, "CHANNEL.NAME.SPORT");
        CHANNEL_MAP.put(5, "CHANNEL.NAME.CARTOON");
        CHANNEL_MAP.put(9, "CHANNEL.NAME.MUSIC");
        CHANNEL_MAP.put(11, "CHANNEL.NAME.VARITY");
        CHANNEL_MAP.put(14, "CHANNEL.NAME.CAR");
        CHANNEL_MAP.put(16, "CHANNEL.NAME.DFILM");
        CHANNEL_MAP.put(20, "CHANNEL.NAME.FASHION");
        CHANNEL_MAP.put(22, "CHANNEL.NAME.CAIJING");
        CHANNEL_MAP.put(23, "CHANNEL.NAME.TRAVELLING");
        CHANNEL_MAP.put(34, "CHANNEL.NAME.PARENT");
        CHANNEL_MAP.put(35, "CHANNEL.NAME.PET");
        CHANNEL_MAP.put(36, "CHANNEL.NAME.ADV");
        CHANNEL_MAP.put(104, "CHANNEL.NAME.GAME");
        CHANNEL_MAP.put(1000, "CHANNEL.NAME.VIP");
        CHANNEL_MAP.put(1004, "CHANNEL.NAME.NBA");
        CHANNEL_MAP.put(1008, "CHANNEL.NAME.HOMEMADE");
        CHANNEL_MAP.put(1009, "CHANNEL.NAME.ZIXUN");
        CHANNEL_MAP.put(1012, "CHANNEL.NAME.4K");
        CHANNEL_MAP.put(1013, "CHANNEL.NAME.1080P");
        CHANNEL_MAP.put(1014, "CHANNEL.NAME.3D");
        CHANNEL_MAP.put(1017, "CHANNEL.NAME.USTV");
        CHANNEL_MAP.put(1019, "CHANNEL.NAME.YINGCHAO");
        CHANNEL_MAP.put(1020, "CHANNEL.NAME.TECH");
        CHANNEL_MAP.put(1021, "CHANNEL.NAME.EDU");
        CHANNEL_MAP.put(1023, "CHANNEL.NAME.DTS");
        CHANNEL_MAP.put(1024, "CHANNEL.NAME.2K");

        CHANNEL_CMS_FILTER_MAP.put("3", "sc"); // 影片分类
        CHANNEL_CMS_FILTER_MAP.put("5", "area"); // 地区
        CHANNEL_CMS_FILTER_MAP.put("7", "language"); // 语言
        CHANNEL_CMS_FILTER_MAP.put("18", "vtp"); // 语言
        CHANNEL_CMS_FILTER_MAP.put("179", "isEnd"); // 是否完结
        CHANNEL_CMS_FILTER_MAP.put("346", "isHomemade");// 是否自制
    }

    public static String getChannelSkipUrl(String pageid, Integer cid) {

        if (cid != null && CHANNEL_SKIP_SEARCH.containsKey(cid)) {
            return SEARCH_URL + "?" + CHANNEL_SKIP_SEARCH.get(cid);
        }

        if (StringUtils.isNotBlank(pageid) && CHANNEL_TOP_PAGEID.containsKey(pageid)) {
            return TOP_URL + CHANNEL_TOP_PAGEID.get(pageid);
        }

        return REC_URL + "?pageid=" + pageid;
    }
}
