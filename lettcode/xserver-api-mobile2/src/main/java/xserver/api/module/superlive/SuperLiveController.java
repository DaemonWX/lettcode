package xserver.api.module.superlive;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.dto.ValueDto;
import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.superlive.dto.CategoryChannelIdsDto;
import xserver.api.module.superlive.dto.ChannelDto;
import xserver.api.module.superlive.dto.LiveChannelCurrentStateDataDto;
import xserver.api.module.superlive.dto.SuperLiveCategoryDto;
import xserver.api.module.superlive.dto.SuperLiveIndexDto;
import xserver.api.response.Response;

@Controller("superLiveController")
public class SuperLiveController extends BaseController {
    /**
     * 获取超级Live首页数据
     * @param param
     *            通用参数
     * @param uid
     *            用户id，登录
     * @return
     */
    @RequestMapping(value = "/superlive/categorys/channel/list")
    public Response<SuperLiveIndexDto> getSuperLiveIndex(@ModelAttribute CommonParam param,
            @RequestParam(value = "uid", required = false) String uid) {
        return this.facadeService.getSuperLiveService().getSuperLiveIndex(param, uid);
    }

    /**
     * 获取超级Live首页数据
     * @param param
     *            通用参数
     * @param uid
     *            用户id，登录
     * @return
     */
    @RequestMapping(value = "/superlive/category/hot/list")
    public Response<SuperLiveIndexDto> getHotCategoryList(@ModelAttribute CommonParam param,
            @RequestParam(value = "uid", required = false) String uid) {
        return this.facadeService.getSuperLiveService().getHotCategoryList(param, uid);
    }

    /**
     * 获取超级Live首页数据,接口单词写错，修正废弃
     * @param param
     *            通用参数
     * @param uid
     *            用户id，登录
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/superlive/categroys/channel/list")
    public Response<SuperLiveIndexDto> getSuperLiveIndex1(@ModelAttribute CommonParam param,
            @RequestParam(value = "uid", required = false) String uid) {
        return this.facadeService.getSuperLiveService().getSuperLiveIndex(param, uid);
    }

    /**
     * 获取所有分类数据
     * @return
     */
    @RequestMapping(value = "/superlive/category/list")
    public Response<List<SuperLiveCategoryDto>> listCategorys(@ModelAttribute CommonParam param) {
        return this.facadeService.getSuperLiveService().listCategorys(param);
    }

    /**
     * 用户定制频道
     * @param param
     *            通用参数
     * @param uid
     *            用户id
     * @param cid
     *            类型id
     * @param channelIds
     *            定制的频道ids
     * @return
     */
    @RequestMapping(value = "/superlive/channel/user/set")
    public Response<ValueDto<Boolean>> setUserChannel(@ModelAttribute CommonParam param,
            @RequestParam(value = "uid") String uid, @RequestParam(value = "categoryId") String categoryId,
            @RequestParam(value = "channelIds") String channelIds) {
        try {
            this.facadeService.getSuperLiveService().setUserChannel(uid, categoryId, channelIds);
        } catch (Exception e) {

        }

        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        response.setData(new ValueDto<Boolean>(Boolean.TRUE));
        return response;
    }

    /**
     * 用户分类定制
     * @param param
     *            通用参数
     * @param uid
     *            用户id
     * @param cids
     *            分类ids,按排序走
     * @return
     */
    @RequestMapping(value = "/superlive/category/user/set")
    public Response<ValueDto<Boolean>> setUserCategory(@ModelAttribute CommonParam param,
            @RequestParam(value = "uid") String uid, @RequestParam(value = "categoryIds") String categoryIds) {
        try {
            this.facadeService.getSuperLiveService().setUserCategory(uid, categoryIds);
        } catch (Exception e) {

        }
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        response.setData(new ValueDto<Boolean>(Boolean.TRUE));
        return response;
    }

    /**
     * 获取更新某分类下频道
     * @param param
     *            通用参数
     * @param uid
     *            用户id
     * @param cid
     *            分类id
     * @param pageIndex
     *            开始位置，从0开始
     * @param pageSize
     *            取个数
     * @return
     */
    @RequestMapping(value = "/superlive/channel/list")
    public Response<SuperLiveIndexDto> listChannelByCid(@ModelAttribute CommonParam param,
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "categoryId") String categoryId,
            @RequestParam(value = "pageIndex") Integer pageIndex, @RequestParam(value = "pageSize") Integer pageSize) {
        Response<SuperLiveIndexDto> channelListInfo = null;

        try {
            channelListInfo = this.facadeService.getSuperLiveService().listChannelByCid(uid, categoryId, pageIndex,
                    pageSize, param);
        } catch (Exception e) {
            log.error("error", e);
        }

        return channelListInfo;
    }

    // public PageResponse<LiveChannelCurrentStateDataDto>
    // listChannelByCid(@ModelAttribute CommonParam param,
    // @RequestParam(value = "uid", required = false) String uid,
    // @RequestParam(value = "categoryId") String categoryId,
    // @RequestParam(value = "pageIndex") Integer pageIndex, @RequestParam(value
    // = "pageSize") Integer pageSize) {
    // PageResponse<LiveChannelCurrentStateDataDto> channelList = null;
    //
    // try {
    // if (pageIndex >= 1) {
    // pageIndex = pageIndex - 1;
    // }
    // channelList =
    // this.facadeService.getSuperLiveService().listChannelByCid(uid,
    // categoryId, pageIndex,
    // pageSize);
    // } catch (Exception e) {
    //
    // }
    //
    // return channelList;
    // }

    /**
     * 直播节目详情或卫视或轮播频道详情
     * @param param
     *            通用参数
     * @param uid
     *            用户id
     * @param id
     *            节目或频道id
     * @param liveType
     *            直播类型：直播：2 轮播：3 卫视：4
     * @return
     */
    @RequestMapping(value = "/superlive/detail/get")
    public Response<LiveChannelCurrentStateDataDto> getDetail(@ModelAttribute CommonParam param,
            @RequestParam(value = "type") String type, @RequestParam(value = "id") String id) {
        Response<LiveChannelCurrentStateDataDto> detail = this.facadeService.getSuperLiveService().getDetail(type, id);

        return detail;
    }

    /**
     * 直播节目详情或卫视或轮播频道详情
     * @param param
     *            通用参数
     * @param uid
     *            用户id，可以不传
     * @param cid
     *            类型：0全部、1体育、2音乐、3娱乐
     * @param pageIndex
     *            分页的起始位置
     * @param pageSize
     *            分页的长度
     * @return
     */
    @RequestMapping(value = "/superlive/live/list")
    public Response<SuperLiveIndexDto> getLiveList(@ModelAttribute CommonParam param,
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "pageIndex") Integer pageIndex, @RequestParam(value = "cid") String cid,
            @RequestParam(value = "pageSize") Integer pageSize) {
        return this.facadeService.getSuperLiveService().getLiveList(uid, pageIndex, cid, pageSize);
    }

    @RequestMapping(value = "/superlive/category/getAllChIds")
    public Response<List<CategoryChannelIdsDto>> getAllChIds(@ModelAttribute CommonParam param,
            @RequestParam(value = "uid", required = false) String uid) {
        return this.facadeService.getSuperLiveService().getAllChIds(uid, param);
    }

    @RequestMapping(value = "/superlive/channel/programs")
    public Response<ChannelDto> getChannelPrograms(@ModelAttribute CommonParam param,
            @RequestParam(value = "id", required = false) String channelId,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "type", required = false, defaultValue = "3") String type) {
        return this.facadeService.getSuperLiveService().getChannelPrograms(channelId, date, param);
    }

    @RequestMapping(value = "/superlive/programs/inc")
    public Response<ChannelDto> getChannelPrograms(@ModelAttribute CommonParam param,
            @RequestParam(value = "id") String programId, @RequestParam(value = "direction") String direction) {
        return this.facadeService.getSuperLiveService().getChannelProgramsWithInc(programId, direction, param);
    }

    @RequestMapping(value = "/superlive/programs/get")
    public Response<List<ChannelDto>> getProgramsByChannelIds(@ModelAttribute CommonParam param,
            @RequestParam(value = "channelIds") String channelIds) {
        return this.facadeService.getSuperLiveService().getChannelProgramsWithChannelIds(channelIds, param);
    }

    @RequestMapping("/superlive/imei")
    public Response<Object> updateStar(
            @RequestParam(value = "oper", required = false, defaultValue = "list") String oper,
            @RequestParam(value = "devId", required = false) String devid,
            @RequestParam(value = "star", required = false) String star) {
        Response<Object> response = new Response<Object>();
        if ("list".equals(oper)) {
            response.setData(SuperLiveConstant.CUSTOMIZE_STARS);
        } else if ("update".equals(oper)) {
            if (StringUtils.isBlank(devid) || StringUtils.isBlank(star)) {
                response.setData("devid和star不能为空");
            } else {
                SuperLiveConstant.CUSTOMIZE_STARS.put(devid, star);
                response.setData(devid + "   " + star + " 关系映射成功");
            }
        } else if ("del".equals(oper)) {
            if (StringUtils.isNotBlank(devid)) {
                SuperLiveConstant.CUSTOMIZE_STARS.remove(devid);
                response.setData("删除 " + devid + " 成功");
            } else {
                response.setData("devid不能为空");
            }
        } else {
            response.setData("无效的操作!");
        }
        return response;
    }
}
