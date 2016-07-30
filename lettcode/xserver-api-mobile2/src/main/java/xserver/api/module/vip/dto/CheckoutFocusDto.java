package xserver.api.module.vip.dto;

import java.util.List;

import xserver.api.dto.BaseDto;

/**
 * 收银台焦点图封装类
 * @author
 */
public class CheckoutFocusDto extends BaseDto {

    /**
     * 普通手机（Android设备）焦点图url；2015-04-20废弃不用
     */
    private String mobilePic;

    /**
     * Pad专用叫短途URL；2015-04-20废弃不用
     */
    private String padPic;

    /**
     * 从CMS获取的焦点图信息
     */
    private List<VipBlockContentDto> focusList;

    /**
     * 页面样式
     */
    private String contentStyle;

    public CheckoutFocusDto() {
        super();
    }

    public String getMobilePic() {
        return this.mobilePic;
    }

    public void setMobilePic(String mobilePic) {
        this.mobilePic = mobilePic;
    }

    public String getPadPic() {
        return this.padPic;
    }

    public void setPadPic(String padPic) {
        this.padPic = padPic;
    }

    public List<VipBlockContentDto> getFocusList() {
        return this.focusList;
    }

    public void setFocusList(List<VipBlockContentDto> focusList) {
        this.focusList = focusList;
    }

    public String getContentStyle() {
        return this.contentStyle;
    }

    public void setContentStyle(String contentStyle) {
        this.contentStyle = contentStyle;
    }

}
