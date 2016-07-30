package xserver.api.module.video.dto;

import xserver.lib.mysql.table.BaseData;

public class UserAttentionStatusDto extends BaseData {
    /**
     * 
     */
    private static final long serialVersionUID = -6866136330890747095L;
    private String attention;// 是否已关注 1：已关注 0：未关注
    private String success;// 1 :成功 0：失败

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}
