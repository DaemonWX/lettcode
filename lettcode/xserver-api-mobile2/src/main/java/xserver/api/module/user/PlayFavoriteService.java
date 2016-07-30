package xserver.api.module.user;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xserver.api.constant.ErrorCodeConstants;
import xserver.api.dto.ValueDto;
import xserver.api.module.BaseService;
import xserver.api.module.user.dto.PlayFavoriteInfoDto;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.lib.constant.VideoConstants.Category;
import xserver.lib.tp.user.UserTpConstant;
import xserver.lib.tp.user.request.AddPlayFavoriteRequest;
import xserver.lib.tp.user.request.CheckPlayFavoriteRequest;
import xserver.lib.tp.user.request.DeletePlayFavoriteRequest;
import xserver.lib.tp.user.request.GetPlayFavoriteRequest;
import xserver.lib.tp.user.response.GetPlayFavoriteTpResponse;
import xserver.lib.tp.user.response.PlayFavoriteCommonTpResponse;
import xserver.lib.tp.user.response.PlayFavoriteInfoTpResponse;
import xserver.lib.tp.user.response.PlayFavoritePageTpResponse;

@Service
public class PlayFavoriteService extends BaseService {

    public PageResponse<PlayFavoriteInfoDto> getPlayFavorite(GetPlayFavoriteRequest getPlayFavoriteRequest,
            Locale locale) {
        PageResponse<PlayFavoriteInfoDto> response = new PageResponse<PlayFavoriteInfoDto>();
        String logPrefix = "getPlayFavorite_" + getPlayFavoriteRequest.getUserid();
        String errorCode = null;

        if (!getPlayFavoriteRequest.assertValid()) {
            errorCode = ErrorCodeConstants.USER_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode, errorCode, locale);
        } else {
            GetPlayFavoriteTpResponse tpResponse = this.facadeTpDao.getPlayFavoriteTpDao().getPlayFavoriteList(
                    getPlayFavoriteRequest);
            if (tpResponse == null || tpResponse.getCode() == null
                    || !UserTpConstant.USERCETER_PLAY_FAVORITE_RESPONSE_CODE_200.equals(tpResponse.getCode())) {
                // TODO 异常 或 返回错误
                errorCode = ErrorCodeConstants.USER_GET_PLAY_FAVORITE_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: get play favorite failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                PlayFavoritePageTpResponse pageResponse = tpResponse.getData();
                List<PlayFavoriteInfoTpResponse> items = pageResponse.getItems();
                List<PlayFavoriteInfoDto> dtoList = new LinkedList<PlayFavoriteInfoDto>();

                if (!CollectionUtils.isEmpty(items)) {

                    for (PlayFavoriteInfoTpResponse item : items) {
                        if (item != null) {
                            PlayFavoriteInfoDto dto = new PlayFavoriteInfoDto();
                            dto.setId(item.getFavorite_id());
                            dto.setCategoryId(item.getCategory());
                            dto.setAlbumId(item.getPlay_id());
                            dto.setVideoId(item.getVideo_id());
                            dto.setTitle(item.getTitle());
                            dto.setSubTitle(item.getSub_title());
                            dto.setCategoryName(item.getCategory_name());
                            dto.setHasCopyright(Boolean.TRUE);
                            dto.setSubCategory(item.getSub_category());
                            dto.setLatestEpisode(item.getPlatform_now_episodes());
                            dto.setImg_400X250(item.getPic(400, 250));

                            // 设置视频更新状态
                            String updateStatus = null;
                            if (StringUtils.isEmpty(item.getPlatform_now_episodes())) {
                                // 无更新信息则不显示更新状态
                                updateStatus = "";
                            } else if (StringUtils.equals(String.valueOf(Category.VARIETY), item.getCategory())
                                    || StringUtils.equals(String.valueOf(Category.DFILM), item.getCategory())) {
                                // 综艺或记录片显示xx期
                                updateStatus = "更新至第" + item.getPlatform_now_episodes() + "期";
                            } else if (StringUtils.equals(String.valueOf(Category.TV), item.getCategory())
                                    || StringUtils.equals(String.valueOf(Category.CARTOON), item.getCategory())) {
                                // 电视剧或动漫显示xx集
                                if (item.isUpdateEnd()) {
                                    updateStatus = "共" + item.getPlatform_now_episodes() + "集全";
                                } else {
                                    updateStatus = "更新至第" + item.getPlatform_now_episodes() + "集";
                                }
                            } else {
                                // 其他频道的不显示更新状态
                                updateStatus = "";
                            }

                            dto.setUpdateStatus(updateStatus);
                            if (UserTpConstant.PLAY_FAVORITE_OFFLINE_STATUS_0.equals(item.getOffline())
                                    && StringUtils.isNotBlank(item.getTitle())) {
                                dto.setOnlineStatus("1");
                            } else {
                                dto.setOnlineStatus("0");
                            }

                            dto.setOffline(item.getOffline());

                            dtoList.add(dto);
                        }
                    }
                }

                // 因为服务端有过滤，所以这里的分页信息不准确
                response.setTotalCount(pageResponse.getTotal());
                int currentIndex = pageResponse.getPage() == null ? 1 : pageResponse.getPage();
                response.setCurrentIndex(currentIndex);
                int nextIndex = pageResponse.hasNextPage() ? currentIndex + 1 : currentIndex;
                response.setNextIndex(nextIndex);

                response.setData(dtoList);
                response.setStatus(ErrorCodeConstants.RESPONSE_SUC_CODE);
            }
        }

        return response;
    }

    public Response<ValueDto<Boolean>> addPlayFavorite(AddPlayFavoriteRequest addPlayFavoriteRequest, Locale locale) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        String logPrefix = "addPlayFavorite_" + addPlayFavoriteRequest.getUserid();
        String errorCode = null;

        if (!addPlayFavoriteRequest.assertValid()) {
            errorCode = ErrorCodeConstants.USER_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode, errorCode, locale);
        } else {
            PlayFavoriteCommonTpResponse addResponse = this.facadeTpDao.getPlayFavoriteTpDao().addPlayFavorite(
                    addPlayFavoriteRequest);

            if (addResponse == null || addResponse.getCode() == null
                    || !UserTpConstant.USERCETER_PLAY_FAVORITE_RESPONSE_CODE_200.equals(addResponse.getCode())) {
                errorCode = ErrorCodeConstants.USER_ADD_PLAY_FAVORITE_FALIURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: update play favorite [albumId="
                        + addPlayFavoriteRequest.getAlbumId() + ", videoId=" + addPlayFavoriteRequest.getVideoId()
                        + "] failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                response.setData(new ValueDto<Boolean>(true));
                response.setStatus(ErrorCodeConstants.RESPONSE_SUC_CODE);
            }
        }

        return response;
    }

    public Response<ValueDto<Boolean>> deletePlayFavorite(DeletePlayFavoriteRequest deletePlayFavoriteRequest,
            Locale locale) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        String logPrefix = "deletePlayFavorite_" + deletePlayFavoriteRequest.getUserid();
        String errorCode = null;

        if (!deletePlayFavoriteRequest.assertValid()) {
            errorCode = ErrorCodeConstants.USER_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode, errorCode, locale);
        } else {
            PlayFavoriteCommonTpResponse deleteResponse = this.facadeTpDao.getPlayFavoriteTpDao().deletePlayFavorite(
                    deletePlayFavoriteRequest);
            this.log.info(logPrefix + ": invoke return [" + deleteResponse + "]");
            if (deleteResponse == null || deleteResponse.getCode() == null
                    || !UserTpConstant.USERCETER_PLAY_FAVORITE_RESPONSE_CODE_200.equals(deleteResponse.getCode())) {
                errorCode = ErrorCodeConstants.USER_DELETE_PLAY_FAVORITE_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: delete play favorite [albumId="
                        + deletePlayFavoriteRequest.getAlbumId() + ", videoId="
                        + deletePlayFavoriteRequest.getVideoId() + "] failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                response.setData(new ValueDto<Boolean>(true));
                response.setStatus(ErrorCodeConstants.RESPONSE_SUC_CODE);
            }
        }

        return response;
    }

    public Response<ValueDto<Boolean>> checkPlayFavorite(CheckPlayFavoriteRequest checkPlayFavoriteRequest,
            Locale locale) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        String logPrefix = "checkPlayFavorite_" + checkPlayFavoriteRequest.getUserid();
        String errorCode = null;

        if (!checkPlayFavoriteRequest.assertValid()) {
            errorCode = ErrorCodeConstants.USER_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode, errorCode, locale);
        } else {
            PlayFavoriteCommonTpResponse addResponse = this.facadeTpDao.getPlayFavoriteTpDao().checkPlayFavorite(
                    checkPlayFavoriteRequest);

            if (addResponse == null || addResponse.getCode() == null
                    || !UserTpConstant.USERCETER_PLAY_FAVORITE_RESPONSE_CODE_200.equals(addResponse.getCode())) {
                errorCode = ErrorCodeConstants.USER_CHECK_PLAY_FAVORITE_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: check play favorite [channelId="
                        + checkPlayFavoriteRequest.getChannelId() + ", albumId="
                        + checkPlayFavoriteRequest.getAlbumId() + ", videoId=" + checkPlayFavoriteRequest.getVideoId()
                        + "] failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else if (UserTpConstant.USERCETER_PLAY_FAVORITE_RESPONSE_CODE_200.equals(addResponse.getCode())) {
                response.setData(new ValueDto<Boolean>(true));
                response.setStatus(ErrorCodeConstants.RESPONSE_SUC_CODE);
            }

            /*
            if (addResponse == null || addResponse.getCode() == null) {
                errorCode = ErrorCodeConstants.USER_ADD_PLAY_FAVORITE_FALIURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: check play favorite [channelId="
                        + checkPlayFavoriteRequest.getChannelId() + ", albumId="
                        + checkPlayFavoriteRequest.getAlbumId() + ", videoId=" + checkPlayFavoriteRequest.getVideoId()
                        + "] failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else if (UserTpConstant.USERCETER_PLAY_FAVORITE_RESPONSE_CODE_200.equals(addResponse.getCode())) {
                response.setData(new ValueDto<Boolean>(true));
                response.setStatus(ErrorCodeConstants.RESPONSE_SUC_CODE);
            } else if (UserTpConstant.USERCETER_PLAY_FAVORITE_RESPONSE_CODE_201.equals(addResponse.getCode())) {
                response.setData(new ValueDto<Boolean>(false));
                response.setStatus(ErrorCodeConstants.RESPONSE_SUC_CODE);
            } else {
                errorCode = ErrorCodeConstants.USER_ADD_PLAY_FAVORITE_FALIURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: check play favorite [channelId="
                        + checkPlayFavoriteRequest.getChannelId() + ", albumId="
                        + checkPlayFavoriteRequest.getAlbumId() + ", videoId=" + checkPlayFavoriteRequest.getVideoId()
                        + "] failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            }
            */
        }

        return response;
    }
}
