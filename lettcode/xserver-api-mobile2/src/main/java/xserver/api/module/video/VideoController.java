package xserver.api.module.video;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.video.dto.LikeDto;
import xserver.api.module.video.dto.PositiveRecommendDto;
import xserver.api.module.video.dto.VideoDto;
import xserver.api.module.video.dto.VideoPlayListDto;
import xserver.api.response.Response;
import xserver.common.dto.BaseDto;
import xserver.lib.dto.video.InteractDto;
import xserver.lib.util.RequestUtil;

@Component(value = "VideoController")
@Controller
public class VideoController extends BaseController {

    @RequestMapping("/video/play/get")
    public Response<VideoDto> getPlayInfo(HttpServletRequest request,
            @RequestParam(value = "videoid", required = false) Long videoId,
            @RequestParam(value = "albumid", required = false) Long albumId,
            @RequestParam(value = "timestamp", required = false) Long timestamp,
            @RequestParam(value = "sig", required = false) String sig,
            @RequestParam(value = "stream", required = false) String stream,
            @RequestParam(value = "devKey", required = false) String deviceKey,
            @RequestParam(value = "sKey", required = false) String sKey, @ModelAttribute CommonParam param) {

        String clientIp = RequestUtil.getClientIp(request);
        String routerId = RequestUtil.getRouterInfo(request);
        return this.facadeService.getPlayService().getVideoPlayInfo(videoId, albumId, stream, timestamp, sig, clientIp,
                deviceKey, param, routerId, sKey);
    }

    @RequestMapping("/video/download/get")
    public Response<VideoDto> getDownloadInfo(HttpServletRequest request,
            @RequestParam(value = "videoid", required = false) Long videoId,
            @RequestParam(value = "albumid", required = false) Long albumId,
            @RequestParam(value = "timestamp", required = false) Long timestamp,
            @RequestParam(value = "sig", required = false) String sig,
            @RequestParam(value = "stream", required = false) String stream,
            @RequestParam(value = "userid", required = false) Long userId, @ModelAttribute CommonParam param) {

        String clientIp = RequestUtil.getClientIp(request);
        return this.facadeService.getPlayService().getDownloadInfo(videoId, stream, timestamp, sig, userId, clientIp,
                param);
    }

    @RequestMapping("/video/album/detail/get")
    public Response<BaseDto> getAlbumDetailAndSeries(@RequestParam(value = "albumid", required = false) Long albumId,
            @RequestParam(value = "videoid", required = false) Long videoId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pagesize", required = false, defaultValue = "100") Integer pageSize,
            @ModelAttribute CommonParam param,
            @RequestParam(value = "isTimerCall", required = false) Integer isTimerCall,
            @RequestParam(value = "act", required = false) Integer act) {

        // if (page == null || (page != null && (page.intValue() == -1))) {
        // page = 1;
        // }
        return this.facadeService.getVideoService().getDetail(albumId, videoId, page, param, isTimerCall, act);
    }

    @RequestMapping("/video/albumInfo/get")
    public Response<BaseDto> getAlbumInfo(@RequestParam(value = "albumid", required = false) Long albumId,
            @RequestParam(value = "videoid", required = false) Long videoId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pagesize", required = false, defaultValue = "100") Integer pageSize,
            @ModelAttribute CommonParam param,
            @RequestParam(value = "isTimerCall", required = false) Integer isTimerCall,
            @RequestParam(value = "act", required = false) Integer act) {

        return new Response<BaseDto>(this.facadeService.getVideoService().getAlbumInfo(albumId, videoId, page,
                pageSize, param, isTimerCall, act));
    }

    /**
     * 播放后联播
     * @param albumId
     * @param videoId
     * @param param
     * @return
     */
    @RequestMapping("/video/play/list")
    public Response<VideoPlayListDto> getPlayList(@RequestParam(value = "albumid", required = false) Long albumId,
            @RequestParam(value = "videoid") Long videoId, @ModelAttribute CommonParam param) {
        return new Response<VideoPlayListDto>(facadeService.getVideoService().getPlayList(albumId, videoId, param));
    }

    @RequestMapping("/video/play/interact")
    public Response<InteractDto> playInteract(@RequestParam(value = "videoid") Long videoId,
            @ModelAttribute CommonParam param) {

        return new Response<InteractDto>(facadeService.getVideoService().playInteract(videoId));
    }

    /**
     * 正片推荐
     * @param albumId
     * @param videoId
     * @param param
     * @return
     */
    @RequestMapping("/video/positive/recommend")
    public Response<PositiveRecommendDto> positiveRecommend(@RequestParam(value = "videoid") Long videoId,
            @ModelAttribute CommonParam param) {
        return new Response<PositiveRecommendDto>(facadeService.getVideoService().getPositiveRecommend(videoId, param));
    }

    /**
     * 半屏播放页 点赞、点踩接口 
     * @param videoId 视频ID
     * @param albulmId 专辑ID
     * @param act true 点赞 false 点踩
     * @param param
     * @return
     */
    @RequestMapping("/video/like/commit")
    public Response<LikeDto> likeCommit(@RequestParam(value = "videoId") Long videoId,
    		@RequestParam(value = "albulmId") Long albulmId,
    		@RequestParam(value = "act") Boolean act,
    		@ModelAttribute CommonParam param) {
    	return facadeService.getVideoService().likeCommit(videoId, albulmId, act, param);
    }
}
