package xserver.api.module.channel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import xserver.api.constant.DataConstants;
import xserver.common.dto.channel.ChannelNavigation;
import xserver.lib.mem.BaseMem;
import xserver.lib.tp.staticconf.request.FilterConditionDto;
import xserver.lib.tp.staticconf.request.FilterConditionDto.FilterData;
import xserver.lib.tp.staticconf.request.FilterConditionDto.FilterDetail;

public class ChannelConstant {
    // ==================页面id================
    // public static final String HOME_PAGE_ID = "1002696800";// 首页对应推荐数据的pageid
    public static final String HOME_PAGE_ID = "1002921155";// 首页对应推荐数据的pageid

    public final static String ERROR_CODE_CHANNEL_NO_DATA = "SCC0001"; // 频道无数据时的错误码
    public final static String ERROR_CODE_CHANNEL_PARAM_ERROR = "SSC0002"; // 参数不合法

    public final static String CMS_URL_SKIP_TYPE_WEB = "1"; // cms配置的外跳URL方式
    public final static String CMS_URL_SKIP_TYPE_WEBVIEW = "2"; // cms配置的内嵌webview方式
    public final static String CMS_URL_SKIP_TYPE_SUBJECT = "3"; // cms配置的外跳小专题的方式

    public final static String CMS_TYPE_VIDEO = "1"; // cms中基本信息维护中的视频
    public final static String CMS_TYPE_ALBUM = "2"; // cms中基本信息维护中的专辑
    public final static String CMS_TYPE_LIVE = "3"; // cms中基本信息维护中的直播
    public final static String CMS_TYPE_SUBJECT = "5"; // cms中基本信息维护中的专题
    public final static String CMS_TYPE_ALL_WEB = "8"; // cms中基本信息维护中的全网

    public final static String DATA_TYPE_ALBUM = "1"; // 数据类型定义--专辑
    public final static String DATA_TYPE_VIDEO = "2"; // 数据类型定义--视频
    public final static String DATA_TYPE_STAR = "3"; // 数据类型定义--明星
    public final static String DATA_TYPE_SUBJECT = "4"; // 数据类型定义--专题
    public final static String DATA_TYPE_ALL_WEB = "5"; // 数据类型定义--外网数据
    public final static String DATA_TYPE_LIVE = "6";// 数据类型定义--直播
    public final static String DATA_TYPE_MUSIC = "7";// 数据类型定义——音乐(mp3)
    public final static String DATA_TYPE_HOTWORDS = "8";// 数据类型定义——乐词/关注词
    public final static String DATA_TYPE_H5ACTIVITY = "9";// 数据类型定义——H5活动
    public final static String DATA_TYPE_MUSIC_ALBUM = "10";// 数据类型定义——音乐专辑
    public final static String DATA_TYPE_WALLPAPER = "11";// 数据类型定义—-壁纸



    public final static String WEB_PLAY_URL = "http://m.letv.com/detail?pid=mms_leso_a_"; // 全网视频播放的地址

    public static List<String> MAIN_PAGEID = new ArrayList<>();// 所有频道首页id

    public static List<String> VIP_CHANNEL_PAGEID = new ArrayList<>();// 会员影视频道二级导航页

    public static List<String> HOMEMADE_CHANNEL_PAGEID = new ArrayList<>();// 自制频道二级导航页

    public static List<String> SUBJECT_PAGEID = new ArrayList<>();// 专题页面

    public static final Map<String, String> MULTI_AREA_HOMEPAGE_MAP = new HashMap<String, String>(); // 不同地域的
    static {
        MULTI_AREA_HOMEPAGE_MAP.put(DataConstants.WCODE_CN, "1002921155");
        MULTI_AREA_HOMEPAGE_MAP.put(DataConstants.WCODE_HK, "1003014920");
    }
    public static final Map<String, String> MULTI_ALLCHANNEL_BLOCK_MAP = new HashMap<String, String>(); // 频道墙cms版块id
    static {
        MULTI_ALLCHANNEL_BLOCK_MAP.put(DataConstants.WCODE_CN, "2759");
        MULTI_ALLCHANNEL_BLOCK_MAP.put(DataConstants.WCODE_HK, "3193");
    }

    public final static Map<Integer, FilterConditionDto> CHANNEL_FILTER_MAP = new HashMap<Integer, FilterConditionDto>();
    public final static Map<Integer, String> CHANNEL_DEFAULT_STREAM = new HashMap<Integer, String>();

    public static Map<String,String> CHANNEL_LIVE_PAGEID_LIVETYPE = new HashMap<>();

    public static final int HOME_PAGE_LIVE_COUNT = 2;// 大首页直播条数


    public interface SUBCID {
        public static final int FILTER = 0;// 筛选
        public static final int TOP = 1;// 排行
        public static final int INDEX = 2;// 首页
    }

    public static final ConcurrentMap<String, BaseMem<List<ChannelNavigation>>> NAVIGATION_MAP = new ConcurrentHashMap<String, BaseMem<List<ChannelNavigation>>>();
    public static final long THIRTY_MINUTES = 86400000;// 24小时

    static {
        initFilter();

        CHANNEL_DEFAULT_STREAM.put(1012, "4k"); // 4K频道
        CHANNEL_DEFAULT_STREAM.put(1013, "1080p"); // 1080P频道
        CHANNEL_DEFAULT_STREAM.put(1014, "3d1080p"); // 3D频道
        CHANNEL_DEFAULT_STREAM.put(1023, "1300_dts"); // 影院声频道
        CHANNEL_DEFAULT_STREAM.put(1024, "2k"); // 2K频道

        MAIN_PAGEID.add("1002921155");// 大首页
        MAIN_PAGEID.add("1002921106");// 电视剧首页
        MAIN_PAGEID.add("1002921119");// 美剧首页
        MAIN_PAGEID.add("1002921156");// 综艺首页
        MAIN_PAGEID.add("1002921073");// 动漫首页
        MAIN_PAGEID.add("1002921162");// 电影首页
        MAIN_PAGEID.add("1002921151");// 会员影视首页
        MAIN_PAGEID.add("1002936647");// 自制首页
        MAIN_PAGEID.add("1002921084");// 娱乐首页
        MAIN_PAGEID.add("1002919858");// 音乐首页
        MAIN_PAGEID.add("1002921141");// 资讯首页
        MAIN_PAGEID.add("1002921044");// 纪录片首页
        MAIN_PAGEID.add("1002921095");// 财经首页
        MAIN_PAGEID.add("1002921062");// 亲子首页
        MAIN_PAGEID.add("1002920461");// 风尚首页
        MAIN_PAGEID.add("1002919705");// 汽车首页
        MAIN_PAGEID.add("1002921057");// 旅游首页
        MAIN_PAGEID.add("1002921143");// 游戏首页
        MAIN_PAGEID.add("1002921146");// 宠物首页
        MAIN_PAGEID.add("1002921148");// 科技首页
        MAIN_PAGEID.add("1002921150");// 教育首页
        MAIN_PAGEID.add("1002921122");// 体育首页
        MAIN_PAGEID.add("1002921132");// 英超首页

        // 会员影视频道二级导航页
        VIP_CHANNEL_PAGEID.add("1002921151");// 会员首页
        VIP_CHANNEL_PAGEID.add("1002921154");// 会员最热
        VIP_CHANNEL_PAGEID.add("1002921153");// 会员专题
        VIP_CHANNEL_PAGEID.add("1002947764");// 会员好莱坞
        VIP_CHANNEL_PAGEID.add("1002947766");// 会员微电影
        VIP_CHANNEL_PAGEID.add("1002947941");// 会员爱情
        VIP_CHANNEL_PAGEID.add("1002947942");// 会员喜剧
        VIP_CHANNEL_PAGEID.add("1002947943");// 会员动作

        VIP_CHANNEL_PAGEID.add("1002921166");// 电影频道会员

        // 自制频道二级导航页
        HOMEMADE_CHANNEL_PAGEID.add("1002936647");// 自制首页
        HOMEMADE_CHANNEL_PAGEID.add("1002936649");// 自制自制剧
        HOMEMADE_CHANNEL_PAGEID.add("1002950440");// 自制电影
        HOMEMADE_CHANNEL_PAGEID.add("1002950441");// 自制综艺
        HOMEMADE_CHANNEL_PAGEID.add("1002950443");// 自制体育
        HOMEMADE_CHANNEL_PAGEID.add("1002936650");// 自制片花
        HOMEMADE_CHANNEL_PAGEID.add("1002936651");// 自制剧星

        // 专题页面
        SUBJECT_PAGEID.add("1002920003");// 音乐专题
        SUBJECT_PAGEID.add("1002921068");// 亲子专题
        SUBJECT_PAGEID.add("1002921081");// 动漫专题
        SUBJECT_PAGEID.add("1002921087");// 娱乐专题
        SUBJECT_PAGEID.add("1002921153");// 会员影视专题


        //频道首页和直播类型对应关系
        CHANNEL_LIVE_PAGEID_LIVETYPE.put("1002921155",liveType.LIVE_TYPE_ALL);//大首页
        CHANNEL_LIVE_PAGEID_LIVETYPE.put("1002921122",liveType.LIVE_TYPE_SPORTS);//体育
        CHANNEL_LIVE_PAGEID_LIVETYPE.put("1002919858",liveType.LIVE_TYPE_MUSIC);//音乐
        CHANNEL_LIVE_PAGEID_LIVETYPE.put("1002921141",liveType.LIVE_TYPE_INFORMATION);//资讯
        CHANNEL_LIVE_PAGEID_LIVETYPE.put("1002921084",liveType.LIVE_TYPE_ENT);//娱乐
        CHANNEL_LIVE_PAGEID_LIVETYPE.put("1002921143",liveType.LIVE_TYPE_GAME);//游戏
//        CHANNEL_LIVE_PAGEID_LIVETYPE.put("1002921122",liveType.LIVE_TYPE_BRAND);//品牌


    }

    /**
     * 初始化频道筛选数据
     */
    public static void initFilter() {
        FilterConditionDto dto = null;
        for (ChannelFilterEnum cfe : ChannelFilterEnum.values()) {
            dto = new FilterConditionDto();
            if (1000 == cfe.getCid()) {// 会员频道返回电影频道cid
                dto.setCid(1);
            } else {
                dto.setCid(cfe.getCid());
            }
            dto.setCname(cfe.getName());
            dto.setDt(cfe.getDataType());
            dto.setFilter(getChannelFilter(cfe.getCid()));
            CHANNEL_FILTER_MAP.put(cfe.getCid(), dto);
        }

    }

    public static List<FilterConditionDto.FilterDetail> getChannelFilter(int cid) {
        List<FilterConditionDto.FilterDetail> filterList = new ArrayList<FilterConditionDto.FilterDetail>();
        List<FilterConditionDto.FilterData> tmpList = null;
        // 排序 or
        FilterDetail orFilter = new FilterDetail();
        orFilter.setFilterKey("or");
        orFilter.setFilterTitle("排序");
        tmpList = getOrderFilter(cid);
        orFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(orFilter);
        }

        // 是否付费
        FilterDetail payFilter = new FilterDetail();
        payFilter.setFilterKey("ispay");
        payFilter.setFilterTitle("是否付费");
        tmpList = getPayFilter(cid);
        payFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(payFilter);
        }

        // 视频类型
        FilterDetail vtpFilter = new FilterDetail();
        vtpFilter.setFilterKey("vtp");
        vtpFilter.setFilterTitle("属性");
        tmpList = getVtpFilter(cid);
        vtpFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(vtpFilter);
        }

        // 子分类 sc
        FilterDetail scFilter = new FilterDetail();
        scFilter.setFilterKey("sc");
        scFilter.setFilterTitle("类型");
        tmpList = getSubCategoryFilter(cid);
        scFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(scFilter);
        }

        // 地区 area
        FilterDetail areaFilter = new FilterDetail();
        areaFilter.setFilterKey("area");
        areaFilter.setFilterTitle("地区");
        tmpList = getAreaFilter(cid);
        areaFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(areaFilter);
        }

        // 适合年龄
        FilterDetail fitAgeFilter = new FilterDetail();
        fitAgeFilter.setFilterKey("fitAge");
        fitAgeFilter.setFilterTitle("年龄");
        tmpList = getFitAgeFilter(cid);
        fitAgeFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(fitAgeFilter);
        }

        // 风格
        FilterDetail styleFilter = new FilterDetail();
        styleFilter.setFilterKey("style");
        styleFilter.setFilterTitle("风格");
        tmpList = getStyleFilter(cid);
        styleFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(styleFilter);
        }

        // 歌手类型
        FilterDetail singerTypeFilter = new FilterDetail();
        singerTypeFilter.setFilterKey("singerType");
        singerTypeFilter.setFilterTitle("艺人");
        tmpList = getSingerTypeFilter(cid);
        singerTypeFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(singerTypeFilter);
        }

        // 语言
        FilterDetail languageFilter = new FilterDetail();
        languageFilter.setFilterKey("language");
        languageFilter.setFilterTitle("语种");
        tmpList = getLanguageFilter(cid);
        languageFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(languageFilter);
        }

        // 画质
        FilterDetail streamFilter = new FilterDetail();
        streamFilter.setFilterKey("playStreamFeatures");
        streamFilter.setFilterTitle("画质");
        tmpList = getStreamFilter(cid);
        streamFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(streamFilter);
        }

        // 电视台
        FilterDetail tvFilter = new FilterDetail();
        tvFilter.setFilterKey("tvid");
        tvFilter.setFilterTitle("电视台");
        tmpList = getTVFilter(cid);
        tvFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(tvFilter);
        }

        // 自制
        FilterDetail homeMadeFilter = new FilterDetail();
        homeMadeFilter.setFilterKey("isHomemade");
        homeMadeFilter.setFilterTitle("是否自制");
        tmpList = getHomeMadeFilter(cid);
        homeMadeFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(homeMadeFilter);
        }

        // 年份 year
        FilterDetail yearFilter = new FilterDetail();
        yearFilter.setFilterKey("releaseYearDecade");
        yearFilter.setFilterTitle("年份");
        tmpList = getYearFilter(cid);
        yearFilter.setVal(tmpList);
        if (tmpList != null && tmpList.size() > 0) {
            filterList.add(yearFilter);
        }

        return filterList;
    }

    private static List<FilterConditionDto.FilterData> getOrderFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        case 1: // 电影
            valList.add(new FilterData("5", "最新更新"));
            valList.add(new FilterData("4", "昨日热播"));
            valList.add(new FilterData("2", "历史热播"));
            valList.add(new FilterData("8", "最高评分"));
            break;
        case 2: // 电视剧
            valList.add(new FilterData("5", "最新更新"));
            valList.add(new FilterData("4", "昨日热播"));
            valList.add(new FilterData("2", "历史热播"));
            valList.add(new FilterData("8", "最高评分"));
            break;
        case 3: // 娱乐
            valList.add(new FilterData("7", "最新更新"));
            valList.add(new FilterData("4", "昨日热播"));
            valList.add(new FilterData("2", "历史热播"));
            break;
        case 4: // 体育
            valList.add(new FilterData("7", "最新最新"));
            valList.add(new FilterData("4", "昨日热播"));
            valList.add(new FilterData("2", "历史热播"));
            break;
        case 5: // 动漫
            valList.add(new FilterData("5", "最新更新"));
            valList.add(new FilterData("4", "昨日热播"));
            valList.add(new FilterData("1", "最新上映"));
            valList.add(new FilterData("2", "历史热播"));
            valList.add(new FilterData("8", "最高评分"));
            break;
        case 9: // 音乐
            valList.add(new FilterData("7", "最新更新"));
            valList.add(new FilterData("4", "昨日热播"));
            valList.add(new FilterData("2", "历史热播"));
            valList.add(new FilterData("1", "最新发行"));
            break;
        case 11: // 综艺
            valList.add(new FilterData("5", "最新更新"));
            valList.add(new FilterData("4", "昨日热播"));
            valList.add(new FilterData("2", "历史热播"));
            break;
        case 14: // 汽车
            valList.add(new FilterData("7", "最新更新"));
            valList.add(new FilterData("4", "昨日热播"));
            valList.add(new FilterData("2", "历史热播"));
            break;
        case 16:// 纪录片
            valList.add(new FilterData("7", "最近更新"));
            valList.add(new FilterData("2", "昨日热播"));
            valList.add(new FilterData("4", "历史热播"));
            break;
        case 17:// 公开课
            break;
        case 19:// 乐视出品
            break;
        case 20:// 风尚
            valList.add(new FilterData("5", "最新最新"));
            valList.add(new FilterData("4", "昨日热播"));
            valList.add(new FilterData("2", "历史热播"));
            break;
        case 22:// 财经
            valList.add(new FilterData("6", "最新"));
            valList.add(new FilterData("4", "最热"));
            valList.add(new FilterData("8", "好评"));
            break;
        case 23:// 旅游
            valList.add(new FilterData("7", "最新最新"));
            valList.add(new FilterData("4", "昨日热播"));
            valList.add(new FilterData("2", "历史热播"));
            break;
        case 1000:// 会员频道
            valList.add(new FilterData("5", "最新更新"));
            valList.add(new FilterData("4", "昨日热播"));
            valList.add(new FilterData("2", "历史热播"));
            valList.add(new FilterData("8", "最高评分"));
            break;
        case 1001:// 杜比频道
            valList.add(new FilterData("1", "最新最新"));
            valList.add(new FilterData("2", "好评"));
            break;
        default:
            break;
        }
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getSubCategoryFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        // 电影
        case 1:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("30010", "动作"));
            valList.add(new FilterData("30015", "惊悚"));
            valList.add(new FilterData("30009", "喜剧"));
            valList.add(new FilterData("30011", "爱情"));
            valList.add(new FilterData("30016", "悬疑"));
            valList.add(new FilterData("30020", "科幻"));
            valList.add(new FilterData("30023", "灾难"));
            valList.add(new FilterData("30018", "犯罪"));
            valList.add(new FilterData("30019", "冒险"));
            valList.add(new FilterData("30024", "伦理"));
            valList.add(new FilterData("30012", "恐怖"));
            valList.add(new FilterData("30013", "动画"));
            valList.add(new FilterData("30014", "战争"));
            valList.add(new FilterData("30021", "警匪"));
            valList.add(new FilterData("30017", "奇幻"));
            valList.add(new FilterData("30022", "武侠"));
            valList.add(new FilterData("30154", "短片"));
            valList.add(new FilterData("30155", "传记"));
            valList.add(new FilterData("30026", "家庭"));
            valList.add(new FilterData("30027", "纪录"));
            valList.add(new FilterData("30153", "历史"));
            valList.add(new FilterData("30025", "歌舞"));
            valList.add(new FilterData("30156", "体育"));
            break;
        // 会员频道
        case 1000:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("30010", "动作"));
            valList.add(new FilterData("30015", "惊悚"));
            valList.add(new FilterData("30009", "喜剧"));
            valList.add(new FilterData("30011", "爱情"));
            valList.add(new FilterData("30016", "悬疑"));
            valList.add(new FilterData("30020", "科幻"));
            valList.add(new FilterData("30023", "灾难"));
            valList.add(new FilterData("30018", "犯罪"));
            valList.add(new FilterData("30019", "冒险"));
            valList.add(new FilterData("30024", "伦理"));
            valList.add(new FilterData("30012", "恐怖"));
            valList.add(new FilterData("30013", "动画"));
            valList.add(new FilterData("30014", "战争"));
            valList.add(new FilterData("30021", "警匪"));
            valList.add(new FilterData("30017", "奇幻"));
            valList.add(new FilterData("30022", "武侠"));
            valList.add(new FilterData("30154", "短片"));
            valList.add(new FilterData("30155", "传记"));
            valList.add(new FilterData("30026", "家庭"));
            valList.add(new FilterData("30027", "纪录"));
            valList.add(new FilterData("30153", "历史"));
            valList.add(new FilterData("30025", "歌舞"));
            valList.add(new FilterData("30156", "体育"));
            break;
        // 电视剧
        case 2:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("30024", "伦理"));
            valList.add(new FilterData("30009", "喜剧"));
            valList.add(new FilterData("30017", "奇幻"));
            valList.add(new FilterData("30014", "战争"));
            valList.add(new FilterData("30022", "武侠"));
            valList.add(new FilterData("30018", "犯罪"));
            valList.add(new FilterData("30320", "偶像"));
            valList.add(new FilterData("30039", "都市"));
            valList.add(new FilterData("30153", "历史"));
            valList.add(new FilterData("30042", "古装"));
            valList.add(new FilterData("30020", "科幻"));
            valList.add(new FilterData("30044", "情景"));
            valList.add(new FilterData("30318", "谍战"));
            valList.add(new FilterData("31367", "经典"));
            valList.add(new FilterData("31370", "农村"));
            valList.add(new FilterData("31369", "年代"));
            valList.add(new FilterData("31368", "穿越"));
            valList.add(new FilterData("30280", "神话"));
            valList.add(new FilterData("30015", "惊悚"));
            valList.add(new FilterData("30054", "励志"));
            valList.add(new FilterData("30011", "爱情"));
            break;
        // 娱乐
        case 3:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("440141", "明星资讯"));
            valList.add(new FilterData("440142", "电影资讯"));
            valList.add(new FilterData("440143", "电视资讯"));
            valList.add(new FilterData("440144", "音乐资讯"));
            valList.add(new FilterData("440145", "综艺资讯"));
            valList.add(new FilterData("440146", "网络热点"));
            valList.add(new FilterData("440147", "搞笑猎奇"));
            valList.add(new FilterData("440150", "娱乐资讯"));
            valList.add(new FilterData("440123", "观影调查"));
            valList.add(new FilterData("440125", "独家策划"));
            valList.add(new FilterData("440126", "独家专访"));
            valList.add(new FilterData("440122", "乐视播报"));
            break;
        // 体育
        case 4:
            // valList.add(new FilterData("", "全部"));
            // valList.add(new FilterData("30232", "NBA"));
            // valList.add(new FilterData("30222", "英超"));
            // valList.add(new FilterData("30223", "西甲"));
            // valList.add(new FilterData("30224", "意甲"));
            // valList.add(new FilterData("30295", "法甲"));
            // valList.add(new FilterData("30365", "德甲"));
            // valList.add(new FilterData("30225", "欧冠"));
            // valList.add(new FilterData("30227", "中超"));
            // valList.add(new FilterData("30228", "国足"));
            // valList.add(new FilterData("30367", "亚冠"));
            // valList.add(new FilterData("30226", "国际足球"));
            // valList.add(new FilterData("30229", "国内足球"));
            // valList.add(new FilterData("30230", "CBA"));
            // valList.add(new FilterData("30231", "中国篮球"));
            // valList.add(new FilterData("30296", "欧冠篮球"));
            // valList.add(new FilterData("30233", "篮球其他"));
            // valList.add(new FilterData("30235", "综合体育"));
            // valList.add(new FilterData("30234", "网球"));
            // valList.add(new FilterData("30236", "台球"));
            // valList.add(new FilterData("30294", "高尔夫"));
            // valList.add(new FilterData("30238", "世界杯"));
            // valList.add(new FilterData("30237", "欧洲杯"));
            // valList.add(new FilterData("30239", "奥运会"));
            break;
        // 动漫
        case 5:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("30261", "战斗"));
            valList.add(new FilterData("30050", "热血"));
            valList.add(new FilterData("30019", "冒险"));
            valList.add(new FilterData("30017", "奇幻"));
            valList.add(new FilterData("30050", "热血"));
            valList.add(new FilterData("30057", "搞笑"));
            valList.add(new FilterData("30063", "恋爱"));
            valList.add(new FilterData("30088", "校园"));
            valList.add(new FilterData("30020", "科幻"));
            valList.add(new FilterData("30265", "魔法"));
            valList.add(new FilterData("31372", "怀旧经典"));
            valList.add(new FilterData("30061", "悬疑推理"));
            valList.add(new FilterData("30052", "运动"));
            valList.add(new FilterData("30058", "真人"));
            valList.add(new FilterData("30059", "神魔"));
            valList.add(new FilterData("30354", "魔幻"));
            valList.add(new FilterData("30051", "机战"));
            valList.add(new FilterData("30269", "竞技"));
            valList.add(new FilterData("30010", "动作"));
            valList.add(new FilterData("30054", "励志"));
            valList.add(new FilterData("30264", "治愈"));
            valList.add(new FilterData("30266", "日常"));
            valList.add(new FilterData("30053", "美少女"));
            valList.add(new FilterData("30271", "百合"));
            valList.add(new FilterData("30270", "后宫"));
            valList.add(new FilterData("30272", "耽美"));
            valList.add(new FilterData("30060", "男性向"));
            valList.add(new FilterData("30056", "女性向"));
            valList.add(new FilterData("30055", "怪物"));
            valList.add(new FilterData("30015", "惊悚"));
            valList.add(new FilterData("31371", "猎奇"));
            valList.add(new FilterData("30089", "成人"));
            valList.add(new FilterData("30282", "宠物"));
            valList.add(new FilterData("30273", "童话"));
            valList.add(new FilterData("30275", "益智"));
            valList.add(new FilterData("30280", "神话"));
            valList.add(new FilterData("30278", "教育"));
            valList.add(new FilterData("30279", "轻松"));
            valList.add(new FilterData("30274", "英雄"));
            break;
        // 音乐
        case 9:
            break;
        // 综艺
        case 11:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("30370", "明星"));
            valList.add(new FilterData("30046", "情感"));
            valList.add(new FilterData("30083", "访谈"));
            valList.add(new FilterData("30072", "选秀"));
            valList.add(new FilterData("30167", "时尚"));
            valList.add(new FilterData("30045", "生活"));
            valList.add(new FilterData("30284", "纪实"));
            valList.add(new FilterData("30290", "文化"));
            valList.add(new FilterData("30291", "曲艺"));
            valList.add(new FilterData("30057", "搞笑"));
            valList.add(new FilterData("30070", "游戏"));
            valList.add(new FilterData("30075", "真人秀"));
            valList.add(new FilterData("30065", "脱口秀"));
            valList.add(new FilterData("30376", "晚会"));
            valList.add(new FilterData("30082", "播报"));
            valList.add(new FilterData("30371", "少儿"));
            valList.add(new FilterData("30372", "相亲"));
            valList.add(new FilterData("30373", "职场"));
            valList.add(new FilterData("30375", "创业"));
            valList.add(new FilterData("30327", "音乐"));
            valList.add(new FilterData("30071", "舞蹈"));
            valList.add(new FilterData("30073", "旅游"));
            valList.add(new FilterData("30069", "美食"));
            valList.add(new FilterData("30119", "军事"));
            valList.add(new FilterData("30275", "益智"));
            valList.add(new FilterData("30374", "理财"));
            valList.add(new FilterData("30165", "命理"));
            valList.add(new FilterData("30323", "其他"));
            break;
        // 汽车
        case 14:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("32001", "微型车"));
            valList.add(new FilterData("32002", "小型车"));
            valList.add(new FilterData("32003", "紧凑车型"));
            valList.add(new FilterData("32004", "中型车"));
            valList.add(new FilterData("32005", "中大型车"));
            valList.add(new FilterData("32006", "豪华车"));
            valList.add(new FilterData("32007", "MPV"));
            valList.add(new FilterData("32008", "SUV"));
            valList.add(new FilterData("32009", "跑车"));
            valList.add(new FilterData("32010", "皮卡"));
            valList.add(new FilterData("32011", "微面"));
            valList.add(new FilterData("32012", "其他"));
            break;
        // 纪录片
        case 16:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("30112", "自然"));
            valList.add(new FilterData("30113", "动物"));
            valList.add(new FilterData("30114", "科学"));
            valList.add(new FilterData("30115", "地理"));
            valList.add(new FilterData("30116", "天文"));
            valList.add(new FilterData("30117", "气候"));
            valList.add(new FilterData("30014", "战争"));
            valList.add(new FilterData("30119", "军事"));
            valList.add(new FilterData("30153", "历史"));
            valList.add(new FilterData("30121", "考古"));
            valList.add(new FilterData("30122", "社会"));
            valList.add(new FilterData("30018", "犯罪"));
            valList.add(new FilterData("30124", "金融"));
            valList.add(new FilterData("30125", "人物"));
            valList.add(new FilterData("30126", "艺术"));
            valList.add(new FilterData("30127", "人文"));
            valList.add(new FilterData("30128", "建筑"));
            valList.add(new FilterData("30069", "美食"));
            valList.add(new FilterData("30130", "百科"));
            break;
        // 公开课
        case 17:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("30153", "历史"));
            valList.add(new FilterData("30094", "文学"));
            valList.add(new FilterData("30095", "经济学"));
            valList.add(new FilterData("30096", "物理"));
            valList.add(new FilterData("30097", "生物学"));
            valList.add(new FilterData("30098", "社会学"));
            valList.add(new FilterData("30099", "艺术类"));
            valList.add(new FilterData("30100", "建筑学"));
            valList.add(new FilterData("30101", "医学"));
            valList.add(new FilterData("30102", "天文学"));
            valList.add(new FilterData("30104", "政治学"));
            valList.add(new FilterData("30105", "心理学"));
            valList.add(new FilterData("30106", "哲学"));
            valList.add(new FilterData("30107", "计算机"));
            valList.add(new FilterData("30109", "法学"));
            valList.add(new FilterData("30110", "演讲"));
            break;
        // 乐视出品
        case 19:
            break;
        // 财经
        case 22:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("530003", "民生经济"));
            valList.add(new FilterData("530001", "市场交易"));
            valList.add(new FilterData("530002", "投资理财"));
            valList.add(new FilterData("530004", "品质生活"));
            valList.add(new FilterData("530005", "经济管理"));
            valList.add(new FilterData("530006", "行业经济"));
            break;
        // 旅游
        case 23:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("540015", "美食"));
            valList.add(new FilterData("540017", "都市"));
            valList.add(new FilterData("540013", "海岛"));
            valList.add(new FilterData("540022", "购物"));
            valList.add(new FilterData("540019", "自驾"));
            valList.add(new FilterData("540016", "探险"));
            valList.add(new FilterData("542002", "欢乐地图"));
            valList.add(new FilterData("540009", "趣味"));
            valList.add(new FilterData("542001", "其他"));
            break;
        // 杜比频道
        case 1001:
            break;
        default:
            break;
        }
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getAreaFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        // 电影
        case 1:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("50001", "内地"));
            valList.add(new FilterData("50071", "美国"));
            valList.add(new FilterData("50002", "香港"));
            valList.add(new FilterData("50042", "韩国"));
            valList.add(new FilterData("50003", "台湾"));
            valList.add(new FilterData("50072", "英国"));
            valList.add(new FilterData("50101", "泰国"));
            valList.add(new FilterData("50041", "日本"));
            valList.add(new FilterData("50103", "印度"));
            valList.add(new FilterData("50102", "俄罗斯"));
            valList.add(new FilterData("50075", "法国"));
            valList.add(new FilterData("50004", "马来西亚"));
            valList.add(new FilterData("50005", "新加坡"));
            valList.add(new FilterData("50073", "加拿大"));
            valList.add(new FilterData("50074", "西班牙"));
            break;
        // 会员频道
        case 1000:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("50001", "内地"));
            valList.add(new FilterData("50071", "美国"));
            valList.add(new FilterData("50002", "香港"));
            valList.add(new FilterData("50042", "韩国"));
            valList.add(new FilterData("50003", "台湾"));
            valList.add(new FilterData("50072", "英国"));
            valList.add(new FilterData("50101", "泰国"));
            valList.add(new FilterData("50041", "日本"));
            valList.add(new FilterData("50103", "印度"));
            valList.add(new FilterData("50102", "俄罗斯"));
            valList.add(new FilterData("50075", "法国"));
            valList.add(new FilterData("50004", "马来西亚"));
            valList.add(new FilterData("50005", "新加坡"));
            valList.add(new FilterData("50073", "加拿大"));
            valList.add(new FilterData("50074", "西班牙"));
            break;
        // 电视剧
        case 2:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("50001", "内地"));
            valList.add(new FilterData("50042", "韩国"));
            valList.add(new FilterData("50071", "美国"));
            valList.add(new FilterData("50002", "香港"));
            valList.add(new FilterData("50003", "台湾"));
            valList.add(new FilterData("50101", "泰国"));
            valList.add(new FilterData("50072", "英国"));
            valList.add(new FilterData("50041", "日本"));
            valList.add(new FilterData("50005", "新加坡"));
            valList.add(new FilterData("50074,50073,50102,50004,50103", "其他"));
            break;
        // 娱乐
        case 3:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("50002,50007,50008,50006,50009,50001", "大陆"));
            valList.add(new FilterData("50002,50003", "港台"));
            valList.add(new FilterData("50040,50041,50042", "日韩"));
            valList.add(new FilterData("50070,50071,50072,50073,50074,50075,50076", "欧美"));
            valList.add(new FilterData("50104,50103,50101", "东南亚"));
            valList.add(new FilterData("50105,50078,50079,50100,50338", "其他"));
            break;
        // 体育
        case 4:
            break;
        // 动漫
        case 5:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("50001", "内地"));
            valList.add(new FilterData("50041", "日本"));
            valList.add(new FilterData("50071", "美国"));
            valList.add(new FilterData("50072", "英国"));
            valList.add(new FilterData("50003", "台湾"));
            valList.add(new FilterData("50042", "韩国"));
            valList.add(new FilterData("50075,50101,50005,50074,50102,50004,50073,50002", "其他"));
            break;
        // 音乐
        case 9:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("50001", "内地"));
            valList.add(new FilterData("50002,50003,50004,50005", "港台"));
            valList.add(new FilterData("50071,50072,50073,50074,50075,50076,50077,50078,50079,50151,50338", "欧美"));
            valList.add(new FilterData("50041", "日本"));
            valList.add(new FilterData("50042", "韩国"));
            valList.add(new FilterData("50100,50101,50102,50103", "其他"));
            break;
        // 综艺
        case 11:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("50001", "内地"));
            valList.add(new FilterData("50002,50003", "港台"));
            valList.add(new FilterData("50040,50041,50042", "日韩"));
            valList.add(new FilterData("50071", "美国"));
            valList.add(new FilterData("50070,50072,50073,50074,50075,50076,50077", "其他"));
            break;
        // 汽车
        case 14:
            break;
        // 纪录片
        case 16:
            break;
        // 公开课
        case 17:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("50071", "美国"));
            valList.add(new FilterData("50072", "英国"));
            valList.add(new FilterData("50076", "德国"));
            break;
        // 乐视出品
        case 19:
            break;
        // 风尚
        case 20:
            break;
        // 财经
        case 22:
            break;
        // 旅游
        case 23:
            break;
        // 杜比频道
        case 1001:
            break;
        default:
            break;
        }
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getYearFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        case 1: // 电影
            valList = getLoopYear();
            break;
        case 2: // 电视剧
            valList = getLoopYear();
            break;
        case 5: // 动漫
            valList = getLoopYear();
            break;
        case 11: // 综艺
            valList = getLoopYear();
            break;
        case 1000: // 会员频道
            valList = getLoopYear();
            break;
        default:
            break;
        }
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getLoopYear() {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        valList.add(new FilterData("", "全部"));
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i <= 4; i++) {
            valList.add(new FilterData(String.valueOf(year - i), String.valueOf(year - i)));
        }
        valList.add(new FilterData("200x", "00年代"));
        valList.add(new FilterData("199x", "90年代"));
        valList.add(new FilterData("197x,198x", "更早"));
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getVtpFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        case 1: // 电影
            // valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("180001", "电影"));
            valList.add(new FilterData("180217", "特辑"));
            valList.add(new FilterData("180002,180003,180004,180005;dt:2", "片花"));
            break;
        case 2: // 电视剧
            valList.add(new FilterData("180001", "全部正片"));
            valList.add(new FilterData("180002,180005,180003,180004;dt:2", "片花"));
            break;
        case 3: // 娱乐
            break;
        case 4: // 体育
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("182206", "战报"));
            valList.add(new FilterData("182056", "新闻"));
            valList.add(new FilterData("180003,182239", "花絮"));
            valList.add(new FilterData("182207,182234,182235", "录播"));
            valList.add(new FilterData("181202", "片段"));
            valList.add(new FilterData("182208", "策划"));
            valList.add(new FilterData("182209", "回顾"));
            valList.add(new FilterData("182210", "访谈"));
            valList.add(new FilterData("180211", "微电影"));
            valList.add(new FilterData("182219", "前瞻"));
            break;
        case 5: // 动漫
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("180001", "正片"));
            valList.add(new FilterData("180002,180003,180004,180005", "短片"));
            valList.add(new FilterData("181031", "TV版"));
            valList.add(new FilterData("181033", "剧场版"));
            valList.add(new FilterData("181032", "OVA版"));
            break;
        case 9: // 音乐
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("182050", "MV"));
            valList.add(new FilterData("182051", "演唱会"));
            valList.add(new FilterData("182052", "音乐节"));
            valList.add(new FilterData("182053", "颁奖礼"));
            valList.add(new FilterData("182054", "现场"));
            valList.add(new FilterData("182055,182056,18003，180005，180003，182213", "其他"));
            break;
        case 11: // 综艺
            valList.add(new FilterData("", "栏目"));
            valList.add(new FilterData("180001", "正片"));
            valList.add(new FilterData("182202", "片段"));
            valList.add(new FilterData("180002", "预告"));
            break;
        case 14: // 汽车
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("180004", "资讯"));
            valList.add(new FilterData("182210", "访谈"));
            valList.add(new FilterData("182241", "节目"));
            valList.add(new FilterData("182246", "视觉"));
            break;
        case 20: // 风尚
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("180004", "资讯"));
            valList.add(new FilterData("182210", "访谈"));
            valList.add(new FilterData("182241", "节目"));
            valList.add(new FilterData("182242", "视觉"));
            valList.add(new FilterData("182243", "达人"));
            valList.add(new FilterData("182245", "试用"));
            valList.add(new FilterData("182244", "品牌"));
            valList.add(new FilterData("180005", "其他"));
            break;
        case 22: // 财经
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("180004", "资讯"));
            valList.add(new FilterData("182208", "策划"));
            valList.add(new FilterData("182210", "访谈"));
            valList.add(new FilterData("180006", "论坛"));
            valList.add(new FilterData("180007", "栏目剧"));
            valList.add(new FilterData("180008", "脱口秀"));
            valList.add(new FilterData("180009", "真人秀"));
            valList.add(new FilterData("180010", "公开课"));
            break;
        case 23: // 旅游
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("182249", "视觉"));
            valList.add(new FilterData("180004,182248", "资讯"));
            valList.add(new FilterData("182250", "分享"));
            valList.add(new FilterData("180013", "节目"));
            valList.add(new FilterData("182251", "其他"));
            break;
        default:
            break;
        }
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getFitAgeFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {

        case 5: // 动漫频道
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("511001", "6岁以下"));
            valList.add(new FilterData("511002", "6-12岁"));
            valList.add(new FilterData("511003", "12-18岁"));
            valList.add(new FilterData("511004", "18岁以上"));
            break;
        default:
            break;
        }
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getStyleFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        case 4: // 体育频道
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("330029", "国际足球"));
            valList.add(new FilterData("330030", "国内足球"));
            valList.add(new FilterData("330026", "篮球"));
            valList.add(new FilterData("330031", "高尔夫"));
            valList.add(new FilterData("330032", "网球"));
            valList.add(new FilterData("330028", "综合"));
            valList.add(new FilterData("330033", "体彩"));
            valList.add(new FilterData("330034", "赛车"));
            break;
        case 9: // 音乐频道
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("500241", "流行"));
            valList.add(new FilterData("500243", "摇滚"));
            valList.add(new FilterData("500242", "独立"));
            valList.add(new FilterData("500244", "电子"));
            valList.add(new FilterData("500246", "嘻哈"));
            valList.add(new FilterData("500247", "R&B"));
            valList.add(new FilterData("500245", "民谣"));
            valList.add(new FilterData("500248", "乡村"));
            valList.add(new FilterData("500249", "爵士"));
            valList.add(new FilterData("500251", "雷鬼"));
            valList.add(new FilterData("500257", "原声"));
            valList.add(new FilterData("500252", "拉丁"));
            valList.add(new FilterData("500254", "古典"));
            valList.add(new FilterData("500250", "布鲁斯"));
            valList.add(new FilterData("500253", "新世纪"));
            valList.add(new FilterData("500255", "轻音乐"));
            valList.add(new FilterData("500256", "世界音乐"));
            valList.add(new FilterData("500258", "民族/曲艺"));
            valList.add(new FilterData("500282", "其他"));
            break;
        case 14: // 汽车
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("492176", "新车"));
            valList.add(new FilterData("492171", "试驾"));
            valList.add(new FilterData("492178", "车载"));
            valList.add(new FilterData("492180", "竞速"));
            valList.add(new FilterData("492173", "改装"));
            valList.add(new FilterData("492181", "活动"));
            valList.add(new FilterData("490182", "其他"));
            break;
        // 风尚
        case 20:
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("350047", "服装"));
            valList.add(new FilterData("350049", "配饰"));
            valList.add(new FilterData("350050", "护肤"));
            valList.add(new FilterData("350051", "彩妆"));
            valList.add(new FilterData("350053", "美发"));
            valList.add(new FilterData("350052", "减肥"));
            valList.add(new FilterData("350057", "健康"));
            valList.add(new FilterData("350060", "艺术公益"));
            valList.add(new FilterData("350064", "腕表"));
            valList.add(new FilterData("350048", "珠宝"));
            break;
        case 23: // 旅游
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("550001", "国内游"));
            valList.add(new FilterData("550002", "出境游"));
            valList.add(new FilterData("550004", "周边游"));
            valList.add(new FilterData("550006", "自由行"));
            valList.add(new FilterData("550003", "旅游攻略"));
            break;
        default:
            break;
        }
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getSingerTypeFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        case 9: // 音乐频道
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("240001", "男艺人"));
            valList.add(new FilterData("240002", "女艺人"));
            valList.add(new FilterData("240003", "乐队/组合"));
            valList.add(new FilterData("240004", "群星"));
            break;
        default:
            break;
        }
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getLanguageFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        case 1: // 电影
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("70001", "国语"));
            valList.add(new FilterData("70002", "粤语"));
            valList.add(new FilterData("70003", "英语"));
            valList.add(new FilterData("70004", "日语"));
            valList.add(new FilterData("70005", "韩语"));
            valList.add(new FilterData("70006", "法语"));
            valList.add(new FilterData("70010", "泰语"));
            valList.add(new FilterData("70009", "德语"));
            valList.add(new FilterData("70007", "意大利语"));
            valList.add(new FilterData("70008", "西班牙语"));
            valList.add(new FilterData("70000", "其他"));
            break;
        case 5: // 动漫
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("70001", "国语"));
            valList.add(new FilterData("70003", "英语"));
            valList.add(new FilterData("70004", "日语"));
            valList.add(new FilterData("70002", "粤语"));
            valList.add(new FilterData("70005", "韩语"));
            valList.add(new FilterData("70011", "闽南语"));
            valList.add(new FilterData("70000", "其他"));
            break;
        case 9: // 音乐
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("70001", "国语"));
            valList.add(new FilterData("70003", "英语"));
            valList.add(new FilterData("70004", "日语"));
            valList.add(new FilterData("70004", "日语"));
            valList.add(new FilterData("70002", "粤语"));
            valList.add(new FilterData("70005", "韩语"));
            valList.add(new FilterData("70011", "闽南语"));
            valList.add(new FilterData("70000", "其他"));
            break;
        case 1000: // 会员
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("70001", "国语"));
            valList.add(new FilterData("70002", "粤语"));
            valList.add(new FilterData("70003", "英语"));
            valList.add(new FilterData("70004", "日语"));
            valList.add(new FilterData("70005", "韩语"));
            valList.add(new FilterData("70006", "法语"));
            valList.add(new FilterData("70010", "泰语"));
            valList.add(new FilterData("70009", "德语"));
            valList.add(new FilterData("70007", "意大利语"));
            valList.add(new FilterData("70008", "西班牙语"));
            valList.add(new FilterData("70000", "其他"));
            break;
        default:
            break;
        }

        return valList;
    }

    private static List<FilterConditionDto.FilterData> getStreamFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        case 1: // 电影
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("3d", "3D"));
            valList.add(new FilterData("4k", "4K"));
            valList.add(new FilterData("2k", "2K"));
            valList.add(new FilterData("dts,db", "影院声"));
            break;
        case 2: // 电视剧
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("3d", "3D"));
            valList.add(new FilterData("4k", "4K"));
            valList.add(new FilterData("2k", "2K"));
            valList.add(new FilterData("dts,db", "影院声"));
            break;
        case 1000: // 会员
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("3d", "3D"));
            valList.add(new FilterData("4k", "4K"));
            valList.add(new FilterData("2k", "2K"));
            valList.add(new FilterData("dts,db", "影院声"));
            break;
        default:
            break;
        }
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getHomeMadeFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        case 1: // 电影
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("1", "自制"));
            valList.add(new FilterData("0", "非自制"));
            break;
        case 2: // 电视剧
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("1", "自制"));
            valList.add(new FilterData("0", "非自制"));
            break;
        case 4: // 体育
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("1", "自制"));
            valList.add(new FilterData("0", "非自制"));
            break;
        case 9: // 音乐
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("1", "自制"));
            valList.add(new FilterData("0", "非自制"));
            break;
        case 11: // 综艺
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("1", "自制"));
            valList.add(new FilterData("0", "非自制"));
            break;
        case 1000: // 会员
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("1", "自制"));
            valList.add(new FilterData("0", "非自制"));
            break;
        default:
            break;
        }
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getTVFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        case 11: // 综艺
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("56", "乐视"));
            valList.add(new FilterData("10", "湖南"));
            valList.add(new FilterData("11", "江苏"));
            valList.add(new FilterData("14", "天津"));
            valList.add(new FilterData("39", "东方"));
            valList.add(new FilterData("9,8,7,6,5,4", "北京"));
            valList.add(new FilterData("2", "浙江"));
            valList.add(new FilterData("29", "深圳"));
            valList.add(new FilterData("20", "贵州"));
            valList.add(new FilterData("25", "四川"));
            valList.add(new FilterData("53", "厦门"));
            valList.add(new FilterData("35", "山东"));
            valList.add(new FilterData("12", "山西"));
            valList.add(new FilterData("24", "江西"));
            valList.add(new FilterData("3", "安徽"));
            valList.add(new FilterData("27", "河北"));
            valList.add(new FilterData("23", "河南"));
            valList.add(new FilterData("26", "湖北"));
            valList.add(new FilterData("18", "旅游"));
            valList.add(new FilterData("48", "重庆"));
            valList.add(new FilterData("38", "吉林"));
            valList.add(new FilterData("36", "黑龙江"));
            valList.add(new FilterData("42", "青海"));
            valList.add(new FilterData("41", "广东"));
            valList.add(new FilterData("34", "广西"));
            valList.add(new FilterData("47", "陕西"));
            valList.add(new FilterData("22", "云南"));
            valList.add(new FilterData("28", "辽宁"));
            valList.add(new FilterData("33", "福建"));
            valList.add(new FilterData("17", "CETV1"));
            valList.add(new FilterData("13,19,21,30,31,32,37,43,44,45,46,50,1", "其他"));
            break;
        }
        return valList;
    }

    private static List<FilterConditionDto.FilterData> getPayFilter(int cid) {
        List<FilterConditionDto.FilterData> valList = new ArrayList<FilterConditionDto.FilterData>();
        switch (cid) {
        case 1: // 电影频道
            valList.add(new FilterData("", "全部"));
            valList.add(new FilterData("1;payPlatform:141003", "会员专享"));
            valList.add(new FilterData("0", "免费"));
            break;
        case 1000: // 会员频道
            valList.add(new FilterData("1;payPlatform:141003", "会员专享"));
            break;
        default:
            break;
        }

        return valList;
    }

    /***
     * 角标类型
     */
    public interface CORNERLABELTYPE {
        public static String IS_ALL = "1";// 全网
        public static String IS_PAY = "2";// 付费
        public static String IS_VIP = "3";// 会员
        public static String IS_EXCLUSIVE = "4";// 独播
        public static String IS_HOMEMADE = "5";// 自制
        public static String IS_SPECIAL = "6";// 专题
        public static String IS_PREVIEW = "7";// 预告
        public static String IS_4K = "8";// 4K
        public static String IS_2K = "9";// 2K
        public static String IS_1080P = "10";// 1080P
        public static String IS_DTS = "11";// 影院音
        public static String IS_HUAXU = "12";// 花絮
    }

    // 填充角标时，保存模块数据时的通用key
    public static String CHANNEL_FOCUS_KEY = "pid:{pid}_vid:{vid}_zid:{zid}";

    public interface contentStyle {
        public static final String CONTENT_SYTLE_FOCUS = "1"; // 焦点图样式
        public static final String CONTENT_SYTLE_NAVIGATION = "9"; // 导航样式
        public static final String CONTENT_SYTLE_BIG_MORE = "26"; // 1大图4小图，有更多---领先版使用
        public static final String CONTENT_SYTLE_BIG_NOMORE = "27"; // 1大图4小图，无更多---领先版使用
        public static final String CONTENT_SYTLE_PIC_LIST = "28"; // 图文列表---领先版使用
        public static final String CONTENT_SYTLE_SMALL_MORE = "29"; // 4张小图，有更多---领先版使用
        public static final String CONTENT_SYTLE_SMALL_NOMORE = "30"; // 4张小图，无更多---领先版使用

        public static final String CONTENT_SYTLE_NATIVE_SUBJECT = "55"; // 领先版首页重大项目模块专用---领先版使用

        public static final String CONTENT_STYLE_LIVE_LIST1 = "81";//直播-列表样式1，大首页专用
        public static final String CONTENT_STYLE_LIVE_LIST2 = "82";//直播-列表样式2
        public static final String CONTENT_STYLE_LIVE_PIC = "83";//直播-图文列表

        public static final String CONTENT_STYLE_ATTENTION = "89";//关注通栏1
        public static final String CONTENT_STYLE_ATTENTION_SLIP = "90";//关注横滑展示更多样式

    }

    // 数据来源
    public interface dataSource {
        public static final int DATA_SOURCE_CMS = 1; // 数据来源--CMS
        public static final int DATA_SOURCE_REC = 2; // 数据来源--推荐
        public static final int DATA_SOURCE_TOP = 3; // 数据来源--排行
        public static final int DATA_SOURCE_SEARCH = 4; // 数据来源--搜索
    }


    public interface playerType{
        public static final int PLAYER_TYPE_LIVE = 1;
        public static final int PLAYER_TYPE_3D = 2;
    }

    public interface liveType{
        public static final String LIVE_TYPE_ALL = "all";// 直播类型：不区分，所有
        public static final String LIVE_TYPE_SPORTS = "sports";// 直播类型：体育
        public static final String LIVE_TYPE_MUSIC = "music";// 直播类型：音乐
        public static final String LIVE_TYPE_ENT = "entertainment";// 直播类型：娱乐
        public static final String LIVE_TYPE_BRAND = "brand";// 直播类型：品牌
        public static final String LIVE_TYPE_OTHER = "other";// 直播类型：其他
        public static final String LIVE_TYPE_GAME = "game";// 直播类型：其他
        public static final String LIVE_TYPE_INFORMATION = "information";// 直播类型：其他

    }

    //推荐数据来自何种推荐
    public interface recSrcType{
        public static final String REC_SRC_TYPE_EDITOR = "editor";//人工推荐
        public static final String REC_SRC_TYPE_AUTO = "auto";//自动推荐
        public static final String REC_SRC_TYPE_MIX = "mix";//混合推荐

    }

    public static interface AttentionType{
        public static final String ATTENTION_TYPE_HOTWORDS = "1";
        public static final String ATTENTION_TYPE_ACTOR = "2";

    }
}
