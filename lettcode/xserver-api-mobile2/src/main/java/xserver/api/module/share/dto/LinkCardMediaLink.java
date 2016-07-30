package xserver.api.module.share.dto;

public class LinkCardMediaLink {

    /**
     * 选填
     * 媒体的时长,单位秒,包含该属性的如视频和 音乐,本案例中,一级的 stream 属性下建议填 写该属性
     */
    private int duration;

    /**
     * 必填
     * 媒体的地址,比如图片地址、视频流地址,本 案例中,一级的 image 属性下应为视频缩略图 地址,一级的 stream 属性下应为 lowband
     * 版本 的视频 mp4 地址
     */
    private String url;

    /**
     * 选填
     * 媒体的高清版地址,比如图片地址、视频流地 址,本案例中,一级的 stream 属性下应为 HD 版本的视频 mp4 地址
     */
    private String hd_url;

    /**
     * 选填
     * 媒体的宽度,比如图片宽度,本案例中,一级 的 image 属性下应为视频缩略图的宽,建议为 120 像素
     */
    private int width;

    /**
     * 选填
     * 媒体的高度,比如图片高度,本案例中,一级 的 image 属性下应为视频缩略图的高,建议为 80 像素
     */
    private int height;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHd_url() {
        return hd_url;
    }

    public void setHd_url(String hd_url) {
        this.hd_url = hd_url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
