package xserver.api.module.superlive.dto;

import java.util.List;

import xserver.common.dto.superlive.v2.SuperLiveChannelDto.SuperLiveTagDto;

public class LiveChannelCurrentStateDataDto {
    private String channelId;
    private String channelName;
    private String channelPic;
    private String isPay;
    private String type;
    private String isNewAdd;
    private LiveChannelProgramShortInfoData cur;
    private LiveChannelProgramShortInfoData next;
    // private List<LiveChannelStream> streams;
    private List<StreamDto> streams;
    private Integer weight; // 权重
    private Long recordingId; // 录播id
    private String liveType; // 直播类型 sports music other等
    private String channelEname; // 频道的英文名称
    private String signal; // 直播数据源分类 2:卫视台 7:轮播台
    private String channelClass; // 轮播的分类id
    private String streamTips; // 盗播流提示
    private String numericKeys; // 台号
    private MutilProgram multiProgram;
    private List<SuperLiveTagDto> leWords; // 乐词信息
    private String selfCopyRight; // 是否自有版权
    private String isDolby; // 是否是杜比直播

    public String getIsDolby() {
        return isDolby;
    }

    public void setIsDolby(String isDolby) {
        this.isDolby = isDolby;
    }

    public String getSelfCopyRight() {
        return selfCopyRight;
    }

    public void setSelfCopyRight(String selfCopyRight) {
        this.selfCopyRight = selfCopyRight;
    }

    public MutilProgram getMultiProgram() {
        return multiProgram;
    }

    public void setMultiProgram(MutilProgram multiProgram) {
        this.multiProgram = multiProgram;
    }

    public String getNumericKeys() {
        return numericKeys;
    }

    public void setNumericKeys(String numericKeys) {
        this.numericKeys = numericKeys;
    }

    public String getStreamTips() {
        return streamTips;
    }

    public void setStreamTips(String streamTips) {
        this.streamTips = streamTips;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String getChannelClass() {
        return channelClass;
    }

    public void setChannelClass(String channelClass) {
        this.channelClass = channelClass;
    }

    public String getChannelEname() {
        return channelEname;
    }

    public void setChannelEname(String channelEname) {
        this.channelEname = channelEname;
    }

    public String getLiveType() {
        return liveType;
    }

    public void setLiveType(String liveType) {
        this.liveType = liveType;
    }

    public Long getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(Long recordingId) {
        this.recordingId = recordingId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelPic() {
        return channelPic;
    }

    public void setChannelPic(String channelPic) {
        this.channelPic = channelPic;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LiveChannelProgramShortInfoData getCur() {
        return cur;
    }

    public void setCur(LiveChannelProgramShortInfoData cur) {
        this.cur = cur;
    }

    public LiveChannelProgramShortInfoData getNext() {
        return next;
    }

    public void setNext(LiveChannelProgramShortInfoData next) {
        this.next = next;
    }

    // public List<LiveChannelStream> getStreams() {
    // return streams;
    // }
    //
    // public void setStreams(List<LiveChannelStream> streams) {
    // this.streams = streams;
    // }
    public List<StreamDto> getStreams() {
        return streams;
    }

    public void setStreams(List<StreamDto> streams) {
        this.streams = streams;
    }

    public String getIsNewAdd() {
        return isNewAdd;
    }

    public void setIsNewAdd(String isNewAdd) {
        this.isNewAdd = isNewAdd;
    }

    public List<SuperLiveTagDto> getLeWords() {
        return leWords;
    }

    public void setLeWords(List<SuperLiveTagDto> leWords) {
        this.leWords = leWords;
    }

    public static class MutilProgram {
        private String branchDesc;
        private List<LiveChannelCurrentStateDataDto> branches;

        public String getBranchDesc() {
            return branchDesc;
        }

        public void setBranchDesc(String branchDesc) {
            this.branchDesc = branchDesc;
        }

        public List<LiveChannelCurrentStateDataDto> getBranches() {
            return branches;
        }

        public void setBranches(List<LiveChannelCurrentStateDataDto> branches) {
            this.branches = branches;
        }
    }

}
