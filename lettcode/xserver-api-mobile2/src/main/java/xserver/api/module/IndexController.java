package xserver.api.module;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import xserver.api.response.Response;

@Controller
public class IndexController extends BaseController {

    @RequestMapping("/serverstatus")
    public Response<Boolean> serverstatus() {
        return new Response<Boolean>(true);
    }
}
