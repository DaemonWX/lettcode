package xserver.api.module.search;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.dto.ValueDto;
import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.search.dto.FilterResultDto;
import xserver.api.module.search.dto.SearchLiveDto;
import xserver.api.module.search.dto.SuggestDto;
import xserver.api.response.Response;
import xserver.lib.tp.search.request.SearchRequest;
import xserver.lib.tp.search.request.SuggestRequest;

@Controller("searchController")
public class SearchController extends BaseController {

    /**
     * 根据条件过滤视频数据
     * @param filter
     *            筛选条件 例如 vtp:180001;sc:30001;
     * @param ph
     *            推送平台 例如 420003(手机)
     * @param dt
     *            视频类型 参考SearchConstant.DATA_TYPE
     * @param page
     * @param pageSize
     * @param commonParam
     * @return
     */
    @RequestMapping("/search/filter")
    public Response<FilterResultDto> searchByFilter(@RequestParam(value = "filter") String filter,
            @RequestParam(value = "cid", required = false) Integer cid,
            @RequestParam(value = "ph", required = false, defaultValue = "420003,420004") String ph,
            @RequestParam(value = "dt", required = false, defaultValue = "1") String dt,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ModelAttribute CommonParam commonParam) {
        SearchRequest request = new SearchRequest();
        if (page != null && page > 0) {
            request.setPn(page);
        }
        if (pageSize != null && pageSize > 0) {
            request.setPs(pageSize <= 50 ? pageSize : 50);
        }
        request.setExtraParam(filter);
        request.setCg(cid);
        request.setPh(ph);
        request.setDt(dt);
        request.setLang(commonParam.getLangcode());
        request.setRegion(commonParam.getWcode());
        request.setLc(commonParam.getDevId());
        request.setClient_ip(commonParam.getIp());

        return this.facadeService.getSearchService().filter(request);
    }

    @RequestMapping("/search/live/hotwords")
    public Response<ValueDto<String>> getLiveHotWords(@ModelAttribute CommonParam commonParam) {

        return this.facadeService.getSearchService().getLiveHotWords(commonParam);
    }

    @RequestMapping("/search/live/searchlive")
    public SearchResponse<SearchLiveDto> searchLive(@RequestParam(value = "word") String word,
            @RequestParam(value = "ph", required = false) String ph,
            @RequestParam(value = "dt", required = false, defaultValue = "6") String dt,
            @RequestParam(value = "splatId", required = false) String splatId,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ModelAttribute CommonParam commonParam) {
        SearchRequest request = new SearchRequest();
        if (page != null && page > 0) {
            request.setPn(page);
        }
        if (pageSize != null && pageSize > 0) {
            request.setPs(pageSize <= 50 ? pageSize : 50);
        }
        request.setWd(word);
        // if (StringUtils.isNotBlank(ph)) {
        // request.setPh(ph);
        // }
        request.setDt(dt);
        request.setFrom("mobile_live08"); // 搜索给超级直播分配的专属from
        request.setLang(commonParam.getLangcode());
        request.setRegion(commonParam.getWcode());
        request.setLc(commonParam.getDevId());
        request.setClient_ip(commonParam.getIp());
        request.setToken(token);
        // 老版本不传splatId参数，只传递ph; ph=420003为内网,splatId为1036; splatId=1048为外网
        if (StringUtils.isNotBlank(splatId)) {
            request.setSplatid(splatId);
        } else {
            if ("420003".equals(ph)) {
                request.setSplatid("1036");
            } else {
                request.setSplatid("1048");
            }
        }

        return this.facadeService.getSearchService().searchLive(request);

    }

    /**
     * 获取搜索建议列表
     * @param type
     *            tv：tv端数据 mobile：手机端数据 pc：pc端数据 superlive:超级live数据
     * @param queryWord
     *            搜索的内容
     * @param method
     *            搜索内容的类型,例如按笔画，按拼音 all（全类型搜索）stroke：笔画 pinyin：拼音
     * @return
     */
    @RequestMapping("/search/suggest")
    public Response<List<SuggestDto>> getSuggestList(@RequestParam("type") String type,
            @RequestParam("query") String queryWord,
            @RequestParam(value = "method", required = false, defaultValue = "all") String method,
            @RequestParam(value = "pl", required = false) String pl, @ModelAttribute CommonParam commonParam) {

        SuggestRequest request = new SuggestRequest();
        request.setQ(queryWord);
        request.setP(type);
        request.setT(method);
        request.setM(pl);
        request.setRegion(commonParam.getWcode());
        request.setLang(commonParam.getLangcode());
        return this.facadeService.getSearchService().getSuggestList(request);
    }
}
