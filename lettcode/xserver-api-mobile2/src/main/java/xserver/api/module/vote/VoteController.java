package xserver.api.module.vote;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.vote.dto.VoteResultDto;
import xserver.api.response.Response;

/**
 * 点赞相关接口控制器
 */
@Controller
public class VoteController extends BaseController {
    /**
     * 获取一批指定id的点赞结果
     * @param request
     * @param ids
     *            id之间用逗号间隔
     * @return
     */
    @RequestMapping(value = "/vote/result")
    public Response<VoteResultDto> getVoteResultByIds(HttpServletRequest request,
            @RequestParam(value = "ids", required = false) String ids, @ModelAttribute CommonParam commonParam) {
        log.info("request:data:" + ids);
        Response<VoteResultDto> response = facadeService.getVoteService().getVoteResultByIds(ids, commonParam);
        return response;
    }

    /**
     * 提交对一个资源的点赞
     * 资源可能是一个视频，一个专辑等
     * @param request
     * @param id
     *            资源对应的点赞id
     * @return
     */
    @RequestMapping(value = "/vote/commit")
    public Response<VoteResultDto> commitVote(HttpServletRequest request, @RequestParam(value = "ids") String ids,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "sign", required = false) String sign, @ModelAttribute CommonParam commonParam) {
        log.info("request:data:" + ids + ",token:" + token);
        Response<VoteResultDto> response = facadeService.getVoteService().commitVote(ids, token, sign, commonParam);
        return response;
    }
}
