package xserver.api.module.superlive.dto;

import java.util.List;

public class ChannelDto {
    private String channelId;
    private String channelName;
    private List<ProgramDto> programs;
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
    public List<ProgramDto> getPrograms() {
        return programs;
    }
    public void setPrograms(List<ProgramDto> programs) {
        this.programs = programs;
    }
    
}
