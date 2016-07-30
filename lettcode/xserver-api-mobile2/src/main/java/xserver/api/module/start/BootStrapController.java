package xserver.api.module.start;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.start.dto.BootStrapDataDto;
import xserver.api.module.start.dto.BootStrapRequest;
import xserver.api.module.start.dto.DateTimeDto;
import xserver.api.response.Response;

@Controller("bootStrapController")
public class BootStrapController extends BaseController {

    @RequestMapping(value = "/start/bootStrap")
    /**
     * @param client
     *            客户端
     * @param mac
     *            MAC地址
     * @param installVersion
     *            安装版本
     * @param request
     * @return
     */
    public Response<BootStrapDataDto> bootStrap(@RequestParam("mac") String mac,
            @RequestParam(value = "testCode", required = false, defaultValue = "0") String testCode,
            @ModelAttribute CommonParam commonParam) {
        BootStrapRequest authRequest = new BootStrapRequest("letv", commonParam.getTerminalSeries(),
                commonParam.getTerminalApplication(), commonParam.getPcode().substring(2, 4), commonParam.getDevId(),
                commonParam.getAppVersion(), commonParam.getIp(), commonParam.getLangcode(), commonParam.getPcode());
        Response<BootStrapDataDto> result = this.facadeService.getBootStrapService().bootStrap(authRequest);

        return result;
    }

    @RequestMapping(value = "/t/t")
    public Response<String> getT() {
        Response<String> r = new Response<String>();
        r.setData("asdds\":\"sowd");
        // do something
        // this.facadeService.getChannelService().getChannelData(request);
        //
        return r;
    }

    @RequestMapping(value = "/message/walltime")
    public Response<DateTimeDto> getTime() {
        Response<DateTimeDto> response = new Response<DateTimeDto>();
        DateTimeDto dto = new DateTimeDto();
        dto.setTime(System.currentTimeMillis());
        response.setData(dto);
        return response;
    }

    @RequestMapping(value = "/test")
    public Response<String> getSth() {
        return new Response<String>(this.facadeService.getBootStrapService().getSth());
    }

    @RequestMapping(value = "/asynctest")
    public Response<String> asyncTest() {
        return new Response<String>(this.facadeService.getBootStrapService().getAsyncSth());
    }

    @RequestMapping(value = "/cost1s")
    public Response<String> cost1s() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new Response<String>("1s");
    }

    @RequestMapping(value = "/cost2s")
    public Response<String> cost2s() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new Response<String>("2s");
    }

    @RequestMapping(value = "/cost3s")
    public Response<String> cost3s() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new Response<String>("3s");
    }
}
