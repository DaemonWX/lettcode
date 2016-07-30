package xserver.api.module.live;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import xserver.api.module.BaseService;
import xserver.api.module.live.dto.HotDto;
import xserver.api.module.superlive.SuperLiveConstant;
import xserver.api.response.Response;
import xserver.common.cache.CbaseRmiClient;
import xserver.lib.constant.LiveConstants;
import xserver.lib.constant.StreamConstants;
import xserver.lib.dto.live.LiveDto;
import xserver.lib.dto.live.LiveStream;
import xserver.lib.tp.live.response.HotTpResponse;
import xserver.lib.tp.live.response.HotTpResponse.DataItems;
import xserver.lib.tp.live.response.LiveChannelStream;
import xserver.lib.tp.live.response.LiveInfo;
import xserver.lib.util.CalendarUtil;
import xserver.lib.util.MD5;
import xserver.lib.util.MessageUtils;
import xserver.lib.util.TimeUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "liveService")
public class LiveService extends BaseService {
    private final Logger log = LoggerFactory.getLogger(LiveService.class);

    public static final Map<String, Object> liveMap = new HashMap<String, Object>();

    private CbaseRmiClient cbaseRmiClient = CbaseRmiClient.getInstance();

    /**
     * 获取直播对应的直播流，直播中和直播结束但是未生成vid的节目都需要用直播流观看视频
     * @param selectId
     * @param langCode
     * @return
     */
    private List<LiveStream> tp2StreamInfo(String selectId, String splitId, String langCode) {
        List<LiveStream> liveStream = new ArrayList<LiveStream>();
        if (selectId == null || selectId.length() < 1) {
            return liveStream;
        }
        List<LiveChannelStream> streamInfo = this.facadeTpDao.getLiveTpDao().getStreamResponseTp(splitId, selectId);
        if (!CollectionUtils.isEmpty(streamInfo)) {
            for (LiveChannelStream stream : streamInfo) {
                LiveStream dto = new LiveStream();
                String sCode = stream.getRateType().replace("flv_", "");
                dto.setCode(sCode);
                dto.setLiveUrl(stream.getStreamUrl(), splitId);
                dto.setStreamId(stream.getStreamId());
                dto.setvType(stream.getRateType());
                if (StringUtils.isNotEmpty(MessageUtils.getMessage(StreamConstants.STREAM_CODE_NAME_MAP.get(sCode),
                        langCode))) {
                    dto.setName(MessageUtils.getMessage(StreamConstants.STREAM_CODE_NAME_MAP.get(sCode), langCode));
                } else {
                    dto.setName(StreamConstants.STREAM_CODE_NAME_MAP.get(sCode));
                }

                liveStream.add(dto);
            }
            Collections.sort(liveStream);
        }

        return liveStream;
    }

    /**
     * 获取热门直播数据
     * @param splitId
     * @param langCode
     * @return
     */
    public Response<HotDto> hotList(String splitId, String langCode) {
        Response<HotDto> result = new Response<HotDto>();
        HotDto data = this.tpCacheTemplate.get(LiveConstants.LIVE_HOT_LIST_CACHE, HotDto.class);

        // 如果缓存中为空，请求第三方
        if (data == null) {
            data = new HotDto();
            HotTpResponse res = this.facadeTpDao.getLiveTpDao().getHotList(splitId);
            if (res != null && res.getSortHotItems().size() > 0) {
                /***
                 * { "sortHotItems": [ { "data": [ ], "displayName": "热门回看" }, {
                 * "data": [ ], "displayName": "热门直播" }, { "data": [ ],
                 * "displayName": "热门预告" } ] }
                 */
                LiveDto temp = null;
                for (DataItems d : res.getSortHotItems()) {
                    if (d != null && d.getData() != null && d.getData().size() > 0) {
                        int count = 0;// 标识当前数组长度，热门的每一类型数据都只返回20条
                        // 直播的时间点
                        long endTimeStmp = 0l;
                        long now = System.currentTimeMillis();
                        for (LiveInfo l : d.getData()) {

                            if (LiveConstants.LIVE_TYPE_SPORTS.equals(l.getLiveType())) {
                                // 体育直播只要求昨天开始，连续八天的数据，所以超过endTimeStmp的数据会被截掉
                                endTimeStmp = TimeUtil.getZeroPiont(System.currentTimeMillis(), 6 * 24 * 60 * 60
                                        * 1000l);
                            } else {
                                endTimeStmp = TimeUtil.getZeroPiont(System.currentTimeMillis(), 7 * 24 * 60 * 60
                                        * 1000l);
                            }

                            temp = this.resp2LiveDto(l, splitId, langCode, now, endTimeStmp);
                            if (temp == null) {
                                continue;
                            }

                            if (d.getDisplayName().equals(LiveConstants.HOT_REPLAY_NAME) && temp.getState() == 3) {
                                data.getHotReplay().add(temp);
                            } else if (d.getDisplayName().equals(LiveConstants.HOT_LIVE_NAME) && temp.getState() == 2) {
                                data.getHotLive().add(temp);
                            } else if (d.getDisplayName().equals(LiveConstants.HOT_AD_NAME) && temp.getState() == 1) {
                                data.getHotAd().add(temp);
                            }

                            if (++count > 19) {
                                break;
                            }
                        }
                    }
                }

                this.cbaseRmiClient.updateSync(LiveConstants.LIVE_HOT_LIST_CACHE, data, 200);
            }
        }
        result.setData(data);
        return result;
    }

    /**
     * @param liveType直播类型
     *            ：sports，music等
     * @param splitId子平台id
     * @param langCode
     * @return
     */
    public List<LiveDto> liveList(String liveType, String splitId, String langCode, int cacheFresh) {

        return this.getLiveList(liveType, splitId, langCode, cacheFresh);
    }

    /**
     * @param liveType直播类型
     *            ：sports，music等
     * @param splitId子平台id
     * @param langCode
     * @return
     */
    public LiveDto liveDetail(String liveType, String liveId, String splitId, String langCode) {

        if (liveType != null && liveType.length() > 0) {
            // 先取缓存
            LiveDto dto = this.fromMemoryMap(liveType, liveId, splitId, langCode);
            if (dto != null) {
                return dto;
            } else {
                // 缓存没有，则调用第三方
                return this.getLiveDto(liveType, liveId, splitId, langCode);
            }
        } else {
            LiveDto dto = new LiveDto();

            // 先遍历一遍缓存，获取直播详情
            for (int i = 0; i < LiveConstants.LIVE_TYPE.size(); i++) {
                liveType = LiveConstants.LIVE_TYPE.get("type_" + i);
                dto = this.fromMemoryMap(liveType, liveId, splitId, langCode);
                if (dto != null) {
                    return dto;
                } else {
                    continue;
                }
            }

            // 如果缓存中没有，则调用第三方
            for (int i = 0; i < LiveConstants.LIVE_TYPE.size(); i++) {
                liveType = LiveConstants.LIVE_TYPE.get("type_" + i);
                dto = this.getLiveDto(liveType, liveId, splitId, langCode);
                if (dto != null) {
                    return dto;
                } else {
                    continue;
                }
            }
            return null;
        }

    }

    private LiveDto getLiveDto(String liveType, String liveId, String splitId, String langCode) {

        List<LiveDto> liveList = this.getLiveList(liveType, splitId, langCode, 0);

        if (liveList != null && liveList.size() > 0) {
            for (LiveDto l : liveList) {
                if (l.getId().equals(liveId)) {
                    return l;
                }
            }
        }
        return null;
    }

    private List<LiveDto> getLiveList(String liveType, String splitId, String langCode, int cacheFresh) {
        // 从本地缓存中获取
        List<LiveDto> liveList = this.fromMemory(liveType, splitId, langCode);
        // 从内存中获取
        if (liveList == null || liveList.size() == 0) {
            liveList = this.fromCache(liveType, splitId, langCode);
        }
        // 从第三方直接获取
        if (cacheFresh == 1 || liveList == null || liveList.size() == 0) {
            liveList = this.fromTp(liveType, splitId, langCode);
        }

        return liveList;
    }

    /**
     * 获取缓存中的直播列表
     * @param liveType
     * @param splitId
     * @param langCode
     * @return
     */
    private List<LiveDto> fromMemory(String liveType, String splitId, String langCode) {
        String cacheCheckCode = this.tpCacheTemplate.get(LiveConstants.LIVE_LIST_CACHE_KEY + liveType + splitId,
                String.class);
        Object memoryCheckCode = liveMap.get(LiveConstants.LIVE_LIST_CACHE_KEY + liveType + splitId);
        if (cacheCheckCode != null && memoryCheckCode != null && cacheCheckCode.equals(memoryCheckCode)
                && liveMap.get(LiveConstants.LIVE_LIST_CACHE + liveType + splitId) != null) {
            return (List<LiveDto>) liveMap.get(LiveConstants.LIVE_LIST_CACHE + liveType + splitId);
        }
        return null;
    }

    /**
     * 获取缓存中的直播map
     * @param liveType
     * @param liveId
     * @param splitId
     * @param langCode
     * @return
     */
    private LiveDto fromMemoryMap(String liveType, String liveId, String splitId, String langCode) {
        String cacheCheckCode = this.tpCacheTemplate.get(LiveConstants.LIVE_LIST_CACHE_KEY + liveType + splitId,
                String.class);
        Object memoryCheckCode = liveMap.get(LiveConstants.LIVE_LIST_CACHE_KEY + liveType + splitId);
        if (cacheCheckCode != null && memoryCheckCode != null && cacheCheckCode.equals(memoryCheckCode)
                && liveMap.get(LiveConstants.LIVE_MAP_CACHE + liveType + splitId) != null) {
            Map<String, LiveDto> map = (HashMap<String, LiveDto>) liveMap.get(LiveConstants.LIVE_MAP_CACHE + liveType
                    + splitId);
            return map.get(LiveConstants.LIVE_CACHE_ID + liveType + splitId + liveId);
        }
        return null;
    }

    private List<LiveDto> fromCache(String liveType, String splitId, String langCode) {
        List<LiveDto> res = this.tpCacheTemplate.get(LiveConstants.LIVE_LIST_CACHE + liveType + splitId,
                new TypeReference<List<LiveDto>>() {
                });
        String checkCode = this.tpCacheTemplate.get(LiveConstants.LIVE_LIST_CACHE_KEY + liveType + splitId,
                String.class);
        if (res != null && res.size() > 0) {
            // 返回之前，先将数据存入jvm自身的内存区
            this.list2Memory(res, liveType, splitId, checkCode);
            return res;
        }
        return null;
    }

    private List<LiveDto> fromTp(String liveType, String splitId, String langCode) {
        List<LiveDto> data = new ArrayList<LiveDto>();
        TreeSet<LiveInfo> ltp = this.facadeTpDao.getLiveTpDao().getLiveList(liveType, splitId,
                this.getLiveStartDate(liveType));
        // Calendar endDate = CalendarUtil.getDateFromDate(new Date(), 7);

        if (ltp != null && ltp.size() > 0) {
            LiveDto temp = null;
            // 直播的时间点
            long endTimeStmp = 0l;
            long now = System.currentTimeMillis();
            for (LiveInfo l : ltp) {

                if (LiveConstants.LIVE_TYPE_SPORTS.equals(l.getLiveType())) {
                    // 体育直播只要求昨天开始，连续八天的数据，所以超过endTimeStmp的数据会被截掉
                    endTimeStmp = TimeUtil.getZeroPiont(System.currentTimeMillis(), 6 * 24 * 60 * 60 * 1000l);
                } else {
                    endTimeStmp = TimeUtil.getZeroPiont(System.currentTimeMillis(), 7 * 24 * 60 * 60 * 1000l);
                }

                temp = this.resp2LiveDto(l, splitId, langCode, now, endTimeStmp);
                if (temp == null) {
                    continue;
                }

                data.add(temp);
            }

            if (data.size() > 0) {
                this.list2Cache(data, liveType, splitId);
            }
        }

        return data;
    }

    private void list2Memory(List<LiveDto> list, String liveType, String splitId, String checkCode) {
        if (list != null && list.size() > 0) {
            // 存储列表
            liveMap.put(LiveConstants.LIVE_LIST_CACHE + liveType + splitId, list);

            // 存储单场直播
            Map<String, LiveDto> map = new HashMap<String, LiveDto>();
            for (LiveDto l : list) {
                // liveMap.put(LiveConstant.LIVE_LIST_CACHE_ID + liveType +
                // splitId + l.getId(), l);
                map.put(LiveConstants.LIVE_CACHE_ID + liveType + splitId + l.getId(), l);
                liveMap.put(LiveConstants.LIVE_MAP_CACHE + liveType + splitId, map);
            }
            // 存储校验key
            liveMap.put(LiveConstants.LIVE_LIST_CACHE_KEY + liveType + splitId, checkCode);
        }
    }

    private void list2Cache(List<LiveDto> list, String liveType, String splitId) {
        if (list != null && list.size() > 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            String checkCode = "";
            try {
                checkCode = MD5.Md5(objectMapper.writeValueAsString(list));
            } catch (IOException e) {
                log.error("liveList to String Exception:", e);
                e.printStackTrace();
            }
            // 放入缓存
            this.cbaseRmiClient.updateSync(LiveConstants.LIVE_LIST_CACHE + liveType + splitId, list, 180);
            this.cbaseRmiClient.updateSync(LiveConstants.LIVE_LIST_CACHE_KEY + liveType + splitId, checkCode, 180);
            // 加工后的数据放入jvm内存
            // this.list2Memory(list, checkCode);
        }
    }

    private LiveDto resp2LiveDto(LiveInfo l, String splitId, String langCode, long now, long endPoint) {
        // 付费类型的，存在录播Id,并且直播已经结束
        if (l.getIsPay().intValue() == 1 && l.getRecordingId() != null && l.getStatus() == 3) {
            l.setIsPay(0);
        }

        // 过滤不支持的直播类型
        if (!LiveConstants.SUPPORT_LIVE_TYPE.contains(l.getLiveType())) {
            return null;
        }

        // 只留预告，直播中，和可回看的直播节目
        if (l.getStatus() > 3) {
            return null;
        }

        Calendar tempBeginTime = CalendarUtil.parseCalendar(l.getBeginTime(), CalendarUtil.SIMPLE_DATE_FORMAT);
        Calendar tempEndTime = CalendarUtil.parseCalendar(l.getEndTime(), CalendarUtil.SIMPLE_DATE_FORMAT);
        // 筛除产品规定时间以后的数据
        if (tempBeginTime.getTimeInMillis() > endPoint) {
            return null;
        }

        // 2天后或者已结束状态，并且无vid的直播，过滤掉 —— 不过滤，改用后台“录播id”
        if ((now - tempEndTime.getTimeInMillis() > 2 * 24 * 60 * 60 * 1000l || l.getStatus() == 3)
                && l.getRecordingId() == null) {
            return null;
        }

        LiveDto liveDto = new LiveDto();
        liveDto.setId(l.getId());
        liveDto.setState(l.getStatus());
        liveDto.setStartTime(TimeUtil.string2timestamp(l.getBeginTime()));
        liveDto.setEndTime(TimeUtil.string2timestamp(l.getEndTime()));
        liveDto.setLiveContentDesc(l.getDescription());
        liveDto.setVid(l.getRecordingId() != null ? l.getRecordingId().toString() : null);
        liveDto.setAlbumId(l.getPid() != null ? l.getPid().toString() : null);
        liveDto.setCh(l.getCh());
        liveDto.setType(l.getLiveType());
        liveDto.setIsPay(l.getIsPay());
        liveDto.setScreenings(l.getScreenings());
        // 已结束的直播可以直接转为点播观看用vid点播即可，未开始的直播只有预告，所以不需要请求直播直播流
        // 直播已结束的时候，视频转码还没有完成的时候，vid是空，这时候还是需要直播流信息
        // if (l.getStatus() == 2 || (l.getStatus() == 3 &&
        // StringUtils.isEmpty(l.getVid()))) {
        liveDto.setLiveStream(this.tp2StreamInfo(l.getSelectId(), splitId, langCode));
        // 默认码流
        if (!CollectionUtils.isEmpty(liveDto.getLiveStream())) {
            liveDto.setPlayUrl(liveDto.getLiveStream().get(0).getLiveUrl());
            liveDto.setvType(liveDto.getLiveStream().get(0).getCode());
        }
        // 如果直播流和转码的vid都没有，证明改直播无法观看，过滤掉即可
        if (StringUtils.isEmpty(liveDto.getVid()) && CollectionUtils.isEmpty(liveDto.getLiveStream())) {
            log.error("LiveId:" + l.getId() + ": could not watch,no vid and stream!");
            return null;
        }
        if (LiveConstants.LIVE_TYPE_SPORTS.equals(l.getLiveType())) {
            liveDto.setScore(getScore(l.getHomescore(), l.getGuestscore()));
            if (l.getIsVS() != null && l.getIsVS() == 1) {
                liveDto.setLiveName(getPk(l.getHome(), l.getGuest(), liveDto.getScore()));
            } else {
                liveDto.setLiveName(l.getTitle());
            }
            liveDto.setMatchStage(l.getLevel2()
                    + ((!StringUtils.isEmpty(l.getCommentaryLanguage()) && !l.getLevel2().contains(
                            l.getCommentaryLanguage())) ? "(" + l.getCommentaryLanguage() + ")" : ""));
            liveDto.setSubType(l.getLevel2()
                    + ((!StringUtils.isEmpty(l.getCommentaryLanguage()) && !l.getLevel2().contains(
                            l.getCommentaryLanguage())) ? "(" + l.getCommentaryLanguage() + ")" : ""));
            liveDto.setSportsType(l.getLevel1());
            liveDto.setSportsSubType(l.getLevel2());
            liveDto.setSeason(l.getSeason());
            liveDto.setIcon1(l.getHomeImgUrl());
            liveDto.setIcon2(l.getGuestImgUrl());
            liveDto.setIsVs(l.getIsVS());
            if (l.getIsVS() == 1) {
                liveDto.setGuest(l.getGuest());
                liveDto.setHome(l.getHome());
            } else {
                liveDto.setIcon1(l.getLevel2ImgUrl());
            }
        } else if (LiveConstants.LIVE_TYPE_MUSIC.equals(l.getLiveType())) {
            liveDto.setSubType(SuperLiveConstant.MUSIC_TYPE.get(l.getType() + ""));
            liveDto.setLiveName(l.getTitle());
            liveDto.setIcon1(l.getTypeICO());
        } else if (LiveConstants.LIVE_TYPE_ENT.equals(l.getLiveType())) {
            liveDto.setSubType(SuperLiveConstant.ENT_TYPE.get(l.getType() + ""));
            liveDto.setLiveName(l.getTitle());
            liveDto.setIcon1(l.getTypeICO());
        } else if (LiveConstants.LIVE_TYPE_BRAND.equals(l.getLiveType())) {
            liveDto.setSubType(SuperLiveConstant.BRAND_TYPE.get(l.getType() + ""));
            liveDto.setLiveName(l.getTitle());
            liveDto.setIcon1(l.getTypeICO());
        } else if (LiveConstants.LIVE_TYPE_OTHER.equals(l.getLiveType())) {
            liveDto.setSubType(SuperLiveConstant.OTHER_TYPE.get(l.getType() + ""));
            liveDto.setLiveName(l.getTitle());
            liveDto.setIcon1(l.getTypeICO());
        }
        return liveDto;
    }

    private String getLiveStartDate(String liveType) {
        if (LiveConstants.LIVE_TYPE_SPORTS.equals(liveType)) {// 体育
            return CalendarUtil.getDateString(CalendarUtil.getDateFromDate(new Date(), -1),
                    CalendarUtil.SHORT_DATE_FORMAT_NO_DASH);
        } else {// 音乐，娱乐，品牌，其他
            return CalendarUtil.getDateString(CalendarUtil.getDateFromDate(new Date(), -89),
                    CalendarUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
    }

    private static String getPk(String home, String guest, String score) {
        // return home + score + guest;
        return home + " - " + guest;
    }

    private static String getScore(String homeScore, String guestScore) {
        homeScore = homeScore == null ? "" : homeScore;
        guestScore = guestScore == null ? "" : guestScore;
        return " " + homeScore + "-" + guestScore + " ";
    }

}
