package xserver.api.module.superlive;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.dto.ValueDto;
import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.superlive.dto.SuperLivePlayLogDto;
import xserver.api.response.PageCommonResponse;
import xserver.api.response.Response;
@Controller
public class SuperLivePlayLogController extends BaseController {
    /**
     * 获取用户的播放记录
     * @param token
     *            用于token，查询云端播放记录必填参数
     * @param page
     *            查询第几页数据
     * @param pagesize
     *            每页数据量
     * @param param
     *            通用参数，这里主要取uid（uid用于日志，并非必传参数）
     * @return
     */
    @RequestMapping(value = "/superlive/playrecord/get")
    public PageCommonResponse<SuperLivePlayLogDto> getPlayRecord(@RequestParam(value = "token") String token,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "types", required = false, defaultValue = "2,3") String types,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @ModelAttribute CommonParam param) {
        PageCommonResponse<SuperLivePlayLogDto> response = this.facadeService.getSuperLivePlayLogService().getPlayRecord(token,types,page,pageSize,param);
        return response;
    }

    /**
     * 更新播放记录
     * @param token
     *            用户登录token
     * @param type
     *            直播类型：1直播、2卫视、3轮播
     * @param id
     *            直播id
     * @param param
     *            通用参数
     */
    @RequestMapping(value = "/superlive/playrecord/update")
    public Response<ValueDto<Boolean>> updatePlayRecord(@RequestParam(value = "token") String token,
            @RequestParam(value = "type", required = false) String type, @RequestParam(value = "id") String id,
            @ModelAttribute CommonParam param) {
        Response<ValueDto<Boolean>> response = this.facadeService.getSuperLivePlayLogService().updatePlayRecord(token,type,id,param);
        return response;
    }

    /**
     * 播放记录删除
     * @param token
     *            用户登录token
     * @param type
     *            类型
     * @param id
     *            直播id
     * @param param
     *            通用参数
     */
    @RequestMapping(value = "/superlive/playrecord/delete")
    public Response<ValueDto<Boolean>> deletePlayRecord(@RequestParam(value = "token") String token,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "id", required = false) String id, @ModelAttribute CommonParam param) {
        Response<ValueDto<Boolean>> response = this.facadeService.getSuperLivePlayLogService().deletePlayRecord(token,type,id,param);
        return response;
    }
}
