package xserver.api.module.user.dto;

import xserver.common.dto.superlive.v2.SuperLiveChannelDto;

public class UserLiveFavDto {
    private String cid; // 频道对应的一级分类id
    private String subCid; // 频道对应的二级分类id
    private String favid; // 收藏id
    private SuperLiveChannelDto channel; // 频道数据

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSubCid() {
        return subCid;
    }

    public void setSubCid(String subCid) {
        this.subCid = subCid;
    }

    public String getFavid() {
        return favid;
    }

    public void setFavid(String favid) {
        this.favid = favid;
    }

    public SuperLiveChannelDto getChannel() {
        return channel;
    }

    public void setChannel(SuperLiveChannelDto channel) {
        this.channel = channel;
    }

}
