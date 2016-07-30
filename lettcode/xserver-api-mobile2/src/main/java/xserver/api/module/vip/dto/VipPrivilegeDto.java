package xserver.api.module.vip.dto;

import java.util.List;

import xserver.api.dto.BaseDto;

/**
 * 会员权益文案
 * @author
 */
public class VipPrivilegeDto extends BaseDto {

    /**
     * 标题文案
     */
    private String title;

    /**
     * 副标题列表；2015-04-20废弃不用
     */
    private List<String> subTitles;

    /**
     * 从CMS获取的会员权益焦点图信息
     */
    private List<VipBlockContentDto> privilegeList;

    /**
     * 页面样式
     */
    private String contentStyle;

    /**
     * 会员服务协议文案
     */
    private String userAggrementText;

    /**
     * 会员服务协议跳转链接
     */
    private String userAggrementUrl;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getSubTitles() {
        return this.subTitles;
    }

    public void setSubTitles(List<String> subTitles) {
        this.subTitles = subTitles;
    }

    public List<VipBlockContentDto> getPrivilegeList() {
        return this.privilegeList;
    }

    public void setPrivilegeList(List<VipBlockContentDto> privilegeList) {
        this.privilegeList = privilegeList;
    }

    public String getContentStyle() {
        return this.contentStyle;
    }

    public void setContentStyle(String contentStyle) {
        this.contentStyle = contentStyle;
    }

    public String getUserAggrementText() {
        return this.userAggrementText;
    }

    public void setUserAggrementText(String userAggrementText) {
        this.userAggrementText = userAggrementText;
    }

    public String getUserAggrementUrl() {
        return this.userAggrementUrl;
    }

    public void setUserAggrementUrl(String userAggrementUrl) {
        this.userAggrementUrl = userAggrementUrl;
    }

}
