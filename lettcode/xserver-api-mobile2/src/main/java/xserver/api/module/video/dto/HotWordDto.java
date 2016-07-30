package xserver.api.module.video.dto;

import xserver.lib.mysql.table.BaseData;

public class HotWordDto extends BaseData {
    /**
     * 
     */
    private static final long serialVersionUID = 5421666868522044668L;
    private Integer id;// 乐词id
    private String name;// 乐词名称
    private String img;// 乐词图片
    private Integer attention;// 关注数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getAttention() {
        return attention;
    }

    public void setAttention(Integer attention) {
        this.attention = attention;
    }

}
