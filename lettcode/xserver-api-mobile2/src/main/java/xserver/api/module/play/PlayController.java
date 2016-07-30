package xserver.api.module.play;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.play.dto.Live;
import xserver.api.module.play.dto.SuperLiveWaterMarkDto;
import xserver.api.module.play.dto.VODDto;
import xserver.api.response.Response;
import xserver.lib.util.RequestUtil;

@Controller
public class PlayController extends BaseController {

    /**
     * @param request
     * @param videoid
     *            视频id
     * @param albumid
     *            专辑id
     * @param stream
     *            请求播放的码流
     * @param timestamp
     *            服务器时间
     * @param sig
     * @param userid
     *            用户id
     * @param appid
     *            由通用播放器下发的appid，使用通用播放器首先需申请appid
     * @param param
     * @return
     */
    @RequestMapping("/play/vod")
    public Response<VODDto> getPlayInfo(HttpServletRequest request,
            @RequestParam(value = "videoId", required = false) Long videoId,
            @RequestParam(value = "albumId", required = false) Long albumId,
            @RequestParam(value = "stream", required = false) String stream,
            @RequestParam(value = "timestamp") Long timestamp, @RequestParam(value = "sig") String sig,
            @RequestParam(value = "businessId") String businessId, @RequestParam(value = "rand") Integer rand,
            @RequestParam(value = "token", required = false) String token, @ModelAttribute CommonParam param) {

        String clientIp = RequestUtil.getClientIp(request);
        return new Response<VODDto>(this.facadeService.getVodService().getVideoPlayInfo(videoId, albumId, stream,
                timestamp, sig, businessId, rand, clientIp, param));
    }

    @RequestMapping("/play/live")
    public Response<Live> getLiveInfo(@RequestParam(value = "liveId", required = false) String liveId,
            @RequestParam(value = "stream", required = false) String stream,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "timestamp", required = false) Long timestamp,
            @RequestParam(value = "businessId", required = false) String businessId,
            @RequestParam(value = "rand", required = false) Integer rand,
            @RequestParam(value = "token", required = false) String token, @ModelAttribute CommonParam param) {

        return new Response<Live>(this.facadeService.getVodService().getLivePlayInfo(liveId, stream, param.getUid(),
                param.getDevId(), businessId, param));
    }

    @RequestMapping("/play/watermark")
    public Response<SuperLiveWaterMarkDto> getLiveWaterMark(
            @RequestParam(value = "channelId", required = false) String channelId,
            @RequestParam(value = "type", required = false) String type) {
        return this.facadeService.getVodService().getLiveWaterMark(channelId, type);
    }

}
