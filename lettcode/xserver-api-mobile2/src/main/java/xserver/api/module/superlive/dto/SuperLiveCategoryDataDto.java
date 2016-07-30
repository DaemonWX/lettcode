package xserver.api.module.superlive.dto;

import java.util.List;

public class SuperLiveCategoryDataDto {
    private String categoryId;
    private String categoryName;
    private String categoryPic;
    private String color;
    private String reid;
    private String bucket;
    private int num;
    private List<LiveChannelCurrentStateDataDto> channelList;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryPic() {
        return categoryPic;
    }

    public void setCategoryPic(String categoryPic) {
        this.categoryPic = categoryPic;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<LiveChannelCurrentStateDataDto> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<LiveChannelCurrentStateDataDto> channelList) {
        this.channelList = channelList;
    }

    public String getReid() {
        return reid;
    }

    public void setReid(String reid) {
        this.reid = reid;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
