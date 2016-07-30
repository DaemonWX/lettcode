package xserver.api.module.subscribe;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.dto.ValueDto;
import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.subscribe.dto.SubscribeEventDto;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;

/**
 * 预约业务控制器
 */

@Controller
public class SubscribeController extends BaseController {
    /**
     * 预约关系创建
     * @param param
     * @param token     用户token值
     * @param sourceID  数据源的事件的唯一标识
     * @return
     */
    @RequestMapping(value = "/subscribe/event/add")
    public Response<SubscribeEventDto> add(@ModelAttribute CommonParam param,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "sourceID") String sourceID) {
        Response<SubscribeEventDto> response = facadeService.getSubscribeService().add(token,  sourceID, param);
        return response;
    }
    /**
     * 预约关系删除
     * @param param
     * @param token     用户token值
     * @param sourceID  数据源的事件的唯一标识
     * @return
     */
    @RequestMapping(value = "/subscribe/event/delete")
    public Response<SubscribeEventDto> delete(@ModelAttribute CommonParam param,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "sourceID") String sourceID) {
        //sourceType 自己塞
        Response<SubscribeEventDto> response = facadeService.getSubscribeService().delete(token,  sourceID, param);
        return response;
    }
    /**
     * 预约批量删除
     * @param param
     * @param token     用户token值
     * @param eventIDList  预约sourceID、sourceType Json
     * @return
     */
    @RequestMapping(value = "/subscribe/event/multidelete")
    public Response<ValueDto<Boolean>> multidelete  (@ModelAttribute CommonParam param,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "eventIDList", required = false) String eventIDList) {
        //sourceType 自己塞
        Response<ValueDto<Boolean>> response = facadeService.getSubscribeService().multidelete(token, eventIDList, param);    
        return response;
    }
    /**
     * 查询单个预约关系
     * @param param
     * @param token     用户token值
     * @param sourceID  数据源的事件的唯一标识
     * @return
     */
    @RequestMapping(value = "/subscribe/event/check")
    public Response<ValueDto<Boolean>> check(@ModelAttribute CommonParam param,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "sourceID") String sourceID) {
        //sourceType 自己塞
        Response<ValueDto<Boolean>> response = facadeService.getSubscribeService().check(token,  sourceID,
                param);
        return response;
    }
    /**
     * 获取预约列表
     * @param param
     * @param token     用户token值
     * @param offSet    偏移量,页码
     * @param length    长度[1,50]
     * @return
     */
    @RequestMapping(value = "/subscribe/event/list")
    public PageResponse<SubscribeEventDto> list(@ModelAttribute CommonParam param,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "offSet", required = false, defaultValue = "0") Integer offSet,
            @RequestParam(value = "length", required = false, defaultValue = "100") Integer length) {
        PageResponse<SubscribeEventDto> response = facadeService.getSubscribeService().list(token, offSet, length,
                param);
        return response;
    }
}
