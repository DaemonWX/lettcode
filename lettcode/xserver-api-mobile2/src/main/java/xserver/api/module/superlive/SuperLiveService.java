package xserver.api.module.superlive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import xserver.api.constant.ErrorCodeConstants;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.superlive.dto.CategoryChannelIdsDto;
import xserver.api.module.superlive.dto.ChannelDto;
import xserver.api.module.superlive.dto.LiveChannelCurrentStateDataDto;
import xserver.api.module.superlive.dto.ProgramDto;
import xserver.api.module.superlive.dto.SuperLiveCategoryDataDto;
import xserver.api.module.superlive.dto.SuperLiveCategoryDto;
import xserver.api.module.superlive.dto.SuperLiveIndexDto;
import xserver.api.module.superlive.dto.UserOrderData;
import xserver.api.response.Response;
import xserver.common.cache.CbaseRmiClient;
import xserver.common.util.CalendarUtil;
import xserver.lib.tp.cms.response.CmsBlockContent;
import xserver.lib.tp.cms.response.CmsBlockTpResponse;
import xserver.lib.tp.live.response.BossPermissionResponse;
import xserver.lib.tp.superlive.request.GetProgamInfoByChannelIdsRequest;
import xserver.lib.tp.superlive.request.GetProgamInfoByDataRequest;
import xserver.lib.tp.superlive.request.GetProgamInfoByIncRequest;
import xserver.lib.tp.superlive.response.ProgramInfo;
import xserver.lib.tp.superlive.response.ProgramInfoByChannelIdsTpResponse;
import xserver.lib.tp.superlive.response.ProgramInfoByChannelIdsTpResponse.ChannelProgram;
import xserver.lib.tp.superlive.response.ProgramInfoByDateTpResponse;
import xserver.lib.tp.superlive.response.ProgramInfoByIncTpResponse;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.tpcache.cache.PlayCache;

import com.fasterxml.jackson.core.type.TypeReference;

@Component(value = "superLiveService")
public class SuperLiveService extends BaseService {
    private final Logger log = LoggerFactory.getLogger(SuperLiveService.class);
    private CbaseRmiClient cbaseRmiClient = CbaseRmiClient.getInstance();

    /**
     * 是否为定制的明星列表
     * @param param
     * @return
     */
    private String getStarCustomize(CommonParam param) {
        if (param != null && StringUtils.isNotBlank(param.getDevId())) {
            return SuperLiveConstant.CUSTOMIZE_STARS.get(param.getDevId());
        }
        return null;
    }

    // TODO 需要修改默认数据
    public Response<SuperLiveIndexDto> getSuperLiveIndex(CommonParam param, String uid) {

        // 是否为定制
        String star = getStarCustomize(param);

        Response<SuperLiveIndexDto> response = new Response<SuperLiveIndexDto>();
        List<SuperLiveCategoryDto> list = getTpCacheTemplate().get(CacheConstants.SUPERLIVE_CATEGORY,
                new TypeReference<List<SuperLiveCategoryDto>>() {
                });
        List<SuperLiveCategoryDto> defaultCategoryList = new LinkedList<SuperLiveCategoryDto>();
        for (String s : SuperLiveConstant.DEFAULT_LIVE_INDEX_CHANNELS.keySet()) {
            for (SuperLiveCategoryDto superLiveCategoryDto : list) {
                if (s.equals(superLiveCategoryDto.getCode())) {
                    defaultCategoryList.add(superLiveCategoryDto);
                }
            }
        }
        Map<String, String> ccids = null;
        if (uid != null && uid.length() > 0) {
            UserOrderData userOrderData = tpCacheTemplate.get(CacheConstants.SUPERLIVE_USER_ORDER + uid,
                    UserOrderData.class);
            if (userOrderData != null) {// 用户是否有定制化
                // 获取频道定制数据
                ccids = userOrderData.getCcids();
                // 获取频道类型定制化
                String cids = userOrderData.getCids();
                if (cids != null && cids.length() > 0) {
                    List<SuperLiveCategoryDto> list2 = new ArrayList<SuperLiveCategoryDto>();
                    Map<String, SuperLiveCategoryDto> slcd = new HashMap<String, SuperLiveCategoryDto>();
                    for (SuperLiveCategoryDto s : list) {
                        slcd.put(s.getCode(), s);
                    }
                    String[] cidsArray = cids.split("-");
                    for (String id : cidsArray) {
                        if (slcd.get(id) != null) {
                            list2.add(slcd.get(id));
                        }
                    }
                    defaultCategoryList = list2;
                }
            }
        }
        SuperLiveIndexDto superLiveIndexDto = new SuperLiveIndexDto();
        List<SuperLiveCategoryDataDto> lcdd = new ArrayList<SuperLiveCategoryDataDto>();
        superLiveIndexDto.setCategoryList(lcdd);
        for (SuperLiveCategoryDto subCategoryDto : defaultCategoryList) {
            SuperLiveCategoryDataDto slcdd = new SuperLiveCategoryDataDto();
            String channelIds = null;
            if (ccids != null && ccids.get(subCategoryDto.getCode()) != null) {
                channelIds = ccids.get(subCategoryDto.getCode());
            } else {
                channelIds = SuperLiveConstant.DEFAULT_LIVE_INDEX_CHANNELS.get(subCategoryDto.getCode());
            }
            if (channelIds == null || channelIds.length() == 0) {
                continue;
            }
            slcdd.setCategoryId(subCategoryDto.getCode());
            slcdd.setCategoryName(subCategoryDto.getName());
            String[] picAndColor = SuperLiveConstant.getLiveCategoryPicAndColor(subCategoryDto.getCode());
            slcdd.setCategoryPic(picAndColor[0]);
            slcdd.setColor(picAndColor[1]);
            long start = System.currentTimeMillis();
            List<LiveChannelCurrentStateDataDto> lccsddlist = new ArrayList<LiveChannelCurrentStateDataDto>();
            for (String id : channelIds.split("-")) {
                /*
                 * 定制需求,特殊定制的明星，每个明星必须在明星轮播台的第一个，后续代码会强行添加第一个，此处忽略掉改明星
                 * 明星大类的categoryid为725
                 */
                if (star != null && "725".equals(subCategoryDto.getCode()) && star.contains(id)) {
                    log.info("custom_star filter star [devid=" + param.getDevId() + ", star=" + id + "]");
                    continue;
                }
                /* 定制需求 END */
                log.debug("********************************:" + id);
                long s = System.currentTimeMillis();
                LiveChannelCurrentStateDataDto liveChannelCurrentStateDataDto = tpCacheTemplate.get(
                        CacheConstants.SUPERLIVE_CHANNEL_ID + id, LiveChannelCurrentStateDataDto.class);
                if (liveChannelCurrentStateDataDto != null && liveChannelCurrentStateDataDto.getCur() != null
                        && liveChannelCurrentStateDataDto.getStreams() != null
                        && liveChannelCurrentStateDataDto.getStreams().size() > 0) {
                    lccsddlist.add(liveChannelCurrentStateDataDto);
                }
                log.debug("get channel by id from cbase waste time:" + (System.currentTimeMillis() - s));
            }

            /* 定制需求,特殊定制的明星，每个明星必须在明星轮播台的第一个 明星大类的categoryid为725 */
            if (star != null && "725".equals(subCategoryDto.getCode())) {
                log.info("customize star for [devid=" + param.getDevId() + ", star=" + star + "]");
                String[] starids = star.split("-");
                if (starids != null) {
                    int i = 0;
                    for (String starid : starids) {
                        LiveChannelCurrentStateDataDto liveChannelCurrentStateDataDto = tpCacheTemplate.get(
                                CacheConstants.SUPERLIVE_CHANNEL_ID + starid, LiveChannelCurrentStateDataDto.class);
                        if (liveChannelCurrentStateDataDto != null && liveChannelCurrentStateDataDto.getCur() != null
                                && liveChannelCurrentStateDataDto.getStreams() != null
                                && liveChannelCurrentStateDataDto.getStreams().size() > 0) {
                            lccsddlist.add(i++, liveChannelCurrentStateDataDto);
                        }
                    }
                }
            }
            /* 定制需求 END */

            slcdd.setChannelList(lccsddlist);
            lcdd.add(slcdd);
            log.debug("==================get channels by cid from cbase waste time "
                    + (System.currentTimeMillis() - start));
        }
        Response<SuperLiveIndexDto> resp = getHotCategoryList(param, uid);
        if (resp != null && resp.getData() != null && resp.getData().getCategoryList() != null
                && resp.getData().getCategoryList().size() > 0) {
            superLiveIndexDto.getCategoryList().add(0, resp.getData().getCategoryList().get(0));
        }
        response.setData(superLiveIndexDto);
        return response;
    }

    public void setUserChannel(String uid, String categoryId, String channelIds) throws Exception {
        if (StringUtils.isEmpty(categoryId) || StringUtils.isEmpty(channelIds)) {// 分类及频道不能为空
            throw new Exception("NULL");
        } else {
            // 用户Key
            String userKey = CacheConstants.SUPERLIVE_USER_ORDER + uid;

            UserOrderData userOrderData = tpCacheTemplate.get(userKey, UserOrderData.class);
            if (userOrderData == null) {// 初次定制分类
                userOrderData = new UserOrderData();
                // userOrderData.setCids(categoryId);
            }

            // 设置频道列表
            Map<String, String> channelIdsMap = userOrderData.getCcids();
            if (channelIdsMap == null) {// 初次定制频道
                channelIdsMap = new HashMap<String, String>();
            }
            Set<String> set = new HashSet<String>();
            StringBuffer sbBuffer = new StringBuffer();
            for (String id : channelIds.split("-")) {
                if (!set.contains(id)) {
                    sbBuffer.append(id).append("-");
                    set.add(id);
                }
            }
            if (sbBuffer.length() > 0) {
                sbBuffer.deleteCharAt(sbBuffer.length() - 1);
            }
            channelIdsMap.put(categoryId, sbBuffer.toString());
            userOrderData.setCcids(channelIdsMap);

            // 更新缓存
            cbaseRmiClient.updateSync(userKey, userOrderData);
        }
    }

    public void setUserCategory(String uid, String categoryIds) throws Exception {
        if (StringUtils.isEmpty(categoryIds)) {// 分类不能为空
            throw new Exception("NULL");
        } else {
            // 用户Key
            String userKey = CacheConstants.SUPERLIVE_USER_ORDER + uid;

            UserOrderData userOrderData = tpCacheTemplate.get(userKey, UserOrderData.class);
            if (userOrderData == null) {// 初次定制分类
                userOrderData = new UserOrderData();
            }
            userOrderData.setCids(categoryIds);

            // 更新缓存
            cbaseRmiClient.updateSync(userKey, userOrderData);
        }
    }

    public Response<LiveChannelCurrentStateDataDto> getDetail(String liveType, String id) {
        LiveChannelCurrentStateDataDto detail = null;
        if (SuperLiveConstant.LIVE_TYPE_LIVEROOM.equals(liveType)) {// 直播
            detail = tpCacheTemplate.get(CacheConstants.SUPERLIVE_LIVEROOM_LIVEID + id,
                    LiveChannelCurrentStateDataDto.class);

        } else {// 轮播、卫视台
            detail = tpCacheTemplate
                    .get(CacheConstants.SUPERLIVE_CHANNEL_ID + id, LiveChannelCurrentStateDataDto.class);
        }

        return new Response<LiveChannelCurrentStateDataDto>(detail);
    }

    // TODO 需要修改默认数据
    public Response<SuperLiveIndexDto> listChannelByCid(String uid, String categoryId, Integer pageIndex,
            Integer pageSize, CommonParam param) throws Exception {
        Response<SuperLiveIndexDto> response = new Response<SuperLiveIndexDto>();
        SuperLiveIndexDto dto = new SuperLiveIndexDto();
        List<SuperLiveCategoryDataDto> categoryList = new ArrayList<SuperLiveCategoryDataDto>();
        SuperLiveCategoryDataDto resultDataDto = new SuperLiveCategoryDataDto();
        resultDataDto.setCategoryId(categoryId);
        response.setData(dto);
        dto.setCategoryList(categoryList);
        categoryList.add(resultDataDto);

        // 是否为定制
        String star = getStarCustomize(param);

        // 用户Key
        String userKey = CacheConstants.SUPERLIVE_USER_ORDER + uid;

        // 从缓存拿数据
        String channelIds = null;
        Map<String, String> channelIdsMap = null;
        UserOrderData userOrderData = tpCacheTemplate.get(userKey, UserOrderData.class);
        if (userOrderData != null) {// 用户定制
            channelIdsMap = userOrderData.getCcids();

            if (channelIdsMap != null) {
                channelIds = channelIdsMap.get(categoryId);
            }
        }
        if (channelIds == null) {// 用户无定制，默认数据
            channelIds = SuperLiveConstant.DEFAULT_LIVE_CHANNELS.get(categoryId);
        }
        if (channelIds == null || channelIds.length() == 0) {// 无数据
            resultDataDto.setChannelList(new ArrayList<LiveChannelCurrentStateDataDto>());
            return response;
        }
        String[] channelIdArr = channelIds.split("-");
        resultDataDto.setNum(channelIdArr.length);
        List<String> channelKeys = new ArrayList<String>();
        if ((pageIndex - 1) * pageSize >= channelIdArr.length) {
            resultDataDto.setChannelList(new ArrayList<LiveChannelCurrentStateDataDto>());
            return response;
        }
        int end = pageIndex * pageSize;
        if (end > channelIdArr.length) {
            end = channelIdArr.length;
        }
        for (int i = (pageIndex - 1) * pageSize; i < end; i++) {

            /*
             * 定制需求,特殊定制的明星，每个明星必须在明星轮播台的第一个，后续代码会强行添加第一个，此处忽略掉改明星
             * 明星大类的categoryid为725
             */
            String chidString = channelIdArr[i];
            if ("725".equals(categoryId) && star != null && star.contains(chidString)) {
                log.info("custom_star filter star [devid=" + param.getDevId() + ", star=" + chidString + "]");
                continue;
            }
            /* 定制需求 END */

            // 频道Key
            String channelKey = CacheConstants.SUPERLIVE_CHANNEL_ID + channelIdArr[i];
            channelKeys.add(channelKey);
        }

        /*
         * 组装频道数据
         */
        Map<String, LiveChannelCurrentStateDataDto> lccsd = tpCacheTemplate.mget(channelKeys,
                LiveChannelCurrentStateDataDto.class);
        List<LiveChannelCurrentStateDataDto> list = new ArrayList<LiveChannelCurrentStateDataDto>();
        for (String s : channelKeys) {

            if (lccsd.get(s) != null) {
                LiveChannelCurrentStateDataDto channelCurrentStateDataDto = lccsd.get(s);
                if (channelCurrentStateDataDto.getCur() != null && channelCurrentStateDataDto.getStreams() != null
                        && channelCurrentStateDataDto.getStreams().size() > 0) {
                    list.add(lccsd.get(s));
                }
            }
        }
        resultDataDto.setChannelList(new ArrayList<LiveChannelCurrentStateDataDto>(list));
        return response;
    }

    public Response<SuperLiveIndexDto> getLiveList(String uid, Integer pageIndex, String cid, Integer pageSize) {
        Response<SuperLiveIndexDto> res = new Response<SuperLiveIndexDto>();
        SuperLiveIndexDto superLive = new SuperLiveIndexDto();
        List<SuperLiveCategoryDataDto> categoryList = new ArrayList<SuperLiveCategoryDataDto>();
        SuperLiveCategoryDataDto resultDataDto = new SuperLiveCategoryDataDto();
        resultDataDto.setCategoryId("001");
        resultDataDto.setCategoryName("直播");
        resultDataDto.setCategoryPic("http://i1.letvimg.com/lc03_tdv/201504/10/15/59/zhibo.png");
        resultDataDto.setColor("#c54242");
        res.setData(superLive);
        superLive.setCategoryList(categoryList);
        categoryList.add(resultDataDto);

        String key = "a";
        if (cid.equals("0")) {
            key = CacheConstants.SUPERLIVE_LIVEROOM;
        } else if (cid.equals("1")) {
            key = CacheConstants.SUPERLIVE_LIVEROOM_SPORTS;
        } else if (cid.equals("2")) {
            key = CacheConstants.SUPERLIVE_LIVEROOM_MUSIC;
        } else if (cid.equals("3")) {
            key = CacheConstants.SUPERLIVE_LIVEROOM_ENT;
        }
        List<LiveChannelCurrentStateDataDto> list = tpCacheTemplate.get(key,
                new TypeReference<List<LiveChannelCurrentStateDataDto>>() {
                });

        if (list != null && list.size() > (pageIndex - 1) * pageSize && pageIndex > 0) {
            int start = (pageIndex - 1) * pageSize;
            int end = (pageIndex * pageSize) > list.size() ? list.size() : (pageIndex * pageSize);
            List<LiveChannelCurrentStateDataDto> tmpList = new ArrayList<LiveChannelCurrentStateDataDto>();
            LiveChannelCurrentStateDataDto tmpDto = null;

            StringBuffer sids = new StringBuffer();// 付费节目的场次id

            for (int i = start; i < end; i++) {
                tmpDto = list.get(i);
                tmpDto.setType(SuperLiveConstant.LIVE_TYPE_LIVEROOM);
                tmpList.add(tmpDto);

                // 拼接场次id，给所有的直播节目配价格
                if (list.get(i).getIsPay() != null && list.get(i).getIsPay().equals("1")) {
                    sids.append(list.get(i).getCur().getScreenings());
                    sids.append(",");
                }
            }

            if (!StringUtils.isEmpty(sids.toString()) && !StringUtils.isEmpty(uid)) {
                Map<String, BossPermissionResponse.PermissionInfo> pmap = facadeTpDao.getSuperLiveTpDao()
                        .getPermission(sids.toString().substring(0, sids.toString().length() - 1), uid);
                if (pmap != null && pmap.size() > 0) {
                    for (LiveChannelCurrentStateDataDto l : tmpList) {
                        if (StringUtils.isNotEmpty(l.getCur().getScreenings())) {
                            BossPermissionResponse.PermissionInfo p = pmap.get(l.getCur().getScreenings());
                            if (p != null) {
                                l.getCur().setIsCharge(p.getStatus() == null ? "0" : p.getStatus());
                                l.getCur().setPrice(p.getPrice() == null ? "0" : p.getPrice());
                                l.getCur().setVipPrice(p.getVipprice() == null ? "0" : p.getVipprice());
                            } else {
                                l.getCur().setIsCharge("0");
                                l.getCur().setPrice("0");
                                l.getCur().setVipPrice("0");
                            }
                        }
                    }
                } else {
                    for (LiveChannelCurrentStateDataDto l : tmpList) {
                        if (StringUtils.isNotEmpty(l.getCur().getScreenings())) {
                            l.getCur().setIsCharge("0");
                            l.getCur().setPrice("0");
                            l.getCur().setVipPrice("0");
                        }
                    }
                }
            }
            resultDataDto.setChannelList(tmpList);
        }
        return res;
    }

    // TODO 需要修改默认数据
    public Response<List<CategoryChannelIdsDto>> getAllChIds(String uid, CommonParam param) {
        Long start = System.currentTimeMillis();
        Response<List<CategoryChannelIdsDto>> response = new Response<List<CategoryChannelIdsDto>>();
        Collection<String> list = SuperLiveConstant.DEFAULT_LIVE_CHANNELS.keySet();
        Map<String, String> ccids = null;
        if (uid != null && uid.length() > 0) {
            UserOrderData userOrderData = tpCacheTemplate.get(CacheConstants.SUPERLIVE_USER_ORDER + uid,
                    UserOrderData.class);
            if (userOrderData != null) {// 用户是否有定制化
                // 获取频道定制数据
                ccids = userOrderData.getCcids();
                // 获取频道类型定制化
                String cids = userOrderData.getCids();
                if (cids != null && cids.length() > 0) {
                    String[] cidsArray = cids.split("-");
                    list = Arrays.asList(cidsArray);
                }
            }
        }
        List<CategoryChannelIdsDto> data = new ArrayList<CategoryChannelIdsDto>();
        for (String code : list) {
            CategoryChannelIdsDto categoryChannelIdsDto = new CategoryChannelIdsDto();
            categoryChannelIdsDto.setCid(code);
            if (ccids != null && ccids.get(code) != null) {
                String ccidsTmp = ccids.get(code);
                categoryChannelIdsDto.setChilds(ccidsTmp);
            } else {
                categoryChannelIdsDto.setChilds(SuperLiveConstant.DEFAULT_LIVE_CHANNELS.get(code));
            }
            if (categoryChannelIdsDto.getChilds() == null || categoryChannelIdsDto.getChilds().trim().length() == 0) {
                continue;
            }
            data.add(categoryChannelIdsDto);
        }
        log.debug("==================getAllChIds " + (System.currentTimeMillis() - start));
        Response<SuperLiveIndexDto> resp = getHotCategoryList(param, uid);
        if (resp != null && resp.getData() != null && resp.getData().getCategoryList() != null
                && resp.getData().getCategoryList().size() > 0) {
            List<LiveChannelCurrentStateDataDto> tmp = resp.getData().getCategoryList().get(0).getChannelList();
            if (tmp != null) {
                StringBuffer sb = new StringBuffer();
                for (LiveChannelCurrentStateDataDto l : tmp) {
                    sb.append(l.getChannelId()).append("-");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                CategoryChannelIdsDto categoryChannelIdsDto = new CategoryChannelIdsDto();
                categoryChannelIdsDto.setChilds(sb.toString());
                categoryChannelIdsDto.setCid(SuperLiveConstant.SUPERLIVE_CATEGORY_FOCUS.getCode());
                data.add(0, categoryChannelIdsDto);
            }
        }
        response.setData(data);
        return response;
    }

    public Response<List<SuperLiveCategoryDto>> listCategorys(CommonParam param) {
        List<SuperLiveCategoryDto> list = getTpCacheTemplate().get(CacheConstants.SUPERLIVE_CATEGORY,
                new TypeReference<List<SuperLiveCategoryDto>>() {
                });
        if (list != null) {
            for (SuperLiveCategoryDto s : list) {
                String[] sc = SuperLiveConstant.getLiveCategoryPicAndColor(s.getCode());
                s.setCategoryPic(sc[0]);
                s.setColor(sc[1]);
            }
            list.add(0, SuperLiveConstant.SUPERLIVE_CATEGORY_FOCUS);
        }
        Response<List<SuperLiveCategoryDto>> response = new Response<List<SuperLiveCategoryDto>>();
        response.setData(list);
        return response;
    }

    public Response<SuperLiveIndexDto> getHotCategoryList(CommonParam param, String uid) {
        Response<SuperLiveIndexDto> response = new Response<SuperLiveIndexDto>();
        SuperLiveIndexDto dto = new SuperLiveIndexDto();
        List<SuperLiveCategoryDataDto> categoryList = new ArrayList<SuperLiveCategoryDataDto>();
        SuperLiveCategoryDataDto resultDataDto = new SuperLiveCategoryDataDto();
        resultDataDto.setCategoryId(SuperLiveConstant.SUPERLIVE_CATEGORY_FOCUS.getCode());
        resultDataDto.setCategoryName(SuperLiveConstant.SUPERLIVE_CATEGORY_FOCUS.getName());
        String[] cc = SuperLiveConstant
                .getLiveCategoryPicAndColor(SuperLiveConstant.SUPERLIVE_CATEGORY_FOCUS.getCode());
        resultDataDto.setColor(cc[1]);
        resultDataDto.setCategoryPic(cc[0]);

        response.setData(dto);
        dto.setCategoryList(categoryList);
        categoryList.add(resultDataDto);
        CmsBlockTpResponse tpResponse = facadeTpDao.getCmsTpDao().getCmsBlock(
                SuperLiveConstant.HOT_CATEGORY_CMS_BLOCK_ID);
        if (tpResponse != null && tpResponse.getBlockContent() != null) {
            List<LiveChannelCurrentStateDataDto> list = new ArrayList<LiveChannelCurrentStateDataDto>();
            for (CmsBlockContent c : tpResponse.getBlockContent()) {
                LiveChannelCurrentStateDataDto liveChannelCurrentStateDataDto = tpCacheTemplate.get(
                        CacheConstants.SUPERLIVE_CHANNEL_ID + c.getContent(), LiveChannelCurrentStateDataDto.class);
                if (liveChannelCurrentStateDataDto != null) {
                    list.add(liveChannelCurrentStateDataDto);
                }
            }
            resultDataDto.setChannelList(list);
        }
        return response;
    }

    public Response<ChannelDto> getChannelPrograms(String channelId, String date, CommonParam param) {
        if (date == null) {
            date = CalendarUtil.getDateString(Calendar.getInstance(), CalendarUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        GetProgamInfoByDataRequest grByDataRequest = new GetProgamInfoByDataRequest();
        Response<ChannelDto> resp = new Response<ChannelDto>();
        grByDataRequest.channelId(channelId);
        grByDataRequest.clientId(SuperLiveConstant.SUPERLIVE_CLIENTID);
        grByDataRequest.date(date);
        ProgramInfoByDateTpResponse repsonse = facadeTpDao.getSuperLiveTpDao().getProgramByDate(grByDataRequest);
        if (repsonse != null && repsonse.getRows() != null) {
            ChannelDto channelDto = new ChannelDto();
            List<ProgramDto> list = new LinkedList<ProgramDto>();
            for (ProgramInfo programInfo : repsonse.getRows()) {
                ProgramDto programDto = new ProgramDto();
                programDto.setAid(programInfo.getAid());
                programDto.setVid(programInfo.getVid());
                programDto.setCategory(programInfo.getCategory());
                programDto.setDuration(programInfo.getDuration());
                programDto.setEndTime(programInfo.getEndTime());
                programDto.setTitle(programInfo.getTitle());
                if (programInfo.getProgramType() != null && programInfo.getProgramType() == 0) {
                    programDto.setViewPic(programInfo.getViewPic() != null ? programInfo.getViewPic().substring(0,
                            programInfo.getViewPic().lastIndexOf("/") + 1)
                            + "2_960_540.jpg" : "");
                } else {
                    programDto.setViewPic(programInfo.getViewPic());
                }
                programDto.setProgramType(programInfo.getProgramType());
                programDto.setPlayTime(programInfo.getPlayTime());
                programDto.setId(programInfo.getId());
                list.add(programDto);
            }
            channelDto.setPrograms(list);
            resp.setData(channelDto);
        } else {
            setErrorResponse(resp, ErrorCodeConstants.LIVE_CHANNEL_PROGRAM_WITH_DATE_GET_FAIL, param.getWcode(),
                    param.getLangcode());
        }
        return resp;
    }

    private boolean validateProgram(String playTime) {
        Calendar yes = CalendarUtil.startOfDayYesterday();
        Calendar tomorrow = CalendarUtil.getDateFromDate(yes.getTime(), 3);
        Calendar playTimeCalendar = CalendarUtil.parseCalendar(playTime, CalendarUtil.SIMPLE_DATE_FORMAT);
        if (playTimeCalendar.getTimeInMillis() < yes.getTimeInMillis()
                || playTimeCalendar.getTimeInMillis() > tomorrow.getTimeInMillis()) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {

    }

    public Response<ChannelDto> getChannelProgramsWithInc(String programId, String direction, CommonParam param) {
        GetProgamInfoByIncRequest getProgramByInc = new GetProgamInfoByIncRequest();
        Response<ChannelDto> resp = new Response<ChannelDto>();
        getProgramByInc.programId(programId).clientId(SuperLiveConstant.SUPERLIVE_CLIENTID).direction(direction);
        ProgramInfoByIncTpResponse repsonse = facadeTpDao.getSuperLiveTpDao().getProgramByInc(getProgramByInc);
        if (repsonse != null && repsonse.getRows() != null) {
            ChannelDto channelDto = new ChannelDto();
            List<ProgramDto> list = new LinkedList<ProgramDto>();
            if (repsonse.getRows().getPrograms() != null) {
                for (ProgramInfo programInfo : repsonse.getRows().getPrograms()) {
                    if (!validateProgram(programInfo.getPlayTime())) {
                        continue;
                    }
                    ProgramDto programDto = new ProgramDto();
                    programDto.setAid(programInfo.getAid());
                    programDto.setVid(programInfo.getVid());
                    programDto.setCategory(programInfo.getCategory());
                    programDto.setDuration(programInfo.getDuration());
                    programDto.setEndTime(programInfo.getEndTime());
                    programDto.setTitle(programInfo.getTitle());
                    if (programInfo.getProgramType() != null && programInfo.getProgramType() == 0) {
                        programDto.setViewPic(programInfo.getViewPic() != null ? programInfo.getViewPic().substring(0,
                                programInfo.getViewPic().lastIndexOf("/") + 1)
                                + "2_960_540.jpg" : "");
                    } else {
                        programDto.setViewPic(programInfo.getViewPic());
                    }
                    programDto.setProgramType(programInfo.getProgramType());
                    programDto.setPlayTime(programInfo.getPlayTime());
                    programDto.setId(programInfo.getId());
                    PlayCache p = tpCacheTemplate.get(CacheConstants.PlayCacheEntity_V_ + programInfo.getVid(),
                            PlayCache.class);
                    if (p != null && p.getvIsmobile() != null && p.getvIsmobile() == 1) {
                        programDto.setStatus(1);
                    } else {
                        programDto.setStatus(0);
                    }
                    list.add(programDto);
                }
            }
            channelDto.setPrograms(list);
            resp.setData(channelDto);
        } else {
            setErrorResponse(resp, ErrorCodeConstants.LIVE_CHANNEL_PROGRAM_WITH_INC_GET_FAIL, param.getWcode(),
                    param.getLangcode());
        }
        return resp;
    }

    public Response<List<ChannelDto>> getChannelProgramsWithChannelIds(String channelIds, CommonParam param) {
        GetProgamInfoByChannelIdsRequest getProgramByChannelIds = new GetProgamInfoByChannelIdsRequest();
        Response<List<ChannelDto>> resp = new Response<List<ChannelDto>>();
        getProgramByChannelIds.channelIds(channelIds).clientId(SuperLiveConstant.SUPERLIVE_CLIENTID);
        ProgramInfoByChannelIdsTpResponse repsonse = facadeTpDao.getSuperLiveTpDao().getProgramByChannelIds(
                getProgramByChannelIds);
        if (repsonse != null && repsonse.getRows() != null) {
            List<ChannelDto> list = new ArrayList<ChannelDto>();
            if (repsonse.getRows() != null) {
                List<ChannelProgram> cpList = repsonse.getRows();
                for (ChannelProgram cp : cpList) {
                    ChannelDto channelDto = new ChannelDto();
                    if (cp.getData() == null || cp.getData().isEmpty()) {
                        channelDto.setChannelId(cp.getChannelId());
                        list.add(channelDto);
                        continue;
                    }
                    List<ProgramDto> listTmp = new LinkedList<ProgramDto>();
                    for (ProgramInfo programInfo : cp.getData()) {
                        ProgramDto programDto = new ProgramDto();
                        programDto.setAid(programInfo.getAid());
                        programDto.setVid(programInfo.getVid());
                        programDto.setCategory(programInfo.getCategory());
                        programDto.setDuration(programInfo.getDuration());
                        programDto.setEndTime(programInfo.getEndTime());
                        programDto.setTitle(programInfo.getTitle());
                        if (programInfo.getProgramType() != null && programInfo.getProgramType() == 0) {
                            programDto
                                    .setViewPic(programInfo.getViewPic() != null ? programInfo.getViewPic().substring(
                                            0, programInfo.getViewPic().lastIndexOf("/") + 1)
                                            + "2_960_540.jpg" : "");
                        } else {
                            programDto.setViewPic(programInfo.getViewPic());
                        }
                        programDto.setProgramType(programInfo.getProgramType());
                        programDto.setPlayTime(programInfo.getPlayTime());
                        programDto.setId(programInfo.getId());
                        PlayCache p = tpCacheTemplate.get(CacheConstants.PlayCacheEntity_V_ + programInfo.getVid(),
                                PlayCache.class);
                        if (p != null && p.getvIsmobile() != null && p.getvIsmobile() == 1) {
                            programDto.setStatus(1);
                        } else {
                            programDto.setStatus(0);
                        }
                        listTmp.add(programDto);
                    }
                    channelDto.setChannelId(cp.getChannelId());
                    channelDto.setPrograms(listTmp);
                    list.add(channelDto);
                }
            }
            resp.setData(list);
        } else {
            setErrorResponse(resp, ErrorCodeConstants.LIVE_CHANNEL_PROGRAM_WITH_INC_GET_FAIL, param.getWcode(),
                    param.getLangcode());
        }
        return resp;
    }

}
