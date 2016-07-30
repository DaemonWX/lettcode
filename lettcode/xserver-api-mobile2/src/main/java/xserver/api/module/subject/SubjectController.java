package xserver.api.module.subject;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.subject.dto.CmsSubjectListDto;
import xserver.api.module.subject.dto.SubjectDto;
import xserver.api.module.subject.dto.SubjectInfoDto;
import xserver.api.response.Response;
import xserver.lib.tp.vote.response.QRCode;

/**
 * 专题相关 接口定义控制器
 */
@Controller
public class SubjectController extends BaseController {
    /**
     * 获取热点数据
     * @param request
     * @param zid
     * @return
     */
    @RequestMapping(value = "/subject/hot/get")
    public Response<SubjectInfoDto> getSubjectById(HttpServletRequest request,
            @RequestParam(value = "zid", required = false) String zid, @ModelAttribute CommonParam commonParam) {
        log.info("request:data:" + zid);
        Response<SubjectInfoDto> response = facadeService.getSubjectService().getHotSubjectById(zid, commonParam);

        return response;
    }

    /**
     * 在cms版块中获取专题列表
     * @param request
     * @param bid
     * @return
     */
    @RequestMapping(value = "/subject/cms/list")
    public Response<CmsSubjectListDto> getSubjectListByBlockId(HttpServletRequest request,
            @RequestParam(value = "bid", required = false) String bid, @ModelAttribute CommonParam commonParam) {
        log.info("request:data:" + bid);
        Response<CmsSubjectListDto> response = facadeService.getSubjectService().getSubjectListByBlockId(bid,
                commonParam);

        return response;
    }

    /**
     * 在cms版块中获取专题列表
     * @param request
     * @param bid
     * @return
     */
    @RequestMapping(value = "/subject/content/get")
    public Response<SubjectInfoDto> getSubjectContentByZid(HttpServletRequest request,
            @RequestParam(value = "zid", required = false) String zid, @ModelAttribute CommonParam commonParam) {
        log.info("request:data:" + zid);
        Response<SubjectInfoDto> response = facadeService.getSubjectService().getSubjectContentById(zid, commonParam);

        return response;
    }

    @RequestMapping(value = "/qrcode")
    public Response<QRCode> qrCode(@RequestParam(value = "guid") String guid,
            @RequestParam(value = "token") String token, @ModelAttribute CommonParam commonParam) {
        return new Response<QRCode>(this.facadeService.getSubjectService().submitQRCode(guid, token, commonParam));
    }
	
	@RequestMapping(value = "/subject/get")
    public Response<SubjectDto> getSubject(@RequestParam(value = "zid") String zid,
            @ModelAttribute CommonParam commonParam) {

        return new Response<SubjectDto>(this.facadeService.getSubjectService().getSubject(zid));
    }
}
