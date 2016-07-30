package xserver.api.module.vip.dto;

import xserver.api.dto.BaseDto;

/**
 * 机卡绑定文案封装类
 * @author
 */
public class DeviceBindTextDto extends BaseDto {

    /**
     * 文案主标题，与content组合使用
     */
    private String title;

    /**
     * 文案内容，与title组合使用
     */
    private String content;

    /**
     * 短文案
     */
    private String tips;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTips() {
        return this.tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

}
