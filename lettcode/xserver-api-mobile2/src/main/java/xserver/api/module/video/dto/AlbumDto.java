package xserver.api.module.video.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import xserver.common.dto.BaseDto;
import xserver.lib.tpcache.cache.ActorCache;
import xserver.lib.tpcache.cache.LinesCache;
import xserver.lib.tpcache.cache.MusicCache;
import xserver.lib.tpcache.cache.OstCache;
import xserver.lib.tpcache.cache.Stream;

public class AlbumDto extends BaseDto {

    public AlbumDto(Integer type) {
        this.type = type;// 标识0专辑、1视频
    }

    public AlbumDto() {

    }

    private Integer type;
    /**
     * 专辑id
     */
    private String albumId;

    /**
     * 专辑分类id
     */
    private Integer categoryId;

    /**
     * 子分类(标签)名称
     */
    private String subCategoryName;

    /**
     * 标签（2.5详情页中“类型”）
     */
    private String tagName;

    /**
     * 焦点图
     */
    private String img;

    /**
     * 名称
     */
    private String name;

    /**
     * 子名称
     */
    private String subName;

    /**
     * 别名
     */
    private String alias;

    /**
     * 描述
     */
    private String description;

    /**
     * 时长（毫秒）
     */
    private Long duration;

    /**
     * 评分
     */
    private String score;

    /**
     * 是否是推荐
     */
    private Integer is_rec;

    /**
     * 是否正片
     */
    private Integer positive;

    /**
     * 正片、预告片等id
     */
    private Integer albumTypeId;

    /**
     * 默认选中码流
     */
    private String defaultStream;

    /**
     * 码流列表
     */
    private List<Stream> streams;

    /**
     * 是否收费 1 shi 0 fou
     */
    private Integer charge;

    /**
     * 适应年龄
     */
    private String fitAge;

    /**
     * 地区
     */
    private String areaName;

    /**
     * 发行日期/上映日期(详情页界面显示用)
     */
    private String releaseDate;

    /**
     * 系列，如变形金刚1、2、3、4等
     */
    private String relationAlbumId;

    /**
     * 系列 , 共几季,可以查看<纸牌屋>
     */
    private Integer relationAlbumCnt;

    /**
     * 系列后缀[季、部、场、届]
     */
    private Integer relationAlbumType;

    /**
     * 是否跟播剧
     */
    private Integer end;

    /**
     * 总集数
     */
    private Integer episodes;

    /**
     * 更新至{nowEpisodes}集
     */
    private String nowEpisode;

    /**
     * 更新至{nowIssue}期
     */
    private String nowIssue;

    /**
     * 更新频率
     */
    private String updateFrequency;

    /**
     * 专辑观看次数
     */
    private Object vv;

    /**
     * 评论数
     */
    private Object commentCnt;

    /**
     * 允许播放平台
     */
    private String playPlatform;

    /**
     * 允许下载平台
     */
    private Map<String, String> downloadPlatform;

    /**
     * 剧集列表（正片）
     */
    private Object positiveSeries;

    /**
     * 周边视频
     */
    private Object nearlyVideos;

    /**
     * 预告
     */
    private List<VideoDto> preSeries;

    /**
     * 片段
     */
    private List<VideoDto> segments;

    /**
     * 剧集列表扩展（预告、抢先看等）
     */
    private Set<VideoDto> positiveAddSeries;

    /**
     * 演员信息
     */
    // private List<Actor> actorInfo;

    /**
     * 主持人
     */
    private String compere;

    /**
     * 歌手
     */
    private String singer;

    /**
     * 播出电视台名称
     */
    private String playTvName;

    /**
     * 播出电视台id
     */
    private Integer playTvId;

    /**
     * 是不是综艺类型,1是 0否
     */
    private Integer varietyShow;

    /**
     * 相关内容
     */
    private Object relation;

    /**
     * 内容分级id
     */
    private String contentRatingId;

    /**
     * 内容分级描述
     */
    private String contentRatingValue;

    /**
     * 内容分级描述
     */
    private String contentRatingDesc;

    /**
     * 付费类型 1支持单点付费
     */
    private Integer payType;

    /**
     * 过期时间
     */
    private Integer expiredTime;

    /**
     * 单点剩余时间
     */
    private String singleOrderLeftTime;

    /**
     * 单点：true:没够买或购买过期，false：购买没过期
     */
    private Integer expired;

    /**
     * 剧集列表样式//TODO
     * 1.图文列表
     * 2.九宫格
     */
    private Integer seriesStyle;

    private String starring;

    private List<ActorCache> starringList;

    /**
     * 导演
     */
    private String director;

    /**
     * 原作
     */
    private String originator;

    /**
     * 主创
     */
    private String supervise;

    /**
     * 声优
     */
    private String cast;

    /**
     * 配音
     */
    private String dub;

    /**
     * 音乐风格
     */
    private String musicStyle;

    private String styleName;

    /**
     * 关联音乐-片头、片尾曲
     */
    private List<MusicCache> relationMusic;
    /**
     * 关联音乐专辑
     */
    private List<OstCache> ost;

    private Integer homeMade;

    /**
     * 能否下载
     */
    private Integer download;

    /**
     * 乐词列表
     */
    private List<HotWordDto> hotWords;
    /**
     * 明星列表
     */
    private List<ActorCache> starList;
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
    /**
     * 正片推荐
     */
    private PositiveRecommendDto positiveRecommend;
    /**
     * 项目类型 - （体育）
     */
    private String sportsType;
    /**
     * 专辑最新三期
     */
    private List<VideoDto> latestVideoList;

    private Long createTime;// 创建时间 体育非栏目

    private String instructor;// 讲师

    private String secondCate;// 二级分类

    private String thirdCate;// 三级分类

    private String seriesShow;// 剧集列表展示类型 1：年月 0：列表

    private List<LinesCache> lines;// 专辑下台词

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAlbumTypeId() {
        return this.albumTypeId;
    }

    public void setAlbumTypeId(Integer albumTypeId) {
        this.albumTypeId = albumTypeId;
    }

    public String getDefaultStream() {
        return this.defaultStream;
    }

    public void setDefaultStream(String defaultStream) {
        this.defaultStream = defaultStream;
    }

    public List<Stream> getStreams() {
        return this.streams;
    }

    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }

    public String getFitAge() {
        return this.fitAge;
    }

    public void setFitAge(String fitAge) {
        this.fitAge = fitAge;
    }

    public String getRelationAlbumId() {
        return this.relationAlbumId;
    }

    public void setRelationAlbumId(String relationAlbumId) {
        this.relationAlbumId = relationAlbumId;
    }

    public Integer getEpisodes() {
        return this.episodes;
    }

    public void setEpisodes(Integer episodes) {
        this.episodes = episodes;
    }

    public String getNowEpisode() {
        return this.nowEpisode;
    }

    public void setNowEpisode(String nowEpisode) {
        this.nowEpisode = nowEpisode;
    }

    public String getNowIssue() {
        return this.nowIssue;
    }

    public void setNowIssue(String nowIssue) {
        this.nowIssue = nowIssue;
    }

    public String getPlayPlatform() {
        return this.playPlatform;
    }

    public void setPlayPlatform(String playPlatform) {
        this.playPlatform = playPlatform;
    }

    public Map<String, String> getDownloadPlatform() {
        return this.downloadPlatform;
    }

    public void setDownloadPlatform(Map<String, String> downloadPlatform) {
        this.downloadPlatform = downloadPlatform;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
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

    public Object getPositiveSeries() {
        return positiveSeries;
    }

    public void setPositiveSeries(Object positiveSeries) {
        this.positiveSeries = positiveSeries;
    }

    public List<VideoDto> getPreSeries() {
        return this.preSeries;
    }

    public void setPreSeries(List<VideoDto> preSeries) {
        this.preSeries = preSeries;
    }

    public List<VideoDto> getSegments() {
        return this.segments;
    }

    public void setSegments(List<VideoDto> segments) {
        this.segments = segments;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSubCategoryName() {
        return this.subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getPlayTvName() {
        return this.playTvName;
    }

    public void setPlayTvName(String playTvName) {
        this.playTvName = playTvName;
    }

    public Integer getPlayTvId() {
        return this.playTvId;
    }

    public void setPlayTvId(Integer playTvId) {
        this.playTvId = playTvId;
    }

    public String getCompere() {
        return this.compere;
    }

    public void setCompere(String compere) {
        this.compere = compere;
    }

    public String getUpdateFrequency() {
        return this.updateFrequency;
    }

    public void setUpdateFrequency(String updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    public String getTagName() {
        return this.tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getSinger() {
        return this.singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Integer getVarietyShow() {
        return this.varietyShow;
    }

    public void setVarietyShow(Integer varietyShow) {
        this.varietyShow = varietyShow;
    }

    public Integer getRelationAlbumCnt() {
        return this.relationAlbumCnt;
    }

    public void setRelationAlbumCnt(Integer relationAlbumCnt) {
        this.relationAlbumCnt = relationAlbumCnt;
    }

    public Integer getRelationAlbumType() {
        return this.relationAlbumType;
    }

    public void setRelationAlbumType(Integer relationAlbumType) {
        this.relationAlbumType = relationAlbumType;
    }

    public Object getRelation() {
        return relation;
    }

    public void setRelation(Object relation) {
        this.relation = relation;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Set<VideoDto> getPositiveAddSeries() {
        return this.positiveAddSeries;
    }

    public void setPositiveAddSeries(Set<VideoDto> positiveAddSeries) {
        this.positiveAddSeries = positiveAddSeries;
    }

    public String getContentRatingId() {
        return contentRatingId;
    }

    public void setContentRatingId(String contentRatingId) {
        this.contentRatingId = contentRatingId;
    }

    public String getContentRatingDesc() {
        return contentRatingDesc;
    }

    public void setContentRatingDesc(String contentRatingDesc) {
        this.contentRatingDesc = contentRatingDesc;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getSingleOrderLeftTime() {
        return singleOrderLeftTime;
    }

    public void setSingleOrderLeftTime(String singleOrderLeftTime) {
        this.singleOrderLeftTime = singleOrderLeftTime;
    }

    public String getContentRatingValue() {
        return contentRatingValue;
    }

    public void setContentRatingValue(String contentRatingValue) {
        this.contentRatingValue = contentRatingValue;
    }

    public Object getNearlyVideos() {
        return nearlyVideos;
    }

    public void setNearlyVideos(Object nearlyVideos) {
        this.nearlyVideos = nearlyVideos;
    }

    public Integer getIs_rec() {
        return is_rec;
    }

    public void setIs_rec(Integer is_rec) {
        this.is_rec = is_rec;
    }

    public Integer getPositive() {
        return positive;
    }

    public void setPositive(Integer positive) {
        this.positive = positive;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getExpired() {
        return expired;
    }

    public void setExpired(Integer expired) {
        this.expired = expired;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSeriesStyle() {
        return seriesStyle;
    }

    public void setSeriesStyle(Integer seriesStyle) {
        this.seriesStyle = seriesStyle;
    }

    public String getStarring() {
        return starring;
    }

    public void setStarring(String starring) {
        this.starring = starring;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<ActorCache> getStarringList() {
        return starringList;
    }

    public void setStarringList(List<ActorCache> starringList) {
        this.starringList = starringList;
    }

    public List<MusicCache> getRelationMusic() {
        return relationMusic;
    }

    public void setRelationMusic(List<MusicCache> relationMusic) {
        this.relationMusic = relationMusic;
    }

    public Integer getHomeMade() {
        return homeMade;
    }

    public void setHomeMade(Integer homeMade) {
        this.homeMade = homeMade;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getSupervise() {
        return supervise;
    }

    public void setSupervise(String supervise) {
        this.supervise = supervise;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDub() {
        return dub;
    }

    public void setDub(String dub) {
        this.dub = dub;
    }

    public String getMusicStyle() {
        return musicStyle;
    }

    public void setMusicStyle(String musicStyle) {
        this.musicStyle = musicStyle;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public Integer getDownload() {
        return download;
    }

    public void setDownload(Integer download) {
        this.download = download;
    }

    public List<HotWordDto> getHotWords() {
        return hotWords;
    }

    public void setHotWords(List<HotWordDto> hotWords) {
        this.hotWords = hotWords;
    }

    public List<ActorCache> getStarList() {
        return starList;
    }

    public void setStarList(List<ActorCache> starList) {
        this.starList = starList;
    }

    public List<OstCache> getOst() {
        return ost;
    }

    public void setOst(List<OstCache> ost) {
        this.ost = ost;
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

    public PositiveRecommendDto getPositiveRecommend() {
        return positiveRecommend;
    }

    public void setPositiveRecommend(PositiveRecommendDto positiveRecommend) {
        this.positiveRecommend = positiveRecommend;
    }

    public String getSportsType() {
        return sportsType;
    }

    public void setSportsType(String sportsType) {
        this.sportsType = sportsType;
    }

    public List<VideoDto> getLatestVideoList() {
        return latestVideoList;
    }

    public void setLatestVideoList(List<VideoDto> latestVideoList) {
        this.latestVideoList = latestVideoList;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getSecondCate() {
        return secondCate;
    }

    public void setSecondCate(String secondCate) {
        this.secondCate = secondCate;
    }

    public String getThirdCate() {
        return thirdCate;
    }

    public void setThirdCate(String thirdCate) {
        this.thirdCate = thirdCate;
    }

    public String getSeriesShow() {
        return seriesShow;
    }

    public void setSeriesShow(String seriesShow) {
        this.seriesShow = seriesShow;
    }

    public List<LinesCache> getLines() {
        return lines;
    }

    public void setLines(List<LinesCache> lines) {
        this.lines = lines;
    }

}
