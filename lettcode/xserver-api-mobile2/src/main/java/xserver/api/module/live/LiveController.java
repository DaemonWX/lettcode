package xserver.api.module.live;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.live.dto.HotDto;
import xserver.api.response.Response;
import xserver.lib.constant.LiveConstants;
import xserver.lib.dto.live.LiveDto;

@Controller("liveController")
public class LiveController extends BaseController {

    @RequestMapping(value = "/lead/hotList")
    public Response<HotDto> hotList(@ModelAttribute CommonParam param,
            @RequestParam(value = "splitId", required = false, defaultValue = LiveConstants.DEFAULT_SPLITID) String splitId) {
        splitId = StringUtils.isEmpty(splitId) ? LiveConstants.DEFAULT_SPLITID : splitId;
        return this.facadeService.getLiveService().hotList(splitId, param.getLangcode());
    }

    /**
     * 获取首页各直播的列表 split id 1036 superlive
     *
     * @return
     */
    @RequestMapping(value = "/lead/liveList")
    public Response<List<LiveDto>> liveList(@ModelAttribute CommonParam param,
            @RequestParam(value = "liveType") String liveType,
            @RequestParam(value = "splitId", required = false, defaultValue = LiveConstants.DEFAULT_SPLITID) String splitId,
            @RequestParam(value = "isFresh", required = false, defaultValue = "0") int isFresh) {

        splitId = StringUtils.isEmpty(splitId) ? LiveConstants.DEFAULT_SPLITID : splitId;
        return new Response<List<LiveDto>>(
                this.facadeService.getLiveService().liveList(liveType, splitId, param.getLangcode(), isFresh));
    }

    /**
     * 获取直播的详情
     *
     * @return
     */
    @RequestMapping(value = "/lead/liveDetail")
    public Response<LiveDto> liveDetail(@ModelAttribute CommonParam param,
            @RequestParam(value = "liveType", required = false) String liveType,
            @RequestParam(value = "liveId") String liveId,
            @RequestParam(value = "splitId", required = false, defaultValue = LiveConstants.DEFAULT_SPLITID) String splitId) {
        splitId = StringUtils.isEmpty(splitId) ? LiveConstants.DEFAULT_SPLITID : splitId;

        return new Response<LiveDto>(
                this.facadeService.getLiveService().liveDetail(liveType, liveId, splitId, param.getLangcode()));
    }

    @Deprecated
    @RequestMapping(value = "/live/hotList")
    public Response<HotDto> hotList1(@ModelAttribute CommonParam param,
            @RequestParam(value = "splitId", required = false, defaultValue = LiveConstants.DEFAULT_SPLITID) String splitId) {
        splitId = StringUtils.isEmpty(splitId) ? LiveConstants.DEFAULT_SPLITID : splitId;

        return this.facadeService.getLiveService().hotList(splitId, param.getLangcode());
    }

    /**
     * 获取首页各直播的列表
     *
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/live/liveList")
    public Response<List<LiveDto>> liveList1(@ModelAttribute CommonParam param,
            @RequestParam(value = "liveType") String liveType,
            @RequestParam(value = "splitId", required = false, defaultValue = LiveConstants.DEFAULT_SPLITID) String splitId,
            @RequestParam(value = "isFresh", required = false, defaultValue = "0") int isFresh) {

        splitId = StringUtils.isEmpty(splitId) ? LiveConstants.DEFAULT_SPLITID : splitId;
        return new Response<List<LiveDto>>(
                this.facadeService.getLiveService().liveList(liveType, splitId, param.getLangcode(), isFresh));
    }

    /**
     * 获取直播的详情
     *
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/live/liveDetail")
    public Response<LiveDto> liveDetail1(@ModelAttribute CommonParam param,
            @RequestParam(value = "liveType") String liveType, @RequestParam(value = "liveId") String liveId,
            @RequestParam(value = "splitId", required = false, defaultValue = LiveConstants.DEFAULT_SPLITID) String splitId) {
        splitId = StringUtils.isEmpty(splitId) ? LiveConstants.DEFAULT_SPLITID : splitId;

        return new Response<LiveDto>(
                this.facadeService.getLiveService().liveDetail(liveType, liveId, splitId, param.getLangcode()));
    }
}
