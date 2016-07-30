package xserver.api.module.vip.dto;

/**
 * 直播鉴权DTO，返回针对某一直播，当前用户是否有权播放；当用户有播放权限， 并且直播开始时，返回token信息，用于播放鉴权。
 * @author KevinYi
 */
public class CheckLiveDto {

    /**
     * 是否有直播的播放权限（是否购买过），0--未购买过，无播放权限，1--购买过，有播放权限
     */
    private Integer hasPlayPermission;

    /**
     * token信息，播放鉴权时使用；注意，该值不一定返回；当用户购买成功，但直播未开始时，该值可能不返回
     */
    private String token;

    /**
     * 会员登录信息，播放鉴权使用；非必填，token有值时，本字段也不一定有值
     */
    private String uinfo;

    public Integer getHasPlayPermission() {
        return this.hasPlayPermission;
    }

    public void setHasPlayPermission(Integer hasPlayPermission) {
        this.hasPlayPermission = hasPlayPermission;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUinfo() {
        return this.uinfo;
    }

    public void setUinfo(String uinfo) {
        this.uinfo = uinfo;
    }

}
