package xserver.api.module;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.response.Response;
import xserver.lib.TpCacheUnit;

/**
 * 样例Controller
 */
@Controller
public class SampleController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(SampleController.class);

    /**
     * 数据接口
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/sample/data")
    public Response<String> data(HttpServletRequest request, @RequestParam(value = "id", required = false) String id) {
        log.info("request:data:" + id);

        // String names = this.facadeService.getSampleService().testMysqlDao(
        // Integer.MIN_VALUE, Integer.MAX_VALUE);
        // log.info("request:data:names:" + names);

        TpCacheUnit tpCache = this.facadeService.getSampleService().testTpCache("key1");

        Response<String> response = new Response<String>();
        response.setErrorCode("001");
        response.setData(id + "," + tpCache.getName());

        return response;
    }

    /**
     * 页面接口
     * @param request
     * @param url
     * @param model
     * @return
     */
    @RequestMapping(value = "/sample/page")
    public String page(HttpServletRequest request, @RequestParam("url") String url, Model model) {
        String data = this.facadeService.getSampleService().testTpDao(url);

        model.addAttribute("data", data);

        return "sample";
    }
}
