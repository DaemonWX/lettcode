package xserver.api.module.video;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.video.constants.VideoErrorCodeConstants;
import xserver.api.module.video.dto.AlbumDto;
import xserver.api.module.video.dto.AlbumSeriesDto;
import xserver.api.module.video.dto.HotWordDto;
import xserver.api.module.video.dto.LikeDto;
import xserver.api.module.video.dto.PositiveAlbumDto;
import xserver.api.module.video.dto.PositiveRecommendDto;
import xserver.api.module.video.dto.SeriesPage;
import xserver.api.module.video.dto.VideoDto;
import xserver.api.module.video.dto.VideoPlayListDto;
import xserver.api.response.Response;
import xserver.common.dto.BaseDto;
import xserver.lib.constant.CommonConstants;
import xserver.lib.constant.VideoConstants;
import xserver.lib.dto.video.InteractDto;
import xserver.lib.tp.cloud.response.HotWordsCountResponse;
import xserver.lib.tp.cloud.response.UserHotWordCount;
import xserver.lib.tp.rec.request.RecBaseRequest;
import xserver.lib.tp.rec.response.RecBaseResponse;
import xserver.lib.tp.rec.response.RecBaseResponse.RecommendDetail;
import xserver.lib.tp.video.request.LikeQueryRequest;
import xserver.lib.tp.video.request.LikeRequest;
import xserver.lib.tp.video.response.BatchCountStatTpResponse;
import xserver.lib.tp.video.response.LikeQueryResponse;
import xserver.lib.tp.video.response.LikeResponse;
import xserver.lib.tp.video.response.TotalCountStatTpResponse;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.tpcache.cache.ActorCache;
import xserver.lib.tpcache.cache.HotWordsCache;
import xserver.lib.tpcache.cache.LinesCache;
import xserver.lib.tpcache.cache.LiveWaterMarkCache;
import xserver.lib.tpcache.cache.MusicCache;
import xserver.lib.tpcache.cache.OstCache;
import xserver.lib.tpcache.cache.Page;
import xserver.lib.tpcache.cache.PlayCache;
import xserver.lib.tpcache.cache.Stream;
import xserver.lib.util.CalendarUtil;
import xserver.lib.util.CommonUtil;
import xserver.lib.util.TimeUtil;

@Service(value = "VideoService")
public class VideoService extends BaseService {

    private final static Logger log = LoggerFactory.getLogger(VideoService.class);

    public List<LinesCache> getAlbumIdLinesCache(Long albumId) {
        List<LinesCache> lines = null;
        if (albumId != null) {
            lines = this.tpCacheTemplate.get(CacheConstants.Lines_Pid_ + albumId,
                    new TypeReference<List<LinesCache>>() {
                    });
        }
        return lines;
    }

    /**
     * 将乐词关注数放入缓存
     * @param id
     * @param attention
     */
    public void putHotWordsAttention2Cache(String id, Integer attention) {
        if (id != null) {
            String key = CacheConstants.HotWord_Attention_ + id;
            this.cbaseRmiClient.updateSync(key, attention, CalendarUtil.SECONDS_OF_PER_HOUR);
            log.debug("putHotWordsAttention2Cache key:" + key);
        } else {
            log.error("putHotWordsAttention2Cache failed id is null");
        }
    }

    /**
     * 从缓存获取乐词关注度
     * @param id
     * @return
     */
    public Integer getHotWordsAttentionCache(String id) {
        return this.tpCacheTemplate.get(CacheConstants.HotWord_Attention_ + id, Integer.class);
    }

    /**
     * 删除乐词关注度缓存
     * @param id
     */
    public void deleteHotWordsAttentionCache(String id) {
        if (id != null) {
            String key = CacheConstants.HotWord_Attention_ + id;
            this.cbaseRmiClient.deleteSync(key);
            log.debug("deleteHotWordsAttention key:" + key);
        } else {
            log.error("deleteHotWordsAttention failed id is null");
        }
    }

    /**
     * 获得剧集列表样式
     * @param category
     * @return
     */
    private Integer getSeriesStyle(Integer category) {
        Integer style = 1;
        if (VideoConstants.Category.TV == category || VideoConstants.Category.CARTOON == category) {
            style = 2;
        }
        return style;
    }

    /**
     * 获取视频关联的片段
     * @param videoId
     * @return
     */
    public List<VideoDto> getSegmentsByVideoId(Long videoId) {
        List<VideoDto> videoList = null;
        if (videoId != null) {
            videoList = this.tpCacheTemplate.get(CacheConstants.Segments_Vid_ + videoId,
                    new TypeReference<List<VideoDto>>() {
                    });
        }
        return videoList;
    }

    /**
     * 从CACHE中获得视频对象
     * @param videoId
     * @return
     */
    public PlayCache getPlayCacheEntityByVideoId(Long videoId) {
        PlayCache playCacheEntity = null;

        if (videoId != null) {
            playCacheEntity = tpCacheTemplate.get(CacheConstants.PlayCacheEntity_V_ + videoId, PlayCache.class);
            if (playCacheEntity == null) {
                log.info("ERROR: " + CacheConstants.PlayCacheEntity_V_ + videoId + " is NULL");
            }
        }

        return playCacheEntity;
    }

    /**
     * 获得对应版权的播放对象
     * @param videoId
     * @param playPlatform
     * @return
     */
    public PlayCache getPlayCacheEntityByVideoId(Long videoId, String... playPlatform) {
        PlayCache playCache = null;

        if (videoId != null) {
            playCache = tpCacheTemplate.get(CacheConstants.PlayCacheEntity_V_ + videoId, PlayCache.class);
            boolean hasPlatform = false;
            if (playCache != null && StringUtils.isNotEmpty(playCache.getvPlayPlatform())) {
                for (String platform : playPlatform) {
                    if (playCache.getvPlayPlatform().contains(platform)) {
                        hasPlatform = true;
                    }
                }
            }

            if (!hasPlatform) {
                playCache = null;
            }
        }

        return playCache;
    }

    public PlayCache getPlayCacheEntityByAlbumId(Long albumId) {
        PlayCache playCacheEntity = null;

        if (albumId != null) {
            playCacheEntity = tpCacheTemplate.get(CacheConstants.PlayCacheEntity_A_ + albumId, PlayCache.class);
            if (playCacheEntity == null) {
                log.info("ERROR: " + CacheConstants.PlayCacheEntity_A_ + albumId + " is NULL");
            }
        }

        return playCacheEntity;
    }

    public PlayCache getPlayCacheEntityByAlbumId(Long albumId, String... playPlatform) {
        PlayCache playCache = null;

        if (albumId != null) {
            playCache = tpCacheTemplate.get(CacheConstants.PlayCacheEntity_A_ + albumId, PlayCache.class);
            boolean hasPlatform = false;
            if (playCache != null && StringUtils.isNotEmpty(playCache.getvPlayPlatform())) {
                for (String platform : playPlatform) {
                    if (playCache.getvPlayPlatform().contains(platform)) {
                        hasPlatform = true;
                    }
                }
            }

            if (!hasPlatform) {
                playCache = null;
            }
        }

        return playCache;
    }

    /**
     * 得到详情页剧集所在页
     * @param albumId
     * @param categoryId
     * @param varietyShow
     * @param page
     * @param pageList
     * @return
     */
    public Integer getSeriesPage(Long albumId, Integer categoryId, String varietyShow, Integer page) {
        Integer realPage = null;
        if (categoryId == null) {
            categoryId = -1;
        }
        switch (categoryId) {
        case VideoConstants.Category.TV:
        case VideoConstants.Category.FILM:
        case VideoConstants.Category.CARTOON:
            realPage = page;
            break;
        case VideoConstants.Category.MUSIC:
            if ("1".equals(varietyShow)) {
                realPage = 0;
            }
            break;
        case VideoConstants.Category.SPORT:
        case VideoConstants.Category.CAI_JING:
        case VideoConstants.Category.GONG_KAI_KE:
        case VideoConstants.Category.FENG_SHANG:
        case VideoConstants.Category.TRAVEL:
        case VideoConstants.Category.CAR:
        case VideoConstants.Category.EDUCATION:
        case VideoConstants.Category.PARENTING:
        case VideoConstants.Category.ENT:
        case VideoConstants.Category.HOTSPOT:
        case VideoConstants.Category.ZIXUN:
            if ("1".equals(varietyShow)) {
                realPage = page / 100;
            } else {
                realPage = page;
            }
            break;
        case VideoConstants.Category.VARIETY:
            realPage = page / 100;
            break;
        case VideoConstants.Category.DFILM:
            if ("1".equals(varietyShow)) {
                realPage = page / 100;
            } else {
                realPage = page;
            }
            break;
        default:
            break;
        }
        return realPage;
    }

    public List<VideoDto> getPageSeries(List<Page> pageList, Integer page, Long albumId) {
        List<VideoDto> iptvVideoList = null;
        for (Page st : pageList) {
            if (st.getPage() != null && st.getPage().intValue() == page.intValue()) {
                iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId + "_"
                        + page, new TypeReference<List<VideoDto>>() {
                });
            }
        }
        return iptvVideoList;
    }

    public List<VideoDto> getYearSeries(List<Page> pageList, Integer page, Long albumId) {
        List<VideoDto> iptvVideoList = new ArrayList<VideoDto>();
        for (Page st : pageList) {
            if (st.getPage() != null
                    && ((st.getPage().intValue() / 100) == page || (st.getPage().intValue() / 100) == (page / 100))) {
                List<VideoDto> videos = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_MONTH
                        + albumId + "_" + st.getPage().intValue(), new TypeReference<List<VideoDto>>() {
                });
                if (!CollectionUtils.isEmpty(videos)) {
                    iptvVideoList.addAll(videos);
                }
            }
        }
        return iptvVideoList;
    }

    public List<VideoDto> getYearLatestVideos(List<Page> pageList, Long albumId) {
        List<VideoDto> latestVideos = new ArrayList<VideoDto>();
        for (int i = 0; i < pageList.size(); i++) {
            if (pageList.get(i).getPage() != null) {
                List<VideoDto> latest = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_MONTH
                        + albumId + "_" + pageList.get(i).getPage().intValue(), new TypeReference<List<VideoDto>>() {
                });
                if (!CollectionUtils.isEmpty(latest)) {
                    latestVideos.addAll(latest);
                    if (latestVideos.size() >= 3) {
                        latestVideos = latestVideos.subList(0, 3);
                        break;
                    }
                }
            }
        }
        return latestVideos;
    }

    public List<VideoDto> getPageLatestVideos(List<Page> pageList, Long albumId) {
        List<VideoDto> latestVideos = new ArrayList<VideoDto>();
        for (int i = pageList.size() - 1; i >= 0; i--) {
            if (pageList.get(i).getPage() != null) {
                List<VideoDto> latest = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId
                        + "_" + pageList.get(i).getPage().intValue(), new TypeReference<List<VideoDto>>() {
                });
                if (!CollectionUtils.isEmpty(latest)) {
                    Collections.reverse(latest);
                    latestVideos.addAll(latest);
                    if (latestVideos.size() >= 3) {
                        latestVideos = latestVideos.subList(0, 3);
                        break;
                    }
                }
            }
        }
        return latestVideos;
    }

    public List<VideoDto> getLatestVideos(Long albumId, Integer categoryId, String varietyShow, List<Page> pageList) {
        List<VideoDto> latestVideos = new ArrayList<VideoDto>();

        if (categoryId == null) {
            categoryId = -1;
        }

        switch (categoryId) {
        case VideoConstants.Category.TV:
        case VideoConstants.Category.FILM:
        case VideoConstants.Category.CARTOON:
            break;
        case VideoConstants.Category.MUSIC:
            if ("1".equals(varietyShow)) {
                latestVideos = getPageLatestVideos(pageList, albumId);
            }
            break;
        case VideoConstants.Category.SPORT:
        case VideoConstants.Category.CAI_JING:
        case VideoConstants.Category.GONG_KAI_KE:
        case VideoConstants.Category.FENG_SHANG:
        case VideoConstants.Category.TRAVEL:
        case VideoConstants.Category.CAR:
        case VideoConstants.Category.EDUCATION:
        case VideoConstants.Category.PARENTING:
        case VideoConstants.Category.ENT:
        case VideoConstants.Category.HOTSPOT:
        case VideoConstants.Category.ZIXUN:
            if ("1".equals(varietyShow)) {
                latestVideos = getYearLatestVideos(pageList, albumId);
            } else {
                latestVideos = getPageLatestVideos(pageList, albumId);
            }
            break;

        case VideoConstants.Category.VARIETY:
            latestVideos = getYearLatestVideos(pageList, albumId);
            break;

        case VideoConstants.Category.DFILM:
            if ("1".equals(varietyShow)) {
                latestVideos = getYearLatestVideos(pageList, albumId);
            } else {
                latestVideos = getPageLatestVideos(pageList, albumId);
            }
            break;
        default:
            break;
        }
        return latestVideos;
    }

    public List<Page> getYearPageList(List<Page> pageList) {
        List<Page> series = new ArrayList<Page>();
        Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
        if (!CollectionUtils.isEmpty(pageList)) {
            for (Page page : pageList) {
                int year = page.getPage().intValue() / 100;
                int pageSize = page.getPageSize().intValue();
                int originSize = 0;
                if (map.containsKey(year)) {
                    originSize = map.get(year);
                }
                pageSize = originSize + pageSize;
                map.put(year, pageSize);
            }
            for (int key : map.keySet()) {
                Page p = new Page();
                p.setPage(key);
                p.setPageSize(map.get(key));
                series.add(p);
            }
        }
        return series;
    }

    public List<Page> getRealPageList(Integer categoryId, String varietyShow, List<Page> pageList) {
        List<Page> series = new ArrayList<Page>();
        if (categoryId == null) {
            categoryId = -1;
        }

        switch (categoryId) {
        case VideoConstants.Category.TV:
        case VideoConstants.Category.FILM:
        case VideoConstants.Category.CARTOON:
            series = pageList;
            break;
        case VideoConstants.Category.MUSIC:
            if ("1".equals(varietyShow)) {
                series = pageList;
            }
            break;
        case VideoConstants.Category.SPORT:
        case VideoConstants.Category.CAI_JING:
        case VideoConstants.Category.GONG_KAI_KE:
        case VideoConstants.Category.FENG_SHANG:
        case VideoConstants.Category.TRAVEL:
        case VideoConstants.Category.CAR:
        case VideoConstants.Category.EDUCATION:
        case VideoConstants.Category.PARENTING:
        case VideoConstants.Category.ENT:
        case VideoConstants.Category.HOTSPOT:
        case VideoConstants.Category.ZIXUN:
            if ("1".equals(varietyShow)) {
                series = getYearPageList(pageList);
            } else {
                series = pageList;
            }
            break;

        case VideoConstants.Category.VARIETY:
            series = getYearPageList(pageList);
            break;

        case VideoConstants.Category.DFILM:
            if ("1".equals(varietyShow)) {
                series = getYearPageList(pageList);
            } else {
                series = pageList;
            }
            break;
        default:
            series = pageList;
            break;
        }
        return series;
    }

    public List<VideoDto> getPositiveAlbumSeries(Long albumId, Integer categoryId, String varietyShow, Integer page,
            List<Page> pageList, Page currentPage) {
        List<VideoDto> iptvVideoList = new ArrayList<VideoDto>();

        if (categoryId == null) {
            categoryId = -1;
        }

        switch (categoryId) {
        case VideoConstants.Category.TV:
        case VideoConstants.Category.FILM:
        case VideoConstants.Category.CARTOON:
            if (currentPage.getPage() != null && currentPage.getPage().intValue() == page.intValue()) {
                iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId + "_"
                        + page, new TypeReference<List<VideoDto>>() {
                });
            }
            break;
        case VideoConstants.Category.MUSIC:
            if ("1".equals(varietyShow)) {
                if (currentPage.getPage() != null && currentPage.getPage().intValue() == page.intValue()) {
                    iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId
                            + "_" + page, new TypeReference<List<VideoDto>>() {
                    });
                }
            }
            break;
        case VideoConstants.Category.SPORT:
        case VideoConstants.Category.CAI_JING:
        case VideoConstants.Category.GONG_KAI_KE:
        case VideoConstants.Category.FENG_SHANG:
        case VideoConstants.Category.TRAVEL:
        case VideoConstants.Category.CAR:
        case VideoConstants.Category.EDUCATION:
        case VideoConstants.Category.PARENTING:
        case VideoConstants.Category.ENT:
        case VideoConstants.Category.HOTSPOT:
        case VideoConstants.Category.ZIXUN:
            if ("1".equals(varietyShow)) {
                if (currentPage.getPage() != null
                        && (currentPage.getPage().intValue() == page || currentPage.getPage().intValue() == (page / 100))) {
                    iptvVideoList = getYearSeries(pageList, page, albumId);
                }

                if (CollectionUtils.isEmpty(iptvVideoList)) {
                    iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_MONTH + albumId
                            + "_0", new TypeReference<List<VideoDto>>() {
                    });
                }
            } else {
                if (currentPage.getPage() != null && currentPage.getPage().intValue() == page.intValue()) {
                    iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId
                            + "_" + page, new TypeReference<List<VideoDto>>() {
                    });
                }
            }
            break;

        case VideoConstants.Category.VARIETY:
            if (currentPage.getPage() != null
                    && (currentPage.getPage().intValue() == page || currentPage.getPage().intValue() == (page / 100))) {
                iptvVideoList = getYearSeries(pageList, page, albumId);
            }
            break;

        case VideoConstants.Category.DFILM:
            if ("1".equals(varietyShow)) {
                if (currentPage.getPage() != null
                        && (currentPage.getPage().intValue() == page || currentPage.getPage().intValue() == (page / 100))) {
                    iptvVideoList = getYearSeries(pageList, page, albumId);
                }
            } else {
                if (currentPage.getPage() != null && currentPage.getPage().intValue() == page.intValue()) {
                    iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId
                            + "_" + page, new TypeReference<List<VideoDto>>() {
                    });
                }
            }
            break;
        default:
            if (currentPage.getPage() != null && currentPage.getPage().intValue() == page.intValue()) {
                iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId + "_"
                        + page, new TypeReference<List<VideoDto>>() {
                });
            }
            break;
        }
        return iptvVideoList;
    }

    /**
     * 获得专辑下视频列表
     * @param albumId
     * @param categoryId
     * @param broadcastId
     * @return
     */
    public List<VideoDto> getAlbumSeries(Long albumId, Integer categoryId, String subCategoryId, String varietyShow,
            Integer page, Integer pageSize) {

        List<VideoDto> iptvVideoList = null;

        if (categoryId == null) {
            categoryId = -1;
        }
        switch (categoryId) {
        case VideoConstants.Category.TV:
        case VideoConstants.Category.FILM:
        case VideoConstants.Category.CARTOON:
            iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId + "_"
                    + page, new TypeReference<List<VideoDto>>() {
            });
            break;
        case VideoConstants.Category.MUSIC:
            if ("1".equals(varietyShow)) {
                iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId + "_"
                        + page, new TypeReference<List<VideoDto>>() {
                });
            }
            break;
        case VideoConstants.Category.SPORT:
        case VideoConstants.Category.CAI_JING:
        case VideoConstants.Category.GONG_KAI_KE:
        case VideoConstants.Category.FENG_SHANG:
        case VideoConstants.Category.TRAVEL:
        case VideoConstants.Category.CAR:
        case VideoConstants.Category.EDUCATION:
        case VideoConstants.Category.PARENTING:
        case VideoConstants.Category.ENT:
        case VideoConstants.Category.HOTSPOT:
        case VideoConstants.Category.ZIXUN:
            if ("1".equals(varietyShow)) {
                iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_MONTH + albumId + "_"
                        + page, new TypeReference<List<VideoDto>>() {
                });
            } else {
                iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId + "_"
                        + page, new TypeReference<List<VideoDto>>() {
                });
            }
            break;

        case VideoConstants.Category.VARIETY:
            iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_MONTH + albumId + "_"
                    + page, new TypeReference<List<VideoDto>>() {
            });
            break;

        case VideoConstants.Category.DFILM:
            if ("1".equals(varietyShow)) {
                iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_MONTH + albumId + "_"
                        + page, new TypeReference<List<VideoDto>>() {
                });
            } else {
                iptvVideoList = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId + "_"
                        + page, new TypeReference<List<VideoDto>>() {
                });
            }
            break;
        default:
            break;
        }

        return iptvVideoList;
    }

    private String cast2year(String date) {
        String year = date;

        if (StringUtils.isNotEmpty(date)) {
            if (date.matches("\\d{4}")) {
                year = date;
            } else if (date.length() > 4 && (date.substring(0, 4)).matches("\\d{4}")) {
                year = date.substring(0, 4);
            }
        }

        return year;
    }

    /**
     * 获取频道和专辑的所有活水印
     * @param keys
     * @return
     */
    public Map<String, List<LiveWaterMarkCache>> getLiveWaterMarkCache(List<String> keys) {
        if (keys == null || keys.size() <= 0) {
            return null;
        }
        Map<String, List<LiveWaterMarkCache>> liveWaterMarkCaches = this.tpCacheTemplate.mget(keys,
                new TypeReference<List<LiveWaterMarkCache>>() {
                });
        if (liveWaterMarkCaches == null) {
            log.error("getLiveWaterMarkCache failed liveWaterMarkCaches is null");
        }
        return liveWaterMarkCaches;
    }

    public BaseDto getAlbumInfo(Long albumId, Long videoId, Integer page, Integer pageSize, CommonParam param,
            Integer isTimerCall, Integer act) {
        AlbumDto album = new AlbumDto(1);
        PlayCache playCacheEntity = null;
        // 半屏详情页一定跟播放器一起走的，必然会有播放视频的vid
        if (videoId != null) {
            playCacheEntity = this.facadeService.getVideoService().getPlayCacheEntityByVideoId(videoId,
                    CommonUtil.PLATFORMS);
            if (playCacheEntity != null) {
                // 简介
                setAlbumSummary(playCacheEntity, album);

                Integer categoryId = playCacheEntity.getvCategoryId();
                String varietyShow = playCacheEntity.getaVarietyShow();
                Integer videoType = playCacheEntity.getvType();
                if (albumId == null) {
                    albumId = playCacheEntity.getaId();
                }
                if (playCacheEntity != null) {
                    if (playCacheEntity.isMobPay()) {
                        album.setCharge(1);
                    } else {
                        album.setCharge(0);
                    }
                }

                List<Stream> downloadStreams = playCacheEntity.getaStreams();
                if (!CollectionUtils.isEmpty(downloadStreams)) {
                    album.setStreams(downloadStreams);
                }

                // 获取台词信息
                setLinesInfo(albumId, videoId, album);
                // 关注 相关信息
                setAttention(playCacheEntity, album);
                int hasAlbum = (playCacheEntity.getaId() != null && playCacheEntity.getaId() != 0) ? 1 : 0;
                // 是否需要剧集
                int needSeries = needSeries(albumId, categoryId, videoType, varietyShow, hasAlbum);
                if (needSeries == 0) {
                    album.setType(0);
                    // 剧集
                    setPositiveSeries(albumId, page, categoryId, varietyShow, album, act);
                    // 周边视频
                    setAlbumNearlyVideos(albumId, album, playCacheEntity.getaRelationId(), categoryId);
                } else {
                    album.setType(1);
                }
                // 相关内容
                setRelation(albumId, videoId, categoryId, varietyShow, videoType, playCacheEntity, album);
            } else {
                // TODO
            }
        } else if (albumId != null) {// 客户端在分页请求第二页的时候不传videoid
            playCacheEntity = this.facadeService.getVideoService().getPlayCacheEntityByAlbumId(albumId,
                    CommonUtil.PLATFORMS);

            if (playCacheEntity != null) {
                Integer categoryId = playCacheEntity.getaCategoryId();
                String varietyShow = playCacheEntity.getaVarietyShow();

                List<Stream> downloadStreams = playCacheEntity.getaStreams();
                if (!CollectionUtils.isEmpty(downloadStreams)) {
                    album.setStreams(downloadStreams);
                }
                if (act != null && act == 1) {
                    // 下载页面需要的相关属性
                    setDownloadDetail(playCacheEntity, album);
                } else {
                    // 关注 相关信息
                    setAttention(playCacheEntity, album);
                }
                // 只返回剧集
                setPositiveSeries(albumId, page, categoryId, varietyShow, album, act);
            }
        } else {
            // TODO 错误码 无专辑或者视频id 无法播放
        }

        return album;

    }

    public Response<BaseDto> getDetail(Long albumId, Long videoId, Integer page, CommonParam param,
            Integer isTimerCall, Integer act) {
        Response<BaseDto> res = new Response<BaseDto>();
        AlbumDto album = new AlbumDto(1);
        PlayCache playCacheEntity = null;
        // 半屏详情页一定跟播放器一起走的，必然会有播放视频的vid
        if (videoId != null) {
            playCacheEntity = this.facadeService.getVideoService().getPlayCacheEntityByVideoId(videoId,
                    CommonUtil.PLATFORMS);
            if (playCacheEntity != null) {

                // 简介
                setAlbumSummary(playCacheEntity, album);

                Integer categoryId = playCacheEntity.getvCategoryId();
                String varietyShow = playCacheEntity.getaVarietyShow();
                Integer videoType = playCacheEntity.getvType();
                if (playCacheEntity != null) {
                    if (playCacheEntity.isMobPay()) {
                        album.setCharge(1);
                    } else {
                        album.setCharge(0);
                    }
                }

                // 关注 相关信息
                setAttention(playCacheEntity, album);
                // 是否需要剧集
                int hasAlbum = (playCacheEntity.getaId() != null && playCacheEntity.getaId() != 0) ? 1 : 0;
                int needSeries = needSeries(albumId, categoryId, videoType, varietyShow, hasAlbum);
                if (needSeries == 0) {
                    album.setType(0);
                    // 剧集
                    setAlbumSeries(albumId, page, categoryId, varietyShow, album, act);
                    // 周边视频
                    setAlbumNearlyVideos(albumId, album, playCacheEntity.getaRelationId(), categoryId);
                }

                // 相关内容
                setRelation(albumId, videoId, categoryId, varietyShow, videoType, playCacheEntity, album);
            } else {
                // TODO
            }
        } else if (albumId != null) {// 客户端在分页请求第二页的时候不传videoid
            playCacheEntity = this.facadeService.getVideoService().getPlayCacheEntityByAlbumId(albumId,
                    CommonUtil.PLATFORMS);

            if (playCacheEntity != null) {
                Integer categoryId = playCacheEntity.getaCategoryId();
                String varietyShow = playCacheEntity.getaVarietyShow();

                if (act != null && act == 1) {
                    // 下载页面需要的相关属性
                    setDownloadDetail(playCacheEntity, album);
                } else {
                    // 关注 相关信息
                    setAttention(playCacheEntity, album);
                }
                // 只返回剧集
                setAlbumSeries(albumId, page, categoryId, varietyShow, album, act);
            }
        } else {
            // TODO 错误码 无专辑或者视频id 无法播放
        }

        res.setData(album);
        return res;
    }

    public List<VideoDto> getAllVideoListByYear(Long albumId, String year) {
        List<VideoDto> iptvVideoList = new ArrayList<VideoDto>();
        List<Page> seriesTitle = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOS_PAGELIST_ALBUM + albumId,
                new TypeReference<List<Page>>() {
                });
        List<Page> years = new ArrayList<Page>();
        if (!CollectionUtils.isEmpty(seriesTitle)) {
            for (Page page : seriesTitle) {
                String pageNum = page.getPage().toString();
                if (pageNum.indexOf(year) != -1) {
                    years.add(page);
                }
            }
        }

        if (!CollectionUtils.isEmpty(years)) {
            for (Page st : years) {
                List<VideoDto> videos = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_MONTH
                        + albumId + "_" + st.getPage(), new TypeReference<List<VideoDto>>() {
                });
                if (!CollectionUtils.isEmpty(videos)) {
                    iptvVideoList.addAll(videos);
                }
            }
        }
        return iptvVideoList;
    }

    public void setDownloadDetail(PlayCache playCacheEntity, AlbumDto album) {
        album.setType(0);
        album.setAlbumId(playCacheEntity.getaId() != null ? playCacheEntity.getaId().toString() : "");
        album.setCategoryId(playCacheEntity.getaCategoryId());
        album.setPositive(playCacheEntity.getaAttr() != null ? playCacheEntity.getaAttr() : 0);
        album.setAlbumTypeId(playCacheEntity.getaType());
        album.setName(playCacheEntity.getaNameCn());
        album.setNowEpisode(playCacheEntity.getaNowEpisodes() == null ? "0" : playCacheEntity.getaNowEpisodes());
        album.setEpisodes(playCacheEntity.getaEpisode());
        album.setEnd((playCacheEntity.getaIsEnd() != null && playCacheEntity.getaIsEnd() == 1) ? 1 : 0);
        if (StringUtils.isNotEmpty(playCacheEntity.getaVarietyShow())) {
            album.setVarietyShow(Integer.valueOf(playCacheEntity.getaVarietyShow()));
        }
        String seriesShow = this.getSeriesShow(playCacheEntity.getaCategoryId(), playCacheEntity.getaVarietyShow());
        if (StringUtils.isNotBlank(seriesShow)) {
            album.setSeriesShow(seriesShow);
        }
    }

    public void setLinesInfo(Long albumId, Long videoId, AlbumDto album) {
        List<LinesCache> lines = this.getAlbumIdLinesCache(albumId);
        if (!CollectionUtils.isEmpty(lines)) {
            for (LinesCache cache : lines) {
                cache.setVid(videoId);
            }
            album.setLines(lines);
        }
    }

    public void setAttention(PlayCache playCache, AlbumDto album) {
        List<ActorCache> starList = new ArrayList<ActorCache>();
        if (!CollectionUtils.isEmpty(playCache.getaStarList())) {
            starList.addAll(playCache.getaStarList());
        }
        album.setStarList(starList);

        List<HotWordsCache> aHotWords = playCache.getaHotWords();
        if (!CollectionUtils.isEmpty(aHotWords)) {
            List<HotWordDto> hotWords = new ArrayList<HotWordDto>();
            HotWordsCache hotWordsCache = null;
            HotWordDto hotWordDto = null;
            for (int i = 0; i < aHotWords.size(); i++) {
                hotWordDto = new HotWordDto();
                hotWordsCache = aHotWords.get(i);
                hotWordDto.setId(hotWordsCache.getId());
                hotWordDto.setName(hotWordsCache.getName());
                hotWordDto.setImg(hotWordsCache.getImg());
                String id = hotWordDto.getId().toString();
                // 查询乐词关注度缓存
                Integer attention = this.facadeService.getVideoService().getHotWordsAttentionCache(id);
                if (attention == null) {
                    // 调用乐视云查询此剧集乐词关注度
                    HotWordsCountResponse response = this.facadeTpDao.getAttTpDao().getUserHotWordsCount(
                            hotWordsCache.getId().toString());
                    if (response != null && "10000".equals(response.getErrno()) && response.getData() != null) {
                        for (String key : response.getData().keySet()) {
                            if (hotWordsCache.getId() == NumberUtils.toInt(key)) {
                                UserHotWordCount userHotWordCount = response.getData().get(key);
                                attention = userHotWordCount.getCount();
                            }
                        }
                    }
                    // 将乐词关注度放缓存
                    this.facadeService.getVideoService().putHotWordsAttention2Cache(id, attention);
                }
                hotWordDto.setAttention(attention);
                hotWords.add(hotWordDto);
            }
            album.setHotWords(hotWords);
        }
    }

    public boolean hasPositive(Long albumId, Integer categoryId, String varietyShow) {
        boolean hasPositive = false;
        List<Page> seriesTitle = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOS_PAGELIST_ALBUM + albumId,
                new TypeReference<List<Page>>() {
                });
        if (!CollectionUtils.isEmpty(seriesTitle)) {
            for (Page page : seriesTitle) {
                if (page.getPageSize() != null && page.getPageSize().intValue() > 0) {
                    hasPositive = true;
                }
            }
        }
        return hasPositive;
    }

    public int needSeries(Long albumId, Integer categoryId, Integer videoType, String varietyShow, int hasAlbum) {
        int type = 1;// 默认两栏
        if (hasAlbum != 0) {

            switch (categoryId) {
            case VideoConstants.Category.FILM:
            case VideoConstants.Category.TV:
            case VideoConstants.Category.CARTOON:
            case VideoConstants.Category.VARIETY:
            case VideoConstants.Category.DFILM:
            case VideoConstants.Category.PARENTING:
            case VideoConstants.Category.EDUCATION:
                if (videoType != null && videoType.intValue() == 180001) {
                    type = 0; // 3栏
                } else {
                    type = 1; // 2栏
                }

                break;
            case VideoConstants.Category.ENT:
            case VideoConstants.Category.MUSIC:
            case VideoConstants.Category.CAR:
            case VideoConstants.Category.TRAVEL:
            case VideoConstants.Category.FENG_SHANG:
            case VideoConstants.Category.HOTSPOT:
            case VideoConstants.Category.AD:
            case VideoConstants.Category.ZIXUN:
                if ("1".equalsIgnoreCase(varietyShow)) {
                    type = 0; // 3栏
                } else {
                    type = 1; // 2栏
                }

                break;
            case VideoConstants.Category.SPORT:
            case VideoConstants.Category.CAI_JING:
            case VideoConstants.Category.GONG_KAI_KE:
                if ("1".equalsIgnoreCase(varietyShow)) {
                    if (videoType != null && videoType.intValue() == 180001) {
                        type = 0; // 3栏
                    } else {
                        type = 1; // 2栏
                    }
                } else {
                    type = 1; // 2栏
                }
                break;
            default:
                type = 0;// 3栏
                break;
            }

            if (type == 0 && !hasPositive(albumId, categoryId, varietyShow)) {
                type = 1;
            }
        }

        return type;
    }

    private List<BaseDto> getRecSeriesList(String relationAlbumIds) {
        List<BaseDto> recList = new ArrayList<BaseDto>();
        if (StringUtils.isNotEmpty(relationAlbumIds)) {
            String[] ids = relationAlbumIds.split(",");
            if (!ArrayUtils.isEmpty(ids)) {
                Map<String, AlbumDto> albumMap = new HashMap<String, AlbumDto>();
                StringBuffer albumIds = new StringBuffer();
                for (String id : ids) {
                    if (StringUtils.isNotEmpty(id)) {
                        PlayCache relationAlbum = this.getPlayCacheEntityByAlbumId(NumberUtils.toLong(id),
                                CommonUtil.PLATFORMS);
                        if (relationAlbum != null) {
                            AlbumDto aDto = new AlbumDto(0);
                            aDto.setAlbumId(relationAlbum.getaId().toString());
                            aDto.setName(relationAlbum.getaNameCn());
                            aDto.setImg(relationAlbum.getAPic(400, 250));
                            aDto.setEnd(relationAlbum.getaIsEnd());
                            aDto.setEpisodes(relationAlbum.getaEpisode());
                            aDto.setCategoryId(relationAlbum.getaCategoryId());
                            aDto.setNowEpisode(relationAlbum.getaNowEpisodes());
                            aDto.setDirector(relationAlbum.getaDirectory());
                            aDto.setStarring(relationAlbum.getaStarring());
                            aDto.setDuration(relationAlbum.getaDuration() == null ? 0 : relationAlbum.getaDuration()
                                    .longValue());
                            aDto.setAreaName(relationAlbum.getaAreaName());
                            aDto.setSinger(relationAlbum.getaStarring());// 频道9时把singer赋值给此字段
                            aDto.setSubCategoryName(relationAlbum.getaSubCategoryName());
                            aDto.setScore(relationAlbum.getaScore().toString());
                            aDto.setPlayTvName(relationAlbum.getaPlayTvName());
                            aDto.setCharge(relationAlbum.isMobPay() ? 1 : 0);
                            aDto.setSubName(relationAlbum.getaSubTitle());
                            if (StringUtils.isNotBlank(relationAlbum.getaReleaseDate())) {
                                String[] date = relationAlbum.getaReleaseDate().split("-");
                                if (!ArrayUtils.isEmpty(date)) {
                                    aDto.setReleaseDate(date[0]);
                                }
                            }
                            albumMap.put(id, aDto);
                            albumIds.append(id).append(",");
                        }
                    }
                }
                if (StringUtils.isNotEmpty(albumIds.toString())) {
                    String aIds = albumIds.deleteCharAt(albumIds.length() - 1).toString();
                    List<BatchCountStatTpResponse> statATpRes = this.facadeTpDao.getStatTpDao().getBatchCountStat(aIds,
                            "plist");
                    for (BatchCountStatTpResponse batchCountStatTpResponse : statATpRes) {
                        AlbumDto a = (AlbumDto) albumMap.get(batchCountStatTpResponse.getId());
                        a.setVv(batchCountStatTpResponse.getPlay_count());
                        a.setCommentCnt(batchCountStatTpResponse.getPcomm_count());
                        a.setScore(batchCountStatTpResponse.getPlist_score());
                    }

                    String[] result = aIds.split(",");
                    for (String id : result) {
                        AlbumDto a = (AlbumDto) albumMap.get(id);
                        if (a != null) {
                            recList.add(a);
                        }
                    }
                }
            }
        }
        return recList;
    }

    private List<BaseDto> getRecList(RecBaseResponse recResponse, Map<String, BaseDto> recAlbumMap,
            Map<String, BaseDto> recVideoMap, StringBuffer statAIds, StringBuffer statVIds, String currentVideoId,
            String relationAlbumIds, String varietyShow) {
        List<BaseDto> recList = new ArrayList<BaseDto>();
        if (recResponse != null && !CollectionUtils.isEmpty(recResponse.getRec())) {
            // 数据上报
            String reid = recResponse.getReid();
            String area = recResponse.getArea();
            String bucket = recResponse.getBucket();
            for (RecommendDetail recommendDetail : recResponse.getRec()) {
                if (recommendDetail.getVid() == null) {
                    if (StringUtils.isNotBlank(relationAlbumIds)) {
                        String[] relationAlbumId = relationAlbumIds.split(",");
                        if (recommendDetail.getPid() != null
                                && ArrayUtils.contains(relationAlbumId, recommendDetail.getPid().toString())) {
                            continue;
                        }
                    }
                    AlbumDto aDto = new AlbumDto(0);
                    aDto.setAlbumId(recommendDetail.getPid() != null ? recommendDetail.getPid().toString() : null);
                    aDto.setName(recommendDetail.getPidname());
                    aDto.setImg(recommendDetail.getImageBySize(400, 250));
                    aDto.setEnd(recommendDetail.getIsend());
                    aDto.setEpisodes(recommendDetail.getEpisodes());
                    aDto.setCategoryId(recommendDetail.getCid());
                    aDto.setNowEpisode(recommendDetail.getVcount() != null ? recommendDetail.getVcount().toString()
                            : null);
                    aDto.setDirector(recommendDetail.getDirector());
                    aDto.setStarring(recommendDetail.getStarring());
                    aDto.setDuration(recommendDetail.getDuration() != null ? recommendDetail.getDuration() : 0l);
                    aDto.setAreaName(recommendDetail.getAlbum_area());
                    // aDto.setReleaseDate(recommendDetail.getCreatetime());
                    aDto.setSinger(recommendDetail.getStarring());// 频道9时把singer赋值给此字段
                    aDto.setSubCategoryName(recommendDetail.getAlbum_sub_category());
                    aDto.setScore(recommendDetail.getScore());
                    aDto.setPlayTvName(recommendDetail.getAlbum_play_tv());
                    aDto.setCharge(Integer.valueOf(recommendDetail.getIs_pay()));
                    // aDto.setReleaseDate(recommendDetail.getCreatetime());
                    aDto.setSubName(recommendDetail.getSubtitle());
                    if (StringUtils.isNotBlank(recommendDetail.getAlbum_release_date())) {
                        String[] date = recommendDetail.getAlbum_release_date().split("-");
                        if (!ArrayUtils.isEmpty(date)) {
                            aDto.setReleaseDate(date[0]);
                        }
                    }

                    aDto.setRecReid(reid);
                    aDto.setRecBucket(bucket);
                    aDto.setRecArea(area);
                    aDto.setPageid("001001");
                    aDto.setSrc("1");

                    statAIds.append(recommendDetail.getPid()).append(",");
                    recAlbumMap.put(recommendDetail.getPid().toString(), aDto);
                } else {
                    VideoDto vDto = new VideoDto(1);
                    vDto.setVideoId(recommendDetail.getVid() != null ? recommendDetail.getVid().toString() : null);
                    vDto.setImg(recommendDetail.getImageBySize(400, 250));
                    vDto.setDuration(recommendDetail.getDuration());
                    vDto.setName(recommendDetail.getTitle());
                    vDto.setCategoryId(recommendDetail.getCid());
                    vDto.setGuest(recommendDetail.getGuest());
                    vDto.setSinger(recommendDetail.getStarring());// 频道9时把singer赋值给此字段
                    vDto.setCreateTime(TimeUtil.string2timestamp(recommendDetail.getCreatetime()));
                    vDto.setAreaName(recommendDetail.getVideo_area());
                    vDto.setSubCategoryName(recommendDetail.getVideo_sub_category());
                    vDto.setVideoTypeId(NumberUtils.toInt(recommendDetail.getVideo_type()));
                    vDto.setVideoType(recommendDetail.getVideo_type_name());
                    vDto.setSubName(recommendDetail.getSubtitle());
                    if (StringUtils.isNotBlank(recommendDetail.getVideo_release_date())) {
                        String[] date = recommendDetail.getVideo_release_date().split("-");
                        if (!ArrayUtils.isEmpty(date)) {
                            vDto.setReleaseDate(date[0]);
                        }
                    }

                    vDto.setRecReid(reid);
                    vDto.setRecBucket(bucket);
                    vDto.setRecArea(area);
                    vDto.setPageid("001001");
                    vDto.setSrc("1");
                    statVIds.append(recommendDetail.getVid()).append(",");
                    recVideoMap.put(recommendDetail.getVid().toString(), vDto);
                }
            }
            String statAIdsStr = statAIds.toString();
            if (StringUtils.isNotBlank(statAIdsStr)) {
                statAIdsStr = statAIdsStr.substring(0, statAIdsStr.length() - 1);
                List<BatchCountStatTpResponse> statATpRes = this.facadeTpDao.getStatTpDao().getBatchCountStat(
                        statAIdsStr, "plist");
                for (BatchCountStatTpResponse batchCountStatTpResponse : statATpRes) {
                    AlbumDto a = (AlbumDto) recAlbumMap.get(batchCountStatTpResponse.getId());
                    a.setVv(batchCountStatTpResponse.getPlay_count());
                    a.setCommentCnt(batchCountStatTpResponse.getPcomm_count());
                    a.setScore(batchCountStatTpResponse.getPlist_score());
                }
            }
            String statVIdsStr = statVIds.toString();
            if (StringUtils.isNotBlank(statVIdsStr)) {
                statVIdsStr = statVIdsStr.substring(0, statVIdsStr.length() - 1);
                List<BatchCountStatTpResponse> statTpRes = this.facadeTpDao.getStatTpDao().getBatchCountStat(
                        statVIdsStr, "vlist");
                for (BatchCountStatTpResponse batchCountStatTpResponse : statTpRes) {
                    VideoDto v = (VideoDto) recVideoMap.get(batchCountStatTpResponse.getId());
                    if (batchCountStatTpResponse != null) {
                        v.setVv(batchCountStatTpResponse.getPlay_count());
                        v.setCommentCnt(batchCountStatTpResponse.getVcomm_count());
                    }
                }
            }

            if (StringUtils.isNotBlank(currentVideoId) && (StringUtils.isEmpty(varietyShow) || "0".equals(varietyShow))) {
                VideoDto v = (VideoDto) recVideoMap.get(currentVideoId);
                if (v != null) {
                    recList.add(v);
                }
            }

            for (RecommendDetail recommendDetail : recResponse.getRec()) {
                if (recommendDetail.getVid() == null) {
                    AlbumDto a = (AlbumDto) recAlbumMap.get(recommendDetail.getPid().toString());
                    if (a != null) {
                        recList.add(a);
                    }
                } else {
                    VideoDto v = (VideoDto) recVideoMap.get(recommendDetail.getVid().toString());
                    if (v != null) {
                        recList.add(v);
                    }
                }
            }
        }
        return recList;
    }

    private void setRelation(Long albumId, Long videoId, Integer categoryId, String varietyShow, Integer videoType,
            PlayCache playCacheEntity, AlbumDto album) {
        RecBaseRequest recRequest = new RecBaseRequest();
        recRequest.setVid(videoId);
        recRequest.setCid(categoryId);
        recRequest.setNum(20);
        StringBuffer statAIds = new StringBuffer();
        StringBuffer statVIds = new StringBuffer();
        Map<String, BaseDto> recAlbumMap = new HashMap<String, BaseDto>();
        Map<String, BaseDto> recVideoMap = new HashMap<String, BaseDto>();
        switch (categoryId) {
        case VideoConstants.Category.FILM:
        case VideoConstants.Category.TV:
        case VideoConstants.Category.CARTOON:
        case VideoConstants.Category.VARIETY:
        case VideoConstants.Category.DFILM:
        case VideoConstants.Category.EDUCATION:
            if (videoType.intValue() == 180001) {
                List<BaseDto> recSeriesList = getRecSeriesList(playCacheEntity.getaRelationAlbumId());
                // 推荐专辑
                recRequest.setArea("rec_0023");
                recRequest.setPid(albumId);

                RecBaseResponse recResponse = this.facadeTpDao.getRecommendTpDao().recommend(recRequest);
                List<BaseDto> recList = this.getRecList(recResponse, recAlbumMap, recVideoMap, statAIds, statVIds,
                        null, playCacheEntity.getaRelationAlbumId(), varietyShow);
                recSeriesList.addAll(recList);
                album.setRelation(recSeriesList);
            } else {
                // 推荐视频
                recRequest.setArea("rec_0021");

                // 当前播放的视频加入推荐列表
                VideoDto curVideo = new VideoDto(1);
                curVideo.setVideoId(playCacheEntity.getvId().toString());
                curVideo.setImg(playCacheEntity.getVPic(400, 250));
                curVideo.setDuration(Long.valueOf(playCacheEntity.getvDuration()));
                curVideo.setName(playCacheEntity.getvNameCn());
                curVideo.setCategoryId(playCacheEntity.getvCategoryId());
                curVideo.setGuest(playCacheEntity.getvGuest());
                curVideo.setSinger(playCacheEntity.getvSinger());
                curVideo.setCreateTime((playCacheEntity.getvCreateTime().getTime()));

                curVideo.setAreaName(playCacheEntity.getvAreaName());
                curVideo.setSubCategoryName(playCacheEntity.getvSubCategoryName());
                curVideo.setVideoTypeId(playCacheEntity.getvType());
                curVideo.setSubName(playCacheEntity.getvSubTitle());
                if (StringUtils.isNotBlank(playCacheEntity.getvReleaseDate())) {
                    String[] date = playCacheEntity.getvReleaseDate().split("-");
                    if (!ArrayUtils.isEmpty(date)) {
                        curVideo.setReleaseDate(date[0]);
                    }
                }

                statVIds.append(curVideo.getVideoId()).append(",");
                recVideoMap.put(curVideo.getVideoId(), curVideo);
                RecBaseResponse recResponse = this.facadeTpDao.getRecommendTpDao().recommend(recRequest);
                List<BaseDto> recList = this.getRecList(recResponse, recAlbumMap, recVideoMap, statAIds, statVIds,
                        curVideo.getVideoId(), null, varietyShow);
                album.setRelation(recList);
            }
            break;

        default:
            // 推荐视频
            recRequest.setArea("rec_0021");

            // 当前播放的视频加入推荐列表
            VideoDto curVideo = new VideoDto(1);
            curVideo.setVideoId(playCacheEntity.getvId().toString());
            curVideo.setImg(playCacheEntity.getVPic(400, 250));
            curVideo.setDuration(Long.valueOf(playCacheEntity.getvDuration()));
            curVideo.setName(playCacheEntity.getvNameCn());
            curVideo.setCategoryId(playCacheEntity.getvCategoryId());
            curVideo.setGuest(playCacheEntity.getvGuest());
            curVideo.setSinger(playCacheEntity.getvSinger());
            curVideo.setCreateTime((playCacheEntity.getvCreateTime().getTime()));

            statVIds.append(curVideo.getVideoId()).append(",");
            recVideoMap.put(curVideo.getVideoId(), curVideo);

            RecBaseResponse recResponse = this.facadeTpDao.getRecommendTpDao().recommend(recRequest);
            List<BaseDto> recList = this.getRecList(recResponse, recAlbumMap, recVideoMap, statAIds, statVIds,
                    curVideo.getVideoId(), null, varietyShow);
            album.setRelation(recList);
            break;
        }
    }

    private void setAlbumNearlyVideos(Long albumId, AlbumDto album, String relationId, Integer categoryId) {
        if (categoryId != null) {
            switch (categoryId) {
            // 特殊频道没有周边视频
            case VideoConstants.Category.ENT:
            case VideoConstants.Category.HOTSPOT:
            case VideoConstants.Category.CAR:
            case VideoConstants.Category.TRAVEL:
            case VideoConstants.Category.FENG_SHANG:
            case VideoConstants.Category.MUSIC:
            case VideoConstants.Category.ZIXUN:
                break;
            case VideoConstants.Category.FILM:
            case VideoConstants.Category.TV:
            case VideoConstants.Category.CARTOON:
            case VideoConstants.Category.AD:
            case VideoConstants.Category.SPORT:
            case VideoConstants.Category.PARENTING:
            case VideoConstants.Category.CAI_JING:
            case VideoConstants.Category.GONG_KAI_KE:
            case VideoConstants.Category.VARIETY:
            case VideoConstants.Category.DFILM:
            case VideoConstants.Category.EDUCATION:
                // 周边视频
                setNearlyVideos(albumId, album, relationId, categoryId);
                break;
            default:
                // 默认频道没有周边视频
                break;
            }
        }
    }

    private void setNearlyVideos(Long albumId, AlbumDto album, String relationId, Integer categoryId) {
        List<VideoDto> nearlyVideos = new ArrayList<VideoDto>();
        List<VideoDto> nearlyRelationVideos = null;
        if (StringUtils.isNotBlank(relationId)) {
            nearlyRelationVideos = this.tpCacheTemplate.get(CacheConstants.OTHER_VIDEOLIST_ALBUM + relationId,
                    new TypeReference<List<VideoDto>>() {
                    });
        }
        // 综艺的周边视频里没有片段类型的视频
        if (categoryId == VideoConstants.Category.VARIETY) {
            List<VideoDto> result = filterPianduanList(nearlyRelationVideos);
            if (!CollectionUtils.isEmpty(result)) {
                nearlyVideos.addAll(result);
            }
            if (nearlyVideos.size() < 5) {// 如果不够5个，再取本专辑的非正片，最多5个
                List<VideoDto> nearlyAddVideos = this.tpCacheTemplate.get(CacheConstants.OTHER_VIDEOLIST_ALBUM
                        + albumId, new TypeReference<List<VideoDto>>() {
                });
                List<VideoDto> filterList = filterPianduanList(nearlyAddVideos);
                if (!CollectionUtils.isEmpty(filterList)) {
                    nearlyVideos.addAll(filterList);
                }
            }
        } else {
            if (!CollectionUtils.isEmpty(nearlyRelationVideos)) {
                nearlyVideos.addAll(nearlyRelationVideos);
            }
            if (nearlyVideos.size() < 5) {// 如果不够5个，再取本专辑的非正片，最多5个
                List<VideoDto> nearlyAddVideos = this.tpCacheTemplate.get(CacheConstants.OTHER_VIDEOLIST_ALBUM
                        + albumId, new TypeReference<List<VideoDto>>() {
                });
                if (!CollectionUtils.isEmpty(nearlyAddVideos)) {
                    nearlyVideos.addAll(nearlyAddVideos);
                }
            }
        }
        if (nearlyVideos.size() > 5) {
            nearlyVideos.subList(0, 5);
        }
        album.setNearlyVideos(nearlyVideos);
    }

    /**
     * 周边视频过滤掉片段
     * @param nearlyVideos
     * @return
     */
    public List<VideoDto> filterPianduanList(List<VideoDto> nearlyVideos) {
        List<VideoDto> notPositiveVideos = new ArrayList<VideoDto>();
        if (!CollectionUtils.isEmpty(nearlyVideos)) {
            for (VideoDto video : nearlyVideos) {
                if (video.getVideoTypeId() != null
                        && video.getVideoTypeId().intValue() != VideoConstants.VideoType.PIAN_DUAN) {
                    notPositiveVideos.add(video);
                }
            }
        }
        return notPositiveVideos;
    }

    private void setPositiveSeries(Long albumId, Integer page, Integer categoryId, String varietyShow, AlbumDto album,
            Integer act) {
        // 剧集列表样式
        album.setSeriesStyle(getSeriesStyle(categoryId));

        List<Page> seriesTitle = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOS_PAGELIST_ALBUM + albumId,
                new TypeReference<List<Page>>() {
                });
        if (categoryId != null && categoryId.intValue() == VideoConstants.Category.FILM) {
            album.setDownload(canDownloadSeries(album, albumId, varietyShow, categoryId));
        }
        List<SeriesPage> seriesPages = new ArrayList<SeriesPage>();
        if (!CollectionUtils.isEmpty(seriesTitle)) {
            if (act != null && act == 1 && page == null) {
                page = seriesTitle.get(0).getPage() == null ? 0 : seriesTitle.get(0).getPage();
            }
            if (page == null) {
                page = 0;
            }
            // 最新三期
            List<VideoDto> latestVideos = getLatestVideos(albumId, categoryId, varietyShow, seriesTitle);
            if (!CollectionUtils.isEmpty(latestVideos)) {
                album.setLatestVideoList(getVvAndCommentCountVideoList(album.getSeriesStyle(), latestVideos));
            }

            List<Page> realPageList = getRealPageList(categoryId, varietyShow, seriesTitle);

            List<VideoDto> iptvVideoList = null;
            for (Page st : realPageList) {
                SeriesPage sp = new SeriesPage();
                sp.setPage(st.getPage());
                sp.setPageSize(st.getPageSize());
                iptvVideoList = getPositiveAlbumSeries(albumId, categoryId, varietyShow, page, seriesTitle, st);
                if (!CollectionUtils.isEmpty(iptvVideoList)) {
                    sp.setPositiveSeries(getVvAndCommentCountVideoList(album.getSeriesStyle(), iptvVideoList));
                }

                if (categoryId == VideoConstants.Category.TV || categoryId == VideoConstants.Category.CARTOON) {
                    if (sp.getPageSize() > 100) {
                        sp.setPageSize(100);
                    }
                }
                seriesPages.add(sp);
            }
            album.setPositiveSeries(seriesPages);
        }
    }

    public Integer canDownloadSeries(AlbumDto album, Long albumId, String varietyShow, Integer categoryId) {
        List<VideoDto> videos = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId + "_0",
                new TypeReference<List<VideoDto>>() {
                });
        if (!CollectionUtils.isEmpty(videos)) {
            for (VideoDto video : videos) {
                if (video.getDownload().intValue() == 1) {
                    return 1;
                }
            }
        }

        return 0;
    }

    private void setAlbumSeries(Long albumId, Integer page, Integer categoryId, String varietyShow, AlbumDto album,
            Integer act) {
        // 剧集列表样式
        album.setSeriesStyle(getSeriesStyle(categoryId));

        List<Page> seriesTitle = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOS_PAGELIST_ALBUM + albumId,
                new TypeReference<List<Page>>() {
                });
        if (categoryId != null && categoryId.intValue() == VideoConstants.Category.FILM) {
            album.setDownload(canDownloadSeries(album, albumId, varietyShow, categoryId));
        }
        List<SeriesPage> seriesPages = new ArrayList<SeriesPage>();
        if (!CollectionUtils.isEmpty(seriesTitle)) {
            if (act != null && act == 1 && page == null) {
                page = seriesTitle.get(0).getPage() == null ? 0 : seriesTitle.get(0).getPage();
            }
            if (page == null) {
                page = 0;
            }
            for (Page st : seriesTitle) {
                SeriesPage sp = new SeriesPage();
                sp.setPage(st.getPage());
                sp.setPageSize(st.getPageSize());

                if ((st.getPage() != null && st.getPage().intValue() == page.intValue())) {
                    List<VideoDto> iptvVideoList = getAlbumSeries(albumId, categoryId, "", varietyShow, page, 100);
                    if (!CollectionUtils.isEmpty(iptvVideoList)) {
                        sp.setPositiveSeries(getVvAndCommentCountVideoList(album.getSeriesStyle(), iptvVideoList));
                    }
                }
                seriesPages.add(sp);
            }

            album.setPositiveSeries(seriesPages);
        }
    }

    public List<VideoDto> getVvAndCommentCountVideoList(Integer seriesStyle, List<VideoDto> iptvVideoList) {
        List<VideoDto> videoList = new ArrayList<VideoDto>();
        if (seriesStyle.intValue() == 1) {
            int index = 0;
            while (index < iptvVideoList.size()) {
                StringBuffer vIds = new StringBuffer();
                Map<String, VideoDto> videosMap = new HashMap<String, VideoDto>();
                int end = (index + 100) > iptvVideoList.size() ? iptvVideoList.size() : (index + 100);
                for (int i = index; i < end; i++) {
                    VideoDto video = iptvVideoList.get(i);
                    vIds.append(video.getVideoId()).append(",");
                    videosMap.put(video.getVideoId(), video);
                }

                String statVIdsStr = vIds.toString();
                if (StringUtils.isNotBlank(statVIdsStr)) {
                    statVIdsStr = statVIdsStr.substring(0, statVIdsStr.length() - 1);
                    List<BatchCountStatTpResponse> statTpRes = this.facadeTpDao.getStatTpDao().getBatchCountStat(
                            statVIdsStr, "vlist");
                    for (BatchCountStatTpResponse batchCountStatTpResponse : statTpRes) {
                        VideoDto v = (VideoDto) videosMap.get(batchCountStatTpResponse.getId());
                        if (batchCountStatTpResponse != null) {
                            v.setVv(batchCountStatTpResponse.getPlay_count());
                            v.setCommentCnt(batchCountStatTpResponse.getVcomm_count());
                        }
                        videoList.add(v);
                    }
                }
                index = end;
            }
        } else {
            videoList = iptvVideoList;
        }
        return videoList;
    }

    public String getSeriesShow(Integer categoryId, String varietyShow) {
        String seriesShow = null;
        if (StringUtils.isNotEmpty(varietyShow)) {
            seriesShow = varietyShow;
            if (categoryId != null) {
                switch (categoryId) {
                case VideoConstants.Category.MUSIC:
                case VideoConstants.Category.ENT:
                case VideoConstants.Category.HOTSPOT:
                    if ("1".equals(varietyShow)) {
                        seriesShow = "0";
                    } else if ("0".equals(varietyShow)) {
                        seriesShow = "1";
                    }
                    break;
                default:
                    break;
                }
            }
        }
        return seriesShow;
    }

    public void setAlbumSummary(PlayCache playCacheEntity, AlbumDto album) {
        Integer categoryId = playCacheEntity.getvCategoryId();
        String varietyShow = playCacheEntity.getaVarietyShow();

        album.setAlbumId(playCacheEntity.getaId() != null ? playCacheEntity.getaId().toString() : "");
        album.setCategoryId(playCacheEntity.getaCategoryId());
        album.setPositive(playCacheEntity.getaAttr() != null ? playCacheEntity.getaAttr() : 0);
        album.setAlbumTypeId(playCacheEntity.getaType());
        album.setPlayPlatform(playCacheEntity.getaPlayPlatform());
        album.setImg(playCacheEntity.getAPic(400, 250));
        album.setHomeMade(playCacheEntity.getaIsHomemade());
        // album.setRelationMusic(playCacheEntity.getaAudioInfoObj());//
        // 关联音乐，来源VRS

        if (StringUtils.isNotEmpty(playCacheEntity.getaVarietyShow())) {
            album.setVarietyShow(Integer.valueOf(playCacheEntity.getaVarietyShow()));
        }

        String seriesShow = this.getSeriesShow(categoryId, varietyShow);
        if (StringUtils.isNotBlank(seriesShow)) {
            album.setSeriesShow(seriesShow);
        }

        if (StringUtils.isNotEmpty(playCacheEntity.getaDownloadPlatform())
                && playCacheEntity.getaDownloadPlatform().contains("290003") && !playCacheEntity.isMobPay()) {
            album.setDownload(1);
        } else {
            album.setDownload(0);
        }

        if (playCacheEntity.getaId() != null) {// 特殊逻辑处理，各集上更新剧集不一致
            PlayCache albumPlayCache = this.facadeService.getVideoService().getPlayCacheEntityByAlbumId(
                    playCacheEntity.getaId(), CommonUtil.PLATFORMS);
            if (albumPlayCache != null) {
                playCacheEntity.setaNowEpisodes(albumPlayCache.getaNowEpisodes());
                playCacheEntity.setaEpisode(albumPlayCache.getaEpisode());
                playCacheEntity.setaAudioInfoObj(albumPlayCache.getaAudioInfoObj());
                playCacheEntity.setaOst(albumPlayCache.getaOst());
            }
        }

        List<MusicCache> musicList = playCacheEntity.getaAudioInfoObj();
        if (!CollectionUtils.isEmpty(musicList)) {
            List<MusicCache> hasCopyRightSongs = new ArrayList<MusicCache>();
            for (MusicCache musicCache : musicList) {
                if (!VideoConstants.NO_COPYRIGHT_SONGS.contains(musicCache.getSongId())) {
                    hasCopyRightSongs.add(musicCache);
                }
            }

            album.setRelationMusic(hasCopyRightSongs);// 关联音乐，来源VRS
        }

        List<OstCache> ostList = playCacheEntity.getaOst();
        if (!CollectionUtils.isEmpty(ostList)) {
            album.setOst(ostList);
        }

        TotalCountStatTpResponse statResp = null;
        if (playCacheEntity.getaId() != null && playCacheEntity.getaId().intValue() != 0) {
            switch (categoryId) {
            case VideoConstants.Category.TV:
                album.setName(playCacheEntity.getaNameCn());
                album.setUpdateFrequency(playCacheEntity.getaPlayStatus());
                if (StringUtils.isNotEmpty(playCacheEntity.getaNowEpisodes())
                        && !"0".equals(playCacheEntity.getaNowEpisodes())) {
                    album.setNowEpisode(playCacheEntity.getaNowEpisodes());
                }
                album.setEpisodes(playCacheEntity.getaEpisode());
                album.setEnd((playCacheEntity.getaIsEnd() != null && playCacheEntity.getaIsEnd() == 1) ? 1 : 0);
                album.setStarring(playCacheEntity.getaStarring());
                album.setDirector(playCacheEntity.getaDirectory());
                album.setReleaseDate(cast2year(playCacheEntity.getaReleaseDate()));
                album.setSubCategoryName(playCacheEntity.getaSubCategoryName());
                album.setAreaName(playCacheEntity.getaAreaName());
                album.setScore(playCacheEntity.getaScore() != null ? playCacheEntity.getaScore().toString() : null);
                album.setDescription(playCacheEntity.getaDescription());
                album.setStarringList(playCacheEntity.getaStarringObj());
                statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(playCacheEntity.getaId(), null, null);
                if (statResp != null) {
                    album.setCommentCnt(statResp.getPcomm_count());
                    album.setVv(statResp.getPlist_play_count());
                }

                break;
            case VideoConstants.Category.FILM:
                album.setName(playCacheEntity.getaNameCn());
                album.setStarring(playCacheEntity.getaStarring());
                album.setDirector(playCacheEntity.getaDirectory());
                album.setReleaseDate(playCacheEntity.getaReleaseDate());
                album.setSubCategoryName(playCacheEntity.getaSubCategoryName());
                album.setAreaName(playCacheEntity.getaAreaName());
                album.setAlias(playCacheEntity.getaAlias());
                album.setScore(playCacheEntity.getaScore() != null ? playCacheEntity.getaScore().toString() : null);
                album.setDescription(playCacheEntity.getaDescription());
                album.setStarringList(playCacheEntity.getaStarringObj());
                statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(playCacheEntity.getaId(), null, null);
                if (statResp != null) {
                    album.setCommentCnt(statResp.getPcomm_count());
                    album.setVv(statResp.getPlist_play_count());
                }

                break;
            case VideoConstants.Category.CARTOON:
                album.setName(playCacheEntity.getaNameCn());
                album.setNowEpisode(playCacheEntity.getaNowEpisodes() == null ? "0" : playCacheEntity.getaNowEpisodes());
                album.setEpisodes(playCacheEntity.getaEpisode());
                album.setEnd((playCacheEntity.getaIsEnd() != null && playCacheEntity.getaIsEnd() == 1) ? 1 : 0);
                album.setUpdateFrequency(playCacheEntity.getaPlayStatus());
                album.setSubCategoryName(playCacheEntity.getaSubCategoryName());
                album.setAreaName(playCacheEntity.getaAreaName());
                album.setReleaseDate(cast2year(playCacheEntity.getaReleaseDate()));
                album.setFitAge(playCacheEntity.getaFitAge());
                album.setOriginator(playCacheEntity.getaOriginator());// 原作
                album.setSupervise(playCacheEntity.getaSupervise());// 主创
                album.setCast(playCacheEntity.getaCast());// 声优
                album.setDub(playCacheEntity.getaDub());// 配音
                album.setScore(playCacheEntity.getaScore() != null ? playCacheEntity.getaScore().toString() : null);
                album.setDescription(playCacheEntity.getaDescription());
                statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(playCacheEntity.getaId(), null, null);
                if (statResp != null) {
                    album.setCommentCnt(statResp.getPcomm_count());
                    album.setVv(statResp.getPlist_play_count());
                }

                break;
            case VideoConstants.Category.VARIETY:
                if (playCacheEntity.getvType().intValue() == 180001) {
                    album.setName(playCacheEntity.getaNameCn());
                    album.setCompere(playCacheEntity.getaCompere());
                    album.setPlayTvName(playCacheEntity.getaPlayTvName());
                    album.setDescription(playCacheEntity.getaDescription());
                    album.setUpdateFrequency(playCacheEntity.getaPlayStatus());
                    album.setScore(playCacheEntity.getaScore() != null ? playCacheEntity.getaScore().toString() : null);
                    album.setAreaName(playCacheEntity.getaAreaName());
                    album.setSubCategoryName(playCacheEntity.getaSubCategoryName());
                    album.setNowIssue(playCacheEntity.getaNowIssue() != null ? playCacheEntity.getaNowIssue()
                            .toString() : null);
                    album.setEnd(playCacheEntity.getaIsEnd());
                    album.setEpisodes(playCacheEntity.getaEpisode());
                    statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(playCacheEntity.getaId(), null, null);
                    if (statResp != null) {
                        album.setCommentCnt(statResp.getPcomm_count());
                        album.setVv(statResp.getPlist_play_count());
                    }

                } else {
                    // 这里比较特殊，虽然是给专辑赋属性，但是取得是播放的视频属性
                    album.setName(playCacheEntity.getvNameCn());
                    album.setSubCategoryName(playCacheEntity.getvSubCategoryName());
                    album.setAreaName(playCacheEntity.getvAreaName());
                    album.setDescription(playCacheEntity.getvDescription());
                    album.setEnd(playCacheEntity.getaIsEnd());
                    statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(null, null, playCacheEntity.getvId());
                    if (statResp != null) {
                        album.setCommentCnt(statResp.getVcomm_count());
                        album.setVv(statResp.getMedia_play_count());
                    }
                }

                break;
            case VideoConstants.Category.EDUCATION:
                album.setName(playCacheEntity.getaNameCn());
                album.setDescription(playCacheEntity.getaDescription());
                album.setInstructor(playCacheEntity.getaInstructor());
                album.setSubCategoryName(playCacheEntity.getaSubCategoryName());
                album.setSecondCate(playCacheEntity.getaSecondCate());
                album.setThirdCate(playCacheEntity.getaThirdCate());
                statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(playCacheEntity.getaId(), null, null);
                if (statResp != null) {
                    album.setVv(statResp.getPlist_play_count());
                }
                break;
            case VideoConstants.Category.DFILM:
                album.setName(playCacheEntity.getaNameCn());
                album.setNowEpisode(playCacheEntity.getaNowEpisodes() == null ? "0" : playCacheEntity.getaNowEpisodes());
                album.setEpisodes(playCacheEntity.getaEpisode());
                album.setEnd((playCacheEntity.getaIsEnd() != null && playCacheEntity.getaIsEnd() == 1) ? 1 : 0);
                album.setUpdateFrequency(playCacheEntity.getaPlayStatus());
                album.setSubCategoryName(playCacheEntity.getaSubCategoryName());
                album.setAreaName(playCacheEntity.getaAreaName());
                album.setDescription(playCacheEntity.getaDescription());
                statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(playCacheEntity.getaId(), null, null);
                if (statResp != null) {
                    album.setCommentCnt(statResp.getPcomm_count());
                    album.setVv(statResp.getPlist_play_count());
                }

                break;
            case VideoConstants.Category.MUSIC:
                // 这里比较特殊，虽然是给专辑赋属性，但是取得是播放的视频属性
                album.setName(playCacheEntity.getvNameCn());
                album.setReleaseDate(playCacheEntity.getvReleaseDate());
                album.setSinger(playCacheEntity.getvSinger());
                album.setMusicStyle(playCacheEntity.getvMusicStyle());
                album.setAreaName(playCacheEntity.getvAreaName());
                album.setDescription(playCacheEntity.getvDescription());
                statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(null, null, playCacheEntity.getvId());
                if (statResp != null) {
                    album.setCommentCnt(statResp.getVcomm_count());
                    album.setVv(statResp.getMedia_play_count());
                }

                break;
            case VideoConstants.Category.SPORT:
                if ("1".equalsIgnoreCase(varietyShow)) {
                    album.setName(playCacheEntity.getaNameCn());
                    album.setDescription(playCacheEntity.getaDescription());
                    album.setAreaName(playCacheEntity.getaAreaName());
                    album.setSubCategoryName(playCacheEntity.getaSubCategoryName());
                    album.setSportsType(playCacheEntity.getvStyleName());
                    statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(playCacheEntity.getaId(), null, null);
                    if (statResp != null) {
                        album.setCommentCnt(statResp.getPcomm_count());
                        album.setVv(statResp.getPlist_play_count());
                    }
                } else {
                    album.setName(playCacheEntity.getvNameCn());
                    album.setDescription(playCacheEntity.getvDescription());
                    album.setAreaName(playCacheEntity.getvAreaName());
                    album.setSubCategoryName(playCacheEntity.getvSubCategoryName());
                    album.setSportsType(playCacheEntity.getvStyleName());
                    album.setType(playCacheEntity.getvType());
                    album.setCreateTime(playCacheEntity.getvCreateTime().getTime());
                    statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(null, null, playCacheEntity.getvId());
                    if (statResp != null) {
                        album.setCommentCnt(statResp.getVcomm_count());
                        album.setVv(statResp.getMedia_play_count());
                    }
                }
                break;
            case VideoConstants.Category.ENT:
                if ("1".equalsIgnoreCase(varietyShow)) {
                    album.setName(playCacheEntity.getaNameCn());
                    album.setSubCategoryName(playCacheEntity.getaSubCategoryName());
                    album.setAreaName(playCacheEntity.getaAreaName());
                    album.setDescription(playCacheEntity.getaDescription());
                    statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(playCacheEntity.getaId(), null, null);
                    if (statResp != null) {
                        album.setCommentCnt(statResp.getPcomm_count());
                        album.setVv(statResp.getPlist_play_count());
                    }
                } else {
                    // 这里比较特殊，虽然是给专辑赋属性，但是取得是播放的视频属性
                    album.setName(playCacheEntity.getvNameCn());
                    album.setSubCategoryName(playCacheEntity.getvSubCategoryName());
                    album.setAreaName(playCacheEntity.getvAreaName());
                    album.setCreateTime(playCacheEntity.getvCreateTime().getTime());
                    album.setDescription(playCacheEntity.getvDescription());
                    statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(null, null, playCacheEntity.getvId());
                    if (statResp != null) {
                        album.setCommentCnt(statResp.getVcomm_count());
                        album.setVv(statResp.getMedia_play_count());
                    }
                }

                break;
            case VideoConstants.Category.CAR:
            case VideoConstants.Category.CAI_JING:
            case VideoConstants.Category.TRAVEL:
            case VideoConstants.Category.FENG_SHANG:
            case VideoConstants.Category.HOTSPOT:
            case VideoConstants.Category.ZIXUN:
                if ("1".equalsIgnoreCase(varietyShow)) {
                    album.setName(playCacheEntity.getaNameCn());
                    album.setSubCategoryName(playCacheEntity.getaSubCategoryName());
                    album.setAreaName(playCacheEntity.getaAreaName());
                    album.setDescription(playCacheEntity.getaDescription());
                    statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(playCacheEntity.getaId(), null, null);
                    if (statResp != null) {
                        album.setCommentCnt(statResp.getPcomm_count());
                        album.setVv(statResp.getPlist_play_count());
                    }
                } else {
                    // 这里比较特殊，虽然是给专辑赋属性，但是取得是播放的视频属性
                    album.setName(playCacheEntity.getvNameCn());
                    album.setSubCategoryName(playCacheEntity.getvSubCategoryName());
                    album.setStyleName(playCacheEntity.getvStyleName());
                    album.setAreaName(playCacheEntity.getvAreaName());
                    album.setCreateTime(playCacheEntity.getvCreateTime().getTime());
                    album.setDescription(playCacheEntity.getvDescription());
                    statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(null, null, playCacheEntity.getvId());
                    if (statResp != null) {
                        album.setCommentCnt(statResp.getVcomm_count());
                        album.setVv(statResp.getMedia_play_count());
                    }
                }

                break;
            case VideoConstants.Category.PARENTING:
                if ("1".equalsIgnoreCase(varietyShow)) {
                    album.setName(playCacheEntity.getaNameCn());
                    album.setDescription(playCacheEntity.getaDescription());
                    statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(playCacheEntity.getaId(), null, null);
                    if (statResp != null) {
                        album.setCommentCnt(statResp.getPcomm_count());
                        album.setVv(statResp.getPlist_play_count());
                    }
                } else {
                    // 这里比较特殊，虽然是给专辑赋属性，但是取得是播放的视频属性
                    album.setName(playCacheEntity.getvNameCn());
                    // album.setFitAge(playCacheEntity.getvf);
                    album.setAreaName(playCacheEntity.getvAreaName());
                    album.setDescription(playCacheEntity.getvDescription());
                    statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(null, null, playCacheEntity.getvId());
                    if (statResp != null) {
                        album.setCommentCnt(statResp.getVcomm_count());
                        album.setVv(statResp.getMedia_play_count());
                    }
                }
            default:
                break;
            }
        } else {
            // 单视频走单视频简介
            album.setName(playCacheEntity.getvNameCn());
            album.setDescription(playCacheEntity.getvDescription());
            statResp = this.facadeTpDao.getStatTpDao().getTotalCountStat(null, null, playCacheEntity.getvId());
            if (statResp != null) {
                album.setCommentCnt(statResp.getVcomm_count());
                album.setVv(statResp.getMedia_play_count());
            }
        }
    }

    public AlbumSeriesDto getAllSeries(Long albumId, Integer page) {
        AlbumSeriesDto series = new AlbumSeriesDto();
        // 过滤掉页码形式
        if (page.toString().length() >= 4) {
            // 获取年份
            String year = page.toString().length() > 4 ? page.toString().substring(0, 4) : page.toString();

            PlayCache album = this.facadeService.getVideoService().getPlayCacheEntityByAlbumId(albumId,
                    CommonUtil.PLATFORMS);
            if (album != null) {
                String varietyShow = album.getaVarietyShow();
                Integer categoryId = album.getaCategoryId();

                List<Page> seriesTitle = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOS_PAGELIST_ALBUM
                        + albumId, new TypeReference<List<Page>>() {
                });
                List<SeriesPage> seriesPages = new ArrayList<SeriesPage>();
                Map<String, SeriesPage> albumSeriesMap = new HashMap<String, SeriesPage>();
                if (!CollectionUtils.isEmpty(seriesTitle)) {
                    String currentYear = null;
                    SeriesPage seriesPage = null;
                    for (Page st : seriesTitle) {
                        if (st.getPage() != null) {
                            String albumPage = st.getPage().toString();
                            if (albumPage.length() == 6) {
                                currentYear = albumPage.substring(0, 4);
                            }
                            if (albumSeriesMap.containsKey(currentYear)) {
                                seriesPage = albumSeriesMap.get(currentYear);
                            } else {
                                seriesPage = new SeriesPage();
                                seriesPage.setPage(NumberUtils.toInt(currentYear));
                            }

                            // 将今年所有月份的视频列表和数量累加
                            Integer currentPageSize = seriesPage.getPageSize() == null ? 0 : seriesPage.getPageSize();
                            Integer newPageSize = currentPageSize + st.getPageSize();
                            seriesPage.setPageSize(newPageSize);
                            if (year.equals(currentYear)) {
                                List<VideoDto> iptvVideoList = getAlbumSeries(albumId, categoryId, "", varietyShow,
                                        st.getPage(), 100);
                                List<VideoDto> currentVideoList = new ArrayList<VideoDto>();
                                if (!CollectionUtils.isEmpty(seriesPage.getPositiveSeries())) {
                                    currentVideoList = seriesPage.getPositiveSeries();
                                }
                                if (!CollectionUtils.isEmpty(iptvVideoList)) {
                                    currentVideoList.addAll(iptvVideoList);
                                    seriesPage.setPositiveSeries(currentVideoList);
                                }
                            }
                            albumSeriesMap.put(currentYear, seriesPage);
                        }
                    }
                    for (String key : albumSeriesMap.keySet()) {
                        SeriesPage s = albumSeriesMap.get(key);
                        seriesPages.add(s);
                    }
                    series.setSeriesPages(seriesPages);
                }
            }
        }
        return series;
    }

    public VideoPlayListDto getPlayList(Long albumId, Long videoId, CommonParam param) {
        VideoPlayListDto playList = new VideoPlayListDto();
        List<VideoDto> result = new ArrayList<VideoDto>();
        if (videoId != null) {
            PlayCache playCacheEntity = this.facadeService.getVideoService().getPlayCacheEntityByVideoId(videoId,
                    CommonConstants.Copyright.PHONE);
            // 调用第三方推荐接口获取视频列表
            Integer cid = null;
            if (playCacheEntity != null && playCacheEntity.getvCategoryId() != null) {
                cid = playCacheEntity.getvCategoryId();
                if (playCacheEntity.getvType().intValue() == VideoConstants.VideoType.YU_GAO_PIAN) {
                    if (cid == VideoConstants.Category.TV || cid == VideoConstants.Category.CARTOON) {
                        if (albumId != null) {
                            List<VideoDto> iptvVideoList = this.tpCacheTemplate.get(
                                    CacheConstants.POSITIVE_VIDEOLIST_ALBUM_PAGE + albumId + "_0",
                                    new TypeReference<List<VideoDto>>() {
                                    });
                            if (!CollectionUtils.isEmpty(iptvVideoList)) {
                                videoId = NumberUtils.toLong(iptvVideoList.get(0).getVideoId());
                            }
                        }
                    }
                }
            }
            RecBaseRequest recRequest = new RecBaseRequest();
            recRequest.setVid(videoId);
            recRequest.setCid(cid);
            recRequest.setNum(20);
            recRequest.setArea("rec_0017");
            recRequest.setUid(param.getUid());
            recRequest.setPid(albumId);
            RecBaseResponse recResponse = this.facadeTpDao.getRecommendTpDao().recommend(recRequest);
            if (recResponse != null && !CollectionUtils.isEmpty(recResponse.getRec())) {
                for (int i = 0; i < recResponse.getRec().size(); i++) {
                    RecommendDetail recommendDetail = recResponse.getRec().get(i);
                    // 过滤只有PID，无VID数据
                    if (recommendDetail.getVid() == null) {
                        continue;
                    }
                    // 有专辑归属的视频，根据不同频道进行校验
                    if (recommendDetail.getPid() != null) {
                        Integer categoryId = recommendDetail.getCid();
                        if (categoryId != null) {
                            switch (categoryId) {
                            case VideoConstants.Category.TV:
                            case VideoConstants.Category.CARTOON:
                            case VideoConstants.Category.PARENTING:
                            case VideoConstants.Category.FILM:
                                // 过滤正片视频
                                if (StringUtils.equals(recommendDetail.getVideo_type(),
                                        String.valueOf(VideoConstants.VideoType.ZHENG_PIAN))) {
                                    continue;
                                }
                                // 过滤本专辑的视频
                                if (recommendDetail.getPid() == albumId) {
                                    continue;
                                }
                                break;
                            case VideoConstants.Category.VARIETY:
                                // 过滤正片视频
                                if (StringUtils.equals(recommendDetail.getVideo_type(),
                                        String.valueOf(VideoConstants.VideoType.ZHENG_PIAN))) {
                                    continue;
                                }
                                // 过滤本专辑的视频
                                if (recommendDetail.getPid() == albumId) {
                                    continue;
                                }
                                break;
                            case VideoConstants.Category.SPORT:
                            case VideoConstants.Category.HOTSPOT:
                            case VideoConstants.Category.CAI_JING:
                            case VideoConstants.Category.FENG_SHANG:
                            case VideoConstants.Category.CAR:
                            case VideoConstants.Category.TRAVEL:
                            case VideoConstants.Category.MUSIC:
                                // nba
                                // 过滤本专辑的视频
                                if (recommendDetail.getPid() == albumId) {
                                    continue;
                                }
                                break;
                            case VideoConstants.Category.DFILM:
                            case VideoConstants.Category.EDUCATION:
                                // 过滤本专辑的视频
                                if (recommendDetail.getPid() == albumId) {
                                    continue;
                                }
                                break;
                            default:
                                break;
                            }
                        }
                    }
                    // 媒资转化为dto
                    result.add(transRecommendDetail2VideoDto(recommendDetail));
                }
            }
        }
        if (!CollectionUtils.isEmpty(result)) {
            playList.setPlayList(result.subList(0, 1));
        }
        return playList;
    }

    public VideoDto transRecommendDetail2VideoDto(RecommendDetail recommendDetail) {
        VideoDto video = new VideoDto();
        video.setVideoId(recommendDetail.getVid() != null ? recommendDetail.getVid().toString() : null);
        video.setCategoryId(recommendDetail.getCid());
        video.setCreateTime(TimeUtil.string2timestamp(recommendDetail.getCreatetime()));
        video.setName(recommendDetail.getTitle());
        video.setDuration(recommendDetail.getDuration());
        video.setGuest(recommendDetail.getGuest());
        video.setSinger(recommendDetail.getStarring());
        video.setImg(recommendDetail.getImageBySize(400, 250));
        video.setVideoTypeId(NumberUtils.toInt(recommendDetail.getVideo_type()));
        if (recommendDetail.getPid() != null) {
            video.setAlbumId(recommendDetail.getPid().toString());
        }
        return video;
    }

    /**
     * 批量从CACHE中获得视频或者专辑对象
     * @return
     */
    public Map<String, PlayCache> mgetPlayCacheEntityByKeys(List<String> keys) {
        if (keys == null || keys.size() <= 0) {
            return null;
        }
        Map<String, PlayCache> playCacheEntitys = tpCacheTemplate.mget(keys, PlayCache.class);
        if (playCacheEntitys == null) {
            log.info("ERROR: " + keys.toString() + " is NULL");
        }
        return playCacheEntitys;
    }

    public InteractDto playInteract(Long videoId) {
        return this.tpCacheTemplate.get(CacheConstants.PlayInteract_ + videoId, InteractDto.class);
    }

    public PositiveRecommendDto getPositiveRecommend(Long videoId, CommonParam param) {
        PlayCache video = this.getPlayCacheEntityByVideoId(videoId, CommonUtil.PLATFORMS);
        PositiveRecommendDto recommend = new PositiveRecommendDto();
        if (videoId != null) {
            Integer videoType = video.getvType();
            // 非正片有正片推荐
            if (videoType != null && videoType.intValue() != VideoConstants.VideoType.ZHENG_PIAN) {
                Integer categoryId = video.getvCategoryId();
                switch (categoryId) {
                case VideoConstants.Category.FILM:
                case VideoConstants.Category.TV:
                case VideoConstants.Category.CARTOON:
                    Integer albumType = video.getaType();
                    if (albumType != null && albumType == 180001) {
                        PositiveAlbumDto positiveAlbum = new PositiveAlbumDto();
                        positiveAlbum.setAlbumId(video.getaId());
                        positiveAlbum.setName(video.getaNameCn());
                        positiveAlbum.setVv(video.getaVv());
                        positiveAlbum.setImg(video.getAPic(400, 250));
                        recommend.setPositiveAlbum(positiveAlbum);
                    }
                    break;
                case VideoConstants.Category.VARIETY:
                    String relationId = video.getaRelationId();
                    Long id = null;
                    PlayCache relationAlbum = this.getPlayCacheEntityByAlbumId(NumberUtils.toLong(relationId),
                            CommonUtil.PLATFORMS);
                    if (relationAlbum != null && relationAlbum.getaType() != null && relationAlbum.getaType() == 180001) {
                        id = relationAlbum.getaId();
                    } else if (video.getaType() != null && video.getaType() == 180001) {
                        id = video.getaId();
                    }

                    // 其他非正片，返正片专辑的最新的三期
                    if (videoType.intValue() != VideoConstants.VideoType.PIAN_DUAN) {
                        List<Page> seriesTitle = this.tpCacheTemplate.get(CacheConstants.POSITIVE_VIDEOS_PAGELIST_ALBUM
                                + id, new TypeReference<List<Page>>() {
                        });
                        if (!CollectionUtils.isEmpty(seriesTitle)) {
                            List<VideoDto> iptvVideoList = getYearLatestVideos(seriesTitle, id);
                            recommend.setPositiveVideoList(iptvVideoList);
                        }
                    }
                    break;
                default:
                    break;
                }
            }
        }
        return recommend;
    }

    /**
     * 半屏播放页 点赞 点踩
     * @param param 
     * @param act 
     * @param albulmId 
     * @param videoId 
     * @param request
     * @param param 
     * @return
     */
	public Response<LikeDto> likeCommit(Long videoId, Long albulmId, Boolean act, CommonParam param) {
		Response<LikeDto> response = new Response<LikeDto>();
		String errorCode = null;
		
		LikeRequest request = new LikeRequest();
		request.setAct(act == true ? 1 : 2);
		request.setVid(videoId);
		if(request == null || request.isSuccess()){
			// 参数错误
			errorCode = VideoErrorCodeConstants.like.ERROR_CODE_LIKE_PARAM_ERROR;
			this.setErrorResponse(response, errorCode, errorCode, param.getLangcode());
		}else{
			// 首先判断点赞或者点踩是否成功，不成功则直接返回，否则再查询数量
			LikeResponse likeCommit = this.facadeTpDao.getVideoTpDao().getLikeCommit(request);
			
			if(likeCommit != null && likeCommit.getCode() != null && "200".equals(likeCommit.getCode())) {
				LikeDto dto = new LikeDto();
				dto.setStatus(true);
				
				LikeQueryRequest queryRequest = new LikeQueryRequest();
				queryRequest.setPid(albulmId);
				queryRequest.setVid(videoId);
				//平台（101:主站,102:移动端,103:TV端）
				queryRequest.setPlatform(102);
				
				LikeQueryResponse queryResponse = this.facadeTpDao.getVideoTpDao().getLikeQuery(queryRequest);
				if(queryResponse != null) {
					dto.setLikeNumber(queryResponse.getUp());
					dto.setUnLikeNumber(queryResponse.getDown());
					response.setData(dto);
				}else {
					// 获取点赞数量失败
					response.setData(dto);
					errorCode = VideoErrorCodeConstants.like.ERROR_CODE_LIKE_NUMBER_ERROR;
					this.setErrorResponse(response, errorCode, errorCode, param.getLangcode());
				}
			}else {
				// 点赞请求失败
				errorCode = VideoErrorCodeConstants.like.ERROR_CODE_LIKE_REQUEST_ERROR;
				this.setErrorResponse(response, errorCode, errorCode, param.getLangcode());
			}
		}
		return response;
	}
}