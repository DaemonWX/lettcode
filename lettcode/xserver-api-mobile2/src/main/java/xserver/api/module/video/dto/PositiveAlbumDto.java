package xserver.api.module.video.dto;

import xserver.lib.mysql.table.BaseData;

public class PositiveAlbumDto extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = 5723533970345451691L;

    private Long albumId;// 正片专辑id
    private String name;// 正片专辑名称
    private Object vv;// 正片播放数
    private String img;// 正片专辑图片

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getVv() {
        return vv;
    }

    public void setVv(Object vv) {
        this.vv = vv;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
