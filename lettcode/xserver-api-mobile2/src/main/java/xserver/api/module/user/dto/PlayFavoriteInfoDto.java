package xserver.api.module.user.dto;

import xserver.api.dto.BaseDto;

/**
 * 我的收藏剧实体封装类
 * @author KevinYi
 */
public class PlayFavoriteInfoDto extends BaseDto {

    /**
     * 收藏记录id
     */
    private String id;

    /**
     * 频道id
     */
    private String categoryId;

    /**
     * 专辑id
     */
    private String albumId;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 标题
     */
    private String title;

    /**
     * 子标题-
     */
    private String subTitle;

    /**
     * 频道名称
     */
    private String categoryName;

    /**
     * 是否有版权
     */
    private Boolean hasCopyright;

    /**
     * 分类
     */
    private String subCategory;

    /**
     * 该平台下最新更新到第几集或第几期
     */
    private String latestEpisode;

    /**
     * 默认400*250图片地址
     */
    private String img_400X250;

    /**
     * 更新状态，如电视剧更新至xx集或xx集完，综艺频道更新至xx期，
     */
    private String updateStatus;

    /**
     * 剧集在线状态，“0”--已下线，“1”--正常上线
     */
    private String onlineStatus;

    /**
     * 视频或者专辑是否下线，1下线，0在线
     */
    private String offline;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getHasCopyright() {
        return this.hasCopyright;
    }

    public void setHasCopyright(Boolean hasCopyright) {
        this.hasCopyright = hasCopyright;
    }

    public String getSubCategory() {
        return this.subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getLatestEpisode() {
        return this.latestEpisode;
    }

    public void setLatestEpisode(String latestEpisode) {
        this.latestEpisode = latestEpisode;
    }

    public String getImg_400X250() {
        return this.img_400X250;
    }

    public void setImg_400X250(String img_400x250) {
        this.img_400X250 = img_400x250;
    }

    public String getUpdateStatus() {
        return this.updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getOnlineStatus() {
        return this.onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getOffline() {
        return this.offline;
    }

    public void setOffline(String offline) {
        this.offline = offline;
    }

}
