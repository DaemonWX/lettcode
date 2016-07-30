package xserver.api.module.channel.dto;

import java.util.List;

import xserver.common.dto.channel.ChannelNavigation;
import xserver.lib.dto.channel.ChannelLiveDto;

/**
 * 频道页数据返回格式
 */
public class ChannelContentDto {

    private List<ChannelNavigation> navigation; // 导航栏
    private List<ChannelFocus> focus; // 焦点图
    private List<ChannelBlock> block; // 频道模块
    private ChannelLiveDto live;

    public List<ChannelNavigation> getNavigation() {
        return navigation;
    }

    public void setNavigation(List<ChannelNavigation> navigation) {
        this.navigation = navigation;
    }

    public List<ChannelFocus> getFocus() {
        return focus;
    }

    public void setFocus(List<ChannelFocus> focus) {
        this.focus = focus;
    }

    public List<ChannelBlock> getBlock() {
        return block;
    }

    public void setBlock(List<ChannelBlock> block) {
        this.block = block;
    }

    public ChannelLiveDto getLive() {
        return live;
    }

    public void setLive(ChannelLiveDto live) {
        this.live = live;
    }
}
