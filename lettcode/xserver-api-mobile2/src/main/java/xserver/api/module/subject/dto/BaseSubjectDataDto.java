package xserver.api.module.subject.dto;

import xserver.api.dto.BaseDto;

/**
 * 专题数据基类
 */
public abstract class BaseSubjectDataDto extends BaseDto {
    /**
     * 
     */
    private static final long serialVersionUID = -3818075150874510046L;
    private String id;// 专辑、视频、直播 id
    private String nameCn;// 名称
    private String picUrl;// 图片地址
    private String dataType;// 1专辑、2视频，3直播
    private int download;//是否可下载
    
    

   

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

}
