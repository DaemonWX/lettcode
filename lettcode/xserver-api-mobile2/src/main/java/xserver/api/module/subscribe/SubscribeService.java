package xserver.api.module.subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import xserver.api.constant.ErrorCodeConstants;
import xserver.api.dto.ValueDto;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.subscribe.dto.SubscribeEventDto;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.common.constant.SuperLiveDictionaryConstant;
import xserver.common.dto.superlive.v2.SuperLiveChannelDto;
import xserver.common.dto.superlive.v2.SuperLiveProgramDto;
import xserver.lib.tp.subscribe.request.AddSubscribeRequest;
import xserver.lib.tp.subscribe.request.UserSubscribeCheckTpRequest;
import xserver.lib.tp.subscribe.request.UserSubscribeDeleteTpRequest;
import xserver.lib.tp.subscribe.request.UserSubscribeListRequest;
import xserver.lib.tp.subscribe.request.UserSubscribeMultiDeleteTpRequest;
import xserver.lib.tp.subscribe.response.SubscribeAddOrDelTpResponse;
import xserver.lib.tp.subscribe.response.UserSubscribeCheckTpResponse;
import xserver.lib.tp.subscribe.response.UserSubscribeListTpResponse;
import xserver.lib.tp.subscribe.response.UserSubscribeListTpResponse.UserSubscribeData;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.util.CalendarUtil;

@Service(value = "subscribeService")
public class SubscribeService extends BaseService {

    public Response<SubscribeEventDto> add(String token, String sourceID, CommonParam param) {
        Response<SubscribeEventDto> response = new Response<SubscribeEventDto>();
        //token 为空，返回用户未登录
        if(token==null||"".equals(token.trim())){
            setErrorResponse(response, ErrorCodeConstants.USER_NOT_LOGIN, ErrorCodeConstants.USER_NOT_LOGIN,
                    param.getLangcode());
            return response;
        }
        // 获取直播数据
        SuperLiveChannelDto superLiveChannelDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_LIVEROOM_LIVEID
                + sourceID, SuperLiveChannelDto.class);
        if(superLiveChannelDto == null){
            setErrorResponse(response, ErrorCodeConstants.SUBSCRIBE_LIVE_NOT_FOUND,
                    ErrorCodeConstants.SUBSCRIBE_LIVE_NOT_FOUND, param.getLangcode());
            return response;
        }
        String sourceType = SuperLiveDictionaryConstant.LIVE_SUBSCRIBE_LIVETYPE_TO_SOURCETYPE.get(superLiveChannelDto
                .getLiveType());
        AddSubscribeRequest addSubscribeRequest = new AddSubscribeRequest(token, sourceID, sourceType,
                SubscribeConstant.APPID);
        SubscribeAddOrDelTpResponse addOrDelTpResponse = facadeTpDao.getSubscribeTpDao().addSubscribe(
                addSubscribeRequest);

        if (addOrDelTpResponse != null && SubscribeConstant.SUBSCRIBE_TP_SUCCESS.equals(addOrDelTpResponse.getErrno())) {
            if (addOrDelTpResponse.getData() != null && addOrDelTpResponse.getData().getResult() != null
                    && addOrDelTpResponse.getData().getResult() == Boolean.TRUE) {
                SubscribeEventDto subscribeEventDto = new SubscribeEventDto();
                subscribeEventDto.setSourceID(sourceID);
                subscribeEventDto.setSourceType(sourceType);
                subscribeEventDto.setV(Boolean.TRUE);
                if(superLiveChannelDto.getCur()!=null){
                    subscribeEventDto.setTitle(superLiveChannelDto.getCur().getTitle());
                }
                response.setData(subscribeEventDto);
            } else {
                setErrorResponse(response, ErrorCodeConstants.SUBSCRIBE_ADD_FAIL_1, addOrDelTpResponse.getErrmsg(),
                        param.getLangcode());
            }
        } else {
            setErrorResponse(response, ErrorCodeConstants.SUBSCRIBE_ADD_FAIL_2,
                    ErrorCodeConstants.SUBSCRIBE_ADD_FAIL_2, param.getLangcode());
        }
        return response;
    }

    public Response<SubscribeEventDto> delete(String token, String sourceID, CommonParam param) {
        Response<SubscribeEventDto> response = new Response<SubscribeEventDto>();
        //token 为空，返回用户未登录
        if(token==null||"".equals(token.trim())){
            setErrorResponse(response, ErrorCodeConstants.USER_NOT_LOGIN, ErrorCodeConstants.USER_NOT_LOGIN,
                    param.getLangcode());
            return response;
        }
        // 获取直播数据
        SuperLiveChannelDto superLiveChannelDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_LIVEROOM_LIVEID
                + sourceID, SuperLiveChannelDto.class);
        if(superLiveChannelDto == null){
            setErrorResponse(response, ErrorCodeConstants.SUBSCRIBE_LIVE_NOT_FOUND,
                    ErrorCodeConstants.SUBSCRIBE_LIVE_NOT_FOUND, param.getLangcode());
            return response;
        }
        String sourceType = SuperLiveDictionaryConstant.LIVE_SUBSCRIBE_LIVETYPE_TO_SOURCETYPE.get(superLiveChannelDto
                .getLiveType());
        UserSubscribeDeleteTpRequest delete = new UserSubscribeDeleteTpRequest();
        delete.token(token).eventId(sourceID).eventType(sourceType).appId(SubscribeConstant.APPID);
        SubscribeAddOrDelTpResponse addOrDelTpResponse = facadeTpDao.getSubscribeTpDao().deleteSubscribe(delete);

        if (addOrDelTpResponse != null && SubscribeConstant.SUBSCRIBE_TP_SUCCESS.equals(addOrDelTpResponse.getErrno())) {
            if (addOrDelTpResponse.getData() != null && addOrDelTpResponse.getData().getResult() != null
                    && addOrDelTpResponse.getData().getResult() == Boolean.TRUE) {
                SubscribeEventDto subscribeEventDto = new SubscribeEventDto();
                subscribeEventDto.setSourceID(sourceID);
                subscribeEventDto.setSourceType(sourceType);
                subscribeEventDto.setV(Boolean.TRUE);
                if(superLiveChannelDto.getCur()!=null){
                    subscribeEventDto.setTitle(superLiveChannelDto.getCur().getTitle());
                }
                response.setData(subscribeEventDto);
            } else {
                setErrorResponse(response, ErrorCodeConstants.SUBSCRIBE_DEL_FAIL_1, addOrDelTpResponse.getErrmsg(),
                        param.getLangcode());
            }
        } else {
            setErrorResponse(response, ErrorCodeConstants.SUBSCRIBE_DEL_FAIL_2,
                    ErrorCodeConstants.SUBSCRIBE_DEL_FAIL_2, param.getLangcode());
        }

        return response;
    }
    public Response<ValueDto<Boolean>> multidelete(String token, String eventIDList, CommonParam param) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        //token 为空，返回用户未登录
        if(token==null||"".equals(token.trim())){
            setErrorResponse(response, ErrorCodeConstants.USER_NOT_LOGIN, ErrorCodeConstants.USER_NOT_LOGIN,
                    param.getLangcode());
            return response;
        }
      //根据映射转换sourceType的值
        for(String key:SuperLiveDictionaryConstant.LIVE_SUBSCRIBE_LIVETYPE_TO_SOURCETYPE.keySet()){
            eventIDList = eventIDList.replace("\""+key+"\"", SuperLiveDictionaryConstant.LIVE_SUBSCRIBE_LIVETYPE_TO_SOURCETYPE.get(key));
        }
        UserSubscribeMultiDeleteTpRequest multidelete = new UserSubscribeMultiDeleteTpRequest(token,eventIDList,SubscribeConstant.APPID);
        SubscribeAddOrDelTpResponse addOrDelTpResponse = facadeTpDao.getSubscribeTpDao().multiDeleteSubscribe(multidelete);

        if (addOrDelTpResponse != null && SubscribeConstant.SUBSCRIBE_TP_SUCCESS.equals(addOrDelTpResponse.getErrno())) {
            if (addOrDelTpResponse.getData() != null && addOrDelTpResponse.getData().getResult() != null
                    && addOrDelTpResponse.getData().getResult() == Boolean.TRUE) {
                response.setData(new ValueDto<Boolean>(Boolean.TRUE));
            } else {
                setErrorResponse(response, ErrorCodeConstants.SUBSCRIBE_MULDEL_FAIL_1, addOrDelTpResponse.getErrmsg(),
                        param.getLangcode());
            }
        } else {
            setErrorResponse(response, ErrorCodeConstants.SUBSCRIBE_MULDEL_FAIL_2,
                    ErrorCodeConstants.SUBSCRIBE_MULDEL_FAIL_2, param.getLangcode());
        }

        return response;
    }

    public Response<ValueDto<Boolean>> check(String token, String sourceID, CommonParam param) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        //token 为空，返回用户未登录
        if(token==null||"".equals(token.trim())){
            setErrorResponse(response, ErrorCodeConstants.USER_NOT_LOGIN, ErrorCodeConstants.USER_NOT_LOGIN,
                    param.getLangcode());
            return response;
        }
        // 获取直播数据
        SuperLiveChannelDto superLiveChannelDto = tpCacheTemplate.get(CacheConstants.SUPERLIVE_LIVEROOM_LIVEID
                + sourceID, SuperLiveChannelDto.class);
        if(superLiveChannelDto == null){
            setErrorResponse(response, ErrorCodeConstants.SUBSCRIBE_LIVE_NOT_FOUND,
                    ErrorCodeConstants.SUBSCRIBE_LIVE_NOT_FOUND, param.getLangcode());
            return response;
        }
        String sourceType = SuperLiveDictionaryConstant.LIVE_SUBSCRIBE_LIVETYPE_TO_SOURCETYPE.get(superLiveChannelDto
                .getLiveType());
        UserSubscribeCheckTpRequest check = new UserSubscribeCheckTpRequest();
        check.token(token).eventId(sourceID).eventType(sourceType);
        UserSubscribeCheckTpResponse checkResponse = facadeTpDao.getSubscribeTpDao().checkSubscribe(check);

        if (checkResponse != null && SubscribeConstant.SUBSCRIBE_TP_SUCCESS.equals(checkResponse.getErrno())) {
            if (checkResponse.getData() != null && checkResponse.getData().getResult() != null
                    && checkResponse.getData().getResult() == Boolean.TRUE) {
                response.setData(new ValueDto<Boolean>(Boolean.TRUE));
            } else {
                response.setData(new ValueDto<Boolean>(Boolean.FALSE));
            }
        } else {
            setErrorResponse(response, ErrorCodeConstants.Appointment_CHECK_FAIL_2,
                    ErrorCodeConstants.Appointment_CHECK_FAIL_2, param.getLangcode());
        }
        return response;
    }

    public PageResponse<SubscribeEventDto> list(String token, Integer offSet, Integer size, CommonParam param) {
        PageResponse<SubscribeEventDto> response = new PageResponse<SubscribeEventDto>();
        //token 为空，返回用户未登录
        if(token==null||"".equals(token.trim())){
            setErrorResponse(response, ErrorCodeConstants.USER_NOT_LOGIN, ErrorCodeConstants.USER_NOT_LOGIN,
                    param.getLangcode());
            return response;
        }
        UserSubscribeListRequest userSubscribeListRequest = new UserSubscribeListRequest(token, offSet, size);
        UserSubscribeListTpResponse userSubscribeListTpResponse = facadeTpDao.getSubscribeTpDao().getSubscribeList(
                userSubscribeListRequest);
        
        if (userSubscribeListTpResponse != null && userSubscribeListTpResponse.getData() != null) {
            ArrayList<SubscribeEventDto> list = new ArrayList<SubscribeEventDto>();
            //获取直播列表MAP
            List<String> channelKeys = new ArrayList<String>();
            for (UserSubscribeData key : userSubscribeListTpResponse.getData()) {
                channelKeys.add(CacheConstants.SUPERLIVE_LIVEROOM_LIVEID + key.getSourceID());
            }
            Map<String, SuperLiveChannelDto> channelMap = this.tpCacheTemplate.mget(channelKeys,
                    SuperLiveChannelDto.class);
            if(channelMap == null){
                setErrorResponse(response, ErrorCodeConstants.SUBSCRIBE_LIVE_NOT_FOUND,
                        ErrorCodeConstants.SUBSCRIBE_LIVE_NOT_FOUND, param.getLangcode());
                return response;
            }
            for (UserSubscribeData usd : userSubscribeListTpResponse.getData()) {
                if(!channelMap.containsKey(CacheConstants.SUPERLIVE_LIVEROOM_LIVEID+usd.getSourceID())){
                    continue;
                }
                SuperLiveChannelDto superLiveChannelDto = channelMap.get(CacheConstants.SUPERLIVE_LIVEROOM_LIVEID+usd.getSourceID());
                SubscribeEventDto subscribeEventDto = new SubscribeEventDto();
                subscribeEventDto.setAppId(usd.getcAppID());
                subscribeEventDto.setSourceID(usd.getSourceID());
                subscribeEventDto.setUserId(usd.getUserID());
                subscribeEventDto.setSourceType(usd.getSourceType());
                subscribeEventDto.setChannel(superLiveChannelDto);
                if (superLiveChannelDto != null && superLiveChannelDto.getCur() != null)
                    subscribeEventDto.setTitle(superLiveChannelDto.getCur().getTitle());
                list.add(subscribeEventDto);
            }
            response.setData(sortSubscribeEventDto(list));
            response.setTotalCount(list.size());
            response.setCurrentIndex(offSet);
            response.setNextIndex(offSet + 1);

        } else {
            setErrorResponse(response, ErrorCodeConstants.SUBSCRIBE_USER_LIST_FAIL_1,
                    ErrorCodeConstants.SUBSCRIBE_USER_LIST_FAIL_1, param.getLangcode());
        }

        return response;
    }

    /**
     * 直播列表排序
     * 直播中 > 未开始 > 回看
     * 即将直播，根据开始时间升序排列
     * 直播中、回看的数据，根据开始时间降序排列
     * @param data
     */
    private List<SubscribeEventDto> sortSubscribeEventDto(List<SubscribeEventDto> data) {
        if (data != null && data.size() > 0) {
            Collections.sort(data, new Comparator<SubscribeEventDto>() {

                @Override
                public int compare(SubscribeEventDto o1, SubscribeEventDto o2) {
                    // TODO Auto-generated method stub
                    if (o1 != null && o1.getChannel() != null && o1.getChannel().getCur() != null && o2 != null
                            && o2.getChannel() != null && o2.getChannel().getCur() != null) {
                        SuperLiveProgramDto program1 = o1.getChannel().getCur();
                        SuperLiveProgramDto program2 = o2.getChannel().getCur();
                        try {
                            // 获取直播状态
                            int status1 = Integer.parseInt(program1.getStatus());
                            int status2 = Integer.parseInt(program2.getStatus());
                            int statusSort = compareLiveStatus(status1, status2);

                            // 直播状态不同的，直接根据状态返回，列表需要倒序排列，状态越高越要往前排，需要返回相反数
                            if (statusSort != 0) {
                                return 0 - statusSort;
                            }
                            // 直播开始时间
                            long beginTime1 = CalendarUtil.parseDate(program1.getPlayTime(),
                                    CalendarUtil.SIMPLE_DATE_FORMAT).getTime();
                            long beginTime2 = CalendarUtil.parseDate(program2.getPlayTime(),
                                    CalendarUtil.SIMPLE_DATE_FORMAT).getTime();

                            if (status1 == 1 && status2 == 1) {
                                // 如果是即将直播，开始时间早的，排前面
                                if (beginTime1 > beginTime2) {
                                    return 1;
                                } else if (beginTime1 < beginTime2) {
                                    return -1;
                                } else {
                                    return 0;
                                }
                            } else {
                                // 开始时间晚的，排前面
                                if (beginTime1 > beginTime2) {
                                    return -1;
                                } else if (beginTime1 < beginTime2) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }

                        } catch (Exception e) {
                            log.error("sort live error", e);
                        }
                        return 0;
                    } else if (o1 == null || o1.getChannel() == null || o1.getChannel().getCur() == null) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });

        }

        return data;
    }

    /**
     * 比较直播的状态
     * 直播中 > 未开始 > 回看
     * 直播中:2 未开始:1 回看:3
     * @param proto
     * @param target
     * @return
     */
    private int compareLiveStatus(int proto, int target) {
        if (proto == 2) {
            return (target == 2) ? 0 : 1;
        } else if (proto == 1) {
            if (target == 2) {
                return -1;
            } else if (target == 1) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return target - proto;
        }
    }

}
