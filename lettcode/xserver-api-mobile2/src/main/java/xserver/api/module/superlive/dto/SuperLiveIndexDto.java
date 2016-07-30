package xserver.api.module.superlive.dto;

import java.util.List;

public class SuperLiveIndexDto {
    private String bucket;
    private List<SuperLiveCategoryDataDto> categoryList;
    private String reid;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public List<SuperLiveCategoryDataDto> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<SuperLiveCategoryDataDto> categoryList) {
        this.categoryList = categoryList;
    }

    public String getReid() {
        return reid;
    }

    public void setReid(String reid) {
        this.reid = reid;
    }

}
