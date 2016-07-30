package xserver.api.module.subject.dto;

import xserver.api.dto.BaseDto;

/**
 * CMS中版块中，定义的专题描述
 */
public class CmsSubjectDto extends BaseDto {
    private static final long serialVersionUID = -7180961384492459659L;
    private String nameCn;// 名称
    private String pic;// 图片地址
    private String type;// 1：视频，2：专辑，3：直播Code，4：用户ID，5：小专题ID，6：轮播台ID，7：直播专题ID
    private String contentId;// 专题id
    private String webViewUrl;// 跳转webView时的跳转地址
    private String padPic;// padPicurl, 目前没有使用到
    private String subTitle;// 子标题
    private String skipType;// 跳转类型：1移动外跳，2移动内跳，3，小专题地址，4tv外跳，5tv内跳

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

   

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getWebViewUrl() {
        return webViewUrl;
    }

    public void setWebViewUrl(String webViewUrl) {
        this.webViewUrl = webViewUrl;
    }

    public String getPadPic() {
        return padPic;
    }

    public void setPadPic(String padPic) {
        this.padPic = padPic;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getSkipType() {
        return skipType;
    }

    public void setSkipType(String skipType) {
        this.skipType = skipType;
    }

}
