package xserver.api.module.superlive.dto;

public class SuperLiveShareDto {
    private String channelId; // 轮播，卫视台，直播的id
    private String type; // 2:直播 3:轮播 4:卫视
    private String shareUrl; // 分享地址

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

}
