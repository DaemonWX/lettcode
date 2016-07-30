package xserver.api.module.superlive.dto;

public class SuperLiveCategoryDto {
    private String code;
    private String name;
    private String color;
    private String categoryPic;
    public SuperLiveCategoryDto() {
        super();
    }
    public SuperLiveCategoryDto(String code, String name) {
        super();
        this.code = code;
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getCategoryPic() {
        return categoryPic;
    }
    public void setCategoryPic(String categoryPic) {
        this.categoryPic = categoryPic;
    }
    
}
