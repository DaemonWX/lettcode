package xserver.api.module.share.dto;

public class LinkCardPerson {

    /**
     * 必填
     * 视频所有者的显示名称
     */
    private String display_name;

    /**
     * 选填
     * 视频所有者的介绍或个人空间地址
     */
    private String url;

    /**
     * 必填
     * 对象类型,本对象是人,那就是 person
     */
    private String object_type;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

}
