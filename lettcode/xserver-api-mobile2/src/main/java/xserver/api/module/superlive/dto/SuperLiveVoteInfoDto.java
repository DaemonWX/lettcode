package xserver.api.module.superlive.dto;

import java.util.List;

public class SuperLiveVoteInfoDto {
    private String channelId; // 直播的id
    private String guideTitle; // 播放器引导文案
    private String guidePic; // 播放器引导图
    private String voteTitle; // 投票的标题
    private List<VoteOption> options; // 投票的选项

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getGuideTitle() {
        return guideTitle;
    }

    public void setGuideTitle(String guideTitle) {
        this.guideTitle = guideTitle;
    }

    public String getGuidePic() {
        return guidePic;
    }

    public void setGuidePic(String guidePic) {
        this.guidePic = guidePic;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public List<VoteOption> getOptions() {
        return options;
    }

    public void setOptions(List<VoteOption> options) {
        this.options = options;
    }

    public static class VoteOption {
        private String optId;
        private String optTitle;
        private String optPic;
        private Long optVotes;

        public String getOptId() {
            return optId;
        }

        public void setOptId(String optId) {
            this.optId = optId;
        }

        public String getOptTitle() {
            return optTitle;
        }

        public void setOptTitle(String optTitle) {
            this.optTitle = optTitle;
        }

        public String getOptPic() {
            return optPic;
        }

        public void setOptPic(String optPic) {
            this.optPic = optPic;
        }

        public Long getOptVotes() {
            return optVotes;
        }

        public void setOptVotes(Long optVotes) {
            this.optVotes = optVotes;
        }
    }
}
