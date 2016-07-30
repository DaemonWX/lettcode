package xserver.api.module.subject.dto;


/**
 * 专题中视频包，视频信息类
 */
public class SubjectVideoInfoDto extends BaseSubjectDataDto {
    private static final long serialVersionUID = 28570561600242562L;
    private String voteId;//投票id
    private Long pid;//所属专辑id
    private Integer type;//是否单片
    private Long duration;//片时长
    private Integer play;//是否可以播放
    private String videoType;//视频类型：正片，预知、片花
    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getPlay() {
        return play;
    }

    public void setPlay(Integer play) {
        this.play = play;
    }
    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

}
