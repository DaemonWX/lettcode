package xserver.api.module.user.dto;

public class UserFavMutilCheckDto {
    private String favType;
    private String channelId;
    private String vid;
    private String pid;
    private String favorite; // 1收藏 0未收藏

    public String getFavType() {
        return favType;
    }

    public void setFavType(String favType) {
        this.favType = favType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
