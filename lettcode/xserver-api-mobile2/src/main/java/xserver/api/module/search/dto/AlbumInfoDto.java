package xserver.api.module.search.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AlbumInfoDto {
    private Integer dataType; // 数据类型---参考SearchConstant中的DATA_TYPE
    private String name; // 名称
    private Long pid; // 专辑id
    private Long vid; // 视频id
    private Long zid; // 专题id
    private String pic; // 图片
    private Integer cid; // 频道ID
    private String director; // 导演
    private String star; // 主演
    private String desc; // 描述
    private String episodes;// 总集数
    private Long playCount; // 播放数
    private String subTitle; // 副标题
    private String nowEpisodes; // 跟播集数
    private String isEnd; // 是否完结
    private String url; // 播放地址
    private String subCategory; // 子类型
    private Long updateTime; // 更新时间
    private Float score; // 评分
    private String duration; // 视频时长
    private String pay; // 1:需要支付;0:免费（只有专辑有此属性）
    private String area; // 地区
    private String releaseDate; // 发布时间

    @JsonIgnore
    private Map<String, String> images; // 各种尺寸图片的集合,不返回给客户端

    /**
     * 角标类型： 1、全网；2、付费；3、会员；4、独播；5、自制；6专题；7预告；
     */
    private String cornerLabel;// 角标类型

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEpisodes() {
        return episodes;
    }

    public void setEpisodes(String episodes) {
        this.episodes = episodes;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCornerLabel() {
        return cornerLabel;
    }

    public void setCornerLabel(String cornerLabel) {
        this.cornerLabel = cornerLabel;
    }
}
