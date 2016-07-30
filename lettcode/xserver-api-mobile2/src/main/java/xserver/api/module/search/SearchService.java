package xserver.api.module.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xserver.api.constant.DataConstants;
import xserver.api.dto.ValueDto;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.channel.dto.AssistData;
import xserver.api.module.search.dto.AlbumInfoDto;
import xserver.api.module.search.dto.FilterResultDto;
import xserver.api.module.search.dto.QueryInfoDto;
import xserver.api.module.search.dto.SearchLiveDto;
import xserver.api.module.search.dto.SuggestDto;
import xserver.api.module.user.dto.UserFavMutilCheckDto;
import xserver.api.response.Response;
import xserver.common.util.StringUtil;
import xserver.lib.thrift.search2.proto.MixSearchType;
import xserver.lib.thrift.search2.proto.SearchType;
import xserver.lib.thrift.shared.serving.proto.AlbumAttribute;
import xserver.lib.thrift.shared.serving.proto.DataType;
import xserver.lib.thrift.shared.serving.proto.GenericServing;
import xserver.lib.thrift.shared.serving.proto.GenericServingRequest;
import xserver.lib.thrift.shared.serving.proto.GenericServingResponse;
import xserver.lib.thrift.shared.serving.proto.GroupInfo;
import xserver.lib.thrift.shared.serving.proto.IdAndName;
import xserver.lib.thrift.shared.serving.proto.ResultBasicAttribute;
import xserver.lib.thrift.shared.serving.proto.ResultDocInfo;
import xserver.lib.thrift.shared.serving.proto.ServingResult;
import xserver.lib.tp.search.request.SearchRequest;
import xserver.lib.tp.search.request.SuggestRequest;
import xserver.lib.tp.search.response.SearchLiveTp;
import xserver.lib.tp.search.response.SearchMixResult;
import xserver.lib.tp.search.response.SearchMixResultTp;
import xserver.lib.tp.search.response.SearchWordResultTpResponse;
import xserver.lib.tp.search.response.SuggestTp;
import xserver.lib.tp.user.request.UserFavCheckRequest;
import xserver.lib.tp.user.request.UserFavRequest;
import xserver.lib.tp.user.request.UserFavRequest.UserFavOperaion;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.tpcache.cache.PlayCache;
import xserver.lib.util.CalendarUtil;

import com.google.common.primitives.Ints;

@Service("searchService")
public class SearchService extends BaseService {

    @Resource
    private GenericServing.Iface genericServing;

    /**
     * 根据检索条件检索视频
     * @param request
     * @return
     */
    public Response<FilterResultDto> filter(SearchRequest request) {
        Response<FilterResultDto> response = new Response<FilterResultDto>();
        String errCode = null;
        if (request == null) {
            errCode = SearchConstant.ERROR_CODE_SEARCH_PARAM_ERROR;
        } else {
            request.setExtraParam(this.parseSearchParam(request.getExtraParam()));
            request.setSrc(SearchRequest.SEARCH_PARAM_SRC_LETV); // 检索只允许出现乐视自有版权的数据
            this.processFilterRequest(request);
            // SearchMixResultTp tpResponse =
            // this.facadeTpDao.getSearchTpDao().searchTypes(request); //
            // 调用搜索接口获取数据

            // thrift接口
            SearchMixResultTp tpResponse = this.getSearchResponse(request); // 调用搜索接口获取数据

            FilterResultDto dto = new FilterResultDto();
            List<AlbumInfoDto> albumList = new LinkedList<AlbumInfoDto>();
            dto.setResult(albumList);
            if (tpResponse != null && tpResponse.getData_count() != 0 && tpResponse.getData_list() != null
                    && tpResponse.getData_list().size() > 0) {

                List<SearchMixResult> searchList = tpResponse.getData_list();
                AlbumInfoDto albumInfo = null;
                // 角标需要的数据
                List<String> searchDataIds = new ArrayList<>();
                Map<String, AlbumInfoDto> dataMap = new HashMap<>();
                Map<String, SearchMixResult> srcMap = new HashMap<>();

                for (SearchMixResult searchResult : searchList) {
                    albumInfo = new AlbumInfoDto();
                    albumInfo.setCid(searchResult.getCategory());
                    // albumInfo.setDesc(searchResult.getDescription());
                    albumInfo.setEpisodes(searchResult.getEpisodes());
                    albumInfo.setIsEnd(searchResult.getIsEnd());
                    albumInfo.setName(searchResult.getName());
                    albumInfo.setNowEpisodes(searchResult.getNowEpisodes());
                    albumInfo.setPic(searchResult.getImage(400, 300));
                    albumInfo.setImages(searchResult.getImages());
                    albumInfo.setPlayCount(searchResult.getPlayCount());
                    albumInfo.setDirector(searchResult.getDirector());
                    albumInfo.setStar(searchResult.getStars());
                    albumInfo.setSubTitle(searchResult.getSubname());
                    albumInfo.setUrl(searchResult.getUrl());
                    albumInfo.setDataType(searchResult.getDataType());
                    albumInfo.setDuration(searchResult.getDuration());
                    albumInfo.setSubCategory(searchResult.getSubCategoryName());
                    albumInfo.setIsEnd(searchResult.getIsEnd());
                    albumInfo.setPay(searchResult.getIspay());
                    albumInfo.setArea(searchResult.getAreaName());
                    if (StringUtils.isNotBlank(searchResult.getRating())) {
                        try {
                            albumInfo.setScore(Float.parseFloat(searchResult.getRating()));
                        } catch (Exception e) {
                        }
                    }

                    if (StringUtils.isNotBlank(searchResult.getMtime())) {
                        try {
                            albumInfo.setUpdateTime(Long.parseLong(searchResult.getMtime()));
                        } catch (Exception e) {
                        }
                    }

                    if (StringUtils.isNotBlank(searchResult.getReleaseDate())) {
                        try {
                            long releaseDate = Long.parseLong(searchResult.getReleaseDate());
                            SimpleDateFormat sdf = new SimpleDateFormat(CalendarUtil.SHORT_DATE_FORMAT);
                            albumInfo.setReleaseDate(sdf.format(new Date(releaseDate)));
                        } catch (Exception e) {
                        }
                    }
                    String mKey = SearchConstant.PID_VID_ZID_KEY.replace("{pid}", searchResult.getAid() + "").replace(
                            "{vid}", searchResult.getVid() + "");
                    if (SearchConstant.DATA_TYPE_ALUM == searchResult.getDataType()) { // 专辑
                        albumInfo.setPid(searchResult.getAid());
                        searchDataIds.add(CacheConstants.PlayCacheEntity_A_ + searchResult.getAid());
                        dataMap.put(mKey, albumInfo);
                        srcMap.put(mKey, searchResult);
                    } else if (SearchConstant.DATA_TYPE_VIDEO == searchResult.getDataType()) { // 视频
                        albumInfo.setPid(searchResult.getAid());
                        albumInfo.setVid(searchResult.getVid());
                        searchDataIds.add(CacheConstants.PlayCacheEntity_V_ + searchResult.getVid());
                        dataMap.put(mKey, albumInfo);
                        srcMap.put(mKey, searchResult);
                    } else if (SearchConstant.DATA_TYPE_SUBJECT == searchResult.getDataType()) { // 专题
                        albumInfo.setZid(searchResult.getAid());
                        mKey = mKey.replace("{zid}", searchResult.getAid() + "");
                        dataMap.put(mKey, albumInfo);
                        srcMap.put(mKey, searchResult);
                    }
                    albumList.add(albumInfo);
                }
                // 批量填充角标
                this.batchFillCornerLabel(dataMap, searchDataIds, srcMap);
                dto.setTotal(tpResponse.getData_count());
                dto.setCurPage(request.getPn());
                int pageSize = (request.getPs() != null && request.getPs() > 0) ? request.getPs() : albumList.size();
                if (pageSize != 0 || request.getPn() != null && request.getPn() > 0) {
                    int maxSize = tpResponse.getData_count() / pageSize
                            + (tpResponse.getData_count() % pageSize == 0 ? 0 : 1);
                    dto.setNextPage((request.getPn() + 1) <= maxSize ? (request.getPn() + 1) : maxSize);
                }
            } else if (tpResponse != null) {
                dto.setTotal(tpResponse.getData_count());
                dto.setCurPage(request.getPn());
            }
            response.setData(dto);
        }

        if (errCode != null) {
            this.setErrorResponse(response, errCode, errCode, request.getLang());
        }

        return response;
    }

    /**
     * @param param
     *            参数格式形如 param1:value1;param2:value2;param3:value3....
     * @return 返回格式形如 param1=value1&param2=value2&param3=value3.....
     */
    private String parseSearchParam(String param) {
        String result = null;
        if (param != null) {
            result = param.replace(":", "=").replace(";", "&");
        }
        return result;
    }

    private Map<String, String> getSearchParamMap(String extraParam) {
        // extraParam形如 cg=1&vt=180001
        Map<String, String> map = null;
        if (StringUtils.isNotBlank(extraParam)) {
            String[] params = extraParam.split("&");
            if (params != null && params.length > 0) {
                map = new HashMap<String, String>();
                for (String param : params) {
                    if (param != null) {
                        String[] str = param.split("=");
                        if (str != null && str.length == 2) {
                            map.put(str[0], str[1]);
                        }
                    }
                }
            }
        }
        return map;
    }

    private void processFilterRequest(SearchRequest searchRequest) {
        if (searchRequest != null) {
            String extraParam = searchRequest.getExtraParam();
            Map<String, String> paramMap = this.getSearchParamMap(extraParam);
            if (paramMap != null) {
                // 检索条件中已经拼接了dt,则忽略掉原有的dt参数值
                String dt = paramMap.get("dt");
                if (StringUtils.isNotBlank(dt)) {
                    searchRequest.setDt(null);
                }
            }
        }
    }

    public Response<ValueDto<String>> getLiveHotWords(CommonParam commonParam) {
        Response<ValueDto<String>> response = new Response<ValueDto<String>>();
        String langcode = commonParam != null ? commonParam.getLangcode() : DataConstants.LANGCODE_CN;
        response.setData(new ValueDto<String>(SearchConstant.getLiveHotWords(langcode)));
        return response;
    }

    public SearchResponse<SearchLiveDto> searchLive(SearchRequest searchRequest) {
        String errCode = null;
        if (searchRequest == null) {
            errCode = SearchConstant.ERROR_CODE_SEARCH_PARAM_ERROR;
        }
        SearchResponse<SearchLiveDto> response = new SearchResponse<SearchLiveDto>(searchRequest.getPn(),
                searchRequest.getPs());
        SearchWordResultTpResponse resultTp = this.facadeTpDao.getSearchTpDao().searchByWord(searchRequest);
        if (resultTp != null && resultTp.getQuery_info() != null) {
            QueryInfoDto queryDto = new QueryInfoDto();
            queryDto.setIllegal(resultTp.getQuery_info().getIllegal());
            response.setQueryInfo(queryDto);
        }

        if (resultTp != null && resultTp.getLive_list_v2() != null) {
            List<SearchLiveTp> liveTpList = resultTp.getLive_list_v2();
            List<SearchLiveDto> liveList = new ArrayList<SearchLiveDto>();
            for (SearchLiveTp liveTp : liveTpList) {
                liveList.add(new SearchLiveDto(liveTp));
            }
            response.setData(liveList);
            Integer liveCount = resultTp.getLive_count_v2();
            response.setCount(liveCount == null ? 0 : liveCount);

            // 批量取轮播和卫视台的收藏状态 sourceId=2 电视台 sourceId=7轮播台
            if (StringUtils.isNotBlank(searchRequest.getToken())) {
                List<UserFavCheckRequest> favCheckList = new LinkedList<UserFavCheckRequest>();
                UserFavCheckRequest checkRequest = null;
                for (SearchLiveDto liveDto : liveList) {
                    checkRequest = new UserFavCheckRequest();
                    checkRequest.setChannel_id(liveDto.getId());
                    if (SearchLiveDto.LIVE_SOURCE_ID_WEISHI.equals(liveDto.getSourceId())) { // 电视台
                        checkRequest.setFavorite_type(UserFavRequest.FAVORITE_TYPE_WEISHI);
                    } else if (SearchLiveDto.LIVE_SOURCE_ID_LUNBO.equals(liveDto.getSourceId())) { // 轮播台
                        checkRequest.setFavorite_type(UserFavRequest.FAVORITE_TYPE_LUNBO);
                    } else {
                        continue;
                    }
                    favCheckList.add(checkRequest);
                }

                if (favCheckList.size() > 0) {
                    UserFavRequest userFavRequest = new UserFavRequest(UserFavOperaion.MUTILCHECK);
                    userFavRequest.setSso_tk(searchRequest.getToken());
                    Response<List<UserFavMutilCheckDto>> checkResponse = this.facadeService.getUserService()
                            .checkMutilFav(userFavRequest, favCheckList);
                    if (checkResponse != null && checkResponse.getData() != null) {
                        Map<String, String> favMap = new HashMap<String, String>();
                        for (UserFavMutilCheckDto check : checkResponse.getData()) {
                            favMap.put(check.getChannelId() + "_" + check.getFavType(), check.getFavorite());
                        }

                        String favoriteType = null;
                        for (SearchLiveDto liveDto : liveList) {
                            if (SearchLiveDto.LIVE_SOURCE_ID_WEISHI.equals(liveDto.getSourceId())) {
                                favoriteType = UserFavRequest.FAVORITE_TYPE_WEISHI;
                            } else if (SearchLiveDto.LIVE_SOURCE_ID_LUNBO.equals(liveDto.getSourceId())) {
                                favoriteType = UserFavRequest.FAVORITE_TYPE_LUNBO;
                            } else {
                                continue;
                            }
                            liveDto.setFavorite(favMap.get(liveDto.getChannelId() + "_" + favoriteType));
                        }

                    }
                }
            }

        }

        if (errCode != null) {
            this.setErrorResponse(response, errCode, errCode, searchRequest.getLang());
        }

        return response;
    }

    /**
     * 获取搜索提示
     * @param request
     * @return
     */
    public Response<List<SuggestDto>> getSuggestList(SuggestRequest request) {
        // TODO Auto-generated method stub
        Response<List<SuggestDto>> response = new Response<List<SuggestDto>>();

        if (request != null && StringUtils.isNotBlank(request.getQ())) {
            List<SuggestTp> tpList = this.facadeTpDao.getSearchTpDao().getSuggestList(request);
            if (tpList != null) {
                List<SuggestDto> sugList = new LinkedList<SuggestDto>();
                SuggestDto dto = null;
                for (SuggestTp sgtTp : tpList) {
                    dto = new SuggestDto();
                    dto.setName(sgtTp.getName());
                    sugList.add(dto);
                }
                response.setData(sugList);
            }
        }

        return response;
    }

    /** 批量填充角标 */
    private void batchFillCornerLabel(Map<String, AlbumInfoDto> dstMap, List<String> cacheIds,
            Map<String, SearchMixResult> srchMap) {
        if (dstMap == null || dstMap.size() <= 0) {
            return;
        }
        Map<String, PlayCache> caches = new HashMap<>();
        if (cacheIds != null || cacheIds.size() > 0) {
            caches = this.facadeService.getVideoService().mgetPlayCacheEntityByKeys(cacheIds);
        }
        for (Map.Entry<String, SearchMixResult> entry : srchMap.entrySet()) {
            SearchMixResult src = entry.getValue();
            if (src != null) {
                String mKey = SearchConstant.PID_VID_ZID_KEY.replace("{pid}", src.getAid() + "").replace("{vid}",
                        src.getVid() + "");
                if (SearchConstant.DATA_TYPE_SUBJECT == src.getDataType()) { // 专题
                    mKey = mKey.replace("{zid}", src.getAid() + "");
                }
                AlbumInfoDto f = dstMap.get(mKey);
                String cornerLabel = null;
                if (f != null) {
                    if (caches != null) {
                        if (f.getVid() != null) {
                            cornerLabel = this.facadeService.getChannelService().fillCornerLabelByCache(
                                    caches.get(CacheConstants.PlayCacheEntity_V_ + f.getVid()),
                                    String.valueOf(SearchConstant.DATA_TYPE_VIDEO), null);
                        } else if (f.getPid() != null) {
                            cornerLabel = this.facadeService.getChannelService().fillCornerLabelByCache(
                                    caches.get(CacheConstants.PlayCacheEntity_A_ + f.getPid()),
                                    String.valueOf(SearchConstant.DATA_TYPE_ALUM), null);
                        }
                    }
                    if (StringUtil.isBlank(cornerLabel)) {
                        cornerLabel = this.fillCornerLabelBySearchData(src);
                    }
                    f.setCornerLabel(cornerLabel);
                }
            }
        }
    }

    private String fillCornerLabelBySearchData(SearchMixResult src) {
        if (src == null) {
            return null;
        }
        String isPay = src.getIspay();
        String dataType = null;
        String isPlayMark = null;
        String isHomemade = null;
        String aType = null;
        if (SearchConstant.DATA_TYPE_SUBJECT.equals(src.getDataType())) {
            dataType = String.valueOf(SearchConstant.DATA_TYPE_SUBJECT);
        } else if (src.getAid() != null) {
            dataType = String.valueOf(SearchConstant.DATA_TYPE_ALUM);
        }
        return this.facadeService.getChannelService().getCornerLabel(
                new AssistData(isPay, dataType, isPlayMark, isHomemade, aType));
    }

    /******************************************************** 搜索 thrift ********************************************/

    private SearchMixResultTp getSearchResponse(SearchRequest request) {
        SearchMixResultTp result = null;
        try {
            GenericServingRequest genericServingRequest = this.fillThriftRequest(request);
            if (genericServingRequest != null) {
                GenericServingResponse response = genericServing.Serve(genericServingRequest);
                result = parseSearchResponse(response);
            }
            return result;
        } catch (Exception e) {
            log.error("get search data error" + e);
        }
        return result;
    }

    /**
     * 填充搜索thrift接口的请求bean
     * @param request
     * @return
     */
    private GenericServingRequest fillThriftRequest(SearchRequest request) {
        GenericServingRequest genericServingRequest = null;
        xserver.lib.thrift.search2.proto.SearchRequest searchRequest = new xserver.lib.thrift.search2.proto.SearchRequest();

        SearchType type = SearchType.HAS_QUERY_SEARCH;
        if (StringUtils.isNotBlank(request.getStype()) && Ints.tryParse(request.getStype()) != null) {
            int stype = Ints.tryParse(request.getStype());
            if (stype == 0) {
                type = SearchType.NO_QUERY_SEARCH;
            }
        }
        searchRequest.setSearch_type(type);
        if (StringUtils.isNotBlank(request.getWd())) {
            searchRequest.setQuery(request.getWd());
            // 如果wd不为空，搜索类型为HAS_QUERY_SEARCH
            searchRequest.setSearch_type(SearchType.HAS_QUERY_SEARCH);
        }
        if (StringUtils.isNotBlank(request.getPh())) {
            searchRequest.setPush_flag(request.getPh());
        }
        if (StringUtils.isNotBlank(request.getSplatid())) {
            searchRequest.setSplat_id(request.getSplatid());
        }
        if (StringUtils.isNotBlank(request.getFrom())) {
            searchRequest.setClient_name(request.getFrom());

        }
        if (StringUtils.isNotBlank(request.getDt())) {
            searchRequest.setRequest_data_types(request.getDt());
        }

        MixSearchType mix = MixSearchType.MIX_FOR_TV;
        if (StringUtils.isNotBlank(request.getMix()) && Ints.tryParse(request.getMix()) != null) {
            mix = MixSearchType.findByValue(Ints.tryParse(request.getMix()));
        }
        searchRequest.setMix(mix);

        if (StringUtils.isNotBlank(request.getClient_ip())) {
            searchRequest.setClient_ip(request.getClient_ip());

        }
        if (StringUtils.isNotBlank(request.getOr())) {
            searchRequest.setSort_by(request.getOr());
        }

        short stt = 1;
        if (StringUtils.isNotBlank(request.getStt()) && Ints.tryParse(request.getStt()) != null) {
            stt = Short.parseShort(request.getStt());
        }
        searchRequest.setSort_type(stt);
        if (request.getPn() != null) {
            searchRequest.setPage_number(request.getPn().shortValue());
        }
        if (request.getPs() != null) {
            searchRequest.setPage_size(request.getPs().shortValue());
        }
        if (StringUtils.isNotBlank(request.getUid())) {
            searchRequest.setUid(request.getUid());
        }

        searchRequest.setHigh_light("0".equals(request.getHl()) ? false : true);

        short jf = 1;
        if (StringUtils.isNotBlank(request.getJf()) && Ints.tryParse(request.getJf()) != null) {
            jf = Short.parseShort(request.getStt());
        }
        searchRequest.setJson_format(jf);
        if (StringUtils.isNotBlank(request.getSf())) {
            List<String> search_fields = new ArrayList<>();
            search_fields.add(request.getSf());
            searchRequest.setSearch_fields(search_fields);
        }

        Map<String, String> filter_param = request.getParamsMap();
        if (!CollectionUtils.isEmpty(filter_param)) {
            searchRequest.setFilter_param(filter_param);
            searchRequest.setAll_param(filter_param);
            genericServingRequest = new GenericServingRequest();
            genericServingRequest.setSearch_request(searchRequest);
            log.info("genericServingRequest request:" + genericServingRequest.toString());
        }
        return genericServingRequest;
    }

    private SearchMixResultTp parseSearchResponse(GenericServingResponse response) {
        log.info("GenericServingResponse search result:" + response.getSearch_response());
        SearchMixResultTp result = null;
        if (response != null && response.getSearch_response() != null) {
            xserver.lib.thrift.search2.proto.SearchResponse searchResponse = response.getSearch_response();
            result = new SearchMixResultTp();
            result.setResponse_time((long) searchResponse.getResponse_time());
            List<xserver.lib.thrift.shared.serving.proto.GroupInfo> groupInfos = searchResponse.getGroup_info();
            for (GroupInfo info : groupInfos) {
                if ("data".equals(info.getGroup_id())) {
                    result.setData_count(Integer.valueOf(info.getTotal_count()));
                    List<ServingResult> servingResults = searchResponse.getServing_result_list().subList(
                            info.getOffset(), info.getCount());
                    List<SearchMixResult> data_list = new ArrayList<>();
                    for (ServingResult res : servingResults) {
                        ResultDocInfo doc_info = res.getDoc_info();
                        if (doc_info != null) {
                            SearchMixResult mixResult = new SearchMixResult();
                            ResultBasicAttribute basicAttribute = res.getBasic_attribute();
                            AlbumAttribute albumAttribute = doc_info.getAlbum_attribute();
                            mixResult.setDataType(doc_info.getData_type().getValue());
                            if (doc_info.getId() == null || Long.valueOf(doc_info.getId()) == null) {
                                continue;
                            }
                            long id = Long.valueOf(doc_info.getId());
                            if (doc_info.getData_type() == DataType.ALBUM) {
                                mixResult.setAid(id);
                            }
                            if (doc_info.getData_type() == DataType.VIDEO) {
                                mixResult.setAid(id);
                            }
                            // mixResult.setSid();
                            mixResult.setName(doc_info.getName());
                            mixResult.setSubname(albumAttribute.getSub_name());
                            mixResult.setEnglishName(doc_info.getEnglish_name());
                            mixResult.setOtherName(doc_info.getOther_name());
                            // mixResult.setTrueName();
                            // mixResult.setBirthday();
                            // mixResult.setGender();
                            mixResult.setArea(albumAttribute.getArea());
                            mixResult.setAreaName(albumAttribute.getArea_name());
                            mixResult.setDescription(doc_info.getDescription());
                            // mixResult.setDisplay();
                            // mixResult.setIsActor();
                            // mixResult.setIsDirector();
                            // mixResult.setIsAvail();
                            // mixResult.setPostS1();
                            // mixResult.setPostS2();
                            // mixResult.setPostS3();
                            // mixResult.setPostH1();
                            // mixResult.setPostH2();
                            mixResult.setIsEnd(String.valueOf(albumAttribute.getIs_end()));
                            // mixResult.setPostOrgin();
                            // mixResult.setFirstWord();
                            // mixResult.setCtime();
                            mixResult.setNameQuanpin(albumAttribute.getName_quanpin());
                            mixResult.setNameJianpin(albumAttribute.getName_jianpin());
                            mixResult.setAlbumSrc(albumAttribute.getAlbum_src());
                            mixResult.setEpisodes(String.valueOf(albumAttribute.getEpisodes()));
                            mixResult.setNowEpisodes(albumAttribute.getNow_episode());
                            mixResult.setPlayCount(basicAttribute.getPlay_total_count());
                            mixResult.setUrl(albumAttribute.getUrl());
                            mixResult.setIspay(String.valueOf(albumAttribute.getIs_pay()));
                            // mixResult.setTitle();
                            // mixResult.setDisplay_tag();
                            mixResult.setCategory(Ints.tryParse(doc_info.getCategory()));
                            mixResult.setCategoryName(doc_info.getCategory_name());
                            // mixResult.setList_order();
                            mixResult.setImages(albumAttribute.getImages());
                            Map<String, String> pushFlag = new HashMap<>();
                            for (IdAndName push_flag : albumAttribute.getPush_flag()) {
                                pushFlag.put(push_flag.getId(), push_flag.getName());
                            }
                            mixResult.setPushFlag(pushFlag);
                            Map<String, String> directory = new HashMap<>();
                            for (IdAndName director : albumAttribute.getDirector()) {
                                directory.put(director.getId(), director.getName());
                            }
                            mixResult.setDirectory(directory);
                            List<Map<String, String>> starring = new ArrayList<>();
                            Map<String, String> starringMap;
                            for (IdAndName star : albumAttribute.getStarring()) {
                                starringMap = new HashMap<>();
                                starringMap.put(star.getId(), star.getName());
                                starring.add(starringMap);
                            }
                            mixResult.setStarring(starring);
                            mixResult.setDuration(albumAttribute.getDuration());
                            mixResult.setSubCategoryName(albumAttribute.getSub_category_name());
                            mixResult.setRating(albumAttribute.getRating());
                            mixResult.setVideoType(String.valueOf(albumAttribute.getVideo_type()));

                            data_list.add(mixResult);
                        }
                    }
                    result.setData_list(data_list);
                }
            }
            result.setResponse_time(Long.valueOf(searchResponse.getResponse_time()));
        }
        return result;
    }
}
