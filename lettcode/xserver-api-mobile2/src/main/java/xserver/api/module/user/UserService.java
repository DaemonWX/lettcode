package xserver.api.module.user;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import xserver.api.constant.ErrorCodeConstants;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.superlive.SuperLiveConstant;
import xserver.api.module.user.dto.CheckUserBindMobileInfoDto;
import xserver.api.module.user.dto.ProfileItemDto;
import xserver.api.module.user.dto.UserFavMutilCheckDto;
import xserver.api.module.user.dto.UserFavSimpleDto;
import xserver.api.module.user.dto.UserInfoDto;
import xserver.api.module.user.dto.UserLiveFavDto;
import xserver.api.module.video.dto.UserAttentionStatusDto;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.common.constant.SuperLiveDictionaryConstant;
import xserver.common.dto.superlive.v2.SuperLiveChannelDto;
import xserver.lib.tp.TpErrorCodeConstant;
import xserver.lib.tp.cloud.response.AttentionAddOrDelTpResponse;
import xserver.lib.tp.cloud.response.UserHotWordsStatisticsResponse;
import xserver.lib.tp.cloud.response.UserHotWordsStatusReponse;
import xserver.lib.tp.user.request.UserFavCheckRequest;
import xserver.lib.tp.user.request.UserFavRequest;
import xserver.lib.tp.user.response.LetvUserInfoResponse;
import xserver.lib.tp.user.response.LetvUserResponse;
import xserver.lib.tp.user.response.UserFavBaseResponse;
import xserver.lib.tp.user.response.UserFavListTpResponse;
import xserver.lib.tp.user.response.UserFavListTpResponse.UserFavData;
import xserver.lib.tp.user.response.UserFavMutilCheckResponse;
import xserver.lib.tp.user.response.UserFavMutilCheckResponse.UserFavMutilCheck;
import xserver.lib.tpcache.CacheConstants;

/**
 * 用户业务相关
 */
@Service
public class UserService extends BaseService {
    /**
     * @param token
     * @return
     */
    public Response<UserInfoDto> tokenLogin(String token, CommonParam commonParam) {
        LetvUserResponse letvUserResponse = this.facadeTpDao.getUserTpDao().getUserInfo(token);
        Response<UserInfoDto> response = new Response<UserInfoDto>();
        /**
         * 用户token合法
         */
        if (this.checkValidTokenLogin(response, letvUserResponse, commonParam)) {
            UserInfoDto userInfoDto = new UserInfoDto();
            LetvUserInfoResponse letvUserInfoResponse = letvUserResponse.getBean();
            // 用户昵称
            userInfoDto.setNickName(letvUserInfoResponse.getNickname());
            // 用户 名
            userInfoDto.setUserName(letvUserInfoResponse.getUsername());
            // 用户id
            userInfoDto.setUid(letvUserInfoResponse.getUid());
            // 用户是否进行实名认证
            if(letvUserInfoResponse.getIsIdentify() != null){
                userInfoDto.setIdentify(letvUserInfoResponse.getIsIdentify() == 1 ? "1" : "0");
            }
            // 用户头像
            setPic(userInfoDto, letvUserInfoResponse);
            // 影视剧、明星关注数
            userInfoDto.setHotWordNum(0);
            userInfoDto.setStarNum(0);
            UserHotWordsStatisticsResponse statisticsesponse = this.facadeTpDao.getAttTpDao()
                    .getUserHotWordsStatistics(token);
            if (statisticsesponse != null && "10000".equals(statisticsesponse.getErrno())) {
                if (statisticsesponse.getData() != null) {
                    userInfoDto.setHotWordNum(statisticsesponse.getData().getTelevision());
                    userInfoDto.setStarNum(statisticsesponse.getData().getStar());
                }
            }
            response.setData(userInfoDto);
        }
        return response;
    }

    /**
     * 兼容用户头像
     * @param userInfoDto
     * @param letvUserInfoResponse
     */
    private void setPic(UserInfoDto userInfoDto, LetvUserInfoResponse letvUserInfoResponse) {
        String picsStr = letvUserInfoResponse.getPicture();
        if (picsStr != null) {
            String[] pics = picsStr.split(",");
            if (pics != null && pics.length > 1) {
                userInfoDto.setPicture(pics[1]);
            } else {
                userInfoDto.setPicture(picsStr);
            }
        }
    }

    /**
     * TODO 待整理错误码
     * 校验第三方结果及错误码
     * @param response
     * @param letvUserResponse
     * @param commonParam
     * @return
     */
    private boolean checkValidTokenLogin(Response<UserInfoDto> response, LetvUserResponse letvUserResponse,
            CommonParam commonParam) {
        if (letvUserResponse == null) {// 返回结果为空
            this.setErrorResponse(response, ErrorCodeConstants.USER_TOKEN_EXPIRE, ErrorCodeConstants.USER_TOKEN_EXPIRE,
                    commonParam.getLangcode());
            return false;
        }
        if (letvUserResponse.getInnerCode() != null) {// 服务器内部有错误码，例如切服
            if (TpErrorCodeConstant.CLOSE_SERVER_CODE.equals(letvUserResponse.getInnerCode())) {// 切服务

                this.setErrorResponse(response, ErrorCodeConstants.USER_TOKEN_EXPIRE,
                        ErrorCodeConstants.USER_TOKEN_EXPIRE, commonParam.getLangcode());
            } else {
                this.setErrorResponse(response, ErrorCodeConstants.USER_TOKEN_EXPIRE,
                        ErrorCodeConstants.USER_TOKEN_EXPIRE, commonParam.getLangcode());
            }
            return false;
        }
        if (letvUserResponse.getStatus() != null && letvUserResponse.getStatus() == 0) {
            this.setErrorResponse(response, ErrorCodeConstants.USER_TOKEN_EXPIRE, ErrorCodeConstants.USER_TOKEN_EXPIRE,
                    commonParam.getLangcode());
            return false;
        }
        if (letvUserResponse.getBean() == null) {
            this.setErrorResponse(response, ErrorCodeConstants.USER_NOT_EXIST, ErrorCodeConstants.USER_NOT_EXIST,
                    commonParam.getLangcode());
            return false;
        }
        return true;
    }

    /**
     * 获取“我的”页面菜单列表
     * @return
     */
    public PageResponse<ProfileItemDto> getProfileList() {
        // TODO 第一期写固定值，后期可修改为从缓存或态文件中读取
        PageResponse<ProfileItemDto> response = new PageResponse<ProfileItemDto>();
        List<ProfileItemDto> data = new ArrayList<ProfileItemDto>(12);
        data.add(new ProfileItemDto("1", "离线缓存"));
        data.add(new ProfileItemDto("2", "播放记录"));
        data.add(new ProfileItemDto("3", "关注"));
        data.add(new ProfileItemDto("4", "预约"));
        data.add(new ProfileItemDto("5", "开通VIP"));
        data.add(new ProfileItemDto("6", "扫一扫"));
        data.add(new ProfileItemDto("7", "乐迷论坛"));
        data.add(new ProfileItemDto("8", "设置"));

        response.setTotalCount(data.size());
        response.setData(data);
        response.setStatus(ErrorCodeConstants.RESPONSE_SUC_CODE);
        return response;
    }

    /**
     * 添加收藏
     * @param request
     * @return
     */
    public Response<UserFavSimpleDto> addFav(UserFavRequest request) {
        Response<UserFavSimpleDto> response = new Response<UserFavSimpleDto>();
        if (request != null) {
            UserFavBaseResponse tpResponse = this.facadeTpDao.getUserTpDao().addFav(request);
            if (tpResponse != null) {
                UserFavSimpleDto dto = new UserFavSimpleDto();
                response.setData(dto);
                if ("200".equals(tpResponse.getCode())) {
                    dto.setFavorite(UserFavSimpleDto.FAVORITE_SUCCESS);
                } else {
                    dto.setFavorite(UserFavSimpleDto.FAVORITE_FAILURE);
                    if (tpResponse.getData() != null && tpResponse.getData().getMsg() != null) {
                        dto.setMessage(tpResponse.getData().getMsg());
                    }
                }
            } else {
                response.setStatus(0);
            }
        }
        return response;
    }

    /**
     * 校验是否已收藏
     * @param request
     * @return
     */
    public Response<UserFavSimpleDto> checkFav(UserFavRequest request) {
        Response<UserFavSimpleDto> response = new Response<UserFavSimpleDto>();
        if (request != null && StringUtils.isNotBlank(request.getSso_tk())) {
            UserFavBaseResponse tpResponse = this.facadeTpDao.getUserTpDao().checkFav(request);
            if (tpResponse != null) {
                UserFavSimpleDto dto = new UserFavSimpleDto();
                response.setData(dto);
                if ("200".equals(tpResponse.getCode())) {
                    dto.setFavorite(UserFavSimpleDto.FAVORITE_SUCCESS);
                } else {
                    dto.setFavorite(UserFavSimpleDto.FAVORITE_FAILURE);
                    if (tpResponse.getData() != null && tpResponse.getData().getMsg() != null) {
                        dto.setMessage(tpResponse.getData().getMsg());
                    }
                }
            } else {
                response.setStatus(0);
            }
        } else {
            response.setStatus(0);
            response.setErrorCode(ErrorCodeConstants.USER_NOT_LOGIN);
        }
        return response;
    }

    /**
     * 取消收藏
     * @param request
     * @return
     */
    public Response<UserFavSimpleDto> delFav(UserFavRequest request) {
        Response<UserFavSimpleDto> response = new Response<UserFavSimpleDto>();
        if (request != null) {
            UserFavBaseResponse tpResponse = this.facadeTpDao.getUserTpDao().addFav(request);
            if (tpResponse != null) {
                UserFavSimpleDto dto = new UserFavSimpleDto();
                response.setData(dto);
                if ("200".equals(tpResponse.getCode())) {
                    dto.setFavorite(UserFavSimpleDto.FAVORITE_SUCCESS);
                } else {
                    dto.setFavorite(UserFavSimpleDto.FAVORITE_FAILURE);
                    if (tpResponse.getData() != null && tpResponse.getData().getMsg() != null) {
                        dto.setMessage(tpResponse.getData().getMsg());
                    }
                }
            } else {
                response.setStatus(0);
            }
        }
        return response;
    }

    /**
     * 获取轮播收藏列表
     * @param request
     * @return
     */
    public PageResponse<UserLiveFavDto> getLiveFavList(UserFavRequest request) {
        PageResponse<UserLiveFavDto> response = new PageResponse<UserLiveFavDto>();
        if (request != null) {

            UserFavListTpResponse tpResponse = this.facadeTpDao.getUserTpDao().listFav(request);

            if (tpResponse == null || !"200".equals(tpResponse.getCode())) {
                response.setStatus(0);
                if (tpResponse != null && tpResponse.getData() != null) {
                    response.setErrorMessage(tpResponse.getData().getMsg());
                }
            } else {
                response.setTotalCount(tpResponse.getData().getTotal());
                List<UserFavData> favList = tpResponse.getData().getItems();
                if (favList != null && favList.size() > 0) {

                    List<String> channelKeys = new ArrayList<String>();
                    for (UserFavData userFav : favList) {
                        channelKeys.add(CacheConstants.SUPERLIVE_CHANNEL_ID + userFav.getChannel_id());
                    }

                    Map<String, SuperLiveChannelDto> channelMap = this.tpCacheTemplate.mget(channelKeys,
                            SuperLiveChannelDto.class);
                    if (channelMap != null) {
                        List<UserLiveFavDto> dtoList = new LinkedList<UserLiveFavDto>();
                        UserLiveFavDto dto = null;
                        for (UserFavData userFav : favList) {
                            dto = new UserLiveFavDto();
                            dto.setFavid(userFav.getFavorite_id());
                            SuperLiveChannelDto channelDto = channelMap.get(CacheConstants.SUPERLIVE_CHANNEL_ID
                                    + userFav.getChannel_id());
                            dto.setChannel(channelDto);
                            if (channelDto != null) {
                                if (StringUtils.isNotBlank(channelDto.getSignal())
                                        && StringUtils.isNotBlank(channelDto.getChannelClass())) {
                                    // 卫视台，地方台合并; signal=2表示来源为电视台，统一合并为电视台
                                    if (SuperLiveConstant.WEISHI_SOURCE.equals(channelDto.getSignal())) {
                                        dto.setCid(SuperLiveDictionaryConstant.C_WS);
                                    } else {
                                        dto.setCid(channelDto.getSignal() + channelDto.getChannelClass());
                                    }


                                }
                                dtoList.add(dto);
                            } else {
                                log.info("ingore channel fav for " + userFav.getChannel_id());
                            }

                        }
                        response.setData(dtoList);
                    }

                }

            }
        }
        return response;
    }

    /**
     * 批量校验用户收藏
     * @param data
     * @return
     */
    public Response<List<UserFavMutilCheckDto>> checkMutilFav(UserFavRequest request, List<UserFavCheckRequest> data) {
        Response<List<UserFavMutilCheckDto>> response = new Response<List<UserFavMutilCheckDto>>();
        if (request != null && data != null && data.size() > 0) {
            UserFavMutilCheckResponse tpResponse = this.facadeTpDao.getUserTpDao().mutilCheckFav(request, data);
            if (tpResponse != null && "200".equals(tpResponse.getCode()) && tpResponse.getData() != null) {
                List<UserFavMutilCheckDto> dtoList = new LinkedList<UserFavMutilCheckDto>();
                UserFavMutilCheckDto checkDto = null;
                for (UserFavMutilCheck check : tpResponse.getData()) {
                    checkDto = new UserFavMutilCheckDto();
                    checkDto.setChannelId(check.getChannel_id());
                    checkDto.setPid(check.getPlay_id());
                    checkDto.setVid(check.getVideo_id());
                    checkDto.setFavType(check.getFavorite_type());
                    checkDto.setFavorite("true".equalsIgnoreCase(check.getIsfavorite()) ? "1" : "0");
                    dtoList.add(checkDto);
                }
                response.setData(dtoList);
            } else {
                response.setStatus(0);
            }
        }
        return response;
    }

    /**
     * 关注/取消关注/查询用户对乐词的关注状态
     * @param id
     * @param operate
     * @param token
     * @param param
     * @return
     */
    public UserAttentionStatusDto getUserHotWords(Integer id, Integer operate, String token, CommonParam param) {
        UserAttentionStatusDto dto = new UserAttentionStatusDto();
        if (operate != null) {
            switch (operate) {
            case 0:// 取消关注
                AttentionAddOrDelTpResponse cancelResponse = this.facadeTpDao.getAttTpDao().cancelAttention(id, token,
                        0, null);
                if (cancelResponse != null && "10000".equals(cancelResponse.getErrno())) {
                    dto.setSuccess("1");
                } else {
                    dto.setSuccess("0");
                }
                break;
            case 1:// 关注
                AttentionAddOrDelTpResponse response = this.facadeTpDao.getAttTpDao().attention(id, token, 0);
                if (response != null && "10000".equals(response.getErrno())) {
                    dto.setSuccess("1");
                } else {
                    dto.setSuccess("0");
                }
                break;
            case 2:// 查询关注状态
                UserHotWordsStatusReponse statusResponse = this.facadeTpDao.getAttTpDao().getUserHotWordsStatus(id,
                        token);
                dto.setAttention("0");
                if (statusResponse != null && "10000".equals(statusResponse.getErrno())) {
                    if (statusResponse.getData() != null
                            && StringUtils.equals(statusResponse.getData().getStatus(), "1")) {
                        dto.setAttention("1");
                    }
                }
                break;
            default:
                break;
            }
        }
        return dto;
    }
    /**
     * 用户token校验是否绑定手机
     * @param token
     * @param commonParam
     * @return 绑定信息
     */
    public Response<CheckUserBindMobileInfoDto> checkmobile(String token, CommonParam commonParam) {
        LetvUserResponse letvUserResponse = this.facadeTpDao.getUserTpDao().getUserInfo(token);
        Response<CheckUserBindMobileInfoDto> response = new Response<CheckUserBindMobileInfoDto>();
        CheckUserBindMobileInfoDto checkUserBindMobileInfoDto = new CheckUserBindMobileInfoDto();
        if(letvUserResponse!=null){
            if(letvUserResponse.getBean()!=null
                    &&letvUserResponse.getBean().getMobile()!=null&&
                    !"".equals(letvUserResponse.getBean().getMobile().trim())){
                checkUserBindMobileInfoDto.setIsBind(true);
//                checkUserBindMobileInfoDto.setUsername(letvUserResponse.getBean().getUsername());
//                checkUserBindMobileInfoDto.setMobile(letvUserResponse.getBean().getMobile());
            }else{
//                checkUserBindMobileInfoDto.setUsername(letvUserResponse.getBean().getUsername());
//                checkUserBindMobileInfoDto.setMobile(letvUserResponse.getBean().getMobile());
                String url = "http://sso.letv.com/user/setUserStatus?tk="+token+"&from=superlive&next_action=http://sso.letv.com/user/mChangeBindMobile";
                checkUserBindMobileInfoDto.setIsBind(false);
                checkUserBindMobileInfoDto.setBindMobileUrl(url);
            }
        }else{
            setErrorResponse(response, ErrorCodeConstants.USER_NOT_EXIST,  ErrorCodeConstants.USER_NOT_EXIST,
                    commonParam.getLangcode());
        }
        response.setData(checkUserBindMobileInfoDto);
        return response;
    }

}
