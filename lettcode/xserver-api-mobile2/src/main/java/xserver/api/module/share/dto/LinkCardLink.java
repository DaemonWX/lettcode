package xserver.api.module.share.dto;

public class LinkCardLink {

    /**
     * 必填
     * 点击跳转交互自定义的 URL,本案例中,为移 动端(接入方的)H5 中间播放页地址
     */
    private String url;

    /**
     * 选填
     * 移动端交互,呼起第三方客户端的 scheme 数 据(暂不支持)
     */
    private String scheme;

    /**
     * 选填
     * 交互动作在前端显示的文案(暂不支持)
     */
    private String display_name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

}
