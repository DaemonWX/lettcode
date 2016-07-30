package xserver.api.module.user.builder;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xserver.api.module.user.UserConstants;
import xserver.api.module.user.dto.PlayRecordInfoDto;
import xserver.api.module.user.dto.SuperLivePlayRecordInfoDto;
import xserver.common.dto.superlive.v2.SuperLiveChannelDto;
import xserver.lib.constant.VideoConstants.Copyright;
import xserver.lib.tp.user.UserTpConstant;
import xserver.lib.tp.user.UserTpConstantUtil;
import xserver.lib.tp.user.response.PlayRecordInfoTpResponse;
import xserver.lib.tpcache.TpCacheTemplate;
import xserver.lib.util.CalendarUtil;
import xserver.lib.util.MessageUtils;

/**
 * PlayRecord转换器，将PlayRecordInfoTpResponse转换为PlayRecordInfoDto
 * @author
 */
public class PlayRecordBuilder {

    private static final Logger log = LoggerFactory.getLogger(PlayRecordBuilder.class);

    public static PlayRecordInfoDto build(final PlayRecordInfoTpResponse playRecordItem, String queryType,
            final Locale locale) {
        PlayRecordInfoDto playRecord = null;
        if (playRecordItem != null && locale != null) {
            boolean hasCopyright = true;
            // 由于历史原因，点播的播放记录并没有type字段的值，所以如果type为null，表示点播，为type赋值"1"
            String type = playRecordItem.getType();
            if (type == null) {
                type = UserTpConstant.USERCETER_PLAY_RECORD_QUERY_TYPE_1;
            }
            // 查询类型为空
            if (queryType == null) {
                return null;
            } else {
                // 查询类型不包含"0"，也没有此类型
                if (queryType.indexOf(UserTpConstant.USERCETER_PLAY_RECORD_QUERY_TYPE_0) == -1
                        && queryType.indexOf(type) == -1) {
                    return null;
                }
            }
            // type为1时，需要校验权限，即点播的播放记录需要校验权限
            if (UserTpConstant.USERCETER_PLAY_RECORD_QUERY_TYPE_1.equals(type)) {
                hasCopyright = isMobileHasCopyright(playRecordItem.getPlayPlatform());
            }
            if (hasCopyright) {
                playRecord = new PlayRecordInfoDto();

                fillPlayRecordWithTp(playRecord, playRecordItem, type, locale);
            }
        }

        return playRecord;
    }

    public static PlayRecordInfoDto build(final PlayRecordInfoTpResponse playRecordItem, String queryType,
            TpCacheTemplate tpCacheTemplate, final Locale locale) {
        PlayRecordInfoDto playRecord = null;
        if (playRecordItem != null && locale != null) {
            boolean hasCopyright = true;
            // 由于历史原因，点播的播放记录并没有type字段的值，所以如果type为null，表示点播，为type赋值"1"
            String type = playRecordItem.getType();
            if (type == null) {
                type = UserTpConstant.USERCETER_PLAY_RECORD_QUERY_TYPE_1;
            }
            // 查询类型为空
            if (queryType == null) {
                return null;
            } else {
                // 查询类型不包含"0"，也没有此类型
                if (queryType.indexOf(UserTpConstant.USERCETER_PLAY_RECORD_QUERY_TYPE_0) == -1
                        && queryType.indexOf(type) == -1) {
                    return null;
                }
            }
            // type为1时，需要校验权限，即点播的播放记录需要校验权限
            if (UserTpConstant.USERCETER_PLAY_RECORD_QUERY_TYPE_1.equals(type)) {
                hasCopyright = isMobileHasCopyright(playRecordItem.getPlayPlatform());
            }
            if (hasCopyright) {

                if (UserTpConstantUtil.isSuperLivePlayRecordType(type)) {
                    playRecord = new SuperLivePlayRecordInfoDto();
                    fillPlayRecordWithTp(playRecord, playRecordItem, type, locale);
                    /*
                     * live相关数据不在单条的从cache中获取，改为批量获取，此处不再处理直播相关的播放记录
                     * // 填充超级live的直播信息
                     * String liveInfoCacheProfix =
                     * UserTpConstant.USERCETER_PLAY_RECORD_QUERY_TYPE_2
                     * .equals(type) ? CacheConstants.SUPERLIVE_LIVEROOM_LIVEID
                     * : CacheConstants.SUPERLIVE_CHANNEL_ID;
                     * SuperLiveChannelDto superLiveChannelDto =
                     * tpCacheTemplate.get(
                     * liveInfoCacheProfix + playRecordItem.getOid(),
                     * SuperLiveChannelDto.class);
                     * if (superLiveChannelDto != null) {
                     * playRecord = new SuperLivePlayRecordInfoDto();
                     * fillPlayRecordWithTp(playRecord, playRecordItem, type,
                     * locale);
                     * fillSuperLivePlayRecord((SuperLivePlayRecordInfoDto)
                     * playRecord, superLiveChannelDto);
                     * } else {
                     * log.info("PlayRecordBuilder#build: get live [" +
                     * playRecordItem.getOid() + "] null");
                     * }
                     */
                } else {
                    playRecord = new PlayRecordInfoDto();
                    fillPlayRecordWithTp(playRecord, playRecordItem, type, locale);
                }
            }
        }

        return playRecord;
    }

    /**
     * 判断手机是否有播放版权
     * @return
     */
    private static boolean isMobileHasCopyright(String playPlatform) {
        return playPlatform != null && playPlatform.contains(String.valueOf(Copyright.PHONE));
    }

    /**
     * 使用PlayRecordInfoTpResponse中相关数据填充PlayRecordInfoDto
     * @param playRecord
     * @param playRecordItem
     */
    private static void fillPlayRecordWithTp(PlayRecordInfoDto playRecord, PlayRecordInfoTpResponse playRecordItem,
            String type, Locale locale) {
        playRecord.setCategoryId(String.valueOf(playRecordItem.getCid()));
        playRecord.setAlbumId(String.valueOf(playRecordItem.getPid()));
        playRecord.setVideoId(String.valueOf(playRecordItem.getVid()));

        if (playRecordItem.getNvid() != null && playRecordItem.getNvid() > 0) {
            playRecord.setNextVideoId(String.valueOf(playRecordItem.getNvid()));
        }

        playRecord.setTitle(playRecordItem.getTitle());
        playRecord.setSeriesNum(playRecordItem.getNc());
        playRecord.setImg_400X250(playRecordItem.getPic(400, 250));
        playRecord.setVideoType(playRecordItem.getVideoType());

        if (playRecordItem.getIsend() != null && playRecordItem.getIsend() == 1) {
            playRecord.setIsEnd(true);
        } else {
            playRecord.setIsEnd(false);
        }

        if (playRecordItem.getHtime() != null && playRecordItem.getHtime() >= 0) {
            // TODO 可能会有位溢出
            playRecord.setPlayTime(playRecordItem.getHtime() * 1000L);
        } else {
            playRecord.setPlayTime(-1L);
        }

        if (playRecordItem.getVtime() != null && playRecordItem.getVtime() > 0) {
            playRecord.setDuration(playRecordItem.getVtime() * 1000L);
        } else {
            playRecord.setDuration(0L);
        }

        playRecord.setFrom(String.valueOf(playRecordItem.getFrom()));
        playRecord.setFromName(getPlayRecordFrom(playRecordItem.getFrom(), locale));

        playRecord.setLastUpdateTime(CalendarUtil.getDateStringFromLong(playRecordItem.getUtime() * 1000,
                CalendarUtil.SIMPLE_DATE_FORMAT));
        playRecord.setProduct(playRecordItem.getProduct());
        playRecord.setProductName(getProductName(playRecordItem, locale));
        // playRecord.setProductName(playRecordItem.getProductName());

        int playStatus = judgePlayStatus(playRecord.getIsEnd(), playRecord.getNextVideoId());
        playRecord.setPlayStatus(String.valueOf(playStatus));
        playRecord.setPlayStatusName(getPlayStatusName(type, playStatus, locale));
        playRecord.setOid(playRecordItem.getOid());
        playRecord.setType(type);
    }

    /**
     * 获取播放记录上传来源文案
     * @param from
     * @return
     */
    private static String getPlayRecordFrom(Integer from, Locale locale) {
        if (from == null || from < 0 || from >= UserConstants.PLAY_RECORD_FROM_NAME.length) {
            return StringUtils.EMPTY;
        }
        return MessageUtils.getMessage(UserConstants.PLAY_RECORD_FROM_NAME[from], locale);
    }

    /**
     * 获取播放状态文案
     * @param playStauts
     * @return
     */
    private static String getProductName(PlayRecordInfoTpResponse playRecordItem, Locale locale) {

//        if (StringUtils.isNotEmpty(playRecordItem.getProduct())
//                && playRecordItem.getProduct().startsWith(
//                        UserTpConstant.USERCETER_PLAY_RECORD_FROM_PRODUCT_PREFIX_SUPER_MOBILE)) {
//            return MessageUtils.getMessage(UserConstants.USER_PLAY_RECORD_FROM_PRODUCT_NAME_MOBILE, locale);
//        }
        return playRecordItem.getProductName();
    }

    /**
     * 判断并确定playStatus取值；0--续播，1--下一集，2--回看
     */
    private static int judgePlayStatus(Boolean isEnd, String nextVideoId) {
        int status = -1;
        if (isEnd == null || !isEnd) {
            status = 0;
        } else if (nextVideoId != null) {
            status = 1;
        } else {
            status = 2;
        }
        return status;
    }

    /**
     * 获取播放状态文案
     * @param playStauts
     * @return
     */
    private static String getPlayStatusName(String playRecordType, Integer playStatus, Locale locale) {
        return MessageUtils.getMessage(
                UserConstants.USER_PLAY_RECORD_PROFIX + playRecordType + "_" + String.valueOf(playStatus), locale);
    }

    /**
     * 填充超级live直播信息
     * @param superLivePlayRecordInfoDto
     * @param superLiveChannelDto
     */
    public static void fillSuperLivePlayRecord(SuperLivePlayRecordInfoDto superLivePlayRecordInfoDto,
            SuperLiveChannelDto superLiveChannelDto) {
        superLivePlayRecordInfoDto.setChannelId(superLiveChannelDto.getChannelId());
        superLivePlayRecordInfoDto.setChannelName(superLiveChannelDto.getChannelName());
        superLivePlayRecordInfoDto.setChannelPic(superLiveChannelDto.getChannelPic());
        superLivePlayRecordInfoDto.setIsPay(superLiveChannelDto.getIsPay());
        superLivePlayRecordInfoDto.setCur(superLiveChannelDto.getCur());
        superLivePlayRecordInfoDto.setNext(superLiveChannelDto.getNext());
        superLivePlayRecordInfoDto.setStreams(superLiveChannelDto.getStreams());
        superLivePlayRecordInfoDto.setStreamTips(superLiveChannelDto.getStreamTips());
    }
}
