package xserver.api.module.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.user.dto.CheckUserBindMobileInfoDto;
import xserver.api.module.user.dto.ProfileItemDto;
import xserver.api.module.user.dto.UserFavSimpleDto;
import xserver.api.module.user.dto.UserInfoDto;
import xserver.api.module.user.dto.UserLiveFavDto;
import xserver.api.module.video.dto.UserAttentionStatusDto;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.lib.tp.user.request.UserFavRequest;
import xserver.lib.tp.user.request.UserFavRequest.UserFavOperaion;

/**
 * 用户相关控制器
 * 会员相关不属于此控制器
 */
@Controller
public class UserController extends BaseController {
    /**
     * 用户token 正确性校验
     * @param request
     * @param token
     *            用户中心token 值
     * @return 返回是否正常
     */
    @RequestMapping(value = "/user/tokenLogin")
    public Response<UserInfoDto> tokenLogin(HttpServletRequest request, @RequestParam(value = "token") String token,
            @ModelAttribute CommonParam commonParam) {
        this.log.info("request:data:" + token);
        Response<UserInfoDto> response = this.facadeService.getUserService().tokenLogin(token, commonParam);
        return response;
    }

    /**
     * 获取“我的”页面菜列表（纯静态数据）
     * 静态化接口http://static.m.letv.com/api/myProfile.json?
     * @return
     */
    @RequestMapping(value = "/user/profile/get")
    public PageResponse<ProfileItemDto> getProfileList() {
        return this.facadeService.getUserService().getProfileList();
    }

    /**
     * 添加收藏
     * @param channelid
     *            直播或者轮播的id
     * @param vid
     *            点播数据的视频id
     * @param pid
     *            点播数据的专辑id
     * @param token
     *            用户登录的token
     * @param favType
     *            收藏类型 1点播 2直播 3轮播 4卫视
     * @return
     */
    @RequestMapping("/user/fav/add")
    public Response<UserFavSimpleDto> addFav(@RequestParam(value = "channelid", required = false) String channelid,
            @RequestParam(value = "videoid", required = false) Long vid,
            @RequestParam(value = "pid", required = false) Long pid, @RequestParam(value = "token") String token,
            @RequestParam(value = "favtype") String favType) {
        UserFavRequest request = new UserFavRequest(UserFavOperaion.ADD);
        request.setChannel_id(channelid);
        request.setVideo_id(vid);
        request.setPlay_id(pid);
        request.setFavorite_type(favType);
        request.setSso_tk(token);

        return this.facadeService.getUserService().addFav(request);
    }

    /**
     * 校验收藏
     * @param channelid
     *            直播或者轮播的id
     * @param vid
     *            点播数据的视频id
     * @param pid
     *            点播数据的专辑id
     * @param token
     *            用户登录的token
     * @param favType
     *            收藏类型 1点播 2直播 3轮播 4卫视
     * @return
     */
    @RequestMapping("/user/fav/check")
    public Response<UserFavSimpleDto> checkFav(@RequestParam(value = "channelid", required = false) String channelid,
            @RequestParam(value = "videoid", required = false) Long vid,
            @RequestParam(value = "pid", required = false) Long pid, @RequestParam(value = "token") String token,
            @RequestParam(value = "favtype") String favType) {
        UserFavRequest request = new UserFavRequest(UserFavOperaion.CHECK);
        request.setChannel_id(channelid);
        request.setVideo_id(vid);
        request.setPlay_id(pid);
        request.setFavorite_type(favType);
        request.setSso_tk(token);

        return this.facadeService.getUserService().checkFav(request);
    }

    /**
     * 添加收藏
     * @param channelid
     *            直播或者轮播的id
     * @param vid
     *            点播数据的视频id
     * @param pid
     *            点播数据的专辑id
     * @param token
     *            用户登录的token
     * @param favType
     *            收藏类型 1点播 2直播 3轮播 4卫视
     * @return
     */
    @RequestMapping("/user/fav/del")
    public Response<UserFavSimpleDto> delFav(@RequestParam(value = "channelid", required = false) String channelid,
            @RequestParam(value = "videoid", required = false) Long vid,
            @RequestParam(value = "pid", required = false) Long pid, @RequestParam(value = "token") String token,
            @RequestParam(value = "favtype") String favType) {
        UserFavRequest request = new UserFavRequest(UserFavOperaion.DELETE);
        request.setChannel_id(channelid);
        request.setVideo_id(vid);
        request.setPlay_id(pid);
        request.setFavorite_type(favType);
        request.setSso_tk(token);

        return this.facadeService.getUserService().delFav(request);
    }

    /**
     * 批量取消收藏
     * @param token
     * @param favids
     *            多个收藏id以英文逗号隔开
     * @return
     */
    @RequestMapping("/user/fav/mutildel")
    public Response<UserFavSimpleDto> delMutilFav(@RequestParam(value = "token") String token,
            @RequestParam("favids") String favids) {
        UserFavRequest request = new UserFavRequest(UserFavOperaion.MUTILDELETE);
        request.setSso_tk(token);
        request.setFavorite_id(favids);
        return this.facadeService.getUserService().delFav(request);
    }

    /**
     * 获取直播收藏
     * @param token
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/user/fav/listlive")
    public PageResponse<UserLiveFavDto> listLiveFav(@RequestParam(value = "token") String token,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "9") Integer pageSize) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize > 100) {
            pageSize = (pageSize < 1) ? 1 : 100;
        }

        UserFavRequest request = new UserFavRequest(UserFavOperaion.LIST);
        request.setPage(page);
        request.setPagesize(pageSize);
        request.setSso_tk(token);
        request.setFavorite_type(UserFavRequest.FAVORITE_TYPE_LUNBO + "," + UserFavRequest.FAVORITE_TYPE_WEISHI);
        return this.facadeService.getUserService().getLiveFavList(request);
    }

    /**
     * 关注/取消关注/查询用户对乐词的关注状态
     * @param id
     * @param operate
     * @param token
     * @param param
     * @return
     */
    @RequestMapping("/user/hotwords")
    public Response<UserAttentionStatusDto> getUserHotWords(@RequestParam(value = "id") Integer id,
            @RequestParam(value = "operate") Integer operate, @RequestParam(value = "token") String token,
            @ModelAttribute CommonParam param) {
        return new Response<UserAttentionStatusDto>(facadeService.getUserService().getUserHotWords(id, operate, token,
                param));
    }
    /**
     * 用户token校验是否绑定手机
     * @param request
     * @param token
     *            用户中心token 值
     * @return 绑定信息
     */
    @RequestMapping(value = "/user/checkmobile")
    public Response<CheckUserBindMobileInfoDto> checkmobile(HttpServletRequest request, @RequestParam(value = "token") String token,
            @ModelAttribute CommonParam commonParam) {
        this.log.info("request:data:" + token);
        Response<CheckUserBindMobileInfoDto> response = this.facadeService.getUserService().checkmobile(token, commonParam);
        return response;
    }

}
