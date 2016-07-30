package xserver.api.module.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xserver.api.constant.ErrorCodeConstants;
import xserver.api.dto.ValueDto;
import xserver.api.module.BaseService;
import xserver.api.module.user.builder.PlayRecordBuilder;
import xserver.api.module.user.dto.PlayRecordInfoDto;
import xserver.api.module.user.dto.SuperLivePlayRecordInfoDto;
import xserver.api.module.vip.util.VipUtil;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.common.dto.superlive.v2.SuperLiveChannelDto;
import xserver.lib.tp.user.UserMsgCodeConstant;
import xserver.lib.tp.user.UserTpConstant;
import xserver.lib.tp.user.UserTpConstantUtil;
import xserver.lib.tp.user.request.DeletePlayRecordRequest;
import xserver.lib.tp.user.request.GetPlayRecordRequest;
import xserver.lib.tp.user.request.UpdatePlayRecordRequest;
import xserver.lib.tp.user.response.GetPlayRecordTpResponse;
import xserver.lib.tp.user.response.PlayRecordCommonTpResponse;
import xserver.lib.tp.user.response.PlayRecordInfoTpResponse;
import xserver.lib.tp.user.response.PlayRecordPageTpResponse;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.tpcache.cache.PlayCache;

@Service
public class PlayLogService extends BaseService {

    /**
     * 获取用户播放记录
     * @param getPlayRecordRequest
     * @param locale
     * @return
     */
    public PageResponse<PlayRecordInfoDto> getPlayRecord(GetPlayRecordRequest getPlayRecordRequest, Locale locale) {
        PageResponse<PlayRecordInfoDto> response = new PageResponse<PlayRecordInfoDto>();
        String logPrefix = "getPlayRecord_" + getPlayRecordRequest.getUserid();
        String errorCode = null;

        int validCode = getPlayRecordRequest.assertValid();
        if (getPlayRecordRequest == null || UserMsgCodeConstant.USER_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.USER_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    errorCode + VipUtil.parseErrorMsgCode(UserConstants.PLAY_RECORD, validCode), locale);
        } else {
            GetPlayRecordTpResponse tpResponse = this.facadeTpDao.getPlayLogTpDao().getPlayRecordList(
                    getPlayRecordRequest);

            if (tpResponse == null || tpResponse.getCode() == null
                    || !UserTpConstant.USERCETER_PLAY_RECORD_RESPONSE_CODE_200.equals(tpResponse.getCode())) {
                errorCode = ErrorCodeConstants.USER_GET_PLAY_RECORD_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: get play record failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                PlayRecordPageTpResponse pageResponse = tpResponse.getData();
                List<PlayRecordInfoTpResponse> items = pageResponse.getItems();
                List<PlayRecordInfoDto> dtoList = new LinkedList<PlayRecordInfoDto>();

                if (!CollectionUtils.isEmpty(items)) {
                    List<String> showAlbumInfoList = new ArrayList<String>();
                    List<String> liveKeyList = new ArrayList<String>();
                    String queryType = getPlayRecordRequest.getType();
                    for (PlayRecordInfoTpResponse playRecordItem : items) {

                        // 超级live播放记录临时逻辑
                        PlayRecordInfoDto playRecord = PlayRecordBuilder.build(playRecordItem, queryType,
                                this.tpCacheTemplate, locale);

                        if (playRecord != null) {
                            dtoList.add(playRecord);
                        }

                        // 需要单独显示专辑信息
                        if (needShowAlbumInfo(playRecord)) {
                            showAlbumInfoList.add(playRecord.getAlbumId());
                        }

                        // 处理直播，轮播，卫视台等类型的播放记录
                        if (StringUtils.isNotBlank(playRecordItem.getType())
                                && UserTpConstantUtil.isSuperLivePlayRecordType(playRecordItem.getType())) {
                            // 直播和其他数据的key前缀不一样
                            String key = UserTpConstant.USERCETER_PLAY_RECORD_QUERY_TYPE_2.equals(playRecordItem
                                    .getType()) ? CacheConstants.SUPERLIVE_LIVEROOM_LIVEID
                                    : CacheConstants.SUPERLIVE_CHANNEL_ID;
                            liveKeyList.add(key + playRecordItem.getOid());
                        }
                    }

                    if (showAlbumInfoList.size() > 0) {
                        Map<String, PlayCache> albumCacheMap = tpCacheTemplate.mget(showAlbumInfoList, PlayCache.class);

                        for (PlayRecordInfoDto record : dtoList) {
                            if (albumCacheMap.get(record.getAlbumId()) != null) {
                                record.setImg_400X250(albumCacheMap.get(record.getAlbumId()).getAPic(400, 250));
                            }
                        }
                    }
                    // 处理直播，轮播，卫视台等类型的播放记录
                    if (!CollectionUtils.isEmpty(liveKeyList)) {
                        Map<String, SuperLiveChannelDto> liveChannelMap = tpCacheTemplate.mget(liveKeyList,
                                SuperLiveChannelDto.class);
                        if (liveChannelMap == null) {
                            liveChannelMap = new HashMap<String, SuperLiveChannelDto>();
                        }
                        for (Iterator<PlayRecordInfoDto> it = dtoList.iterator(); it.hasNext();) {
                            PlayRecordInfoDto playRecord = it.next();
                            if (playRecord instanceof SuperLivePlayRecordInfoDto) {
                                // 直播和其他数据的key前缀不一样
                                String key = UserTpConstant.USERCETER_PLAY_RECORD_QUERY_TYPE_2.equals(playRecord
                                        .getType()) ? CacheConstants.SUPERLIVE_LIVEROOM_LIVEID
                                        : CacheConstants.SUPERLIVE_CHANNEL_ID;
                                SuperLiveChannelDto channel = liveChannelMap.get(key + playRecord.getOid());
                                if (channel != null) {
                                    // 设置直播，轮播，卫视台的数据
                                    PlayRecordBuilder.fillSuperLivePlayRecord((SuperLivePlayRecordInfoDto) playRecord,
                                            channel);
                                } else {
                                    // 如果对应的直播，轮播已经下线，则从播放记录中移除掉
                                    it.remove();
                                    log.warn("PlayRecordBuilder#build: get live [" + playRecord.getOid() + "] null");
                                }
                            }
                        }
                    }

                }

                // 因为服务端有过滤，所以这里的分页信息不准确
                response.setTotalCount(dtoList.size());
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

    /**
     * 新增或更新播放记录
     * @param updatePlayRecordRequest
     * @param locale
     * @return
     */
    public Response<ValueDto<Boolean>> updatePlayRecord(UpdatePlayRecordRequest updatePlayRecordRequest, Locale locale) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        String logPrefix = "updatePlayRecord_" + updatePlayRecordRequest.getUserid();
        String errorCode = null;

        int validCode = updatePlayRecordRequest.assertValid();
        if (updatePlayRecordRequest == null || UserMsgCodeConstant.USER_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.USER_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    errorCode + VipUtil.parseErrorMsgCode(UserConstants.PLAY_RECORD, validCode), locale);
        } else {
            // 这里没有设置UpdatePlayRecordRequest#channelId，新增后，用户中心查询接口返回值cid为0，数据不全，目前为全部由客户端传值，可以考虑在服务端仅从缓存中查一次进行数据补全
            Long htime = updatePlayRecordRequest.getHtime();
            // 传-1表示播放结束，不需要进行毫秒和秒之间的单位转换
            if (htime != null && htime > 0) {
                updatePlayRecordRequest.setHtime(htime / 1000);
            }

	        /**
	         * 设置用户播放记录的设备型号
	         */
            setPlayProduct(updatePlayRecordRequest);

            PlayRecordCommonTpResponse updateResponse = this.facadeTpDao.getPlayLogTpDao().updatePlayRecord(
                    updatePlayRecordRequest);

            this.log.info(logPrefix + ": invoke return [" + updateResponse + "]");
            if (updateResponse == null || updateResponse.getCode() == null
                    || !UserTpConstant.USERCETER_PLAY_RECORD_RESPONSE_CODE_200.equals(updateResponse.getCode())) {
                errorCode = ErrorCodeConstants.USER_UPDATE_PLAY_RECORD_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: update play record [albumid="
                        + updatePlayRecordRequest.getAlbumId() + ",videoid=" + updatePlayRecordRequest.getVideoId()
                        + ",htime=" + updatePlayRecordRequest.getHtime() + "] failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                response.setData(new ValueDto<Boolean>(true));
                response.setStatus(ErrorCodeConstants.RESPONSE_SUC_CODE);
            }

            /*
             * AlbumCacheEntity album =
             * this.facadeService.getVideoService().getAlbumById(
             * updatePlayRecordRequest.getAlbumId(), Copyright.PHONE);
             * VideoCacheEntity video =
             * this.facadeService.getVideoService().getVideoById(
             * updatePlayRecordRequest.getVideoId(), Copyright.PHONE);
             * if (album == null && video == null) {
             * errorCode = ErrorCodeConstants.USER_UPDATE_PLAY_RECORD_FAILURE;
             * this.log.error(logPrefix + "[errorCode=" + errorCode +
             * "]: update play record [albumid="
             * + updatePlayRecordRequest.getAlbumId() + ",videoid=" +
             * updatePlayRecordRequest.getVideoId()
             * + ",htime=" + updatePlayRecordRequest.getHtime() + "] failed.");
             * this.setErrorResponse(response, errorCode, errorCode, locale);
             * } else {
             * updatePlayRecordRequest.setChannelId((album != null) ?
             * album.getCategory() : video.getCategory());
             * updatePlayRecordRequest.setHtime(updatePlayRecordRequest.getHtime(
             * ) / 1000);
             * updatePlayRecordRequest.setNextVideoId(0L);
             * PlayRecordCommonTpResponse updateResponse =
             * this.facadeTpDao.getPlayLogTpDao().updatePlayRecord(
             * updatePlayRecordRequest);
             * this.log.info(logPrefix + ": invoke return [" + updateResponse +
             * "]");
             * if (updateResponse == null || updateResponse.getCode() == null
             * || UserTpConstant.USERCETER_PLAYRECORD_RESPONSE_CODE_200 !=
             * updateResponse.getCode()) {
             * errorCode = ErrorCodeConstants.USER_UPDATE_PLAY_RECORD_FAILURE;
             * this.log.error(logPrefix + "[errorCode=" + errorCode +
             * "]: update play record [albumid="
             * + updatePlayRecordRequest.getAlbumId() + ",videoid=" +
             * updatePlayRecordRequest.getVideoId()
             * + ",htime=" + updatePlayRecordRequest.getHtime() + "] failed.");
             * this.setErrorResponse(response, errorCode, errorCode, locale);
             * } else {
             * response.setData(new ValueDto<Boolean>(true));
             * response.setStatus(ErrorCodeConstants.RESPONSE_SUC_CODE);
             * }
             * }
             */
        }

        return response;
    }

    /**
     * 设置用户播放记录的设备型号
     * @param updatePlayRecordRequest
     */
    private void setPlayProduct(UpdatePlayRecordRequest updatePlayRecordRequest) {
		if(updatePlayRecordRequest.getProduct() != null) {
			String playProduct = null;
			if(updatePlayRecordRequest.getProduct().contains(UserConstants.LE_1)){
				playProduct = UserConstants.USER_PLAY_RECORD_PRODUCT.get(UserConstants.LE_1);
			}
			
			if(updatePlayRecordRequest.getProduct().contains(UserConstants.LE_1_PRO)) {
				playProduct = UserConstants.USER_PLAY_RECORD_PRODUCT.get(UserConstants.LE_1_PRO);
			}
			
			if(updatePlayRecordRequest.getProduct().contains(UserConstants.LE_MAX)) {
				playProduct = UserConstants.USER_PLAY_RECORD_PRODUCT.get(UserConstants.LE_MAX);
			}
			
			if(playProduct != null){
				updatePlayRecordRequest.setProduct(playProduct);
			}
		}
	}

    /**
     * 删除或清空播放记录
     * @param deletePlayRecordRequest
     * @param locale
     * @return
     */
    public Response<ValueDto<Boolean>> deletePlayRecord(DeletePlayRecordRequest deletePlayRecordRequest, Locale locale) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        String logPrefix = "deletePlayRecord_" + deletePlayRecordRequest.getUserid();
        String errorCode = null;

        int validCode = deletePlayRecordRequest.assertValid();
        if (deletePlayRecordRequest == null || UserMsgCodeConstant.USER_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.USER_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    errorCode + VipUtil.parseErrorMsgCode(UserConstants.PLAY_RECORD, validCode), locale);
        } else {
            PlayRecordCommonTpResponse updateResponse = this.facadeTpDao.getPlayLogTpDao().deletePlayRecord(
                    deletePlayRecordRequest);

            if (updateResponse == null || updateResponse.getCode() == null
                    || !UserTpConstant.USERCETER_PLAY_RECORD_RESPONSE_CODE_200.equals(updateResponse.getCode())) {
                errorCode = ErrorCodeConstants.USER_DELETE_PLAY_RECORD_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: delete play record [albumid="
                        + deletePlayRecordRequest.getAlbumId() + ", videoid=" + deletePlayRecordRequest.getVideoId()
                        + "] failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                response.setData(new ValueDto<Boolean>(true));
                response.setStatus(ErrorCodeConstants.RESPONSE_SUC_CODE);
            }
        }

        return response;
    }

    private boolean needShowAlbumInfo(PlayRecordInfoDto playRecord) {
        boolean need = false;

        String vt = playRecord.getVideoType();
        String albumId = playRecord.getAlbumId();
        if (StringUtils.isNotEmpty(vt) && vt.equalsIgnoreCase("180001")) {
            if (StringUtils.isNotEmpty(albumId) && !albumId.equalsIgnoreCase("0")) {
                need = true;
            }
        }

        return need;
    }
}
