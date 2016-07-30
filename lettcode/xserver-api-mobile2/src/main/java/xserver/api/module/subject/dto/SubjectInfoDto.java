package xserver.api.module.subject.dto;

import java.util.List;

import xserver.api.dto.BaseDto;

/**
 * 小专题描述
 */
public class SubjectInfoDto extends BaseDto {
    private static final long serialVersionUID = 5653737682123702816L;
    private String name;// 专题名称
    private String type;// 专题类型，视频、专辑、直播
    private String ctime;// 创建时间
    private String cid;// 数据类型
    private String desc;// 描述
    private String tag;// 标签
    private List<BaseSubjectDataDto> dataList;// 数据列表

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public List<BaseSubjectDataDto> getDataList() {
        return dataList;
    }

    public void setDataList(List<BaseSubjectDataDto> dataList) {
        this.dataList = dataList;
    }

}
