package xserver.api.module.video.dto;

import java.util.List;

import xserver.lib.mysql.table.BaseData;

public class PositiveRecommendDto extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = -4021102647739894048L;

    private PositiveAlbumDto positiveAlbum;// 正片专辑（用于电视剧、电影、动漫非正片视频）
    private List<VideoDto> positiveVideoList;// 相关正片 （用于综艺 预告片、其他）

    public PositiveAlbumDto getPositiveAlbum() {
        return positiveAlbum;
    }

    public void setPositiveAlbum(PositiveAlbumDto positiveAlbum) {
        this.positiveAlbum = positiveAlbum;
    }

    public List<VideoDto> getPositiveVideoList() {
        return positiveVideoList;
    }

    public void setPositiveVideoList(List<VideoDto> positiveVideoList) {
        this.positiveVideoList = positiveVideoList;
    }

}
