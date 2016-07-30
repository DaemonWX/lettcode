package xserver.api.module.user.dto;

import java.util.List;

import xserver.common.dto.superlive.v2.SuperLiveProgramDto;
import xserver.common.dto.superlive.v2.SuperLiveStreamDto;

/**
 * 超级live的播放记录
 * @author
 */
public class SuperLivePlayRecordInfoDto extends PlayRecordInfoDto {
    private String channelId; // 频道id
    private String channelName; // 频道名称
    private String channelPic; // 频道图片
    private String isPay; // 是否付费 1付费 0:免费

    private SuperLiveProgramDto cur; // 当前正在播放的节目信息
    private SuperLiveProgramDto next; // 下一个节目信息
    private List<SuperLiveStreamDto> streams; // 节目码流信息
    private String streamTips; // 盗播流提示

    public String getStreamTips() {
        return streamTips;
    }

    public void setStreamTips(String streamTips) {
        this.streamTips = streamTips;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelPic() {
        return this.channelPic;
    }

    public void setChannelPic(String channelPic) {
        this.channelPic = channelPic;
    }

    public String getIsPay() {
        return this.isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public SuperLiveProgramDto getCur() {
        return this.cur;
    }

    public void setCur(SuperLiveProgramDto cur) {
        this.cur = cur;
    }

    public SuperLiveProgramDto getNext() {
        return this.next;
    }

    public void setNext(SuperLiveProgramDto next) {
        this.next = next;
    }

    public List<SuperLiveStreamDto> getStreams() {
        return this.streams;
    }

    public void setStreams(List<SuperLiveStreamDto> streams) {
        this.streams = streams;
    }

}
