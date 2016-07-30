package xserver.api.module.subscribe.dto;

import xserver.common.dto.superlive.v2.SuperLiveChannelDto;

public class SubscribeEventDto {
    private String sourceID;    //数据源的事件的唯一标识
    private String sourceType;  //事件类型
    private String userId;      //用户ID
    private String appId;       //默认superlive
    private String title;       //直播title
    private SuperLiveChannelDto channel; // 直播数据
    private Boolean v ;         //创建或删除预约是否成功
    
    public Boolean getV() {
        return v;
    }
    public void setV(Boolean v) {
        this.v = v;
    }
    public SuperLiveChannelDto getChannel() {
        return channel;
    }
    public void setChannel(SuperLiveChannelDto channel) {
        this.channel = channel;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSourceID() {
        return sourceID;
    }
    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }
    public String getSourceType() {
        return sourceType;
    }
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
   
    
}
