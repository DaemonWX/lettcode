package xserver.api.module.superlive;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.api.module.superlive.dto.SuperLiveLiveCountDto;
import xserver.api.module.superlive.dto.SuperLiveShareDto;
import xserver.api.module.superlive.dto.SuperLiveVoteInfoDto;
import xserver.api.module.superlive.request.CategoryDetailDataRequest;
import xserver.common.dto.superlive.v2.DefualtStreamDto;
import xserver.common.dto.superlive.v2.SuperLiveCategoryDto;
import xserver.common.dto.superlive.v2.SuperLiveChannelDto;
import xserver.common.dto.superlive.v2.SuperLiveIndexPageDto;
import xserver.common.request.CommonParam;
import xserver.common.response.PageResponse;
import xserver.common.response.Response;

/**
 * 超级Live二期改版接口
 */
@Controller
public class SuperLiveV2Controller extends BaseController {

    /**
     * 超级Live 大首页数据接口
     * @param param
     */
    @RequestMapping(value = "/superlive/homepage")
    public Response<SuperLiveIndexPageDto> getSuperLiveHomePage(@ModelAttribute CommonParam param) {
        Response<SuperLiveIndexPageDto> response = facadeService.getSuperLiveV2Service().getSuperLiveHomePage(param);
        return response;
    }

    /**
     * 根据id获取首页分类数据，多个id之间使用,隔开
     * @param cids
     * @return
     */
    @RequestMapping(value = "/superlive/homepage/refresh")
    public Response<SuperLiveIndexPageDto> getHomePageDataByCateogryId(@RequestParam("cids") String cids) {
        return this.facadeService.getSuperLiveV2Service().getHomePageDataByCateogryId(cids);
    }

    /**
     * 超级Live 大分类主页数据列表
     * @param param
     */
    @RequestMapping(value = "/superlive/category/homepage")
    public PageResponse<SuperLiveCategoryDto> getSuperLiveCategoryHomePage(
            @RequestParam("categoryId") String categoryId, @ModelAttribute CommonParam param) {
        PageResponse<SuperLiveCategoryDto> response = facadeService.getSuperLiveV2Service()
                .getSuperLiveCategoryHomePage(categoryId, param);
        return response;
    }

    /**
     * 二级分类数据列表
     * @param param
     */
    @RequestMapping(value = "/superlive/category/data/list")
    public Response<List<SuperLiveChannelDto>> getCategoryDataList(@ModelAttribute CommonParam param,
            @RequestParam(value = "cid") String cid, @RequestParam(value = "subCid") String subCid,
            @RequestParam(value = "direction", required = false) String direction,
            @RequestParam(value = "value", required = false) String value,
            @RequestParam(value = "size", required = false, defaultValue = "18") Integer size) {

        CategoryDetailDataRequest request = new CategoryDetailDataRequest();
        request.setCid(cid);
        request.setSubCid(subCid);
        request.setDirection(direction);
        request.setValue(value);
        request.setSize(size != null ? size : 18);

        Response<List<SuperLiveChannelDto>> response = facadeService.getSuperLiveV2Service().getCategoryDataList(param,
                request);
        return response;
    }

    /**
     * 二级分类数据列表
     * 接口路径单词拼写错误，过时作废
     * @param param
     */
    @Deprecated
    @RequestMapping(value = "/superlive/catetory/data/list")
    public Response<List<SuperLiveChannelDto>> getCatetoryDataList(@ModelAttribute CommonParam param,
            @RequestParam(value = "cid") String cid, @RequestParam(value = "subCid") String subCid,
            @RequestParam(value = "direction", required = false) String direction,
            @RequestParam(value = "value", required = false) String value,
            @RequestParam(value = "size", required = false, defaultValue = "18") Integer size) {

        CategoryDetailDataRequest request = new CategoryDetailDataRequest();
        request.setCid(cid);
        request.setSubCid(subCid);
        request.setDirection(direction);
        request.setValue(value);
        request.setSize(size != null ? size : 18);

        Response<List<SuperLiveChannelDto>> response = facadeService.getSuperLiveV2Service().getCategoryDataList(param,
                request);
        return response;
    }

    // @RequestMapping(value = "/superlive/channel/order")
    // public Response<ValueDto<Boolean>> setUserChannel(@ModelAttribute
    // CommonParam param,
    // @RequestParam(value = "uid") String uid, @RequestParam(value =
    // "categoryId") String categoryId,
    // @RequestParam(value = "channelId") String channelId) {
    // boolean result = Boolean.FALSE;
    // try {
    // categoryId = "00000";// 一期不分类
    // result = this.facadeService.getSuperLiveV2Service().addUserChannel(uid,
    // categoryId, channelId);
    // } catch (Exception e) {
    // log.error("addUserChannel error !", e);
    // }
    // Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
    // response.setData(new ValueDto<Boolean>(result));
    // return response;
    // }

    @RequestMapping(value = "/superlive/user/channel/list")
    public Response<List<SuperLiveChannelDto>> listUserChannel(@ModelAttribute CommonParam param) {
        Response<List<SuperLiveChannelDto>> response = this.facadeService.getSuperLiveV2Service()
                .listUserChannel(param);
        return response;
    }

    /**
     * 超级Live默认码流设置
     */
    @RequestMapping("/superlive/user/streams/get")
    public Response<DefualtStreamDto> getDefaultStreams(@ModelAttribute CommonParam param) {
        Response<DefualtStreamDto> response = facadeService.getSuperLiveV2Service().getDefaultStreams(param);
        return response;
    }

    @RequestMapping(value = "/superlive/detail/gets")
    public Response<List<SuperLiveChannelDto>> getDetail(@ModelAttribute CommonParam param,
            @RequestParam(value = "ids") String ids) {
        Response<List<SuperLiveChannelDto>> detail = this.facadeService.getSuperLiveV2Service().getDetails(ids);

        return detail;
    }

    @RequestMapping("/superlive/star/phone")
    public Response<Boolean> refreshStarPhone(@RequestParam(value = "un", required = false) String username,
            @RequestParam(value = "pd", required = false) String password) {
        boolean bool = false;
        if ("mobile".equals(username) && "iptv".equals(password)) {
            bool = this.facadeService.getSuperLiveV2Service().refreshStarPhone();
        }
        Response<Boolean> response = new Response<Boolean>();
        response.setData(bool);
        return response;
    }

    @RequestMapping("/superlive/share/url")
    public Response<SuperLiveShareDto> getShareUrl(@RequestParam("channelid") String channelid,
            @RequestParam("type") String type) {
        return this.facadeService.getSuperLiveV2Service().getShareUrl(channelid, type);
    }

    @RequestMapping("/superlive/vote/info")
    public Response<SuperLiveVoteInfoDto> getLiveVoteInfo(@RequestParam("channelId") String liveid) {
        return this.facadeService.getSuperLiveV2Service().getLiveVoteInfo(liveid);
    }

    /**
     * 获取用户直播收藏，以直播大类进行分类
     * @param token
     * @return
     */
    @RequestMapping("/superlive/fav/category")
    public Response<Map<String, List<SuperLiveChannelDto>>> getLiveFavByCategory(
            @RequestParam(value = "token") String token) {
        return this.facadeService.getSuperLiveV2Service().getLiveFavByCategory(token);
    }
    /**
     * 获取当日直播数及直播顺序
     * @param param
     * @return
     */
    @RequestMapping("/superlive/catetory/livecount")
    public Response<SuperLiveLiveCountDto> getLiveCount(@ModelAttribute CommonParam param) {
        return this.facadeService.getSuperLiveV2Service().getLiveCountInfo();
    }

}
