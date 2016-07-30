package xserver.api.module.play.dto;

import java.util.List;

import xserver.api.module.video.dto.LiveWaterMarkDto;

public class SuperLiveWaterMarkDto {
    private String channelId; // 频道id
    private String type; // 频道类型:卫视,轮播台,直播
    private List<LiveWaterMarkDto> wartermark;

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

    public List<LiveWaterMarkDto> getWartermark() {
        return wartermark;
    }

    public void setWartermark(List<LiveWaterMarkDto> wartermark) {
        this.wartermark = wartermark;
    }
}
