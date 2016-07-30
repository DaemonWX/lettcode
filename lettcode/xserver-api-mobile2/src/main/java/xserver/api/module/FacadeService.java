package xserver.api.module;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import xserver.api.module.appointment.AppointmentService;
import xserver.api.module.attention.AttentionService;
import xserver.api.module.channel.ChannelService;
import xserver.api.module.comment.CommentService;
import xserver.api.module.live.LiveService;
import xserver.api.module.play.VODService;
import xserver.api.module.search.SearchService;
import xserver.api.module.share.ShareService;
import xserver.api.module.start.BootStrapService;
import xserver.api.module.subject.SubjectService;
import xserver.api.module.subscribe.SubscribeService;
import xserver.api.module.superlive.SuperLiveChatRoomService;
import xserver.api.module.superlive.SuperLivePlayLogService;
import xserver.api.module.superlive.SuperLiveService;
import xserver.api.module.superlive.SuperLiveV2Service;
import xserver.api.module.user.PlayFavoriteService;
import xserver.api.module.user.PlayLogService;
import xserver.api.module.user.UserService;
import xserver.api.module.video.PlayService;
import xserver.api.module.video.VideoService;
import xserver.api.module.vip.VipService;
import xserver.api.module.vote.VoteService;

/**
 * Service门面
 */
@Component
public class FacadeService {

    @Resource
    private SampleService sampleService;
    @Resource
    private UserService userService;

    @Resource(name = "PlayService")
    private PlayService playService;

    @Resource(name = "VideoService")
    private VideoService videoService;

    @Resource(name = "channelService")
    private ChannelService channelService;

    @Resource(name = "vipService")
    private VipService vipService;

    @Resource(name = "subjectService")
    private SubjectService subjectService;

    @Resource(name = "voteService")
    private VoteService voteService;

    @Resource(name = "searchService")
    private SearchService searchService;

    @Resource(name = "playLogService")
    private PlayLogService playLogService;

    @Resource(name = "playFavoriteService")
    private PlayFavoriteService playFavoriteService;

    @Resource(name = "commentService")
    private CommentService commentService;

    @Resource(name = "liveService")
    private LiveService liveService;

    @Resource(name = "superLiveService")
    private SuperLiveService superLiveService;
    @Resource(name = "superLiveV2Service")
    private SuperLiveV2Service superLiveV2Service;
    @Resource(name = "appointmentService")
    private AppointmentService appointmentService;

    //预约service
    @Resource(name = "subscribeService")
    private SubscribeService subscribeService;
    //关注service
    @Resource(name = "attentionService")
    private AttentionService attentionService;
    
    @Resource(name = "superLivePlayLogService")
    private SuperLivePlayLogService superLivePlayLogService;

    @Resource(name = "VODService")
    private VODService vodService;
    @Resource(name = "superLiveChatRoomService")
    private SuperLiveChatRoomService superLiveChatRoomService;
    @Resource(name = "shareService")
    private ShareService shareService;

    public SuperLiveChatRoomService getSuperLiveChatRoomService() {
        return superLiveChatRoomService;
    }

    public SuperLiveV2Service getSuperLiveV2Service() {
        return superLiveV2Service;
    }

    public SuperLivePlayLogService getSuperLivePlayLogService() {
        return superLivePlayLogService;
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }

    public SuperLiveService getSuperLiveService() {
        return superLiveService;
    }

    public PlayLogService getPlayLogService() {
        return playLogService;
    }
    
    public SubscribeService getSubscribeService() {
        return subscribeService;
    }
    public AttentionService getAttentionService() {
        return attentionService;
    }

    public PlayFavoriteService getPlayFavoriteService() {
        return playFavoriteService;
    }

    public VoteService getVoteService() {
        return voteService;
    }

    public SubjectService getSubjectService() {
        return subjectService;
    }

    public VipService getVipService() {
        return vipService;
    }

    @Resource(name = "bootStrapService")
    private BootStrapService bootStrapService;

    public UserService getUserService() {
        return userService;
    }

    public SampleService getSampleService() {
        return sampleService;
    }

    public PlayService getPlayService() {
        return playService;
    }

    public VideoService getVideoService() {
        return videoService;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public BootStrapService getBootStrapService() {
        return bootStrapService;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public CommentService getCommentService() {
        return commentService;
    }

    public LiveService getLiveService() {
        return liveService;
    }

    public VODService getVodService() {
        return vodService;
    }
    public ShareService getShareService() {
        return shareService;
    }
}
