package xserver.api.module.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.constant.MutilLanguageConstants;
import xserver.api.dto.ValueDto;
import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.user.dto.PlayRecordInfoDto;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.lib.tp.user.UserTpConstant;
import xserver.lib.tp.user.request.DeletePlayRecordRequest;
import xserver.lib.tp.user.request.GetPlayRecordRequest;
import xserver.lib.tp.user.request.UpdatePlayRecordRequest;

/**
 * 用户相关控制器
 * 会员相关不属于此控制器
 */
@Controller
public class PlayLogController extends BaseController {

    /**
     * 获取用户的播放记录
     * @param token
     *            用于token，查询云端播放记录必填参数
     * @param page
     *            查询第几页数据
     * @param type
     *            查询类型，0--全部，1--点播，2--直播，3--轮播，4--卫视，默认1
     * @param pagesize
     *            每页数据量
     * @param timeInterval
     *            时间间隔，查询多少天之内的播放记录
     * @param param
     *            通用参数，这里主要取uid（uid用于日志，并非必传参数）
     * @return
     */
    @RequestMapping(value = "/user/playrecord/get")
    public PageResponse<PlayRecordInfoDto> getPlayRecord(@RequestParam(value = "token") String token,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "type", required = false, defaultValue = "1") String type,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(value = "timeInterval", required = false, defaultValue = "30") Integer timeInterval,
            @ModelAttribute CommonParam param) {

        if (type == null) {
            type = UserTpConstant.USERCETER_PLAY_RECORD_QUERY_TYPE_1;
        }

        if (pageSize != null && pageSize.intValue() > 100) {
            pageSize = 100;// 最多返回100条
        }
        GetPlayRecordRequest getPlayRecordRequest = new GetPlayRecordRequest(param.getUid(), token, page, pageSize,
                timeInterval, type);
        return this.facadeService.getPlayLogService().getPlayRecord(getPlayRecordRequest,
                MutilLanguageConstants.getLocale(param));
    }

    /**
     * 提交（新增或更新）播放记录
     * @param token
     *            用于token，更新云端播放记录必填参数
     * @param albumId
     *            专辑id；（单视频无专辑id）
     * @param videoId
     *            视频id
     * @param oid
     *            直播、轮播、卫视等id
     * @param htime
     *            观看时长，单位：毫秒，0--刚开始观看，-1--观看完毕
     * @param type
     *            视频类型，1--点播，2--直播，3--轮播，4--卫视，默认1
     * @param channelId
     *            专辑或视频分类id，与媒资定义保持一致
     * @param from
     *            播放记录来源，1--web，2--mobile，3--pad，4--tv，5--PC桌面，默认1
     * @param nextVideoId
     *            下一集视频id
     * @param product
     *            如果是超级设备（如超级电视，超级手机，乐视盒子等），播放记录上传来源需传超级设备硬件型号，如“X60”
     * @param longitude
     *            用户位置的经度（适用于移动端网监上报，非必填）
     * @param latitude
     *            用户位置的纬度（适用于移动端网监上报，非必填）
     * @param param
     *            通用参数，这里主要取uid（uid用于日志，并非必传参数）
     * @return
     */
    @RequestMapping(value = "/user/playrecord/update")
    public Response<ValueDto<Boolean>> updatePlayRecord(@RequestParam(value = "token") String token,
            @RequestParam(value = "albumId", required = false) String albumId,
            @RequestParam(value = "videoId", required = false) String videoId,
            @RequestParam(value = "oid", required = false) String oid,
            @RequestParam(value = "htime", defaultValue = "0") Long htime,
            @RequestParam(value = "type", required = false, defaultValue = "1") String type,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "nextVideoId", required = false) String nextVideoId,
            @RequestParam(value = "product", required = false) String product,
            @RequestParam(value = "longitude", required = false) String longitude,
            @RequestParam(value = "latitude", required = false) String latitude, @ModelAttribute CommonParam param) {

        if (type == null) {
            type = UserTpConstant.USERCETER_PLAY_RECORD_QUERY_TYPE_1;
        }
        // 超级手机默认为2
        if (from == null) {
            from = UserTpConstant.USERCETER_PLAY_RECORD_FROM_2;
        }
        UpdatePlayRecordRequest updatePlayRecordRequest = new UpdatePlayRecordRequest(param.getUid(), token, albumId,
                videoId, oid, htime, categoryId, nextVideoId, product, longitude, latitude, type, from);
        return this.facadeService.getPlayLogService().updatePlayRecord(updatePlayRecordRequest,
                MutilLanguageConstants.getLocale(param));
    }

    /**
     * 删除（一条或全部）播放记录；重复删除不会报错，而是返回成功；
     * 经测试发现，flush=1时，忽略pid，vid和idstr；flush不为1时，优先考虑idstr，忽略pid和vid；最后才考虑单条删除
     * @param token
     *            用于token，删除云端播放记录必填参数
     * @param flush
     *            删除操作类型，0--非全部情况，一般只删除单条，此时videoId必传；1--全部清空，albumId、
     *            videoId将被忽略；3--批量删除
     * @param albumId
     *            专辑id；单视频无专辑id
     * @param videoId
     *            视频id
     * @param idstr
     *            id字符串，批量删除使用。格式如（vid:1870565,pid:76105,vid:706542,pid:76106）
     * @param param
     *            通用参数，这里主要取uid（uid用于日志，并非必传参数）
     * @return
     */
    @RequestMapping(value = "/user/playrecord/delete")
    public Response<ValueDto<Boolean>> deletePlayRecord(@RequestParam(value = "token") String token,
            @RequestParam(value = "flush") Integer flush,
            @RequestParam(value = "albumId", required = false) Long albumId,
            @RequestParam(value = "videoId", required = false) Long videoId,
            @RequestParam(value = "idstr", required = false) String idstr, @ModelAttribute CommonParam param) {

        DeletePlayRecordRequest deletePlayRecordRequest = new DeletePlayRecordRequest(param.getUid(), token, flush,
                albumId, videoId, idstr);
        return this.facadeService.getPlayLogService().deletePlayRecord(deletePlayRecordRequest,
                MutilLanguageConstants.getLocale(param));
    }

}
