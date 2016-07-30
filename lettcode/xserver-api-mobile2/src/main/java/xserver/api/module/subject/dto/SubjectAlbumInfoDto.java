package xserver.api.module.subject.dto;


/**
 * 专题中视频包，视频信息类
 */
public class SubjectAlbumInfoDto extends BaseSubjectDataDto {
    private static final long serialVersionUID = 28570561600242562L;
    private String subTitle;// 副标题
    public String getSubTitle() {
        return subTitle;
    }
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
    
    
}
