package xserver.api.module.video.dto;

import java.util.List;

import xserver.lib.mysql.table.BaseData;

public class VideoPlayListDto extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = 7199966016483433955L;

    private List<VideoDto> playList;

    public List<VideoDto> getPlayList() {
        return playList;
    }

    public void setPlayList(List<VideoDto> playList) {
        this.playList = playList;
    }

}
