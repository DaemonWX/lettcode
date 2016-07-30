package xserver.api.module.user.dto;

import xserver.api.dto.BaseDto;

/**
 * "我的"页面展示的菜单列表
 * @author
 */
public class ProfileItemDto extends BaseDto {

    /**
     * 菜单id
     */
    private String id;

    /**
     * 主标题
     */
    private String title;

    /**
     * 副标题
     */
    // private String subTitle;

    /**
     * 排序，服务端约定，值越小，排序越在前
     */
    // private Integer sort = 1;

    /**
     * 是否显示，0--不显示，1--显示，默认为1
     */
    // private Integer isDisplay;

    public ProfileItemDto(String id, String title) {
        super();
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
