package xserver.api.module.superlive.dto;

import xserver.lib.tp.superlive.response.LiveChannelCurProgram.LiveChannelProgramDetail;

public class LiveChannelProgramShortInfoData {
    private String id; // 节目单id
    private String duration;
    private String endTime;
    private String playTime;
    private String title;
    private String viewPic;
    private String price;// 价格
    private String vipPrice;// vip价格
    private String isCharge;// 是否已经付费
    private String screenings;// 场次
    private String status;// 直播专用字段，状态
    private String type;//二级分类
    private String subType;//三级分类
    private String chatRoomNum;// 聊天室RoomId
    private String isChat ;//是否启用聊天室是否启用聊天（1 是 0 否）

    public LiveChannelProgramShortInfoData() {
        super();
    }

    public LiveChannelProgramShortInfoData(LiveChannelProgramDetail cur) {
        duration = cur.getDuration();
        endTime = cur.getEndTime();
        playTime = cur.getPlayTime();
        title = cur.getTitle();
        viewPic = cur.getViewPic();
        id = cur.getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatRoomNum() {
        return chatRoomNum;
    }

    public void setChatRoomNum(String chatRoomNum) {
        this.chatRoomNum = chatRoomNum;
    }

    public String getIsChat() {
        return isChat;
    }

    public void setIsChat(String isChat) {
        this.isChat = isChat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViewPic() {
        return viewPic;
    }

    public void setViewPic(String viewPic) {
        this.viewPic = viewPic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getScreenings() {
        return screenings;
    }

    public void setScreenings(String screenings) {
        this.screenings = screenings;
    }

    public String getIsCharge() {
        return isCharge;
    }

    public void setIsCharge(String isCharge) {
        this.isCharge = isCharge;
    }

}
