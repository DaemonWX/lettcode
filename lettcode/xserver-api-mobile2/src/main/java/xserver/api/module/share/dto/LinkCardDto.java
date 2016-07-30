package xserver.api.module.share.dto;

public class LinkCardDto {

    /**
     * 必填
     * 视频的显示名称
     */
    private String display_name;

    /**
     * 选填
     * 视频的所有者,为一个 person 类型的对象
     */
    private LinkCardPerson author;

    /**
     * 必填
     * 视频的缩略显示图片,图片大小强烈建议为 120×80 像素,为一个 media link 类型的对象
     */
    private LinkCardMediaLink image;

    /**
     * 必填
     * 视频嵌入播放器的路径代码,用于 PC 设备上 在消息流里直接展开播放,应为一个 swf 的播 放器地址,强烈建议必填,如果没有将直接跳 转至
     * url 字段所对应的链接页面
     */
    private String embed_code;

    /**
     * 选填
     * 视频流的播放源(mp4 地址),用于移动设备 上的直接播放,为一个 media link 类型的对象
     */
    private LinkCardMediaLink stream;

    /**
     * 选填
     * 视频的文字描述,字数建议控制在 300 字以内
     */
    private String summary;

    /**
     * 必填
     * 视频页面的 URL 地址,该地址必须为一个纯净 的 URL,尽量不带有无关的参数,其将作为对 象数据的唯一标识依据,保持该 URL 的干净将
     * 有利于赞数据的统一和有效
     */
    private String url;

    /**
     * 选填
     * 视频的交互属性,对象数据的通用属性,为一 个 object 对象,其子属性将影响 LinkCard 的点 击交互行为
     */
    private LinkCardLink links;

    /**
     * 选填
     * 视频的标签属性,对象数据的通用属性,为一 个 object array 的对象数组
     */
    private Object[] tags;

    /**
     * 选填
     * 视频的创建时间,格式强烈建议用国际化格
     * 式:Wed Jan 06 11: 26: 01+0800 2010,或者用 简易格式:2012-10-18
     */
    private String create_at;

    /**
     * 必填
     * 对象类型,本对象是视频,那就是 video
     */
    private String object_type;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public LinkCardPerson getAuthor() {
        return author;
    }

    public void setAuthor(LinkCardPerson author) {
        this.author = author;
    }

    public LinkCardMediaLink getImage() {
        return image;
    }

    public void setImage(LinkCardMediaLink image) {
        this.image = image;
    }

    public String getEmbed_code() {
        return embed_code;
    }

    public void setEmbed_code(String embed_code) {
        this.embed_code = embed_code;
    }

    public LinkCardMediaLink getStream() {
        return stream;
    }

    public void setStream(LinkCardMediaLink stream) {
        this.stream = stream;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LinkCardLink getLinks() {
        return links;
    }

    public void setLinks(LinkCardLink links) {
        this.links = links;
    }

    public Object[] getTags() {
        return tags;
    }

    public void setTags(Object[] tags) {
        this.tags = tags;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }
}
