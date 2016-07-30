package xserver.api.module.user.dto;

import xserver.api.dto.BaseDto;
import xserver.lib.tp.user.response.PlayRecordInfoTpResponse;

/**
 * 单条播放记录DTO
 * @author KevinYi
 */
public class PlayRecordInfoDto extends BaseDto {

    /**
     * 专辑id
     */
    private String albumId;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 直播、轮播、卫视id
     */
    private String oid;

    /**
     * 下一集视频id
     */
    private String nextVideoId;

    /**
     * 专辑名称-
     */
    private String albumName;

    /**
     * 视频名称-
     */
    private String videoName;

    /**
     * 标题
     */
    private String title;

    /**
     * 当前观看的是第几集或第几期-
     */
    private String seriesNum;

    /**
     * 跟播到第几集或第几期-
     */
    private String followNum;

    /**
     * 总集数或总期数-
     */
    private String totalNum;

    /**
     * 上次播放时间点
     */
    private Long playTime;

    /**
     * 当前这一集或这一期总时长-
     */
    private Long duration;

    /**
     * 最后更新时间（观看结束时的系统时间）
     */
    private String lastUpdateTime;

    /**
     * 是否播放完毕
     */
    private Boolean isEnd;

    /**
     * 400X250图
     */
    private String img_400X250;

    /**
     * 当前请求端是否有版权-
     */
    private Boolean hasCopyright;

    /**
     * 视频类型，1--单视频(电影)，2--多视频(连续剧、动漫、综艺)，0--其他
     */
    private String videoType;

    /**
     * 视频分类，如电影、电视剧、综艺、动漫等
     */
    private String categoryId;

    /**
     * PC版播放地址-
     */
    private String url;

    /**
     * 是否外跳 0:无需外跳 1:外跳-
     */
    private String isPlayOutside;

    /**
     * 播放记录来源 1："网页", 2:"手机客户端", 3:"平板",4:"电视",5:"桌面客户端"，需要添加“超级手机”
     */
    private String from;

    /**
     * 来源-
     */
    private String fromName;

    /**
     * 观看状态，0--续播，1--下一集，2--回看
     */
    private String playStatus;

    /**
     * 观看状态文案
     */
    private String playStatusName;

    /**
     * 播放记录上传来源的终端应用类型，包含超级电视和超级手机，如“MAX1”
     */
    private String product;

    /**
     * 播放记录上传来源的终端应用类型名称，目前返回空，或“超级电视”，或“超级手机”
     */
    private String productName;

    /**
     * 播放记录类型，0--全部记录，1--点播记录（由于历史原因，用户中心没有记录点播的type值），2--直播，3--轮播，4--卫视，默认为1
     */
    private String type;

    public PlayRecordInfoDto() {
        super();
    }

    public PlayRecordInfoDto(PlayRecordInfoTpResponse playRecordItem) {
        // TODO 注意空指针处理
        super();
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getOid() {
        return this.oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getNextVideoId() {
        return this.nextVideoId;
    }

    public void setNextVideoId(String nextVideoId) {
        this.nextVideoId = nextVideoId;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getVideoName() {
        return this.videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeriesNum() {
        return this.seriesNum;
    }

    public void setSeriesNum(String seriesNum) {
        this.seriesNum = seriesNum;
    }

    public String getFollowNum() {
        return this.followNum;
    }

    public void setFollowNum(String followNum) {
        this.followNum = followNum;
    }

    public String getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public Long getPlayTime() {
        return this.playTime;
    }

    public void setPlayTime(Long playTime) {
        this.playTime = playTime;
    }

    public Long getDuration() {
        return this.duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getIsEnd() {
        return this.isEnd;
    }

    public void setIsEnd(Boolean isEnd) {
        this.isEnd = isEnd;
    }

    public String getImg_400X250() {
        return this.img_400X250;
    }

    public void setImg_400X250(String img_400x250) {
        this.img_400X250 = img_400x250;
    }

    public Boolean getHasCopyright() {
        return this.hasCopyright;
    }

    public void setHasCopyright(Boolean hasCopyright) {
        this.hasCopyright = hasCopyright;
    }

    public String getVideoType() {
        return this.videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsPlayOutside() {
        return this.isPlayOutside;
    }

    public void setIsPlayOutside(String isPlayOutside) {
        this.isPlayOutside = isPlayOutside;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromName() {
        return this.fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getPlayStatus() {
        return this.playStatus;
    }

    public void setPlayStatus(String playStatus) {
        this.playStatus = playStatus;
    }

    public String getPlayStatusName() {
        return this.playStatusName;
    }

    public void setPlayStatusName(String playStatusName) {
        this.playStatusName = playStatusName;
    }

    public String getProduct() {
        return this.product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
