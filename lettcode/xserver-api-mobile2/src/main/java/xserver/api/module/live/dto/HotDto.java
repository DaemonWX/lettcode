package xserver.api.module.live.dto;

import java.util.ArrayList;
import java.util.List;

import xserver.lib.dto.live.LiveDto;

public class HotDto {
    List<LiveDto> hotReplay = new ArrayList<LiveDto>();// 热门回看
    List<LiveDto> hotLive = new ArrayList<LiveDto>();// 热门直播
    List<LiveDto> hotAd = new ArrayList<LiveDto>();// 热门预告

    public List<LiveDto> getHotReplay() {
        return hotReplay;
    }

    public void setHotReplay(List<LiveDto> hotReplay) {
        this.hotReplay = hotReplay;
    }

    public List<LiveDto> getHotLive() {
        return hotLive;
    }

    public void setHotLive(List<LiveDto> hotLive) {
        this.hotLive = hotLive;
    }

    public List<LiveDto> getHotAd() {
        return hotAd;
    }

    public void setHotAd(List<LiveDto> hotAd) {
        this.hotAd = hotAd;
    }

}
