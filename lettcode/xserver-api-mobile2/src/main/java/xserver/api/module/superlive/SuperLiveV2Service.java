package xserver.api.module.superlive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import xserver.api.constant.DataConstants;
import xserver.api.constant.ErrorCodeConstants;
import xserver.api.module.BaseService;
import xserver.api.module.channel.ChannelConstant;
import xserver.api.module.superlive.dto.SuperLiveLiveCountDto;
import xserver.api.module.superlive.dto.SuperLiveLiveCountDto.LiveOrderInfo;
import xserver.api.module.superlive.dto.SuperLiveShareDto;
import xserver.api.module.superlive.dto.SuperLiveVoteInfoDto;
import xserver.api.module.superlive.dto.SuperLiveVoteInfoDto.VoteOption;
import xserver.api.module.superlive.request.CategoryDetailDataRequest;
import xserver.api.module.user.dto.UserLiveFavDto;
import xserver.common.cache.CbaseRmiClient;
import xserver.common.constant.SuperLiveDictionaryConstant;
import xserver.common.constant.SuperLiveV2Constant;
import xserver.common.dto.superlive.v2.DefualtStreamDto;
import xserver.common.dto.superlive.v2.SuperLiveCategoryDto;
import xserver.common.dto.superlive.v2.SuperLiveChannelDto;
import xserver.common.dto.superlive.v2.SuperLiveIndexPageDto;
import xserver.common.dto.superlive.v2.SuperLiveProgramDto;
import xserver.common.dto.superlive.v2.UserOrderChannelDto;
import xserver.common.request.CommonParam;
import xserver.common.response.PageResponse;
import xserver.common.response.Response;
import xserver.common.util.JsonUtil;
import xserver.lib.mysql.table.SuperLiveUserOrderDataMysqlTable;
import xserver.lib.tp.cms.response.CmsBlockContent;
import xserver.lib.tp.cms.response.CmsBlockTpResponse;
import xserver.lib.tp.user.request.UserFavRequest;
import xserver.lib.tp.user.request.UserFavRequest.UserFavOperaion;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.util.CalendarUtil;

import com.fasterxml.jackson.core.type.TypeReference;

@Component(value = "superLiveV2Service")
public class SuperLiveV2Service extends BaseService {
    private final Logger log = LoggerFactory.getLogger(SuperLiveV2Service.class);
    private CbaseRmiClient cbaseRmiClient = CbaseRmiClient.getInstance();

    private static Map<String, String> STAR_PHONE_MAP;

    /**
     * 是否为定制的明星列表
     * @param param
     * @return
     */
    private String getStarCustomize(CommonParam param) {
        if (param != null && StringUtils.isNotBlank(param.getDevId())) {
            if (STAR_PHONE_MAP == null) {
                Map<String, String> starMap = this.facadeTpDao.getSuperLiveTpDao().getStarPhone();
                if (starMap != null) {
                    STAR_PHONE_MAP = starMap;
                } else {
                    STAR_PHONE_MAP = new HashMap<String, String>();
                }
            }
            return STAR_PHONE_MAP.get(param.getDevId());
        }
        return null;
    }

    private void addStarToCategory(SuperLiveCategoryDto categoryDto, String star) {
        if (categoryDto != null && StringUtils.isNotBlank(star)) {
            String[] starids = star.split("-");
            if (starids != null) {
                List<SuperLiveChannelDto> starList = new LinkedList<SuperLiveChannelDto>();
                for (String starid : starids) {
                    SuperLiveChannelDto starChannel = tpCacheTemplate.get(CacheConstants.SUPERLIVE_CHANNEL_ID + starid,
                            SuperLiveChannelDto.class);
                    if (starChannel != null && starChannel.getCur() != null && starChannel.getStreams() != null
                            && starChannel.getStreams().size() > 0) {
                        starList.add(starChannel);
                    }
                }
                if (categoryDto.getChannelList() == null) {
                    categoryDto.setChannelList(starList);
                } else {
                    // 首先从原列表中去重，然后添加当前的定制明星到最前面
                    for (Iterator<SuperLiveChannelDto> it = categoryDto.getChannelList().iterator(); it.hasNext();) {
                        SuperLiveChannelDto slcd = it.next();
                        int index = findChannel(starList, slcd.getChannelId());
                        if (index != -1) {
                            it.remove();
                        }
                    }
                    categoryDto.getChannelList().addAll(0, starList);
                }
            }
        }
    }

    public Response<SuperLiveIndexPageDto> getSuperLiveHomePage(CommonParam param) {
        // TODO Auto-generated method stub
        Response<SuperLiveIndexPageDto> response = new Response<SuperLiveIndexPageDto>();
        long start = System.currentTimeMillis();
        SuperLiveIndexPageDto dto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_HOMEPAGE_DTO,
                SuperLiveIndexPageDto.class);
        log.info("getSuperLiveHomePage waste time ===" + (System.currentTimeMillis() - start) + " ms");
        // 明星手机定制特殊需求，相应的明星放在第一位
        start = System.currentTimeMillis();
        String star = getStarCustomize(param);
        if (dto != null && dto.getCategoryList() != null && StringUtils.isNotBlank(star)) {
            for (SuperLiveCategoryDto categoryDto : dto.getCategoryList()) {
                // 焦点和明星大类将对应的明星放在第一位
                if (SuperLiveDictionaryConstant.C_STAR.equals(categoryDto.getCategoryId())
                        || SuperLiveDictionaryConstant.C_FOCUS.equals(categoryDto.getCategoryId())) {
                    addStarToCategory(categoryDto, star);
                }
            }
        }
        log.info("getSuperLiveHomePageaaa waste time ===" + (System.currentTimeMillis() - start) + " ms");
        response.setData(dto);
        return response;
    }

    public PageResponse<SuperLiveCategoryDto> getSuperLiveCategoryHomePage(String categoryId, CommonParam param) {
        // TODO Auto-generated method stub
        String wcode = param != null ? param.getWcode() : DataConstants.WCODE_CN;
        String langcode = param != null ? param.getLangcode() : DataConstants.LANGCODE_CN;
        PageResponse<SuperLiveCategoryDto> response = new PageResponse<SuperLiveCategoryDto>();
        if (StringUtils.isBlank(categoryId) || !SuperLiveDictionaryConstant.TOP_CATEGORY.containsKey(categoryId)) {
            return response;
        }

        // SuperLiveCategoryDto placeHolder = new SuperLiveCategoryDto();
        // placeHolder.setPlaceHolder(true);

        List<SuperLiveCategoryDto> cgList = new LinkedList<SuperLiveCategoryDto>();
        SuperLiveCategoryDto categoryDto = null;
        if (SuperLiveDictionaryConstant.C_LIVE.equals(categoryId)) { // 直播的二级分类页，包含全部，体育分类，娱乐分类，音乐分类
            /*
             * 产品需求，直播大类下的二级分类，不再取详细的二级分类
             * for (Map<String, String> liveMap :
             * SuperLiveDictionaryConstant.LIVE_SUBCATEGORY) {
             * String cid = categoryId;
             * if (liveMap == SuperLiveDictionaryConstant.SPORT_SUBCATEGORY) {
             * cid += SuperLiveDictionaryConstant.C_SPORT_1;
             * } else if (liveMap ==
             * SuperLiveDictionaryConstant.ENT_SUBCATEGORY) {
             * cid += SuperLiveDictionaryConstant.C_ENT;
             * } else if (liveMap ==
             * SuperLiveDictionaryConstant.MUSIC_SUBCATEGORY) {
             * cid += SuperLiveDictionaryConstant.C_MUSIC;
             * } else if (liveMap ==
             * SuperLiveDictionaryConstant.DEF_SUBCATEGORY) {
             * cid += SuperLiveDictionaryConstant.C_LIVE;
             * }
             * for (Map.Entry<String, String> entry : liveMap.entrySet()) {
             * categoryDto = new SuperLiveCategoryDto();
             * categoryDto.setCategoryId(entry.getKey());
             * categoryDto.setParentCgId(cid);
             * categoryDto.setCategoryName(entry.getValue());
             * categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_LIVE
             * );
             * cgList.add(categoryDto);
             * }
             * // 每种分类下之间有间隔符，告知客户端进行间隔
             * // cgList.add(placeHolder);
             * }
             */

            // 产品需求，直播大类下的二级分类包括，全部直播、体育下的所有二级分类、娱乐、音乐

            // 全部直播
            categoryDto = new SuperLiveCategoryDto();
            categoryDto.setCategoryId(SuperLiveDictionaryConstant.LIVE_SUBCATEGORY_ALL);
            categoryDto.setParentCgId(SuperLiveDictionaryConstant.C_LIVE + categoryId);
            categoryDto.setCategoryName(SuperLiveDictionaryConstant.DEF_SUBCATEGORY
                    .get(SuperLiveDictionaryConstant.LIVE_SUBCATEGORY_ALL));
            categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_LIVE);
            cgList.add(categoryDto);

            // 体育二级分类
            for (Map.Entry<String, String> entry : SuperLiveDictionaryConstant.SPORT_SUBCATEGORY.entrySet()) {
                categoryDto = new SuperLiveCategoryDto();
                categoryDto.setParentCgId(SuperLiveDictionaryConstant.C_LIVE + SuperLiveDictionaryConstant.C_SPORT_1);
                categoryDto.setCategoryId(entry.getKey());
                categoryDto.setCategoryName(entry.getValue());
                categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_LIVE);
                cgList.add(categoryDto);
            }

            // 音乐直播
            categoryDto = new SuperLiveCategoryDto();
            categoryDto.setCategoryId(SuperLiveDictionaryConstant.L_LIVE);
            categoryDto.setParentCgId(SuperLiveDictionaryConstant.C_MUSIC);
            categoryDto.setCategoryName("音乐");
            categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_LIVE);
            cgList.add(categoryDto);

            // 娱乐直播
            categoryDto = new SuperLiveCategoryDto();
            categoryDto.setCategoryId(SuperLiveDictionaryConstant.L_LIVE);
            categoryDto.setParentCgId(SuperLiveDictionaryConstant.C_ENT);
            categoryDto.setCategoryName("娱乐");
            categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_LIVE);
            cgList.add(categoryDto);

            // 资讯直播
            categoryDto = new SuperLiveCategoryDto();
            categoryDto.setCategoryId(SuperLiveDictionaryConstant.L_LIVE);
            categoryDto.setParentCgId(SuperLiveDictionaryConstant.C_INFORMATION);
            categoryDto.setCategoryName("资讯");
            categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_LIVE);// 所有直播的数据类型统一
            cgList.add(categoryDto);

            // 游戏直播
            categoryDto = new SuperLiveCategoryDto();
            categoryDto.setCategoryId(SuperLiveDictionaryConstant.L_LIVE);
            categoryDto.setParentCgId(SuperLiveDictionaryConstant.C_GAME);
            categoryDto.setCategoryName("游戏");
            categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_LIVE);// 所有直播的数据类型统一
            cgList.add(categoryDto);

            // 品牌直播
            categoryDto = new SuperLiveCategoryDto();
            categoryDto.setCategoryId(SuperLiveDictionaryConstant.L_LIVE);
            categoryDto.setParentCgId(SuperLiveDictionaryConstant.C_BRAND);
            categoryDto.setCategoryName("品牌");
            categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_LIVE);// 所有直播的数据类型统一
            cgList.add(categoryDto);

            // 其他直播
            categoryDto = new SuperLiveCategoryDto();
            categoryDto.setCategoryId(SuperLiveDictionaryConstant.L_LIVE);// 与娱乐，音乐直播categoryId一致
            categoryDto.setParentCgId(SuperLiveDictionaryConstant.C_OTHER);//
            categoryDto.setCategoryName("其他");// 写死
            categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_LIVE);// 所有直播的数据类型统一
            cgList.add(categoryDto);

        } else if (SuperLiveDictionaryConstant.C_SPORT_1.equals(categoryId)
                || SuperLiveDictionaryConstant.C_SPORT_2.equals(categoryId)
                || SuperLiveDictionaryConstant.C_SPORT_3.equals(categoryId)
                || SuperLiveDictionaryConstant.C_SPORT_1_YINGCHAO.equals(categoryId)) {// 体育足球，体育高网，体育综合大类的二级分类页
            for (Map.Entry<String, String> entry : SuperLiveDictionaryConstant.SPORT_LABEL.entrySet()) {
                categoryDto = new SuperLiveCategoryDto();
                categoryDto.setCategoryId(entry.getKey());
                categoryDto.setCategoryName(entry.getValue());
                categoryDto.setParentCgId(categoryId);
                if (SuperLiveDictionaryConstant.L_LIVE.equals(entry.getKey())) { // 如果是直播标签
                    // 暂时只有大陆有直播数据
                    if (DataConstants.WCODE_HK.equals(wcode)) {
                        continue;
                    }
                    categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_LIVE);
                } else {
                    categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_CHANNEL);
                }
                cgList.add(categoryDto);
            }
            // cgList.add(placeHolder);

            List<SuperLiveCategoryDto> selfSubCategoryList = tpCacheTemplate.get(
                    CacheConstants.SUPERLIVE_SUBCATEGORY_LIST_ + categoryId,
                    new TypeReference<List<SuperLiveCategoryDto>>() {
                    });
            if (selfSubCategoryList != null && selfSubCategoryList.size() > 0) {
                cgList.addAll(selfSubCategoryList);
            }

        } else { // 普通二级分类页面

            final List<SuperLiveCategoryDto> categoryList = SuperLiveDictionaryConstant.COMMON_LABLE_MAP
                    .get(categoryId);
            if (categoryList != null) {
                for (SuperLiveCategoryDto label : categoryList) {
                    categoryDto = new SuperLiveCategoryDto();
                    categoryDto.setCategoryId(label.getCategoryId());
                    categoryDto.setCategoryName(label.getCategoryName());
                    categoryDto.setParentCgId(categoryId);
                    if (SuperLiveDictionaryConstant.L_LIVE.equals(label.getCategoryId())) { // 如果是直播标签
                        if (DataConstants.WCODE_HK.equals(wcode)) {
                            continue;
                        }
                        categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_LIVE);
                    } else {
                        categoryDto.setDataType(SuperLiveV2Constant.SUPERLIVE_DATA_TYPE_CHANNEL);
                    }
                    cgList.add(categoryDto);

                    if (SuperLiveDictionaryConstant.L_REC.equals(label.getCategoryId())) { // 如果是推荐，则从缓存中拉取推荐数据
                        // SuperLiveCategoryDto recCategory =
                        // tpCacheTemplate.get(
                        // CacheConstants.SUPERLIVE_CMS_CATEGORY_DATA + "_" +
                        // categoryId,
                        // SuperLiveCategoryDto.class);
                        SuperLiveCategoryDto recCategory = tpCacheTemplate.get(CacheConstants.SUPERLIVE_CMS_REC_DATA
                                + "_" + categoryId, SuperLiveCategoryDto.class);
                        if (recCategory != null && recCategory.getChannelList() != null) {
                            categoryDto.setChannelList(recCategory.getChannelList());
                        }
                        // 特殊定制需求，明星手机添加到第一位
                        String star = getStarCustomize(param);
                        if (StringUtils.isNotBlank(star)
                                && (SuperLiveDictionaryConstant.C_STAR.equals(categoryId) || SuperLiveDictionaryConstant.C_FOCUS
                                        .equals(categoryId))) {
                            addStarToCategory(categoryDto, star);
                        }
                    }
                }
            }

            List<SuperLiveCategoryDto> selfSubCategoryList = tpCacheTemplate.get(
                    CacheConstants.SUPERLIVE_SUBCATEGORY_LIST_ + categoryId,
                    new TypeReference<List<SuperLiveCategoryDto>>() {
                    });
            if (selfSubCategoryList != null && selfSubCategoryList.size() > 0) {
                cgList.addAll(selfSubCategoryList);
            }
        }
        response.setData(cgList);
        return response;
    }

    /**
     * 获取频道二级分类数据
     * @param param
     * @param request
     * @return
     */
    public Response<List<SuperLiveChannelDto>> getCategoryDataList(CommonParam param, CategoryDetailDataRequest request) {
        // TODO Auto-generated method stub
        Response<List<SuperLiveChannelDto>> response = new Response<List<SuperLiveChannelDto>>();
        if (request == null || StringUtils.isBlank(request.getCid()) || StringUtils.isBlank(request.getSubCid())) {
            response.setErrorCode(ErrorCodeConstants.SUPERLIVE_PARAM_ERROR);
            response.setStatus(0);
            return response;
        }

        List<SuperLiveChannelDto> allChannels = null;
        if (request.getCid().startsWith(SuperLiveDictionaryConstant.C_LIVE)) { // 直播大类
            request.setCid(request.getCid().replaceFirst(SuperLiveDictionaryConstant.C_LIVE, ""));
            allChannels = getLiveSubCategoryChannel(param, request);
        } else {
            if (SuperLiveDictionaryConstant.L_LIVE.equals(request.getSubCid())) { // 大类直播标签数据
                allChannels = tpCacheTemplate.get(
                        CacheConstants.SUPERLIVE_LIVEROOM_TOP_CATEGORY_PORGRAMS_ + request.getCid(),
                        new TypeReference<List<SuperLiveChannelDto>>() {
                        });
            } else { // 轮播,电视台,普通二级分类的数据
                String key = CacheConstants.buildKey(CacheConstants.SUPERLIVE_SUBTYPE_CHANNELLIST_, request.getCid(),
                        request.getSubCid());
                // 卫视频道的地方台标签数据，取地方台频道的全部数据
                if (SuperLiveDictionaryConstant.C_WS.equals(request.getCid())
                        && SuperLiveDictionaryConstant.L_DIFANG.equals(request.getSubCid())) {
                    key = CacheConstants.buildKey(CacheConstants.SUPERLIVE_SUBTYPE_CHANNELLIST_,
                            SuperLiveDictionaryConstant.C_DF, SuperLiveDictionaryConstant.L_WEISHI);
                }
                allChannels = tpCacheTemplate.get(key, new TypeReference<List<SuperLiveChannelDto>>() {
                });
            }
        }

        // 处理分页逻辑
        if (allChannels != null && allChannels.size() > 0) {
            int pageSize = (request.getSize() == null) ? 18 : request.getSize();
            int total = allChannels.size();
            List<SuperLiveChannelDto> channelList = null;

            if (StringUtils.isBlank(request.getValue())) { // 取第一页的数据
                int minSize = (pageSize < total) ? pageSize : total;
                channelList = allChannels.subList(0, minSize);
            } else {
                // 找到当前客户端最有一个频道，然后取后续的数据
                int index = findChannel(allChannels, request.getValue());
                if (index != -1) {
                    int end = index;
                    if ("0".equals(request.getDirection())) { // 向上取数据
                        end = end - pageSize;
                        if (end < 0) {
                            end = 0;
                        }
                        channelList = allChannels.subList(end, index);
                    } else {
                        end = end + pageSize;
                        if (end > total) {
                            end = total;
                        }
                        channelList = allChannels.subList(index + 1, end);
                    }

                }
            }

            response.setData(channelList);
        }

        return response;
    }

    /**
     * 查找对应频道在列表中的位置
     * @param channelList
     * @param channelId
     * @return
     */
    private int findChannel(List<SuperLiveChannelDto> channelList, String channelId) {
        int index = -1;
        if (channelList != null && StringUtils.isNotBlank(channelId)) {
            for (int i = 0; i < channelList.size(); i++) {
                if (channelId.equals(channelList.get(i).getChannelId())) {
                    return i;
                }
            }
        }

        return index;
    }

    private List<SuperLiveChannelDto> getLiveSubCategoryChannel(CommonParam param, CategoryDetailDataRequest request) {
        List<SuperLiveChannelDto> channelList = null;
        if (request != null && StringUtils.isNotBlank(request.getCid()) && StringUtils.isNotBlank(request.getSubCid())) {
            if (SuperLiveDictionaryConstant.LIVE_SUBCATEGORY_ALL.equals(request.getSubCid())) { // 全部数据
                channelList = tpCacheTemplate.get(CacheConstants.SUPERLIVE_LIVEROOM_PORGRAMS_ALL,
                        new TypeReference<List<SuperLiveChannelDto>>() {
                        });
            } else { // 具体分类的直播
                // key由 "直播基本key + 大类id + 子分类id" 构成
                String key = CacheConstants.buildKey(CacheConstants.SUPERLIVE_LIVEROOM_SUB_CATEGORY_PORGRAMS_,
                        request.getCid(), request.getSubCid());
                channelList = tpCacheTemplate.get(key, new TypeReference<List<SuperLiveChannelDto>>() {
                });
            }
        }

        return channelList;
    }

    public Response<DefualtStreamDto> getDefaultStreams(CommonParam param) {
        Response<DefualtStreamDto> response = new Response<DefualtStreamDto>();
        response.setData(SuperLiveV2Constant.DEFUALT_STREAM_DTO);
        return response;
    }

    public boolean addUserChannel(String uid, String categoryId, String channelId) {
        UserOrderChannelDto userOrderChannelDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_USER_CHANNEL_ORDER_
                + uid, UserOrderChannelDto.class);
        boolean isNew = true;
        if (userOrderChannelDto == null || userOrderChannelDto.getData() == null) {
            userOrderChannelDto = new UserOrderChannelDto();
            userOrderChannelDto.setUid(uid);
            Map<String, String> map = new HashMap<String, String>();
            userOrderChannelDto.setData(map);
            map.put(categoryId, channelId);
        } else {
            isNew = false;
            // 此处未添加排重
            String value = userOrderChannelDto.getData().get(categoryId);
            if (value == null || value.trim().length() == 0) {
                value = channelId;
            } else {
                value = channelId + "-" + value;
                String[] cids = value.split("-");
                if (cids.length > 20) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 20; i++) {
                        sb.append(cids[i]).append("-");
                    }
                    value = sb.substring(0, sb.length() - 1);
                }
            }
            userOrderChannelDto.getData().put(categoryId, value);
        }
        // 1.更新mysql,以mysql为准,数据库中没有更新成功，则认为失败。理由：数据失败、缓存成功、数据不可追溯
        SuperLiveUserOrderDataMysqlTable superLiveUserOrderDataMysqlTable = null;
        try {
            superLiveUserOrderDataMysqlTable = new SuperLiveUserOrderDataMysqlTable(userOrderChannelDto.getUid(),
                    JsonUtil.OBJECT_MAPPER.writeValueAsString(userOrderChannelDto.getData()));
        } catch (Exception e) {
            log.error("user order data convert error", e);
        }
        // if (isNew) {
        // if (superLiveUserOrderDataMysqlTable != null) {
        // facadeLeadMysqlDao.getSuperLiveUserOrderDataDaoMysqlDao().insert(superLiveUserOrderDataMysqlTable);
        // } else {
        // return false;
        // }
        // } else {
        // if (superLiveUserOrderDataMysqlTable != null) {
        // facadeLeadMysqlDao.getSuperLiveUserOrderDataDaoMysqlDao().update(superLiveUserOrderDataMysqlTable);
        // } else {
        // return false;
        // }
        // }
        // 2.更新cbase
        cbaseRmiClient.updateSync(CacheConstants.SUPERLIVE_USER_CHANNEL_ORDER_ + uid, userOrderChannelDto);
        return true;
    }

    /**
     * 获取用户定制列表
     * @param commonParam
     * @return
     */
    public xserver.common.response.Response<List<SuperLiveChannelDto>> listUserChannel(CommonParam commonParam) {
        Response<List<SuperLiveChannelDto>> response = new Response<List<SuperLiveChannelDto>>();
        List<SuperLiveChannelDto> list = new ArrayList<SuperLiveChannelDto>();
        response.setData(list);
        UserOrderChannelDto userOrderChannelDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_USER_CHANNEL_ORDER_
                + commonParam.getUid(), UserOrderChannelDto.class);
        if (userOrderChannelDto == null || userOrderChannelDto.getData() == null
                || userOrderChannelDto.getData().isEmpty()) {
            return response;
        }
        Collection<String> c = userOrderChannelDto.getData().values();

        for (String channelIds : c) {
            String[] cids = channelIds.split("-");
            if (cids != null) {
                for (String s : cids) {
                    SuperLiveChannelDto channelDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_CHANNEL_ID + s,
                            SuperLiveChannelDto.class);
                    if (channelDto != null && channelDto.getCur() != null) {
                        list.add(channelDto);
                    }
                }
            }
        }
        return response;
    }

    public Response<List<SuperLiveChannelDto>> getDetails(String ids) {
        Response<List<SuperLiveChannelDto>> response = new Response<List<SuperLiveChannelDto>>();
        List<SuperLiveChannelDto> list = new ArrayList<SuperLiveChannelDto>();
        response.setData(list);
        if (ids == null || ids.length() == 0) {
            return response;
        }
        String[] type_ids = ids.split(",");
        for (String id : type_ids) {
            String[] tid = id.split("_");
            if (tid.length == 2) {
                SuperLiveChannelDto channelDto = null;
                if ("2".equals(tid[0])) {
                    channelDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_LIVEROOM_LIVEID + tid[1],
                            SuperLiveChannelDto.class);
                }
                if ("3".equals(tid[0]) || "4".equals(tid[0])) {
                    channelDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_CHANNEL_ID + tid[1],
                            SuperLiveChannelDto.class);
                }
                if (channelDto != null) {
                    list.add(channelDto);
                }
            }
        }
        return response;
    }

    public boolean refreshStarPhone() {
        Map<String, String> starMap = this.facadeTpDao.getSuperLiveTpDao().getStarPhone();
        // starMap为null时，数据获取失败，此时不更新
        if (starMap != null) {
            STAR_PHONE_MAP = starMap;
            return true;
        }

        return false;
    }

    /**
     * 获取分享地址
     * @param channelid
     *            轮播卫视台直播的id
     * @param type
     *            2:直播 3:轮播 4:卫视
     * @return
     */
    public Response<SuperLiveShareDto> getShareUrl(String channelid, String type) {
        Response<SuperLiveShareDto> response = new Response<SuperLiveShareDto>();
        if (StringUtils.isNotBlank(channelid) && StringUtils.isNotBlank(type)) {
            SuperLiveShareDto shareDto = new SuperLiveShareDto();
            response.setData(shareDto);
            shareDto.setChannelId(channelid);
            shareDto.setType(type);
            String shareUrl = SuperLiveConstant.SUPERLIVE_SHARE_URL_DEFAULT;
            SuperLiveChannelDto channelDto = null;
            if (SuperLiveConstant.LIVE_TYPE_LIVEROOM.equals(type)) { // 直播
                channelDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_LIVEROOM_LIVEID + channelid,
                        SuperLiveChannelDto.class);
                if (channelDto != null && StringUtils.isNotBlank(channelDto.getLiveType())) {
                    String tmpUrl = SuperLiveConstant.SUPERLIVE_SHARE_LIVE_MAP.get(channelDto.getLiveType());
                    if (tmpUrl != null) {
                        shareUrl = tmpUrl.replace(SuperLiveConstant.SUPERLIVE_SHARE_URL_REPLACE,
                                channelDto.getChannelId());
                    }
                }
            } else if (SuperLiveConstant.LIVE_TYPE_CHANNEL_LUNBO.equals(type)) { // 轮播
                channelDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_CHANNEL_ID + channelid,
                        SuperLiveChannelDto.class);
                if (channelDto != null) {
                    shareUrl = SuperLiveConstant.SUPERLIVE_SHARE_URL_LUNBO.replace(
                            SuperLiveConstant.SUPERLIVE_SHARE_URL_REPLACE, channelDto.getChannelId());
                }
            } else if (SuperLiveConstant.LIVE_TYPE_CHANNEL_WEISHI.equals(type)) { // 卫视台
                channelDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_CHANNEL_ID + channelid,
                        SuperLiveChannelDto.class);
                if (channelDto != null) {
                    shareUrl = SuperLiveConstant.SUPERLIVE_SHARE_URL_WEISHI.replace(
                            SuperLiveConstant.SUPERLIVE_SHARE_URL_REPLACE, channelDto.getChannelId());
                }
            }

            shareDto.setShareUrl(shareUrl);
        }
        return response;

    }

    /**
     * 根据id获取首页分类数据，多个id之间使用,隔开
     * @param cids
     * @return
     */
    public Response<SuperLiveIndexPageDto> getHomePageDataByCateogryId(String cids) {
        Response<SuperLiveIndexPageDto> response = new Response<SuperLiveIndexPageDto>();

        if (StringUtils.isNotBlank(cids)) {
            String[] cidArray = cids.split(",");
            if (cidArray != null && cidArray.length > 0) {
                List<String> cidList = Arrays.asList(cidArray);
                SuperLiveIndexPageDto homePageDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_HOMEPAGE_DTO,
                        SuperLiveIndexPageDto.class); // 获取首页数据
                if (homePageDto != null && homePageDto.getCategoryList() != null
                        && homePageDto.getCategoryList().size() > 0) {
                    SuperLiveIndexPageDto dto = new SuperLiveIndexPageDto();
                    List<SuperLiveCategoryDto> categoryList = new LinkedList<SuperLiveCategoryDto>();
                    dto.setCategoryList(categoryList);

                    List<SuperLiveCategoryDto> allCategorys = homePageDto.getCategoryList();
                    for (SuperLiveCategoryDto category : allCategorys) {
                        if (category != null && category.getCategoryId() != null
                                && cidList.contains(category.getCategoryId())) {
                            categoryList.add(category);
                        }
                    }

                    response.setData(dto);
                } else {
                    log.warn("get homepage data failure from cache");
                }
            } else {
                log.warn("invalid cids[" + cids + "]");
            }
        }

        return response;
    }

    /**
     * 获取直播投票信息
     * @return
     */
    public Response<SuperLiveVoteInfoDto> getLiveVoteInfo(String liveId) {
        Response<SuperLiveVoteInfoDto> response = new Response<SuperLiveVoteInfoDto>();
        SuperLiveVoteInfoDto voteInfoDto = new SuperLiveVoteInfoDto();

        if (StringUtils.isNotBlank(liveId)) {
            CmsBlockContent liveBlock = null;

            // 获取直播投票版块数据，版块内包含所有拥有投票的直播
            CmsBlockTpResponse voteBlcokTpResponse = this.facadeTpDao.getCmsTpDao().getCmsBlock(
                    SuperLiveConstant.SUPERLIVE_VOTE_BLOCK_ID);
            if (voteBlcokTpResponse != null && voteBlcokTpResponse.getBlockContent() != null
                    && voteBlcokTpResponse.getBlockContent().size() > 0) {
                // 从直播列表中查找当前直播
                for (CmsBlockContent cmsBlock : voteBlcokTpResponse.getBlockContent()) {
                    if (ChannelConstant.CMS_TYPE_LIVE.equals(cmsBlock.getType())
                            && liveId.equals(cmsBlock.getContent())) {
                        liveBlock = cmsBlock;
                        break;
                    }
                }
            }

            /**
             * 当前直播存在投票
             * CMS tilte中存储投票选项的简略信息
             * CMS subTitle中存储投票选项列表的版块id
             * CMS position中存储用户中心投票的id
             */
            if (liveBlock != null && StringUtils.isNotBlank(liveBlock.getTitle())
                    && StringUtils.isNotBlank(liveBlock.getSubTitle())
                    && StringUtils.isNotBlank(liveBlock.getPosition())) {
                voteInfoDto.setChannelId(liveId);
                voteInfoDto.setGuideTitle(liveBlock.getTag()); // tag中存储投票引导文案
                // voteInfoDto.setGuidePic(liveBlock.getPic1()); // pic1中存储投票引导图
                voteInfoDto.setVoteTitle(liveBlock.getTagUrl()); // tagUrl中存储投票的标题
                List<String> voteList = Arrays.asList(liveBlock.getTitle().split(",")); // 本场有效的投票选项列表
                // 获取投票选项的总列表
                CmsBlockTpResponse optionTpResponse = this.facadeTpDao.getCmsTpDao().getCmsBlock(
                        liveBlock.getSubTitle().trim());
                if (optionTpResponse != null && optionTpResponse.getBlockContent() != null
                        && optionTpResponse.getBlockContent().size() > 0) {
                    List<VoteOption> optionList = new LinkedList<SuperLiveVoteInfoDto.VoteOption>();
                    voteInfoDto.setOptions(optionList);
                    for (CmsBlockContent block : optionTpResponse.getBlockContent()) {
                        // 如有当前投票选项属于本场直播
                        if (StringUtils.isNotBlank(block.getContent()) && voteList.contains(block.getContent())) {
                            VoteOption option = new VoteOption();
                            // 选项的id由"投票id_cmsid"拼接而成
                            option.setOptId(liveBlock.getPosition().trim() + "_" + block.getId());
                            option.setOptTitle(block.getTitle());
                            option.setOptPic(block.getPic1());
                            optionList.add(option);
                        }
                    }
                }
            }
        }

        if (voteInfoDto.getOptions() != null && voteInfoDto.getOptions().size() > 0) {
            response.setData(voteInfoDto);
        }

        return response;
    }

    /**
     * 获取用户直播收藏，以直播大类进行分类
     * @param token
     * @return
     */
    public Response<Map<String, List<SuperLiveChannelDto>>> getLiveFavByCategory(String token) {
        Response<Map<String, List<SuperLiveChannelDto>>> response = new Response<Map<String, List<SuperLiveChannelDto>>>();
        final int categoryMax = 2; // 每个分类最多取两个收藏
        if (StringUtils.isNotBlank(token)) {
            UserFavRequest request = new UserFavRequest(UserFavOperaion.LIST);
            request.setPage(1);
            request.setPagesize(100);
            request.setSso_tk(token);
            request.setFavorite_type(UserFavRequest.FAVORITE_TYPE_LUNBO + "," + UserFavRequest.FAVORITE_TYPE_WEISHI);
            xserver.api.response.PageResponse<UserLiveFavDto> favResponse = this.facadeService.getUserService()
                    .getLiveFavList(request);
            if (favResponse != null && favResponse.getData() != null && favResponse.getData().size() > 0) {
                Collection<UserLiveFavDto> favList = favResponse.getData();
                Map<String, List<SuperLiveChannelDto>> favCategoryMap = new HashMap<String, List<SuperLiveChannelDto>>();
                for (UserLiveFavDto favDto : favList) {
                    if (StringUtils.isNotBlank(favDto.getCid()) && favDto.getChannel() != null) {
                        List<SuperLiveChannelDto> channelList = favCategoryMap.get(favDto.getCid());
                        if (channelList == null) {
                            channelList = new ArrayList<SuperLiveChannelDto>();
                            favCategoryMap.put(favDto.getCid(), channelList);
                        }
                        if (channelList.size() < categoryMax) {
                            favDto.getChannel().setFav("1");
                            channelList.add(favDto.getChannel());
                        }
                    }
                }
                response.setData(favCategoryMap);
            }
        }
        return response;

    }

    /**
     * 获取当日直播数及直播顺序
     * @return
     */
    public Response<SuperLiveLiveCountDto> getLiveCountInfo() {
        Response<SuperLiveLiveCountDto> response = new Response<SuperLiveLiveCountDto>();
        SuperLiveLiveCountDto superLiveLiveCountDto = new SuperLiveLiveCountDto();

        List<SuperLiveChannelDto> allLives = tpCacheTemplate.get(CacheConstants.SUPERLIVE_LIVEROOM_PORGRAMS_ALL,
                new TypeReference<List<SuperLiveChannelDto>>() {
                });
        List<SuperLiveChannelDto> todayLives = new ArrayList<SuperLiveChannelDto>();
        if (allLives != null && allLives.size() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long todayStartTime = calendar.getTimeInMillis();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            long todayEndTime = calendar.getTimeInMillis();
            // 筛选当日直播
            for (SuperLiveChannelDto s : allLives) {
                if (s.getCur() != null) {
                    String status = s.getCur().getStatus();
                    if ("1".equals(status)) {// 未开始，判断开始时间在不在当日
                        long playTime = CalendarUtil
                                .parseDate(s.getCur().getPlayTime(), CalendarUtil.SHORT_DATE_FORMAT).getTime();
                        if (playTime >= todayStartTime && playTime < todayEndTime) {
                            todayLives.add(s);
                        }
                    } else if ("3".equals(status)) {// 回看，判断结束时间在不在当日
                        long endTime = CalendarUtil.parseDate(s.getCur().getEndTime(), CalendarUtil.SHORT_DATE_FORMAT)
                                .getTime();
                        if (endTime >= todayStartTime && endTime < todayEndTime) {
                            todayLives.add(s);
                        }
                    } else {// 直播中，直接塞到list中
                        todayLives.add(s);
                    }
                }
            }
        } else {
            superLiveLiveCountDto.setLiveCount(0);
            response.setData(superLiveLiveCountDto);
            return response;
        }
        if (todayLives != null && todayLives.size() > 0) {

            Collections.sort(todayLives, new Comparator<SuperLiveChannelDto>() {
                @Override
                public int compare(SuperLiveChannelDto o1, SuperLiveChannelDto o2) {
                    // TODO Auto-generated method stub
                    if (o1 != null && o1.getCur() != null && o2 != null && o2.getCur() != null) {
                        SuperLiveProgramDto program1 = o1.getCur();
                        SuperLiveProgramDto program2 = o2.getCur();
                        try {
                            // 直播开始时间
                            long beginTime1 = CalendarUtil.parseDate(program1.getPlayTime(),
                                    CalendarUtil.SIMPLE_DATE_FORMAT).getTime();
                            long beginTime2 = CalendarUtil.parseDate(program2.getPlayTime(),
                                    CalendarUtil.SIMPLE_DATE_FORMAT).getTime();

                            if (beginTime1 > beginTime2) {
                                return 1;
                            } else if (beginTime1 < beginTime2) {
                                return -1;
                            }

                            // 权重高的排在前面
                            if (o1.getWeight() != null && o2.getWeight() != null
                                    && o1.getWeight().intValue() != o2.getWeight().intValue()) {
                                return o2.getWeight() - o1.getWeight();
                            } else {
                                return 0;
                            }

                        } catch (Exception e) {
                            log.error("sort live error", e);
                        }
                        return 0;
                    } else if (o1 == null || o1.getCur() == null) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });

        } else {
            superLiveLiveCountDto.setLiveCount(0);
            response.setData(superLiveLiveCountDto);
            return response;
        }

        List<LiveOrderInfo> lstLiveOrderInfo = new ArrayList<LiveOrderInfo>();
        int i = 1;
        for (SuperLiveChannelDto s : todayLives) {
            LiveOrderInfo liveOrderInfo = new LiveOrderInfo();
            liveOrderInfo.setLiveId(s.getChannelId());
            liveOrderInfo.setLiveOrder(i++);
            lstLiveOrderInfo.add(liveOrderInfo);
        }
        superLiveLiveCountDto.setLiveCount(lstLiveOrderInfo.size());
        if (lstLiveOrderInfo != null && lstLiveOrderInfo.size() > 0) {
            superLiveLiveCountDto.setLiveOrderInfo(lstLiveOrderInfo);
        }
        response.setData(superLiveLiveCountDto);
        return response;

    }

}
