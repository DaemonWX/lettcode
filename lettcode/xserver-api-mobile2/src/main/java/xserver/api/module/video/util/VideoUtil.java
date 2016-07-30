package xserver.api.module.video.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import xserver.api.module.video.dto.VideoHot;
import xserver.lib.constant.StreamConstants;
import xserver.lib.constant.VideoConstants;
import xserver.lib.tp.video.constants.VideoTpConstants;
import xserver.lib.tp.video.response.MmsFile;
import xserver.lib.tp.video.response.MmsInfo;
import xserver.lib.tp.video.response.MmsStore;
import xserver.lib.tp.video.response.UserPlayAuth;
import xserver.lib.tpcache.cache.Stream;
import xserver.lib.tpcache.cache.WatchingFocusCache;
import xserver.lib.util.CommonUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VideoUtil {

    private final static Logger log = LoggerFactory.getLogger(VideoUtil.class);

    private static final String REGEX = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";// 特殊字符

    public static Map<String, Stream> PRE_SET_STREAM = new HashMap<String, Stream>();

    public static Map<String, Stream> PRE_SET_STREAM_ZH_CN = new HashMap<String, Stream>();
    public static Map<String, Stream> PRE_SET_STREAM_ZH_HK = new HashMap<String, Stream>();
    public static Map<String, Stream> PRE_SET_STREAM_EN_US = new HashMap<String, Stream>();

    public static Stream USER_SETTING_STREAMS_ZH_CN = new Stream();
    public static Stream USER_SETTING_STREAMS_ZH_HK = new Stream();
    public static Stream USER_SETTING_STREAMS_EN_US = new Stream();

    public static interface validateMmsStore {
        public static final int SUCCESS = 0;
        public static final int NULL_STORE = 1;
        public static final int VIDEO_CN_PLAY_FORBIDDEN = 2;
        public static final int VIDEO_NOT_CN_PLAY_FORBIDDEN = 3;
        public static final int STORE_STATUS_NOT_SUCCESS = 4;
        public static final int INFO_NULL = 5;
        public static final int VIDEO_HK_PLAY_FORBIDDEN = 6;
    }

    // http://wiki.letv.cn/pages/viewpage.action?pageId=37330964
    public static final Map<String, Integer> COMMON_PLAYER_SPLATID = new HashMap<String, Integer>();

    public static Set<String> NO_AREA_PLAY_RESTRICT_MAC_SET = new HashSet<String>();

    public static final String downloadkey = "itv12345678!@#$%^&*";

    protected static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        COMMON_PLAYER_SPLATID.put("720", 1506);// 明星闹钟
        COMMON_PLAYER_SPLATID.put("3004", 1504);// 乐视音乐app Android
        COMMON_PLAYER_SPLATID.put("3002", 1502);// 乐视体育app Android/TV
    }

    /**
     * 获得子平台ID
     * @param wcode
     * @param terminalApplication
     * @param terminalSeries
     * @return
     */
    public static int getSplatId(String wcode, String terminalApplication, String terminalSeries, String businessId) {
        int splatId = 1503;

        Integer splatid = COMMON_PLAYER_SPLATID.get(businessId);
        if (splatid != null) {
            splatId = splatid.intValue();
        }

        return splatId;
    }

    /**
     * 根据码流获得VTYPE值
     * @param streamCode
     * @return
     */
    public static String getVType(String streamCode) {
        String vType = null;
        if (StringUtils.isNotEmpty(streamCode)) {
            vType = StreamConstants.VTYPE_REDUCED_MAP.get(streamCode);
        }

        return vType;
    }

    /**
     * 是否请求TS流
     * @param stream
     * @return
     */
    public static Boolean expectTS(String stream) {
        if ("720p_db".equalsIgnoreCase(stream) || "1300_db".equalsIgnoreCase(stream)
                || "800_db".equalsIgnoreCase(stream) || "1080p6m_db".equalsIgnoreCase(stream)
                || stream.toLowerCase().indexOf("3d") > -1) {
            return false;
        }
        return true;
    }

    /**
     * 根据码流获得媒资id
     * @param pid
     * @param playStream
     * @param videoStreams
     * @param midStreams
     * @return
     */
    public static Long getMidByStream(Long pid, String playStream, String videoStreams, String midStreams) {

        Long mid = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (midStreams != null) {
                Map<String, String> map = mapper.readValue(midStreams, new TypeReference<Map<String, String>>() {
                });
                if (map != null) {
                    Set<Entry<String, String>> entrySet = map.entrySet();
                    for (Entry<String, String> e : entrySet) {
                        String id = e.getKey();
                        String value = e.getValue();
                        if (value == null) {
                            break;
                        }
                        String[] streams = e.getValue().split(",");
                        for (String s : streams) {
                            if (s.equalsIgnoreCase(playStream)) {
                                mid = new Long(id);
                                break;
                            }
                        }
                        if (mid != null) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mid;
    }

    /**
     * 获得片头、片尾时间
     * @param cid
     * @param video
     * @return
     */
    public static VideoHot getHeadTailInfo(Integer categoryId, Integer bTime, Integer eTime) {
        VideoHot h = new VideoHot();
        if (categoryId != null && VideoConstants.Category.TV == categoryId) {
            h.setT(bTime == null ? 0 : bTime * 1000);
            h.setF(eTime == null ? 0 : eTime * 1000);
        }
        return h;
    }

    /**
     * 过滤特殊字符
     * @param str
     * @return
     */
    public static String StringFilter(String str) {
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(str);
        String result = m.replaceAll("").trim();
        try {
            result = URLEncoder.encode(result, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 验证合法性
     * @param store
     * @return
     */
    public static int validateMmsStore(MmsStore store) {

        int validateResult = validateMmsStore.SUCCESS;// 默认校验成功

        if (store == null) {
            validateResult = validateMmsStore.NULL_STORE;// 获得媒资播放文件为空
        } else {
            if ((store.getData() == null) || (store.getData().size() < 1)
                    || (store.getData().get(0).getInfos() == null) || (store.getData().get(0).getInfos().size() < 1)) {
                validateResult = validateMmsStore.NULL_STORE;
            }

            if (store.getPlayStatus() != null && 1 == store.getPlayStatus()
                    && StringUtils.isNotEmpty(store.getCountry()) && store.getCountry().equals("CN")) {
                validateResult = validateMmsStore.VIDEO_CN_PLAY_FORBIDDEN;
            }
            if (store.getPlayStatus() != null && 1 == store.getPlayStatus()
                    && StringUtils.isNotEmpty(store.getCountry()) && !store.getCountry().equals("CN")) {
                if (store.getCountry().equals("HK")) {
                    validateResult = validateMmsStore.VIDEO_HK_PLAY_FORBIDDEN;
                } else {
                    validateResult = validateMmsStore.VIDEO_NOT_CN_PLAY_FORBIDDEN;
                }
            }

            if (!store.getStatusCode().equalsIgnoreCase(VideoTpConstants.MmsStore.CODE_SUCCESS)) {
                validateResult = validateMmsStore.STORE_STATUS_NOT_SUCCESS;
            }
        }

        return validateResult;
    }

    /**
     * 获得VIP G3调度地址
     * @return
     */
    public static String getVIPUrl(String uid, UserPlayAuth userPlayAuth, String playUrl) {
        String userPlayUrl = playUrl;

        // VIP 用户加入token和uid
        if (userPlayAuth != null && userPlayAuth.getCode() != null && userPlayAuth.getCode().intValue() == 0
                && userPlayAuth.getValues() != null && "1".equalsIgnoreCase(userPlayAuth.getValues().getStatus())) {
            userPlayUrl = playUrl + "&token=" + userPlayAuth.getValues().getToken() + "&uid=" + uid;
        }

        return userPlayUrl;
    }

    /**
     * 返回试看时长
     * @param category
     * @param tailTime
     * @param duration
     * @return
     */
    public static Long getTryPlayTime(Integer category, Integer tailTime, Long duration) {
        Long trytime = 0l;
        if (VideoConstants.Category.TV == category || VideoConstants.Category.CARTOON == category) {// 电视剧、动漫有片尾返回片尾时间、否则返回时长
            if (tailTime != null && tailTime > 0) {
                trytime = new Long(tailTime);
            } else if (duration != null && duration > 0) {
                trytime = duration;
            }
        } else {// 电影返回6分钟
            trytime = new Long(1000 * 60 * 6);
        }
        return trytime;
    }

    /**
     * 获得预置码流
     * @return
     */
    public static Map<String, Stream> getPreSetStream(String locale) {

        if ("zh_hk".equalsIgnoreCase(locale)) {
            if (CollectionUtils.isEmpty(PRE_SET_STREAM_ZH_HK)) {
                PRE_SET_STREAM_ZH_HK = StreamConstants.getStreamMap(StreamConstants.ALL_STREAMS, locale);
            }

            return PRE_SET_STREAM_ZH_HK;
        } else if ("en_us".equalsIgnoreCase(locale)) {
            if (CollectionUtils.isEmpty(PRE_SET_STREAM_EN_US)) {
                PRE_SET_STREAM_EN_US = StreamConstants.getStreamMap(StreamConstants.ALL_STREAMS, locale);
            }

            return PRE_SET_STREAM_EN_US;
        } else {
            if (CollectionUtils.isEmpty(PRE_SET_STREAM_ZH_CN)) {
                PRE_SET_STREAM_ZH_CN = StreamConstants.getStreamMap(StreamConstants.ALL_STREAMS, locale);
            }

            return PRE_SET_STREAM_ZH_CN;
        }
    }

    /**
     * 获得用户设置码流列表
     * @param langCode
     * @param terminalApplication
     * @return
     */
    public static Stream getUserSettringStreams(String langCode, String terminalApplication) {

        if ("zh_hk".equalsIgnoreCase(langCode)) {
            if (CollectionUtils.isEmpty(USER_SETTING_STREAMS_ZH_HK.getPlayStreams())
                    || CollectionUtils.isEmpty(USER_SETTING_STREAMS_ZH_HK.getLiveStreams())) {

                USER_SETTING_STREAMS_ZH_HK.setPlayStreams(getUserSettingPlayStreams(langCode));
                USER_SETTING_STREAMS_ZH_HK.setLiveStreams(getUserSettingLiveStreams(langCode));
            }

            return USER_SETTING_STREAMS_ZH_HK;
        } else if ("en_us".equalsIgnoreCase(langCode)) {
            if (CollectionUtils.isEmpty(USER_SETTING_STREAMS_EN_US.getPlayStreams())
                    || CollectionUtils.isEmpty(USER_SETTING_STREAMS_EN_US.getLiveStreams())) {
                USER_SETTING_STREAMS_EN_US.setPlayStreams(getUserSettingPlayStreams(langCode));
                USER_SETTING_STREAMS_EN_US.setLiveStreams(getUserSettingLiveStreams(langCode));
            }

            return USER_SETTING_STREAMS_EN_US;
        } else {
            if (CollectionUtils.isEmpty(USER_SETTING_STREAMS_ZH_CN.getPlayStreams())
                    || CollectionUtils.isEmpty(USER_SETTING_STREAMS_ZH_CN.getLiveStreams())) {
                USER_SETTING_STREAMS_ZH_CN.setPlayStreams(getUserSettingPlayStreams(langCode));
                USER_SETTING_STREAMS_ZH_CN.setLiveStreams(getUserSettingLiveStreams(langCode));
            }

            return USER_SETTING_STREAMS_ZH_CN;
        }
    }

    private static List<Stream> getUserSettingPlayStreams(String langCode) {
        List<Stream> playStreams = new ArrayList<Stream>();
        String allPlayStream = StreamConstants.USER_SETTING_PLAY_STREAM;// 通用版客户端过滤4K

        String[] streamArr = allPlayStream.split("#");
        for (String key : streamArr) {
            Stream stream = new Stream();
            stream.setName(StreamConstants.nameOf(key, langCode));
            stream.setCode(key);
            if (key.equals(StreamConstants.CODE_NAME_720p)) {
                stream.setIsDefault(true);
            }
            if (StreamConstants.CODE_NAME_1080p6m.equals(key) || StreamConstants.CODE_NAME_1080p.equals(key)) {
                stream.setKbps(StreamConstants.getMbps(StreamConstants.CODE_NAME_1080p3m));
            } else {
                stream.setKbps(StreamConstants.getMbps(StreamConstants.CODE_NAME_800.equals(key) ? StreamConstants.CODE_NAME_1000
                        : key));
            }
            stream.setBandWidth(StreamConstants.getTipText(key, langCode));
            playStreams.add(stream);
        }

        return playStreams;
    }

    private static List<Stream> getUserSettingLiveStreams(String langCode) {
        List<Stream> liveStreams = new ArrayList<Stream>();
        String allLiveStream = StreamConstants.USER_SETTING_LIVE_STREAM;
        String[] liveStreamArr = allLiveStream.split("#");
        for (String key : liveStreamArr) {
            Stream stream = new Stream();
            stream.setName(StreamConstants.nameOf(key, langCode));
            stream.setCode(key);
            if (key.equals(StreamConstants.CODE_NAME_1300)) {
                stream.setIsDefault(true);
            }
            if (StreamConstants.CODE_NAME_1080p6m.equals(key) || StreamConstants.CODE_NAME_1080p.equals(key)) {
                stream.setKbps(StreamConstants.getMbps(StreamConstants.CODE_NAME_1080p3m));
            } else {
                stream.setKbps(StreamConstants.getMbps(StreamConstants.CODE_NAME_800.equals(key) ? StreamConstants.CODE_NAME_1000
                        : key));
            }
            stream.setBandWidth(StreamConstants.getLiveTipText(key, langCode));
            liveStreams.add(stream);
        }

        return liveStreams;
    }

    public static Integer parseAppCode(String appCode) {
        Integer ac = null;
        try {
            ac = Integer.parseInt(appCode);
        } catch (Exception e) {
        }
        return ac;
    }

    public static MmsFile getMmsFileByVTypeOrder(String playStream, MmsStore mmsStore) {
        MmsFile compatibleMmsFile = null;
        if (!ArrayUtils.isEmpty(StreamConstants.PLAY_REDUCED_MAP.get(playStream))) {
            Integer[] vtypesInOrder = StreamConstants.PLAY_REDUCED_MAP.get(playStream);

            for (Integer vtype : vtypesInOrder) {
                if (mmsStore != null && !CollectionUtils.isEmpty(mmsStore.getData())) {
                    MmsInfo mmsInfo = mmsStore.getData().get(0);
                    List<MmsFile> mmsFiles = mmsInfo.getInfos();
                    if (!CollectionUtils.isEmpty(mmsFiles)) {
                        for (MmsFile mmsFile : mmsFiles) {
                            if (mmsFile != null && vtype.intValue() == mmsFile.getVtype().intValue()) {
                                compatibleMmsFile = mmsFile;
                                break;
                            }
                        }
                    }
                }
                if (compatibleMmsFile != null) {
                    break;
                }
            }
        } else {
            if (mmsStore != null && !CollectionUtils.isEmpty(mmsStore.getData())) {
                MmsInfo mmsInfo = mmsStore.getData().get(0);
                compatibleMmsFile = mmsInfo.getInfos().get(0);
            }
        }

        return compatibleMmsFile;
    }

    public static Boolean checkSig(Map<String, Object> params) {
        String sig = (String) params.get("sig");
        params.remove("sig");
        String md5sig = CommonUtil.getMd5Str(params, downloadkey);
        if (!md5sig.equalsIgnoreCase(sig)) {
            return false;
        }

        return true;
    }

    public static String getMobileUrlWithParam(String playUrl, String vid) {
        playUrl = playUrl + "&p1=0&p2=00&p3=008&format=1&sign=mb&dname=mobile&expect=3&tag=mobile";
        if (StringUtils.isNotEmpty(vid)) {
            playUrl = playUrl + "&vid=" + vid;
        }
        return playUrl;
    }

    /**
     * m3v=3 10s ts
     * @param playUrl
     * @return
     */
    public static String getMobileUrl4MediaPlayer(String playUrl) {
        playUrl = playUrl + "&ext=m3u8&m3v=3";

        return playUrl;
    }

    public static List<WatchingFocusCache> getWatchingFocus(String watchingFocus) {
        List<WatchingFocusCache> wf = null;
        try {
            wf = objectMapper.readValue(watchingFocus, new TypeReference<ArrayList<WatchingFocusCache>>() {
            });
        } catch (Exception e) {

        }
        return wf;
    }
}
