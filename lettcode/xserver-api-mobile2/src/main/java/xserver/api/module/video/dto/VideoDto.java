package xserver.api.module.video.dto;

import java.util.List;
import java.util.Map;

import xserver.api.module.play.dto.ChargeDto;
import xserver.common.dto.BaseDto;
import xserver.lib.constant.VideoConstants;
import xserver.lib.dto.video.InteractDto;
import xserver.lib.tp.video.response.WatchingFocus;
import xserver.lib.tpcache.cache.ActorCache;
import xserver.lib.tpcache.cache.Stream;

public class VideoDto extends BaseDto implements Comparable {

    public VideoDto() {

    }

    public VideoDto(Integer type) {
        this.type = type;// 标识0专辑、1视频
    }

    private Integer type;
    /**
     * 视频id
     */
    private String videoId;

    /**
     * 内容分类（电影、电视剧）
     */
    private Integer categoryId;

    /**
     * 视频所属专辑id
     */
    private String albumId;

    /**
     * 是否有所属专辑
     */
    private Integer hasAlbum;

    /**
     * 在专辑中顺序
     */
    private Integer orderInAlbum;

    /**
     * 是否正片
     */
    private Integer positive;
    private Integer videoTypeId;
    private String videoType;

    /**
     * 第几集、第几期
     */
    private String episode;

    /**
     * 预告片类型 0 有效的预告片(0<跟播集数<3) 1 过期的预告片
     */
    private Integer preType;

    private String img;

    private String albumImg;

    /**
     * 视频名称
     */
    private String name;

    /**
     * 视频子名称
     */
    private String subName;

    /**
     * 描述
     */
    private String desc;

    /**
     * 视点图
     */
    private String viewPic;

    /**
     * 观看次数
     */
    private Object vv;

    /**
     * 评论数
     */
    private Object commentCnt;

    /**
     * 时长(毫秒)
     */
    private Long duration;

    /**
     * 嘉宾
     */
    private String guest;

    /**
     * 歌手
     */
    private String singer;

    /**
     * 推送到TV版的时间
     */
    private Long pushTVTime;

    private String defaultStream;
    // private List<Stream> streams;
    private List<Stream> normalStreams;

    private List<Stream> theatreStreams;

    private List<Stream> threeDStreams;
    /**
     * 推送平台
     */
    private String playPlatform;

    /**
     * 下载平台
     */
    private String downloadPlatform;

    /**
     * 能否下载
     */
    private Integer download;

    /**
     * 是否收费
     */
    private String ifCharge;

    /**
     * 视频播放短地址
     */
    private String storePath;

    /**
     * 播放/下载地址
     */
    private String playUrl;

    /**
     * 备用播放地址1
     */
    private String backUrl0;

    /**
     * 备用播放地址2
     */
    private String backUrl1;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 备用播放地址3
     */
    private String backUrl2;

    /**
     * 视频大小byte
     */
    private Long gsize;

    /**
     * 当前码流
     */
    private String currentStream;

    /**
     * 当前码流名称
     */
    private String currentStreamName;
    /**
     * 当前码流播放速度
     */
    private String currentStreamKps;

    /**
     * 当前免费码流 code :1300 name:高清
     */
    private Stream freeStream;

    /**
     * 媒资返回的md5值
     */
    private String md5;
    private Integer hasBelow;

    /**
     * 附属信息。 如已经向下兼容
     */
    private String tipMsg;

    /**
     * 播放时提示的标题
     */
    private String showName;

    /**
     * 片头时间 单位毫秒
     */
    private Integer videoHeadTime;

    /**
     * 片尾时间
     */
    private Integer videoTailTime;
    private String statisticsCode;

    /**
     * 0不需要加水印,1需要加水印
     */
    private Integer needWaterMarking;

    /**
     * 播放模式 1:正常播放 2:试看播放 3:350免费播放 4:院线试看 5:码流收费
     */
    private Integer playType;

    /**
     * 试看时长
     */
    private Long tryPlayTime;

    /**
     * 是否院线
     */
    private Long isYuanXian;

    private String tryPlayTipCode;

    /**
     * 试看提示
     */
    private String tryPlayTipMsg;
    private String drmFlagId;

    /**
     * DRM视频token文件地址
     */
    private String drmTokenUrl;

    /**
     * 声纹文件地址
     */
    private String soundInkCodeUrl;

    /**
     * 剧集列表视频类型 0预告
     */
    private Integer seriesType;

    /**
     * 单点付费
     */
    private String price;

    /**
     * 海报图，单点支付页面使用
     */
    private String poster;

    /**
     * 有效时长
     */
    private Integer expiredTime;

    /**
     * 付费类型 1支持单点付费
     */
    private Integer payType;

    private Integer userTicketSize;// 可用观影券数量
    private Integer videoTicketSize;// 看视频需要几张观影券
    private Long ticketValideTime;// 观影券有效期限

    private String areaName;
    private String director;

    /**
     * 是否是推荐
     */
    private Integer is_rec;

    private Map<String, String> allPlayUrl;

    /**
     * 家长锁 0否，1是
     */
    private Integer isPlayLock;

    /**
     * 看点
     */
    private List<WatchingFocus> watchingFocus;

    private Integer page;

    private Integer pageNum;

    /**
     * 相关内容
     */
    private Object relation;
    /**
     * 推荐事件id
     */
    private String recReid;
    /**
     * 页面区域
     */
    private String recArea;
    /**
     * 分桶测试编号
     */
    private String recBucket;
    /**
     * 页面id
     */
    private String pageid;
    /**
     * 数据来源 0：编辑手动 1：推荐自动
     */
    private String src;

    private ChargeDto chargeObj;

    private Integer vipStatus;// 0：非vip 1:vip

    /**
     * 明星列表
     */
    private List<ActorCache> starList;
    /**
     * 每期嘉宾列表
     */
    private List<ActorCache> guestList;
    /**
     * web版权播放跳转方式
     */
    private Integer webOpenType;// 1:webview 0:webbrowser
    /**
     * 跳转到浏览器的M站地址
     */
    private String Murl;
    /**
     * 有版权播放的平台
     */
    private Integer playCopyright;// 1:Web 0:TV
    /**
     * 活水印
     */
    private List<LiveWaterMarkDto> waterMark;
    /**
     * 可以提交反馈报告
     */
    private Integer canFeedBack;// 1:可以 0:不可以
    /**
     * 视频关联片段
     */
    private List<VideoDto> segments;// 用于综艺和其他栏目类的按年月排序

    /**
     * 播放器交互
     */
    private InteractDto interact;

    /**
     * 频道子类型
     */
    private String subCategoryName;
    /**
     * 上线时间
     */
    private String releaseDate;

    private Integer hasSeries;// 是否有剧集 0:有剧集 1:没有剧集

    private Long relationAlbumId;// 关联的正片专辑id(播放综艺频道的片段时返)

    private String loadingImg;// 播放loading显示图片

    public String getTryPlayTipCode() {
        return tryPlayTipCode;
    }

    public void setTryPlayTipCode(String tryPlayTipCode) {
        this.tryPlayTipCode = tryPlayTipCode;
    }

    public String getIfCharge() {
        return this.ifCharge;
    }

    public void setIfCharge(String ifCharge) {
        this.ifCharge = ifCharge;
    }

    public Stream getFreeStream() {
        return this.freeStream;
    }

    public void setFreeStream(Stream freeStream) {
        this.freeStream = freeStream;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public Integer getVideoTypeId() {
        return this.videoTypeId;
    }

    public void setVideoTypeId(Integer videoTypeId) {
        this.videoTypeId = videoTypeId;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return this.subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getDuration() {
        return this.duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDefaultStream() {
        return this.defaultStream;
    }

    public void setDefaultStream(String defaultStream) {
        this.defaultStream = defaultStream;
    }

    public String getStorePath() {
        return this.storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public String getPlayUrl() {
        return this.playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getBackUrl0() {
        return this.backUrl0;
    }

    public void setBackUrl0(String backUrl0) {
        this.backUrl0 = backUrl0;
    }

    public String getBackUrl1() {
        return this.backUrl1;
    }

    public void setBackUrl1(String backUrl1) {
        this.backUrl1 = backUrl1;
    }

    public String getBackUrl2() {
        return this.backUrl2;
    }

    public void setBackUrl2(String backUrl2) {
        this.backUrl2 = backUrl2;
    }

    public Long getGsize() {
        return this.gsize;
    }

    public void setGsize(Long gsize) {
        this.gsize = gsize;
    }

    public String getCurrentStream() {
        return this.currentStream;
    }

    public void setCurrentStream(String currentStream) {
        this.currentStream = currentStream;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getHasBelow() {
        return this.hasBelow;
    }

    public void setHasBelow(Integer hasBelow) {
        this.hasBelow = hasBelow;
    }

    public String getTipMsg() {
        return this.tipMsg;
    }

    public void setTipMsg(String tipMsg) {
        this.tipMsg = tipMsg;
    }

    public String getShowName() {
        return this.showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public Integer getVideoHeadTime() {
        return this.videoHeadTime;
    }

    public void setVideoHeadTime(Integer videoHeadTime) {
        this.videoHeadTime = videoHeadTime;
    }

    public Integer getVideoTailTime() {
        return this.videoTailTime;
    }

    public void setVideoTailTime(Integer videoTailTime) {
        this.videoTailTime = videoTailTime;
    }

    public String getStatisticsCode() {
        return this.statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public Integer getNeedWaterMarking() {
        return this.needWaterMarking;
    }

    public void setNeedWaterMarking(Integer needWaterMarking) {
        this.needWaterMarking = needWaterMarking;
    }

    public Integer getPlayType() {
        return this.playType;
    }

    public void setPlayType(Integer playType) {
        this.playType = playType;
    }

    public Long getTryPlayTime() {
        return this.tryPlayTime;
    }

    public void setTryPlayTime(Long tryPlayTime) {
        this.tryPlayTime = tryPlayTime;
    }

    public Long getIsYuanXian() {
        return this.isYuanXian;
    }

    public void setIsYuanXian(Long isYuanXian) {
        this.isYuanXian = isYuanXian;
    }

    public String getTryPlayTipMsg() {
        return this.tryPlayTipMsg;
    }

    public void setTryPlayTipMsg(String tryPlayTipMsg) {
        this.tryPlayTipMsg = tryPlayTipMsg;
    }

    public String getDrmFlagId() {
        return this.drmFlagId;
    }

    public void setDrmFlagId(String drmFlagId) {
        this.drmFlagId = drmFlagId;
    }

    public String getDrmTokenUrl() {
        return this.drmTokenUrl;
    }

    public void setDrmTokenUrl(String drmTokenUrl) {
        this.drmTokenUrl = drmTokenUrl;
    }

    public String getEpisode() {
        return this.episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public Integer getOrderInAlbum() {
        return this.orderInAlbum;
    }

    public void setOrderInAlbum(Integer orderInAlbum) {
        this.orderInAlbum = orderInAlbum;
    }

    public String getGuest() {
        return this.guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getSinger() {
        return this.singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getCurrentStreamKps() {
        return this.currentStreamKps;
    }

    public void setCurrentStreamKps(String currentStreamKps) {
        this.currentStreamKps = currentStreamKps;
    }

    public Object getVv() {
        return vv;
    }

    public void setVv(Object vv) {
        this.vv = vv;
    }

    public Object getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(Object commentCnt) {
        this.commentCnt = commentCnt;
    }

    public String getPlayPlatform() {
        return this.playPlatform;
    }

    public void setPlayPlatform(String playPlatform) {
        this.playPlatform = playPlatform;
    }

    public String getSoundInkCodeUrl() {
        return this.soundInkCodeUrl;
    }

    public void setSoundInkCodeUrl(String soundInkCodeUrl) {
        this.soundInkCodeUrl = soundInkCodeUrl;
    }

    public Integer getSeriesType() {
        return this.seriesType;
    }

    public void setSeriesType(Integer seriesType) {
        this.seriesType = seriesType;
    }

    public Integer getPreType() {
        return this.preType;
    }

    public void setPreType(Integer preType) {
        this.preType = preType;
    }

    @Override
    public int compareTo(Object o) {
        VideoDto video = (VideoDto) o;

        if (this.categoryId != null
                && (this.categoryId == VideoConstants.Category.TV || this.categoryId == VideoConstants.Category.CARTOON)
                && this.orderInAlbum != null && this.orderInAlbum < video.getOrderInAlbum()) {
            return -1;
        } else {
            return 1;
        }
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPoster() {
        return this.poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Integer getExpiredTime() {
        return this.expiredTime;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Integer getPayType() {
        return this.payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDirector() {
        return this.director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Map<String, String> getAllPlayUrl() {
        return allPlayUrl;
    }

    public void setAllPlayUrl(Map<String, String> allPlayUrl) {
        this.allPlayUrl = allPlayUrl;
    }

    public String getViewPic() {
        return viewPic;
    }

    public void setViewPic(String viewPic) {
        this.viewPic = viewPic;
    }

    public Long getPushTVTime() {
        return pushTVTime;
    }

    public void setPushTVTime(Long pushTVTime) {
        this.pushTVTime = pushTVTime;
    }

    public Integer getIsPlayLock() {
        return isPlayLock;
    }

    public void setIsPlayLock(Integer isPlayLock) {
        this.isPlayLock = isPlayLock;
    }

    public String getDownloadPlatform() {
        return downloadPlatform;
    }

    public void setDownloadPlatform(String downloadPlatform) {
        this.downloadPlatform = downloadPlatform;
    }

    public Integer getHasAlbum() {
        return hasAlbum;
    }

    public void setHasAlbum(Integer hasAlbum) {
        this.hasAlbum = hasAlbum;
    }

    public Integer getPositive() {
        return positive;
    }

    public void setPositive(Integer positive) {
        this.positive = positive;
    }

    public Integer getIs_rec() {
        return is_rec;
    }

    public void setIs_rec(Integer is_rec) {
        this.is_rec = is_rec;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDownload() {
        return download;
    }

    public void setDownload(Integer download) {
        this.download = download;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Object getRelation() {
        return relation;
    }

    public void setRelation(Object relation) {
        this.relation = relation;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public List<Stream> getNormalStreams() {
        return normalStreams;
    }

    public void setNormalStreams(List<Stream> normalStreams) {
        this.normalStreams = normalStreams;
    }

    public List<Stream> getTheatreStreams() {
        return theatreStreams;
    }

    public void setTheatreStreams(List<Stream> theatreStreams) {
        this.theatreStreams = theatreStreams;
    }

    public List<Stream> getThreeDStreams() {
        return threeDStreams;
    }

    public void setThreeDStreams(List<Stream> threeDStreams) {
        this.threeDStreams = threeDStreams;
    }

    public ChargeDto getChargeObj() {
        return chargeObj;
    }

    public void setChargeObj(ChargeDto chargeObj) {
        this.chargeObj = chargeObj;
    }

    public Integer getUserTicketSize() {
        return userTicketSize;
    }

    public void setUserTicketSize(Integer userTicketSize) {
        this.userTicketSize = userTicketSize;
    }

    public Integer getVideoTicketSize() {
        return videoTicketSize;
    }

    public void setVideoTicketSize(Integer videoTicketSize) {
        this.videoTicketSize = videoTicketSize;
    }

    public Long getTicketValideTime() {
        return ticketValideTime;
    }

    public void setTicketValideTime(Long ticketValideTime) {
        this.ticketValideTime = ticketValideTime;
    }

    public List<WatchingFocus> getWatchingFocus() {
        return watchingFocus;
    }

    public void setWatchingFocus(List<WatchingFocus> watchingFocus) {
        this.watchingFocus = watchingFocus;
    }

    public String getAlbumImg() {
        return albumImg;
    }

    public void setAlbumImg(String albumImg) {
        this.albumImg = albumImg;
    }

    public String getCurrentStreamName() {
        return currentStreamName;
    }

    public void setCurrentStreamName(String currentStreamName) {
        this.currentStreamName = currentStreamName;
    }

    public String getRecReid() {
        return recReid;
    }

    public void setRecReid(String recReid) {
        this.recReid = recReid;
    }

    public String getRecArea() {
        return recArea;
    }

    public void setRecArea(String recArea) {
        this.recArea = recArea;
    }

    public String getRecBucket() {
        return recBucket;
    }

    public void setRecBucket(String recBucket) {
        this.recBucket = recBucket;
    }

    public Integer getVipStatus() {
        return vipStatus;
    }

    public void setVipStatus(Integer vipStatus) {
        this.vipStatus = vipStatus;
    }

    public List<ActorCache> getStarList() {
        return starList;
    }

    public void setStarList(List<ActorCache> starList) {
        this.starList = starList;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Integer getWebOpenType() {
        return webOpenType;
    }

    public void setWebOpenType(Integer webOpenType) {
        this.webOpenType = webOpenType;
    }

    public List<ActorCache> getGuestList() {
        return guestList;
    }

    public void setGuestList(List<ActorCache> guestList) {
        this.guestList = guestList;
    }

    public Integer getPlayCopyright() {
        return playCopyright;
    }

    public void setPlayCopyright(Integer playCopyright) {
        this.playCopyright = playCopyright;
    }

    public String getMurl() {
        return Murl;
    }

    public void setMurl(String murl) {
        Murl = murl;
    }

    public List<LiveWaterMarkDto> getWaterMark() {
        return waterMark;
    }

    public void setWaterMark(List<LiveWaterMarkDto> waterMark) {
        this.waterMark = waterMark;
    }

    public Integer getCanFeedBack() {
        return canFeedBack;
    }

    public void setCanFeedBack(Integer canFeedBack) {
        this.canFeedBack = canFeedBack;
    }

    public List<VideoDto> getSegments() {
        return segments;
    }

    public void setSegments(List<VideoDto> segments) {
        this.segments = segments;
    }

    public InteractDto getInteract() {
        return interact;
    }

    public void setInteract(InteractDto interact) {
        this.interact = interact;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getHasSeries() {
        return hasSeries;
    }

    public void setHasSeries(Integer hasSeries) {
        this.hasSeries = hasSeries;
    }

    public Long getRelationAlbumId() {
        return relationAlbumId;
    }

    public void setRelationAlbumId(Long relationAlbumId) {
        this.relationAlbumId = relationAlbumId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getLoadingImg() {
        return loadingImg;
    }

    public void setLoadingImg(String loadingImg) {
        this.loadingImg = loadingImg;
    }

}
