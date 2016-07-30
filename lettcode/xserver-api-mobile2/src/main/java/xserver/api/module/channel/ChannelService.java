package xserver.api.module.channel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xserver.api.constant.DataConstants;
import xserver.api.constant.ErrorCodeConstants;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.channel.constants.SkipTypeConstant;
import xserver.api.module.channel.dto.AssistData;
import xserver.api.module.channel.dto.ChannelBlock;
import xserver.api.module.channel.dto.ChannelContentDto;
import xserver.api.module.channel.dto.ChannelFocus;
import xserver.api.module.channel.dto.ChannelPageDto;
import xserver.api.module.channel.dto.ChannelSkip;
import xserver.api.module.channel.skippolicy.ISkipPolicy;
import xserver.api.module.search.dto.AlbumInfoDto;
import xserver.api.module.search.dto.FilterResultDto;
import xserver.api.response.BaseResponse;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.common.dto.channel.ChannelNavigation;
import xserver.common.util.TimeUtil;
import xserver.lib.constant.LiveConstants;
import xserver.lib.dto.channel.ChannelLiveDto;
import xserver.lib.tp.cms.response.CmsBlockTpResponse;
import xserver.lib.tp.cms.response.RatingAndPlayRankTp;
import xserver.lib.tp.rec.request.RecBaseRequest;
import xserver.lib.tp.rec.response.RecommendTpResponse;
import xserver.lib.tp.rec.response.RecommendTpResponse.RecData;
import xserver.lib.tp.search.request.SearchRequest;
import xserver.lib.tp.staticconf.request.FilterConditionDto;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.tpcache.cache.PlayCache;
import xserver.lib.util.MessageUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Ints;

@Service("channelService")
public class ChannelService extends BaseService {

    @Autowired
    private List<ISkipPolicy> iSkipPolicies;

    @Autowired
    private ChannelHelper channelHelper;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 获取频道墙的所有频道
     * @param common
     * @return
     */
    public PageResponse<ChannelPageDto> getChannelPage(CommonParam common) {
        PageResponse<ChannelPageDto> response = new PageResponse<ChannelPageDto>();
        String errCode = null;
        String wcode = DataConstants.WCODE_CN;
        if (common != null && StringUtils.isNotBlank(common.getWcode())) {
            wcode = common.getWcode();
        }
        String blockid = ChannelConstant.MULTI_ALLCHANNEL_BLOCK_MAP.get(wcode);
        if (StringUtils.isBlank(blockid)) {
            blockid = "2759";
        }
        CmsBlockTpResponse tpResponse = this.facadeTpDao.getCmsTpDao().getCmsBlock(blockid);

        if (tpResponse == null || tpResponse.getSubBlocks() == null || tpResponse.getSubBlocks().size() == 0) {
            errCode = ChannelConstant.ERROR_CODE_CHANNEL_NO_DATA;
        }

        if (errCode == null) {
            List<CmsBlockTpResponse> channelList = tpResponse.getSubBlocks(); // 获取频道墙的频道组
            List<ChannelPageDto> dtoList = new ArrayList<ChannelPageDto>();
            for (CmsBlockTpResponse channels : channelList) {
                dtoList.add(new ChannelPageDto(channels));
            }
            response.setData(dtoList);
        }

        if (errCode != null) {
            response = this.setErrorResponse(response, errCode, ((common != null) ? common.getLangcode() : null));
        }

        return response;
    }

    /**
     * 获取频道中的筛选条件
     * @param cid
     *            频道id
     * @return
     */
    public Response<FilterConditionDto> getFilterCondition(Integer cid, CommonParam commonParam) {
        Response<FilterConditionDto> response = new Response<FilterConditionDto>();
        String errCode = null;
        if (cid == null) {
            errCode = ChannelConstant.ERROR_CODE_CHANNEL_PARAM_ERROR;
            response = this.setErrorResponse(response, errCode, null);
        } else {
            if (DataConstants.WCODE_HK.equals(commonParam.getWcode())) {
                response.setData(this.facadeTpDao.getStaticConfTpDao().getFilterCondition(cid));
            } else {
                response.setData(ChannelConstant.CHANNEL_FILTER_MAP.get(cid));
            }
        }

        return response;
    }

    /**
     * 根据搜索条件获取频道数据
     */
    public Response<ChannelContentDto> getSearchChannel(SearchRequest searchRequest, Integer channelid,
            String defaultStreamParam) {
        Response<ChannelContentDto> response = new Response<ChannelContentDto>();

        if (searchRequest != null) {
            searchRequest.setPh(DataConstants.MOBILE_COPYRIGHT);
            String defaultStream = getDefaultStream(channelid);
            if (StringUtils.isNotEmpty(defaultStreamParam)) {
                defaultStream = defaultStreamParam;
            }
            Response<FilterResultDto> searchTpResponse = this.facadeService.getSearchService().filter(searchRequest);
            if (searchTpResponse != null && searchTpResponse.getData() != null
                    && searchTpResponse.getData().getResult() != null) {
                ChannelContentDto contentDto = new ChannelContentDto();
                List<ChannelBlock> blockList = new ArrayList<ChannelBlock>();
                contentDto.setBlock(blockList);
                ChannelBlock block = new ChannelBlock();
                block.setStyle(ChannelConstant.contentStyle.CONTENT_SYTLE_PIC_LIST); // 搜索数据来源展现样式为图为列表
                List<ChannelFocus> focusList = new LinkedList<ChannelFocus>();
                ChannelFocus focus = null;
                List<AlbumInfoDto> dataList = searchTpResponse.getData().getResult();
                for (AlbumInfoDto album : dataList) {
                    focus = new ChannelFocus();
                    focus.setAt(ChannelFocus.HOME_ACTION_TYPE_FULL);
                    focus.setPid(album.getPid());
                    focus.setVid(album.getVid());
                    focus.setName(album.getName());
                    focus.setSubTitle(album.getSubTitle());
                    focus.setCid(album.getCid());
                    focus.setType(String.valueOf(album.getDataType()));
                    focus.setEpisode(album.getEpisodes());
                    focus.setNowEpisodes(album.getNowEpisodes());
                    focus.setIsEnd(album.getIsEnd());
                    String pic = album.getImages().get("960*540");
                    if (StringUtils.isBlank(pic)) {
                        pic = album.getImages().get("400*225");
                    }
                    if (StringUtils.isBlank(pic)) {
                        pic = album.getPic();
                    }
                    focus.setPic(pic);
                    focus.setPicAll(this.getChannelImage(pic));
                    focus.setDirector(album.getDirector());
                    focus.setSinger(album.getStar());
                    focus.setUpdateTime(album.getUpdateTime());
                    focus.setDuration(album.getDuration());
                    focus.setScore(album.getScore());
                    focus.setSubCategory(album.getSubCategory());
                    focus.setDefaultStream(defaultStream);
                    focus.setPay(album.getPay());
                    focus.setSource(ChannelConstant.dataSource.DATA_SOURCE_SEARCH);
                    focus.setReleaseDate(album.getReleaseDate());
                    focus.setArea(album.getArea());
                    focusList.add(focus);
                }
                if (focusList.size() > 0) {
                    block.setList(focusList);
                    blockList.add(block);
                    response.setData(contentDto);
                }
            } else {
                // 如果搜索取不到数据，走缓存
                String cache = tpCacheTemplate
                        .get(CacheConstants.channel.CHANNEL_SEARCH_PAGE + channelid, String.class);
                if (StringUtils.isNotBlank(cache)) {
                    try {
                        response = OBJECT_MAPPER.readValue(cache, new TypeReference<Response<ChannelContentDto>>() {
                        });
                        return response;
                    } catch (Exception e) {

                    }
                }
            }
        }

        return response;
    }

    /**
     * 获取频道排行数据
     * @param type
     * @return
     */
    public Response<ChannelContentDto> getTopData(String type, CommonParam commonParam) {
        Response<ChannelContentDto> response = new Response<ChannelContentDto>();
        String lang = null;
        if (commonParam != null) {
            lang = commonParam.getLangcode();
        }
        if (StringUtils.isBlank(lang)) {
            lang = "zh_cn";
        }
        String errCode = null;
        String dataType = ChannelSkip.CHANNEL_TOP_DATA_TYPE.get(type);
        if (StringUtils.isBlank(type) || StringUtils.isBlank(dataType)) {
            errCode = ChannelConstant.ERROR_CODE_CHANNEL_NO_DATA;
        } else {
            // List<RatingAndPlayRankTp> rankList1 =
            // this.facadeTpDao.getCmsTpDao().getTopData(type);
            List<RatingAndPlayRankTp> rankList = this.tpCacheTemplate.get(CacheConstants.channel.CHANNEL_PAGE_TOP
                    + type, new TypeReference<List<RatingAndPlayRankTp>>() {
            });
            if (rankList != null && rankList.size() > 0) {
                ChannelContentDto dto = new ChannelContentDto();
                response.setData(dto);
                List<ChannelBlock> blockList = new ArrayList<ChannelBlock>();
                ChannelBlock block = new ChannelBlock();
                blockList.add(block);
                dto.setBlock(blockList);
                List<ChannelFocus> focusList = new LinkedList<ChannelFocus>();
                // block.setName("昨日热播排行");
                block.setName(MessageUtils.getMessage("CHANNEL.TOP.BLOCK.NAME", lang));
                block.setStyle(ChannelConstant.contentStyle.CONTENT_SYTLE_PIC_LIST); // 排行数据来源展现样式为图为列表
                block.setList(focusList);
                ChannelFocus focus = null;
                List<String> cacheKeys = new ArrayList<>();
                for (RatingAndPlayRankTp rank : rankList) {
                    if (rank.getId() != null) {
                        if (ChannelConstant.DATA_TYPE_ALBUM.equals(dataType)) {
                            cacheKeys.add(CacheConstants.PlayCacheEntity_A_ + rank.getId());
                        } else if (ChannelConstant.DATA_TYPE_VIDEO.equals(dataType)) {
                            cacheKeys.add(CacheConstants.PlayCacheEntity_V_ + rank.getId());
                        }
                    }
                }
                Map<String, PlayCache> caches = null;
                if (cacheKeys != null && cacheKeys.size() > 0) {
                    caches = this.facadeService.getVideoService().mgetPlayCacheEntityByKeys(cacheKeys);
                    if (caches != null && caches.size() > 0) {
                        for (RatingAndPlayRankTp rank : rankList) {
                            if (ChannelConstant.DATA_TYPE_ALBUM.equals(dataType)) { // 排行数据为专辑
                                String k = CacheConstants.PlayCacheEntity_A_ + rank.getId();
                                PlayCache album = caches.get(k);
                                if (album != null) {
                                    focus = this.fillChannelTopForAlbum(album, rank);
                                }
                            } else if (ChannelConstant.DATA_TYPE_VIDEO.equals(dataType)) { // 排行数据为视频
                                String k = CacheConstants.PlayCacheEntity_V_ + rank.getId();
                                PlayCache video = caches.get(k);
                                if (video != null) {
                                    focus = this.fillChannelTopForVideo(video, rank);
                                }
                            }
                            if (focus != null) {
                                focusList.add(focus);
                            }
                        }
                    }
                }
                if (focusList == null || focusList.size() <= 0) {
                    errCode = ChannelConstant.ERROR_CODE_CHANNEL_NO_DATA;
                }
            } else {
                errCode = ChannelConstant.ERROR_CODE_CHANNEL_NO_DATA;
            }

            if (errCode != null) {
                response = this.setErrorResponse(response, errCode, null);
            }
        }
        return response;
    }

    /**
     * 获取频道的默认播放码流
     * @param channelid
     * @return
     */
    private String getDefaultStream(Integer channelid) {
        if (channelid != null) {
            return ChannelConstant.CHANNEL_DEFAULT_STREAM.get(channelid);
        }
        return null;
    }

    /**
     * 获取频道图片--数据来源为推荐
     * 当前支持 16:9 16:10 4:3比例的图
     * @return
     */
    public Map<String, String> getChannelImage(RecData rec) {
        Map<String, String> picMap = null;
        if (rec != null) {
            picMap = new HashMap<String, String>();
            picMap.put("pic_16_10", rec.getImageByRatio(16, 10));
            picMap.put("pic_16_9", rec.getImageByRatio(16, 9));
            picMap.put("pic_4_3", rec.getImageByRatio(4, 3));
        }
        return picMap;
    }

    /**
     * 获取频道图片--数据来源为cms
     * 当前支持 16:9 16:10 4:3比例的图
     * @return
     */
    public Map<String, String> getChannelImage(String cmsPic) {
        Map<String, String> picMap = null;
        if (StringUtils.isNotBlank(cmsPic)) {
            picMap = new HashMap<String, String>();
            picMap.put("pic_16_10", cmsPic);
            picMap.put("pic_16_9", cmsPic);
            picMap.put("pic_4_3", cmsPic);
        }
        return picMap;
    }

    public Map<String, String> getChannelImage(PlayCache play, String defaultPic, String dataType, Integer videoType) {
        Map<String, String> picMap = null;
        if (play != null) {
            picMap = new HashMap<String, String>();
            if (ChannelConstant.DATA_TYPE_ALBUM.endsWith(dataType)) {
                if (videoType != null && videoType != 180001) {
                    picMap.put("pic_16_10", StringUtils.isNotBlank(defaultPic) ? defaultPic : play.getAPic(400, 250));
                    picMap.put("pic_16_9", StringUtils.isNotBlank(defaultPic) ? defaultPic : play.getAPic(960, 540));
                    picMap.put("pic_4_3", StringUtils.isNotBlank(defaultPic) ? defaultPic : play.getAPic(400, 300));
                } else {
                    picMap.put("pic_16_10", StringUtils.isNotBlank(play.getAPic(400, 250)) ? play.getAPic(400, 250)
                            : defaultPic);
                    picMap.put("pic_16_9", StringUtils.isNotBlank(play.getAPic(960, 540)) ? play.getAPic(960, 540)
                            : defaultPic);
                    picMap.put("pic_4_3", StringUtils.isNotBlank(play.getAPic(400, 300)) ? play.getAPic(400, 300)
                            : defaultPic);
                }
            } else if (ChannelConstant.DATA_TYPE_VIDEO.equals(dataType)) {
                if (videoType != null && videoType != 180001) {
                    picMap.put("pic_16_10", StringUtils.isNotBlank(defaultPic) ? defaultPic : play.getAPic(400, 250));
                    picMap.put("pic_16_9", StringUtils.isNotBlank(defaultPic) ? defaultPic : play.getAPic(960, 540));
                    picMap.put("pic_4_3", StringUtils.isNotBlank(defaultPic) ? defaultPic : play.getAPic(400, 300));
                } else {
                    picMap.put("pic_16_10", StringUtils.isNotBlank(play.getVPic(400, 250)) ? play.getVPic(400, 250)
                            : defaultPic);
                    picMap.put("pic_16_9", StringUtils.isNotBlank(play.getVPic(960, 540)) ? play.getVPic(960, 540)
                            : defaultPic);
                    picMap.put("pic_4_3", StringUtils.isNotBlank(play.getVPic(400, 300)) ? play.getVPic(400, 300)
                            : defaultPic);
                }
            }

        }
        return picMap;
    }

    /**
     * 从推荐数据中解析得到频道导航数据
     * @param recTp
     * @return
     */
    private List<ChannelNavigation> parseNavigations(RecommendTpResponse recTp) {
        if (recTp == null || recTp.getRec() == null || recTp.getRec().size() == 0) {
            return null;
        }

        List<RecommendTpResponse.RecData> recs = recTp.getRec();
        List<ChannelNavigation> navList = new LinkedList<ChannelNavigation>();
        ChannelNavigation nav = null;
        for (RecommendTpResponse.RecData rec : recs) {
            // 需要根据推送平台判断是否推送Android平台
            if (rec.getPushflag() != null && rec.getPushflag().contains(DataConstants.ANDROID_COPYRIGHT)) {
                nav = new ChannelNavigation();
                nav.setCid(recTp.getCid());
                nav.setName(rec.getTitle());
                nav.setPageid(rec.getContent());
                // 暂时只有数据来源为推荐的频道才有导航，导航的数据地址暂时全部拼接推荐
                nav.setDataUrl(ChannelSkip.getChannelSkipUrl(rec.getContent(), null));
                nav.setSubCid(getSubCid(rec.getContent()));// 子频道id
                navList.add(nav);
            }
        }

        return navList;
    }

    /**
     * 获得子频道id
     * @param pageid
     * @return
     */
    private Integer getSubCid(String pageid) {
        Integer subCid = null;
        if (StringUtils.isNotBlank(pageid) && ChannelSkip.CHANNEL_TOP_PAGEID.containsKey(pageid)) {
            subCid = ChannelConstant.SUBCID.TOP;
        }

        return subCid;
    }

    /**
     * 从推荐数据中解析得到频道模块数据
     * @param recTp
     * @return
     */
    private ChannelBlock parseChannelBlockForRec(RecommendTpResponse recTp, String lang) {
        if (recTp == null || recTp.getRec() == null || recTp.getRec().size() == 0 || recTp.getCms_num() == null
                || recTp.getNum() == null) {
            return null;
        }
        ChannelBlock block = new ChannelBlock();
        block.setRecFragId(recTp.getFragId());// 推荐数据上报字段
        block.setRecReid(recTp.getReid());// 推荐数据上报字段
        block.setRecArea(recTp.getArea());// 推荐数据上报字段
        block.setRecBucket(recTp.getBucket());// 推荐数据上报字段
        String recType;
        int total = recTp.getNum() != null ? recTp.getNum().intValue() : 0;
        int cmsNum = recTp.getCms_num() != null ? recTp.getCms_num().intValue() : 0;
        if (cmsNum == total) {// 全部来自人工推荐
            recType = ChannelConstant.recSrcType.REC_SRC_TYPE_EDITOR;
        } else if (cmsNum == 0) {// 全部自动推荐
            recType = ChannelConstant.recSrcType.REC_SRC_TYPE_AUTO;
        } else {
            recType = ChannelConstant.recSrcType.REC_SRC_TYPE_MIX;
        }
        block.setRecSrcType(recType);

        boolean isNativeSubject = ChannelConstant.contentStyle.CONTENT_SYTLE_NATIVE_SUBJECT.equals(recTp
                .getContentStyle()) ? true : false;

        block.setName(recTp.getBlockname());
        block.setCid(String.valueOf(recTp.getCid()));
        block.setRectCid(recTp.getRedirectCid());
        block.setRectPageId(recTp.getRedirectPageId());
        block.setRectType(recTp.getRedirectType());
        block.setRectUrl(recTp.getRedirectUrl());
        block.setRectVt(recTp.getRedirectVideoType());
        block.setStyle(recTp.getContentStyle());

        // block中的数据
        // 设置主标题跳转
        if (!isNativeSubject) {
            // native专题的标题不会跳转
            setMainTitleRedirect(recTp.getRedirectType(), recTp, block, lang);
        }
        /** 添加副标题跳转数据 */
        if (StringUtils.isNotBlank(recTp.getContentSubName())) {
            // 如果没有副标题，不用填充跳转
            block.setBlockSubName(recTp.getContentSubName());
            block.setRectSubCid(recTp.getRedirectSubCid());
            block.setRectSubPageId(recTp.getRedirectSubPageId());
            block.setRectSubType(recTp.getRedirectSubType());
            block.setRectSubUrl(recTp.getRedirectSubUrl());
            block.setRectSubVt(recTp.getRedirectSubVideoType());

            // 设置副标题跳转
            if (!isNativeSubject) {
                // native专题的标题不会跳转
                setSubTitleRedirect(recTp.getRedirectType(), recTp, block, lang);
            } else {
                // native专题的副标题填充的是专题开始时间
                block.setBlockSubName("上映倒计时");
                block.setNativeSubjectStartTime(TimeUtil.string2timestamp(recTp.getContentSubName()));
            }

        }

        return block;
    }

    private void setMainTitleRedirect(String redirectType, RecommendTpResponse recTp, ChannelBlock block, String lang) {
        if (redirectType == null) {
            return;
        }
        switch (redirectType) {
        case ChannelBlock.CHANNLE_BLOCK_SKIP_PAGE:
            // 跳转频道页，拼接跳转url
            Integer skipCid = null;
            if (StringUtils.isNotBlank(recTp.getRedirectCid())) {
                try {
                    skipCid = Integer.parseInt(recTp.getRedirectCid());
                    block.setRectCName(MessageUtils.getMessage(ChannelSkip.CHANNEL_MAP.get(skipCid), lang));
                } catch (Exception e) {
                    log.error("parse redirect id:" + recTp.getRedirectCid());
                }
            }
            block.setDataUrl(ChannelSkip.getChannelSkipUrl(recTp.getRedirectPageId(), skipCid));

            break;
        case ChannelBlock.CHANNEL_BLOCK_STIP_FILTR:
            // 跳转筛选条件
            block.setRectField(this.parseRetrieve(recTp.getRedFieldTypeList(), recTp.getRedFieldDetailList()));
            break;
        default:
            break;
        }
    }

    private void setSubTitleRedirect(String redirectType, RecommendTpResponse recTp, ChannelBlock block, String lang) {
        if (redirectType == null) {
            return;
        }
        switch (redirectType) {
        case ChannelBlock.CHANNLE_BLOCK_SKIP_PAGE:
            // 跳转频道页，拼接跳转url
            Integer skipCid = null;
            if (StringUtils.isNotBlank(recTp.getRedirectSubCid()) && Ints.tryParse(recTp.getRedirectSubCid()) != null) {
                skipCid = Ints.tryParse(recTp.getRedirectSubCid());
                block.setRectSubCName(MessageUtils.getMessage(ChannelSkip.CHANNEL_MAP.get(skipCid), lang));
            }
            block.setDataSubUrl(ChannelSkip.getChannelSkipUrl(recTp.getRedirectSubPageId(), skipCid));

            break;
        case ChannelBlock.CHANNEL_BLOCK_STIP_FILTR:
            // 跳转筛选条件
            block.setRectSubField(this.parseRetrieve(recTp.getRedSubFieldTypeList(), recTp.getRedSubFieldDetailList()));

            break;
        default:
            break;
        }
    }

    /**
     * 解析跳转需要的筛选条件
     * @param fTypeList
     * @param fDetailList
     * @return
     */
    private List<ChannelBlock.RectRetrieve> parseRetrieve(String fTypeList, String fDetailList) {
        if (StringUtils.isBlank(fTypeList) || StringUtils.isBlank(fDetailList)) {
            return null;
        }
        // 跳转筛选，由于cms配置的筛选跳转和筛选条件对应不上，需要进行一个映射转化
        List<ChannelBlock.RectRetrieve> list = new ArrayList<ChannelBlock.RectRetrieve>();
        ChannelBlock.RectRetrieve retri = new ChannelBlock.RectRetrieve();
        retri.setRetrieveKey(ChannelSkip.CHANNEL_CMS_FILTER_MAP.get(fTypeList));
        retri.setRetrieveValue(fDetailList);
        list.add(retri);
        return list;
    }

    /**
     * 设置接口处理失败时的返回值
     * @param response
     *            返回的BaseResponse
     * @param errCode
     *            错误代码
     * @param langcode
     *            用户语言环境
     */
    private <T extends BaseResponse> T setErrorResponse(T response, String errCode, String langcode) {
        if (response != null) {
            response.setStatus(ErrorCodeConstants.RESPONSE_FAIL_CODE);
            response.setErrorCode(errCode);
            response.setErrorMessage(MessageUtils.getMessage(errCode, langcode));
        }
        return response;
    }

    /**
     * @param channelId
     */
    private List<ChannelNavigation> getNavFromMem(String channelId) {
        List<ChannelNavigation> navigationList = this.tpCacheTemplate.get(
                CacheConstants.channel.CHANNEL_PAGE_NAVIGATION + channelId,
                new TypeReference<List<ChannelNavigation>>() {
                });
        if (navigationList != null) {
            Collections.sort(navigationList, new Comparator<ChannelNavigation>() {
                @Override
                public int compare(ChannelNavigation o1, ChannelNavigation o2) {
                    return o1.getOrder() - o2.getOrder();// 按照order正序排列
                }
            });
        }
        return navigationList;
    }

    /***
     * 通过缓存的数据得到角标
     * @param cache
     *            缓存中的数据
     * @param dType
     *            推荐给的数据类型，专辑还是专辑中的视频
     * @return
     */
    public String fillCornerLabelByCache(PlayCache cache, String dType, String pageid) {
        return this.channelHelper.fillCornerLabelByCache(cache, dType, pageid);
    }

    /**
     * 视频角标类型
     * @return
     */
    public String getCornerLabel(AssistData aData) {
        return this.channelHelper.getCornerLabel(aData);
    }

    private ChannelFocus fillChannelTopForAlbum(PlayCache album, RatingAndPlayRankTp rank) {
        ChannelFocus focus = new ChannelFocus();
        focus.setAt(ChannelFocus.HOME_ACTION_TYPE_FULL);
        focus.setPid(album.getaId());
        focus.setName(StringUtils.isNotBlank(rank.getName()) ? rank.getName() : album.getaNameCn());
        focus.setSubTitle(StringUtils.isNotBlank(rank.getSubname()) ? rank.getSubname() : album.getaSubTitle());
        focus.setCid(album.getaCategoryId());
        focus.setType(ChannelConstant.DATA_TYPE_ALBUM);
        focus.setEpisode(String.valueOf(album.getaEpisode()));
        focus.setNowEpisodes(String.valueOf(album.getaNowEpisodes()));
        focus.setIsEnd(String.valueOf(album.getaIsEnd()));
        if (album.getaIsyuanxian() != null) {
            focus.setPay(String.valueOf(album.getaIsyuanxian()));
        }
        focus.setTag(album.getaTag());
        String pic = album.getAPic(320, 200);
        focus.setPic(pic);
        focus.setPicAll(this.getChannelImage(pic));
        focus.setSinger(album.getaStarring());
        focus.setAlbumType(String.valueOf(album.getaType()));
        focus.setReleaseDate(album.getaReleaseDate());
        focus.setArea(album.getaAreaName());
        try {
            if (StringUtils.isNotBlank(rank.getRating())) {
                Float score = Float.parseFloat(rank.getRating());
                focus.setScore(score);
            }

        } catch (Exception nfe) {
        }

        focus.setSubCategory(album.getaSubCategoryName());
        focus.setDirector(album.getaDirectory());
        focus.setPlayCount(rank.getPlaycount());
        focus.setSource(ChannelConstant.dataSource.DATA_SOURCE_TOP);
        Date date = album.getaUpdateTime();
        if (date != null) {
            focus.setUpdateTime(date.getTime());
        }
        focus.setCornerLabel(this.fillCornerLabelByCache(album, ChannelConstant.DATA_TYPE_ALBUM, null));
        return focus;
    }

    private ChannelFocus fillChannelTopForVideo(PlayCache video, RatingAndPlayRankTp rank) {
        ChannelFocus focus = new ChannelFocus();
        focus.setAt(ChannelFocus.HOME_ACTION_TYPE_FULL);
        focus.setVid(video.getvId());
        focus.setPid(video.getaId());

        focus.setName(StringUtils.isNotBlank(rank.getName()) ? rank.getName() : video.getvNameCn());
        focus.setSubTitle(video.getvSubTitle());
        focus.setCid(video.getvCategoryId());
        focus.setType(ChannelConstant.DATA_TYPE_VIDEO);
        focus.setTag(video.getvTag());
        String pic = video.getVPic(320, 200);
        focus.setPic(pic);
        focus.setPicAll(this.getChannelImage(pic));
        focus.setDuration(String.valueOf(video.getvDuration()));
        focus.setSinger(video.getvStarring());
        focus.setVideoType(String.valueOf(video.getvType()));
        focus.setSubCategory(video.getvSubCategoryName());
        focus.setDirector(video.getvDirectory());
        focus.setReleaseDate(video.getvReleaseDate());
        focus.setArea(video.getvAreaName());
        focus.setAlbumName(video.getaNameCn());// 专辑名称
        focus.setGuest(video.getvGuest());
        try {
            if (StringUtils.isNotBlank(rank.getRating())) {
                Float score = Float.parseFloat(rank.getRating());
                focus.setScore(score);
            }
            if (StringUtils.isNotEmpty(video.getvEpisode())) {
                focus.setIssue(Integer.valueOf(video.getvEpisode()));// 期数
            }

        } catch (Exception nfe) {
        }
        focus.setPlayCount(rank.getPlaycount());
        Date date = video.getvUpdateTime();
        if (date != null) {
            focus.setUpdateTime(date.getTime());
        }
        if (video.getaId() != null) {
            focus.setCornerLabel(this.fillCornerLabelByCache(video, ChannelConstant.DATA_TYPE_VIDEO, null));
        }
        return focus;
    }

    /***************************************************************** 重构接口 ******************************************/
    /**
     * 获取频道内容数据
     * @return
     */
    public Response<ChannelContentDto> getChannelData2(String history, CommonParam commonParam, String pageId,
            String cityLevel) {
        Response<ChannelContentDto> response = new Response<ChannelContentDto>();
        // 检查输入参数
        if (pageId == null) {
            return this.setErrorResponse(response, ChannelConstant.ERROR_CODE_CHANNEL_PARAM_ERROR,
                    commonParam != null ? commonParam.getLangcode() : null);
        }
        RecBaseRequest request = new RecBaseRequest();
        request.setPageid("page_cms" + pageId);
        request.setHistory(history);
        request.setLang(commonParam.getLangcode());
        request.setRegion(commonParam.getWcode());
        request.setUid(commonParam.getUid());
        request.setCitylevel(cityLevel);
        Map<String, RecommendTpResponse> tpResponse = this.facadeTpDao.getRecommendTpDao().getMultiBlocks(request); // 调用推荐接口获取数据
        ChannelContentDto dto;

        if (tpResponse != null && tpResponse.size() > 0) {
            // 填充板块数据
            dto = this.fillChannelBlock(tpResponse, pageId, request.getLang());
            if (dto != null) {
                response.setData(dto);
            } else {
                response = this.setErrorResponse(response, ChannelConstant.ERROR_CODE_CHANNEL_NO_DATA,
                        request != null ? request.getLang() : null);
            }
        } else {
            // 如果推荐取不到数据，走缓存
            String cache = tpCacheTemplate.get(CacheConstants.channel.CHANNEL_PAGE_DATA + pageId, String.class);
            if (StringUtils.isNotBlank(cache)) {
                try {
                    response = OBJECT_MAPPER.readValue(cache, new TypeReference<Response<ChannelContentDto>>() {
                    });
                    return response;
                } catch (Exception e) {

                }
            }
            response = this.setErrorResponse(response, ChannelConstant.ERROR_CODE_CHANNEL_NO_DATA,
                    request != null ? request.getLang() : null);
        }
        return response;
    }

    private ChannelContentDto fillChannelBlock(Map<String, RecommendTpResponse> tpResponse, String pageId, String lang) {
        ChannelContentDto dto = new ChannelContentDto();
        // 填充页面导航栏、焦点图和通用板块
        List<ChannelBlock> blockList = new ArrayList<>();
        List<RecommendTpResponse> validTpResponseList = new ArrayList<>();
        RecommendTpResponse recTpRes = null;

        List<String> cacheKeys = new ArrayList<>();
        for (int i = 1; i <= tpResponse.size(); i++) {
            recTpRes = tpResponse.get("rec_" + i);
            if (recTpRes != null && recTpRes.getRec() != null && recTpRes.getRec().size() > 0
                    && StringUtils.isNotBlank(recTpRes.getContentStyle())) {
                validTpResponseList.add(recTpRes);
                List<String> keys = this.getCacheDataKeys(recTpRes);
                if (keys != null && keys.size() > 0) {
                    cacheKeys.addAll(keys);
                }
            }
        }
        Map<String, PlayCache> caches = null;
        if (cacheKeys != null && cacheKeys.size() > 0) {
            caches = this.facadeService.getVideoService().mgetPlayCacheEntityByKeys(cacheKeys);
        }
        for (RecommendTpResponse recTp : validTpResponseList) {
            String contentStyle = recTp.getContentStyle();
            switch (contentStyle) {
            case ChannelConstant.contentStyle.CONTENT_SYTLE_NAVIGATION:
                List<ChannelNavigation> navigationList = this.getNavFromMem(pageId);
                dto.setNavigation(navigationList);
                break;
            case ChannelConstant.contentStyle.CONTENT_SYTLE_FOCUS:
                List<ChannelFocus> blockDatas = this.parseBlockData(recTp, pageId, caches);
                if (!CollectionUtils.isEmpty(blockDatas) && blockDatas.size() > 3) {
                    blockDatas = blockDatas.subList(0, 3);
                }
                dto.setFocus(blockDatas);
                break;
            case ChannelConstant.contentStyle.CONTENT_STYLE_LIVE_PIC:
            case ChannelConstant.contentStyle.CONTENT_STYLE_LIVE_LIST1:
            case ChannelConstant.contentStyle.CONTENT_STYLE_LIVE_LIST2:
            case ChannelConstant.contentStyle.CONTENT_STYLE_ATTENTION:
            case ChannelConstant.contentStyle.CONTENT_STYLE_ATTENTION_SLIP:
                // 直播数据
                ChannelBlock liveBlock = this.parseChannelBlockForCms(recTp, pageId, lang);
                if (liveBlock != null) {
                    blockList.add(liveBlock);
                }
                break;
            default:
                ChannelBlock block = this.parseChannelBlockForRec(recTp, lang);
                if (block != null) {
                    // 板块内填充数据
                    block.setList(this.parseBlockData(recTp, pageId, caches));
                    blockList.add(block);
                }
                break;
            }
        }
        if (blockList.size() > 0) {
            dto.setBlock(blockList);
        }
        // 填充大首页live，1.7.0版本之前的直播数据
        if (ChannelConstant.HOME_PAGE_ID.equalsIgnoreCase(pageId)) {
            ChannelLiveDto channelLiveDto = this.tpCacheTemplate.get(CacheConstants.channel.HOME_PAGE_LIVE,
                    ChannelLiveDto.class);
            if (channelLiveDto != null && channelLiveDto.getLive() != null
                    && channelLiveDto.getLive().size() == LiveConstants.HOME_PAGE_LIVE_COUNT) {
                dto.setLive(channelLiveDto);
            }
        }
        return dto;
    }

    /**
     * 从推荐数据中解析得到频道焦点图数据
     * @param recTp
     * @return
     */
    private List<ChannelFocus> parseBlockData(RecommendTpResponse recTp, String pageId, Map<String, PlayCache> caches) {
        List<ChannelFocus> focusList = new ArrayList<>();
        ChannelBlock block = null;
        for (RecData rec : recTp.getRec()) {
            // 领先版过滤
            if (StringUtils.isNotBlank(rec.getVersionPlatform()) && rec.getVersionPlatform().equalsIgnoreCase("10010")) {
                continue;
            }
            Integer type = channelHelper.getSkipType(rec, null);
            Map<String, Object> otherParams = new HashMap<>();
            otherParams.put("pageid", pageId);
            if (caches != null) {
                otherParams.put("caches", caches);
            }
            if (type != null) {
                for (ISkipPolicy policy : iSkipPolicies) {
                    block = policy.parseSkipPolicy(rec, type, otherParams);
                    if (block != null && block.getList() != null) {
                        focusList.addAll(block.getList());
                        break;
                    }
                }
            }
        }
        return focusList;
    }

    /***
     * 解析频道页中的直播模块数据
     * @param recTp
     * @return
     */
    private ChannelBlock parseChannelBlockForCms(RecommendTpResponse recTp, String pageId, String lang) {
        ChannelBlock blockData = this.parseChannelBlockForRec(recTp, lang);

        ChannelBlock block = null;
        Integer type = channelHelper.getSkipType(null, recTp.getContentStyle());
        Map<String, Object> otherParams = new HashMap<>();
        otherParams.put("pageid", pageId);
        otherParams.put("cms_num", recTp.getNum());
        if (type != null) {
            for (ISkipPolicy policy : iSkipPolicies) {
                if (recTp.getRec() != null && recTp.getRec().size() > 0) {
                    block = policy.parseSkipPolicy(recTp.getRec().get(0), type, otherParams);
                    if (block != null) {
                        blockData.setLiveList(block.getLiveList());
                        blockData.setLiveCount(block.getLiveCount());
                        blockData.setDataList(block.getDataList());
                        break;
                    }
                }
            }
        }
        return blockData;

    }

    /***
     * 获取推荐返回的专辑和视频缓存key
     * @param recTp
     * @return
     */
    private List<String> getCacheDataKeys(RecommendTpResponse recTp) {
        List<String> cacheKeys = new ArrayList<>();
        for (RecData rec : recTp.getRec()) {
            // 领先版过滤
            if (StringUtils.isNotBlank(rec.getVersionPlatform()) && rec.getVersionPlatform().equalsIgnoreCase("10010")) {
                continue;
            }
            Integer type = channelHelper.getSkipType(rec, null);
            if (type != null) {
                if (SkipTypeConstant.CMS_SKIP_ALBUM.equals(type) || SkipTypeConstant.REC_SKIP_ALBUM.equals(type)) {
                    // 专辑
                    String pid = rec.getPid() != null ? String.valueOf(rec.getPid()) : rec.getContent();
                    if (StringUtils.isNotBlank(pid)) {
                        cacheKeys.add(CacheConstants.PlayCacheEntity_A_ + pid);
                    }
                }
                if (SkipTypeConstant.CMS_SKIP_VIDEO.equals(type) || SkipTypeConstant.REC_SKIP_VIDEO.equals(type)) {
                    // 视频
                    String vid = rec.getVid() != null ? String.valueOf(rec.getVid()) : rec.getContent();
                    if (StringUtils.isNotBlank(vid)) {
                        cacheKeys.add(CacheConstants.PlayCacheEntity_V_ + vid);
                    }
                }
            }
        }
        return cacheKeys;
    }
}
