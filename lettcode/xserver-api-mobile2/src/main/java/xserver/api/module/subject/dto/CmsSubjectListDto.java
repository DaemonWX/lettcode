package xserver.api.module.subject.dto;

import java.util.List;

import xserver.api.dto.BaseDto;

/**
 * CMS专题版块
 */
public class CmsSubjectListDto extends BaseDto {
    private static final long serialVersionUID = 5653737682123702816L;
    private List<CmsSubjectDto> subjects;// 版块下配置的专题列表

    public List<CmsSubjectDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<CmsSubjectDto> subjects) {
        this.subjects = subjects;
    }

}
