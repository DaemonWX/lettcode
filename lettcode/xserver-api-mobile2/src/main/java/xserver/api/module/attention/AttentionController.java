package xserver.api.module.attention;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.dto.ValueDto;
import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.attention.dto.AttentionCheckDto;
import xserver.api.response.Response;

/**
 * 关注业务控制器
 */

@Controller
public class AttentionController extends BaseController {
    
    
    @RequestMapping(value = "/attention/add")
    public Response<ValueDto<Boolean>> add(@ModelAttribute CommonParam param,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "tagid") Long tagid,
            @RequestParam(value = "type") Integer type) {
        Response<ValueDto<Boolean>> response = facadeService.getAttentionService().add(token,tagid,type,param);
        return response;
    }
    @RequestMapping(value = "/attention/del")
    public Response<ValueDto<Boolean>> del(@ModelAttribute CommonParam param,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "tagid") Long tagid,
            @RequestParam(value = "type") Integer type) {
        Response<ValueDto<Boolean>> response = facadeService.getAttentionService().del(token,tagid,type,param);
        return response;
    }
    @RequestMapping(value = "/attention/check")
    public Response<List<AttentionCheckDto>> check(@ModelAttribute CommonParam param,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "tagid") String tagid) {
        Response<List<AttentionCheckDto>> response = facadeService.getAttentionService().check(token,tagid,param);
        return response;
    }
    @RequestMapping(value = "/attention/list")
    public Response<ValueDto<Boolean>> list(@ModelAttribute CommonParam param,
            @RequestParam(value = "token") String token) {
        Response<ValueDto<Boolean>> response = facadeService.getAttentionService().list();
        return response;
    }
    
    
    
}
