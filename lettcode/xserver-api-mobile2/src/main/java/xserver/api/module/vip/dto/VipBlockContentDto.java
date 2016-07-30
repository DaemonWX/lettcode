package xserver.api.module.vip.dto;

import xserver.api.dto.BaseDto;

/**
 * 会员模块使用的CMS配置内容封装类
 * @author
 */
public class VipBlockContentDto extends BaseDto {

    /**
     * 标题文案
     */
    private String title;

    /**
     * 移动端图片
     */
    private String mobilePic;

    /**
     * 跳转链接
     */
    private String skipUrl;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMobilePic() {
        return this.mobilePic;
    }

    public void setMobilePic(String mobilePic) {
        this.mobilePic = mobilePic;
    }

    public String getSkipUrl() {
        return this.skipUrl;
    }

    public void setSkipUrl(String skipUrl) {
        this.skipUrl = skipUrl;
    }

}
