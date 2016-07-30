package xserver.api.module.search.dto;

public class SuggestDto {
    private String name;// 名称

    public SuggestDto() {
    }

    public SuggestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
