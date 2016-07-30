package xserver.api.module.channel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.channel.dto.ChannelContentDto;
import xserver.api.module.channel.dto.ChannelPageDto;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.lib.tp.search.request.SearchRequest;
import xserver.lib.tp.staticconf.request.FilterConditionDto;

@Controller("channelController")
public class ChannelController extends BaseController {

    /**
     * 获取频道墙所有频道
     * @return
     */
    @RequestMapping("/channel/all")
    public PageResponse<ChannelPageDto> getChannelPage(@ModelAttribute CommonParam param) {
        return this.facadeService.getChannelService().getChannelPage(param);
    }

    /**
     * @param history
     *            用户最近的10条视频记录 vid1-vid2.....-vid10
     * @param pageid
     *            对应频道的pageid
     * @return
     */
    @RequestMapping("/channel/data")
    public Response<ChannelContentDto> getChannelData(
            @RequestParam(value = "history", required = false) String history,
            @RequestParam(value = "pageid") String pageid,
            @RequestParam(value = "citylevel", required = false) String cityLevel,
            @ModelAttribute CommonParam commonParam) {

        return this.facadeService.getChannelService().getChannelData2(history, commonParam, pageid, cityLevel);
    }

    /**
     * @param history
     *            用户最近的10条视频记录 vid1-vid2.....-vid10
     * @return
     */
    @RequestMapping("/home/homePage")
    public Response<ChannelContentDto> getHomepage(@RequestParam(value = "history", required = false) String history,
            @RequestParam(value = "citylevel", required = false) String cityLevel,
            @ModelAttribute CommonParam commonParam) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // RecBaseRequest request = new RecBaseRequest();
        // String pageid =
        // ChannelConstant.MULTI_AREA_HOMEPAGE_MAP.get(commonParam.getWcode());
        // if (StringUtils.isEmpty(pageid)) {
        // pageid = ChannelConstant.HOME_PAGE_ID;
        // }
        // request.setPageid("page_cms" + pageid);
        // request.setHistory(history);
        // request.setLang(commonParam.getLangcode());
        // request.setRegion(commonParam.getWcode());
        // request.setUid(commonParam.getUid());
        //
        // return this.facadeService.getChannelService().getChannelData(request,
        // ChannelConstant.HOME_PAGE_ID);
        return this.facadeService.getChannelService().getChannelData2(history, commonParam,
                ChannelConstant.HOME_PAGE_ID, cityLevel);
    }

    /**
     * 获取频道筛选条件
     * @param cid
     *            频道id
     * @return
     */
    @RequestMapping("/channel/filter")
    public Response<FilterConditionDto> getFilterCondition(@RequestParam(value = "cid") int cid,
            @ModelAttribute CommonParam commonParam) {
        return this.facadeService.getChannelService().getFilterCondition(cid, commonParam);
    }

    @RequestMapping("/channel/search")
    public Response<ChannelContentDto> getSearchChannel(@RequestParam("filter") String filter,
            @RequestParam(value = "channelid", required = false) Integer channelid,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "defaultStream", required = false) String defaultStream,
            @ModelAttribute CommonParam commonParam) {

        if (page == null || page <= 0) {
            page = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 20;
        }
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setExtraParam(filter);
        searchRequest.setPn(page);
        searchRequest.setPs(pageSize);
        searchRequest.setLang(commonParam.getLangcode());
        searchRequest.setRegion(commonParam.getWcode());
        return this.facadeService.getChannelService().getSearchChannel(searchRequest, channelid, defaultStream);
    }

    /**
     * 获取频道排行数据
     * @param type
     * @return
     */
    @RequestMapping("/channel/top")
    public Response<ChannelContentDto> getTopChannelData(@RequestParam("type") String type,
            @ModelAttribute CommonParam commonParam) {
        return this.facadeService.getChannelService().getTopData(type, commonParam);
    }
}
