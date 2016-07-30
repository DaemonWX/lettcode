package xserver.api.module.video.dto;

import xserver.lib.mysql.table.BaseData;

public class LiveWaterMarkDto extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = 2419694606709155699L;

    private String offset;// 水印相对位置 //数值%，表示距离画面边缘相对的距离
    private String lasttime;// 持续时间 //单位秒，该水印持续出现的时间
    private String position;// 位置 //该水印出现的位置 1为左上，2为右上，3为左下，4为右下
    private String url;// 水印图片地址

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
