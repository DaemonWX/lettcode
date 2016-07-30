package xserver.api.module.comment.dto;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import xserver.lib.tp.comment.response.CommentUser.Cooperation;

/**
 * 发表评论的用户信息
 */
public class CommentUserDto {
    private String uid; // 用户id 为0则表示为第三方或未登录的用户发布
    private String username; // 用户名
    private String photo; // 用户头像
    private String isvip; // 是否是vip
    private List<Cooperation> cooperation; // 第三方合作用户信息

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        if (StringUtils.isNotEmpty(photo) && photo.endsWith("/tx50.png")) {
            this.photo = null;
        } else {
            this.photo = photo;
        }
    }

    public String getIsvip() {
        return isvip;
    }

    public void setIsVip(String isvip) {
        this.isvip = isvip;
    }

    public List<Cooperation> getCooperation() {
        return cooperation;
    }

    public void setCooperation(List<Cooperation> cooperation) {
        this.cooperation = cooperation;
    }
}
