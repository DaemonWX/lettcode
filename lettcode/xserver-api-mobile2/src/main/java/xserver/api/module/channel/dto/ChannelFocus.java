package xserver.api.module.channel.dto;

import java.util.List;
import java.util.Map;

/**
 * 频道焦点图数据
 */
public class ChannelFocus {

    // 首页列表中点击下一步动作：半屏播放器
    public static final String HOME_ACTION_TYPE_FULL = "1";
    // 列表中点击视频下一步动作：全屏无专辑单视频
    public static final String HOME_ACTION_TYPE_HALF = "2";
    // 列表中点击视频下一步动作：全屏播放直播流
    public static final String HOME_ACTION_TYPE_LIVE = "3";
    // 列表中点击视频下一步动作：外跳web
    public static final String HOME_ACTION_TYPE_WEB = "4";
    // 列表中点击视频下一步动作：内嵌webview
    public static final String HOME_ACTION_TYPE_WEBVIEW = "5";
    // 列表中点击视频下一步动作：进入精品推荐页
    public static final String HOME_ACTION_TYPE_EXCHANGE = "6";
    // 列表中点击视频下一步动作：频道入口引导（点击进入对应的频道）
    public static final String HOME_ACTION_TYPE_CHANNEL = "7";
    // 列表中点击视频下一步动作：进入native专题页
    public static final String HOME_ACTION_TYPE_NATIVE = "8";

    private String cmsid; // cms唯一id
    private Long pid; // 专辑id
    private Long vid; // 视频id
    private Long zid; // 专题id
    /**
     * 点击展示方式
     * 1.半屏播放器
     * 2.全屏无专辑单视频
     * 3.全屏播放直播流
     * 4.外跳web
     * 5.内嵌webview
     * 6.进入精品推荐页
     * 7.频道入口引导（点击进入对应的频道,根据cid和pageid进入相应页面）
     * 11.会员频道
     * 12.收银台界面
     * 13.联通流量包套餐订购页
     * 14.我的积分
     * 19.登录界面
     */
    private String at;
    private String episode; // 总集数
    private String nowEpisodes; // 当前更新集数
    private String isEnd; // 是否完结
    private String pic; // 焦点图片
    private String name; // 名称
    private String pay; // 1:需要支付;0:免费（只有专辑有此属性）
    private String singer; // 歌手
    private String subTitle;// 副标题
    private String tag; // 盖章标签
    private String tm; // 过期时间戳
    private String type; // 影片来源标示：1-专辑,2-视频,3-明星,4-专题,5 外网影片
    private String liveCode; // 直播编号
    private String liveUrl; // 直播地址
    private String webUrl; // 外跳web地址
    private String webViewUrl; // 内嵌webview地址
    private String albumType; // 专辑类型
    private String varietyShow; // 是否是栏目:1 - 是，0 - 否
    private Integer cid; // 频道入口
    private String cname; // 频道名称
    private String duration; // 视频时长
    private String videoType; // 视频类型
    private String liveid; // 直播场次id
    private String homeImgUrl; // 主队图标
    private String guestImgUrl; // 客队图标
    private String id; // 直播id
    private String pageid; // 页面id
    private List<ShowTagList> showTagList; // 分类标签列表

    private String dataUrl; // 跳转频道的url
    private String defaultStream;// 默认码流

    private String director; // 导演
    private String subCategory; // 子类型
    private Float score; // 评分
    private String playCount; // 播放量
    private Long updateTime; // 更新时间
    private Map<String, String> picAll; // 多钟尺寸的图片
    private int source; // 数据来源
    private String releaseDate; // 发布时间
    private String area; // 地区
    private Integer issue;// 期数
    private String albumName;// 专辑名称
    private String guest;// 嘉宾

    private String recFragId;// 推荐数据上报字段
    private String recReid; // 推荐数据上报字段
    private String recArea;// 推荐数据上报字段
    private String recBucket;// 推荐数据上报字段
    private String src;// 数据来源 0 编辑手动 1 推荐自动

    /**
     *角标类型： 1、全网；2、付费；3、会员；4、独播；5、自制；6专题；7预告；
     */
    private String cornerLabel;//角标类型

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public Map<String, String> getPicAll() {
        return picAll;
    }

    public void setPicAll(Map<String, String> picAll) {
        this.picAll = picAll;
    }

    public String getDefaultStream() {
        return defaultStream;
    }

    public void setDefaultStream(String defaultStream) {
        this.defaultStream = defaultStream;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getCmsid() {
        return cmsid;
    }

    public void setCmsid(String cmsid) {
        this.cmsid = cmsid;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public Long getZid() {
        return zid;
    }

    public void setZid(Long zid) {
        this.zid = zid;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getNowEpisodes() {
        return nowEpisodes;
    }

    public void setNowEpisodes(String nowEpisodes) {
        this.nowEpisodes = nowEpisodes;
    }

    public String getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLiveCode() {
        return liveCode;
    }

    public void setLiveCode(String liveCode) {
        this.liveCode = liveCode;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebViewUrl() {
        return webViewUrl;
    }

    public void setWebViewUrl(String webViewUrl) {
        this.webViewUrl = webViewUrl;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public String getVarietyShow() {
        return varietyShow;
    }

    public void setVarietyShow(String varietyShow) {
        this.varietyShow = varietyShow;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getLiveid() {
        return liveid;
    }

    public void setLiveid(String liveid) {
        this.liveid = liveid;
    }

    public String getHomeImgUrl() {
        return homeImgUrl;
    }

    public void setHomeImgUrl(String homeImgUrl) {
        this.homeImgUrl = homeImgUrl;
    }

    public String getGuestImgUrl() {
        return guestImgUrl;
    }

    public void setGuestImgUrl(String guestImgUrl) {
        this.guestImgUrl = guestImgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public List<ShowTagList> getShowTagList() {
        return showTagList;
    }

    public void setShowTagList(List<ShowTagList> showTagList) {
        this.showTagList = showTagList;
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
        this.issue = issue;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getRecFragId() {
        return recFragId;
    }

    public void setRecFragId(String recFragId) {
        this.recFragId = recFragId;
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getCornerLabel() {
        return cornerLabel;
    }

    public void setCornerLabel(String cornerLabel) {
        this.cornerLabel = cornerLabel;
    }

    public static class ShowTagList {
        private String key; // 筛选字段
        private String id; // 筛选字段值
        private String value;// 分类标签显示名称

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
