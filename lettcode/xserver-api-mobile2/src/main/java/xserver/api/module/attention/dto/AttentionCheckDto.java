package xserver.api.module.attention.dto;


public class AttentionCheckDto {
    private String tagID;    
    private Boolean isAttention;
    public String getTagID() {
        return tagID;
    }
    public void setTagID(String tagID) {
        this.tagID = tagID;
    }
    public Boolean getIsAttention() {
        return isAttention;
    }
    public void setIsAttention(Boolean isAttention) {
        this.isAttention = isAttention;
    }
    
}
