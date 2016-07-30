package xserver.api.module.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.constant.MutilLanguageConstants;
import xserver.api.dto.ValueDto;
import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.user.dto.PlayFavoriteInfoDto;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.lib.tp.user.request.AddPlayFavoriteRequest;
import xserver.lib.tp.user.request.CheckPlayFavoriteRequest;
import xserver.lib.tp.user.request.DeletePlayFavoriteRequest;
import xserver.lib.tp.user.request.GetPlayFavoriteRequest;

/**
 * 用户相关控制器
 * 会员相关不属于此控制器
 */
@Controller
public class PlayFavoriteController extends BaseController {

    /**
     * 获取用户收藏的专辑或视频
     * @param token
     *            用于token，查询云端收藏必填参数
     * @param page
     *            查询第几页数据
     * @param pagesize
     *            每页数据量
     * @param param
     *            通用参数，这里主要取uid（uid用于日志，并非必传参数）
     * @return
     */
    @RequestMapping(value = "/user/playfavorite/get")
    public PageResponse<PlayFavoriteInfoDto> getPlayFavorite(@RequestParam(value = "token") String token,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @ModelAttribute CommonParam param) {

        GetPlayFavoriteRequest getPlayFavoriteRequest = new GetPlayFavoriteRequest(param.getUid(), token, page,
                pageSize);

        return this.facadeService.getPlayFavoriteService().getPlayFavorite(getPlayFavoriteRequest,
                MutilLanguageConstants.getLocale(param));
    }

    /**
     * 添加收藏
     * @param token
     *            用于token，添加云端收藏必填参数
     * @param albumId
     *            专辑id，电影、电视剧、动漫频道视频ID可以为空
     * @param videoId
     *            视频id
     * @param channelId
     *            专辑或视频分类id，与媒资定义保持一致
     * @param param
     *            通用参数，这里主要取uid（uid用于日志，并非必传参数）
     * @return
     */
    @RequestMapping(value = "/user/playfavorite/add")
    public Response<ValueDto<Boolean>> addPlayFavorite(@RequestParam(value = "token") String token,
            @RequestParam(value = "albumId", required = false) String albumId,
            @RequestParam(value = "videoId", required = false) String videoId,
            @RequestParam(value = "channelId", required = false) String channelId, @ModelAttribute CommonParam param) {

        AddPlayFavoriteRequest addPlayFavoriteRequest = new AddPlayFavoriteRequest(param.getUid(), token, albumId,
                videoId, channelId);

        return this.facadeService.getPlayFavoriteService().addPlayFavorite(addPlayFavoriteRequest,
                MutilLanguageConstants.getLocale(param));
    }

    /**
     * 取消收藏
     * @param token
     *            用于token，取消云端收藏必填参数
     * @param type
     *            删除操作类型，1--根据战绩id和视频id，删除一条记录；0--根据favoriteIds删除多条记录（兼容删除一条）
     * @param albumId
     *            专辑id；电影、电视剧、动漫频道收藏以专辑ID为单位，取消收藏时这些频道需要专辑ID
     * @param videoId
     *            视频id，单视频无专辑id
     * @param favoriteIds
     *            收藏id列表，多条收藏id使用英文逗号拼接
     * @param param
     *            通用参数，这里主要取uid（uid用于日志，并非必传参数）
     * @return
     */
    @RequestMapping(value = "/user/playfavorite/delete")
    public Response<ValueDto<Boolean>> deletePlayFavorite(@RequestParam(value = "token") String token,
            @RequestParam(value = "type") Integer type,
            @RequestParam(value = "albumId", required = false) String albumId,
            @RequestParam(value = "videoId", required = false) String videoId,
            @RequestParam(value = "favoriteIds", required = false) String favoriteIds, @ModelAttribute CommonParam param) {

        DeletePlayFavoriteRequest deletePlayFavoriteRequest = new DeletePlayFavoriteRequest(param.getUid(), token,
                type, albumId, videoId, favoriteIds);

        return this.facadeService.getPlayFavoriteService().deletePlayFavorite(deletePlayFavoriteRequest,
                MutilLanguageConstants.getLocale(param));
    }

    /**
     * 检查某一频道、专辑或视频是否被收藏过；注意channelId、albumId、videoId三者不能同时为空
     * @param token
     * @param channelId
     * @param albumId
     * @param videoId
     * @param param
     * @return
     */
    @RequestMapping(value = "/user/playfavorite/check")
    public Response<ValueDto<Boolean>> checkPlayFavorite(@RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "channelId", required = false) String channelId,
            @RequestParam(value = "albumId", required = false) String albumId,
            @RequestParam(value = "videoId", required = false) String videoId, @ModelAttribute CommonParam param) {

        CheckPlayFavoriteRequest checkPlayFavoriteRequest = new CheckPlayFavoriteRequest(param.getUid(), token,
                albumId, videoId, channelId);

        return this.facadeService.getPlayFavoriteService().checkPlayFavorite(checkPlayFavoriteRequest,
                MutilLanguageConstants.getLocale(param));
    }

}
