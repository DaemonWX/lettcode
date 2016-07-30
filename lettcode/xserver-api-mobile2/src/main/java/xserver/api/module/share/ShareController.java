package xserver.api.module.share;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.api.module.share.dto.LinkCardDto;

@Controller
public class ShareController extends BaseController {

    @RequestMapping("/share/linkcard")
    public LinkCardDto linkCardShare(@RequestParam(value = "url") String url) {

        return this.facadeService.getShareService().linkCardShare(url);
    }
}
