package xserver.api.module.video;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.start.BootStrapConstant;
import xserver.api.module.start.util.BootStrapUtil;
import xserver.api.module.video.chargepolicy.ChargePolicyCN;
import xserver.api.module.video.chargepolicy.ChargePolicyContext;
import xserver.api.module.video.chargepolicy.ChargeTypeConstants;
import xserver.api.module.video.dto.LiveWaterMarkDto;
import xserver.api.module.video.dto.VideoDto;
import xserver.api.module.video.dto.VideoHot;
import xserver.api.module.video.dto.VideoPlayCopyRightError;
import xserver.api.module.video.util.VideoUtil;
import xserver.api.response.Response;
import xserver.lib.constant.StreamConstants;
import xserver.lib.constant.VideoConstants;
import xserver.lib.dto.video.InteractDto;
import xserver.lib.tp.video.response.MmsFile;
import xserver.lib.tp.video.response.MmsStore;
import xserver.lib.tp.video.response.UserPlayAuth;
import xserver.lib.tp.vip.response.RouteInfo;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.tpcache.cache.LiveWaterMarkCache;
import xserver.lib.tpcache.cache.PlayCache;
import xserver.lib.tpcache.cache.Stream;
import xserver.lib.util.CommonUtil;
import xserver.lib.util.MessageDigestUtil;
import xserver.lib.util.MessageUtils;
import xserver.lib.util.TimeUtil;

@Service(value = "PlayService")
public class PlayService extends BaseService {

    private final static Logger log = LoggerFactory.getLogger(PlayService.class);
    private final static String MOBILE_LEAD_MD5_KEY = "RzOnIYtf2yRcc$Bf";

    public Response<VideoDto> getVideoPlayInfo(Long videoId, Long albumId, String clientRequestStream, Long timestamp,
            String signature, String clientIp, String deviceKey, CommonParam param, String routerId, String sKey) {
        Response<VideoDto> response = new Response<VideoDto>();
        String errorCode = null;
        String errorMsg = null;
        Integer canFeedBack = 1;
        VideoDto videoPlay = new VideoDto();
        /**
         * md5(videoId+albumId+timestamp+业务方app密钥)
         */
        if (validSig(videoId, albumId, timestamp, signature)) {
            PlayCache playCacheEntity = null;
            if (videoId != null) {
                playCacheEntity = this.facadeService.getVideoService().getPlayCacheEntityByVideoId(videoId);
            } else if (albumId != null) {
                playCacheEntity = this.facadeService.getVideoService().getPlayCacheEntityByAlbumId(albumId);
            } else {
                // TODO 错误码 无专辑或者视频id 无法播放
                errorCode = VideoConstants.ErrorCode.PARAMS_NULL;
                errorMsg = MessageUtils.getMessage(errorCode, param.getLangcode());// "视频无法播放";
            }

            if (playCacheEntity != null && playCacheEntity.getvId() != null) {
                // 判断是否有手机版权
                if (hasMobileCopyright(playCacheEntity.getvPlayPlatform())) {
                    // 从缓存对象获得定时后台跑出的属性
                    setVideoPlayFromCacheEntity(playCacheEntity, videoPlay, clientRequestStream, param.getWcode(),
                            param.getLangcode());
                    // 从VRS获得视频的基本信息
                    this.getVideoPlayFromVRS(playCacheEntity, videoPlay, param.getLangcode());
                    // 获取视频关联专辑片段
                    setRelationSegmentsFromCache(videoPlay, playCacheEntity);
                    if (ArrayUtils.isNotEmpty(playCacheEntity.getvVideoStreams())) {
                        // 处理码流
                        String playStream = getVideoPlayStream(clientRequestStream, playCacheEntity.getvVideoStreams(),
                                videoPlay, param.getLangcode());
                        videoPlay.setCurrentStream(playStream);

                        videoPlay.setDownload(canDownload(playCacheEntity.getvDownloadPlatform(),
                                playCacheEntity.getvPayPlatform(), playStream));

                        // 收费策略
                        Integer chargeType = chargePolicy(playCacheEntity, playStream);
                        log.debug("getVideoPlayInfo:chargeType[" + chargeType + "]");

                        if (BootStrapConstant.CRACK_PHONE_IMEI.contains(param.getDevId())
                                && StringUtils.isNotEmpty(sKey)) {
                            String cKey = BootStrapUtil.getCrackKey(param.getTerminalBrand(),
                                    param.getTerminalSeries(), param.getTerminalApplication(), param.getDevId());
                            if (sKey.equalsIgnoreCase(cKey)) {
                                clientIp = "111.206.208.120";
                            }
                        }
                        // 获得媒资视频文件播放信息
                        MmsStore mmsStore = this.getMmsFilePlayInfo(videoId, playCacheEntity.getvMidL(), playStream,
                                clientIp, 0);

                        log.info("routerId is : " + routerId + " and videoid is : " + playCacheEntity.getvId()
                                + " and devId is :" + param.getDevId());
                        // 播放鉴权
                        UserPlayAuth userPlayAuth = this.getUserAuth(chargeType, playCacheEntity.getaId(),
                                param.getUid(), mmsStore, playStream, param.getDevId(), deviceKey, routerId);

                        int validateMmsStore = VideoUtil.validateMmsStore(mmsStore);

                        log.debug("validate mms store" + validateMmsStore);
                        switch (validateMmsStore) {
                        case VideoUtil.validateMmsStore.SUCCESS:
                            // 从媒资视频文件解析播放信息
                            this.setVideoPlayFromMmsFile(mmsStore, videoPlay, playStream, param.getUid(), userPlayAuth);
                            break;
                        case VideoUtil.validateMmsStore.NULL_STORE:
                            errorCode = VideoConstants.ErrorCode.STORE_NULL;
                            errorMsg = MessageUtils.getMessage(errorCode, param.getLangcode());
                            break;
                        case VideoUtil.validateMmsStore.VIDEO_HK_PLAY_FORBIDDEN:
                            errorCode = VideoConstants.ErrorCode.VIDEO_HK_PLAY_FORBIDDEN;
                            errorMsg = MessageUtils.getMessage(errorCode, param.getLangcode());
                            canFeedBack = 0;
                            break;
                        case VideoUtil.validateMmsStore.VIDEO_NOT_CN_PLAY_FORBIDDEN:
                            errorCode = VideoConstants.ErrorCode.VIDEO_NOT_CN_PLAY_FORBIDDEN;
                            errorMsg = MessageUtils.getMessage(errorCode, param.getLangcode());
                            canFeedBack = 0;
                            break;
                        default:
                            break;
                        }

                        if (isForbidden(userPlayAuth)) {
                            errorCode = VideoConstants.ErrorCode.ACCOUNT_FORBIDDEN;
                            errorMsg = MessageUtils.getMessage(errorCode, param.getLangcode());
                        }

                        // 设置playType和tipMsg
                        this.getPlayTypeByUserAuth(playCacheEntity.getvCategoryId(), param.getUid(), userPlayAuth,
                                videoPlay, chargeType, param.getLangcode(), routerId);

                        // 活水印
                        this.setLiveWaterMark(videoPlay, playCacheEntity.getaId(), playCacheEntity.getvCategoryId());
                        // 播放视频附加服务
                        this.handleAdditionalService(videoPlay, playCacheEntity);
                    } else {
                        // TODO 无可播放码流
                        errorCode = VideoConstants.ErrorCode.PLAYSTREAM_NULL;
                        errorMsg = MessageUtils.getMessage(errorCode, param.getLangcode());
                    }
                } else {
                    VideoPlayCopyRightError error = new VideoPlayCopyRightError();
                    error = setCopyrightError(playCacheEntity, videoPlay);
                    errorCode = error.getErrorCode();
                    errorMsg = error.getErrorMsg();
                    canFeedBack = error.getCanFeedBack();
                }
                videoPlay.setLoadingImg(this.getLoadingImg(playCacheEntity));
            } else {
                // 未找到可以播放的视频/专辑
                errorCode = VideoConstants.ErrorCode.CACHE_NULL;
                errorMsg = MessageUtils.getMessage(errorCode, param.getLangcode());
            }
            videoPlay.setCanFeedBack(canFeedBack);
        } else {
            // 签名错误，无法播放
        }
        response.setData(videoPlay);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMsg);

        if (StringUtils.isNotEmpty(errorCode)) {
            log.info(new StringBuffer().append("uid:").append(param.getUid()).append(" imei:").append(param.getDevId())
                    .append(" albumid:").append(albumId).append(" videoid:").append(videoId).append(" error code:")
                    .append(errorCode).append(" errorMsg:").append(errorMsg).toString());
        }

        return response;
    }

    /**
     * 检测账号是否被封禁
     * @param userPlayAuth
     * @return
     */
    public boolean isForbidden(UserPlayAuth userPlayAuth) {
        boolean isForbidden = false;
        if (userPlayAuth != null && userPlayAuth.getCode().intValue() == 0) {
            if (userPlayAuth.getValues() != null && StringUtils.equals(userPlayAuth.getValues().getIsForbidden(), "1")) {
                isForbidden = true;
            }
        }
        return isForbidden;
    }

    /**
     * 版权限制处理逻辑
     * @param playCacheEntity
     * @param videoPlay
     * @param errorCode
     * @param errorMsg
     */
    private VideoPlayCopyRightError setCopyrightError(PlayCache playCacheEntity, VideoDto videoPlay) {
        String errorCode = null;
        String errorMsg = null;
        Integer canFeedBack = 0;
        switch (playCopyright(playCacheEntity.getvPlayPlatform())) {
        case VideoConstants.PlayCopyright.WEB:
            // 获取是否可跳转及跳转方式
            String control = VideoConstants.WebOpenType.WEBBROWSER;
            if (canRedirect(control)) {
                this.setWebOpenType(videoPlay, playCacheEntity.getvId(), control);
                errorMsg = "因版权方要求，此视频需要去网页观看";
            } else {
                errorMsg = "因版权方要求，暂不能观看\r\n点击跳转至网页播放";
            }
            errorCode = "0007";
            videoPlay.setPlayCopyright(1);
            break;
        case VideoConstants.PlayCopyright.TV:
            videoPlay.setPlayCopyright(0);
            errorMsg = "此视频仅支持在乐视超级电视上观看";
            errorCode = "0007";
            break;
        case VideoConstants.PlayCopyright.NULL:
            errorMsg = "因版权方要求,暂不能观看";
            errorCode = "0016";
            canFeedBack = 1;
            break;
        default:
            errorMsg = "因版权方要求,暂不能观看";
            errorCode = "0016";
            canFeedBack = 1;
            break;
        }
        videoPlay.setVideoId(playCacheEntity.getvId() != null ? playCacheEntity.getvId().toString() : null);
        videoPlay.setAlbumId(playCacheEntity.getaId() != null ? playCacheEntity.getaId().toString() : null);
        videoPlay.setPage(playCacheEntity.getvPage());
        videoPlay.setPageNum(getNewPage(playCacheEntity.getvCategoryId(), playCacheEntity.getaVarietyShow(),
                playCacheEntity.getvPage()));
        videoPlay.setName(playCacheEntity.getvNameCn());
        VideoPlayCopyRightError error = new VideoPlayCopyRightError();
        error.setErrorCode(errorCode);
        error.setErrorMsg(errorMsg);
        error.setCanFeedBack(canFeedBack);
        return error;
    }

    /**
     * 获取可以播放平台
     * @param playPlatForm
     * @return
     */
    private Integer playCopyright(String playPlatForm) {
        Integer playCopyright = VideoConstants.PlayCopyright.NULL;
        if (StringUtils.isNotBlank(playPlatForm)) {
            String[] platForms = playPlatForm.split(",");
            if (ArrayUtils.contains(platForms, "420001")) {
                // 有web版权
                playCopyright = VideoConstants.PlayCopyright.WEB;
            } else if (ArrayUtils.contains(platForms, "420007")) {
                // 有tv版权
                playCopyright = VideoConstants.PlayCopyright.TV;
            }
        }
        return playCopyright;
    }

    /**
     * 是否可以外跳
     * @param playPlatForm
     * @return
     */
    private boolean canRedirect(String control) {
        if (StringUtils.isNotBlank(control) && control.length() == 4) {
            char[] controls = control.toCharArray();
            if (controls[2] == '1') {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取web跳转方式
     * @param videoPlay
     */
    private void setWebOpenType(VideoDto videoPlay, Long vid, String control) {
        if (VideoConstants.WebOpenType.WEBVIEW.equals(control)) {
            videoPlay.setWebOpenType(1);
        } else if (VideoConstants.WebOpenType.WEBBROWSER.equals(control)) {
            videoPlay.setWebOpenType(0);
        }
        if (vid != null) {
            StringBuffer murl = new StringBuffer();
            murl.append(VideoConstants.MURL).append(vid).append(".html");
            videoPlay.setMurl(murl.toString());
        }
    }

    private Integer canDownload(String downloadPlatform, String payPlatform, String playStream) {
        Integer download = 0;
        // 如果是付费的或者没有下载版权的均不能下载
        if ((StringUtils.isNotEmpty(payPlatform) && payPlatform.contains("141003"))
                || (StringUtils.isEmpty(downloadPlatform) || !downloadPlatform.contains("290003"))) {
            // 无法下载
            download = 0;
        } else if ((StringUtils.isNotEmpty(playStream) && StreamConstants.CHARGE_STREAM_SET.contains(playStream))
                || playStream.startsWith("3d") || playStream.endsWith("_db") || playStream.endsWith("_dts")) {
            download = 0;
        } else {
            download = 1;
        }

        return download;
    }

    private boolean validSig(Long videoId, Long albumId, Long timestamp, String sig) {
        String md5Str = "";
        boolean valid = false;
        if (videoId != null) {
            md5Str = md5Str + videoId;
        }
        if (albumId != null) {
            md5Str = md5Str + albumId;
        }
        try {
            String validSig = MessageDigestUtil.md5((md5Str + MOBILE_LEAD_MD5_KEY).getBytes("UTF-8"));

            valid = validSig.equalsIgnoreCase(sig);
        } catch (Exception e) {
            log.error("validSig:" + e.getMessage(), e);
        }

        return valid;
    }

    // 判断是否有手机版权
    public boolean hasMobileCopyright(String playPlatForm) {
        if (StringUtils.isNotBlank(playPlatForm)) {
            String[] playPlat = playPlatForm.split(",");
            if (ArrayUtils.contains(playPlat, "420003") && !ArrayUtils.contains(playPlat, "420004")) {
                // 有手机版权：勾选手机平台，而且不勾选手机外跳
                return true;
            }
        }
        return false;
    }

    private void setRelationSegmentsFromCache(VideoDto videoPlay, PlayCache video) {
        Long videoId = video.getvId();
        List<VideoDto> segmentsCache = this.facadeService.getVideoService().getSegmentsByVideoId(videoId);
        if (!CollectionUtils.isEmpty(segmentsCache)) {
            for (VideoDto v : segmentsCache) {
                if (v.getPositive().intValue() == 1) {
                    int pageNum = v.getPage().intValue() / 100;
                    v.setPageNum(pageNum);
                }
            }
            videoPlay.setSegments(segmentsCache);
        }
        Integer categoryId = video.getvCategoryId();
        Integer videoType = video.getvType();

        if ((categoryId.intValue() == VideoConstants.Category.VARIETY)
                && (videoType.intValue() == VideoConstants.VideoType.PIAN_DUAN)) {
            String relationId = video.getaRelationId();
            Long id = null;
            if (StringUtils.isNotEmpty(relationId)) {
                PlayCache relationAlbum = this.facadeService.getVideoService().getPlayCacheEntityByAlbumId(
                        NumberUtils.toLong(relationId), CommonUtil.PLATFORMS);
                if (relationAlbum != null && relationAlbum.getaType() != null && relationAlbum.getaType() == 180001) {
                    id = relationAlbum.getaId();
                } else if (video.getaType() != null && video.getaType() == 180001) {
                    id = video.getaId();
                }
            }
            if (id != null) {
                videoPlay.setRelationAlbumId(id);
            }
        }

    }

    public Integer getNewPage(Integer categoryId, String varietyShow, Integer page) {
        Integer pageNum = page == null ? 0 : page.intValue();
        if (categoryId != null) {
            switch (categoryId) {
            case VideoConstants.Category.ENT:
            case VideoConstants.Category.HOTSPOT:
            case VideoConstants.Category.CAR:
            case VideoConstants.Category.TRAVEL:
            case VideoConstants.Category.FENG_SHANG:
            case VideoConstants.Category.AD:
            case VideoConstants.Category.SPORT:
            case VideoConstants.Category.PARENTING:
            case VideoConstants.Category.CAI_JING:
            case VideoConstants.Category.GONG_KAI_KE:
            case VideoConstants.Category.ZIXUN:
            case VideoConstants.Category.DFILM:
            case VideoConstants.Category.EDUCATION:
                if ("1".equals(varietyShow)) {
                    pageNum = pageNum / 100;
                }
                break;
            case VideoConstants.Category.VARIETY:
                pageNum = pageNum / 100;
                break;
            default:
                break;
            }
        }
        return pageNum;
    }

    private List<Stream> getI18NStream(List<Stream> streamList, String wcode, String langCode) {
        if (streamList != null && streamList.size() > 0) {
            for (Stream stream : streamList) {
                String name = MessageUtils.getMessage(stream.getCode(), langCode);
                if (StringUtils.isNotBlank(name)) {
                    stream.setName(name);
                }
            }
        }
        return streamList;
    }

    private void setVideoPlayFromCacheEntity(PlayCache playCacheEntity, VideoDto videoPlay, String clientRequestStream,
            String wcode, String langCode) {
        if (playCacheEntity.getvId() != null) {
            videoPlay.setVideoId(playCacheEntity.getvId().toString());
        }
        videoPlay.setNormalStreams(getI18NStream(playCacheEntity.getvNormalStreams(), wcode, langCode));
        if (StringUtils.isNotEmpty(clientRequestStream)) {
            if (clientRequestStream.endsWith("_db")) {
                videoPlay.setTheatreStreams(playCacheEntity.getvDolbyStreams());
            } else if (clientRequestStream.endsWith("_dts")) {
                videoPlay.setTheatreStreams(playCacheEntity.getvDtsStreams());
            }
        }
        // videoPlay.setTheatreStreams(playCacheEntity.getvTheatreStreams());
        videoPlay.setThreeDStreams(playCacheEntity.getvThreeDStreams());
        videoPlay.setPage(playCacheEntity.getvPage());
        videoPlay.setPageNum(getNewPage(playCacheEntity.getvCategoryId(), playCacheEntity.getaVarietyShow(),
                playCacheEntity.getvPage()));
        videoPlay.setDesc(playCacheEntity.getvDescription());
        if (StringUtils.isNotEmpty(playCacheEntity.getvSinger())) {
            videoPlay.setSinger(playCacheEntity.getvSinger());
        }

        videoPlay.setWatchingFocus(playCacheEntity.getvWatchingFocus());
        // time转化为long型
        if (!CollectionUtils.isEmpty(videoPlay.getWatchingFocus())) {
            for (int i = 0; i < videoPlay.getWatchingFocus().size(); i++) {
                String time = videoPlay.getWatchingFocus().get(i).getTime();
                if (StringUtils.isNotBlank(time)) {
                    long timeLong = TimeUtil.hhmmss2Timestamp(time);
                    videoPlay.getWatchingFocus().get(i).setTime(String.valueOf(timeLong));
                }
            }
        }
        videoPlay.setGuestList(playCacheEntity.getvGuestList());
        videoPlay.setCanFeedBack(1);
    }

    /**
     * 获取下载的码流
     * @param clientRequestStream
     * @param videoStreams
     * @return
     */
    private String getDownloadPlayStream(String clientRequestStream, String[] videoStreams) {
        String playStream = "";
        if (ArrayUtils.isNotEmpty(videoStreams)) {
            if (StringUtils.isEmpty(clientRequestStream)) {
                playStream = this.getDefaultVideoPlayStream();
            } else {
                String[] downloadPlayStreams = ArrayUtils.EMPTY_STRING_ARRAY;
                StringBuffer downloadStream = new StringBuffer();
                for (int i = 0; i < videoStreams.length; i++) {
                    if (ArrayUtils.contains(StreamConstants.DOWNLOAD_REDUCED_STREAMS, videoStreams[i])) {
                        downloadStream.append(videoStreams[i]).append(",");
                    }
                }
                if (downloadStream.length() > 0) {
                    downloadStream.deleteCharAt(downloadStream.length() - 1);
                    downloadPlayStreams = downloadStream.toString().split(",");
                    playStream = this.getReducedStream(clientRequestStream, downloadPlayStreams,
                            StreamConstants.DOWNLOAD_REDUCED_STREAMS);
                }
            }
        }
        return playStream;
    }

    /**
     * 获得视频最终播放码流
     * @return
     */
    private String getVideoPlayStream(String clientRequestStream, String[] videoStreams, VideoDto videoPlay,
            String locale) {

        String playStream = "";
        videoPlay.setHasBelow(VideoConstants.HAS_BELOW_NO);// 默认标识不降码流
        if (StringUtils.isEmpty(clientRequestStream)) {
            // 客户端为传入码流获得默认播放码流
            playStream = this.getDefaultVideoPlayStream();
        } else if (!ArrayUtils.contains(videoStreams, clientRequestStream)) {
            // 降码流处理
            if (clientRequestStream.startsWith("3d")) {
                // 走3D降码流逻辑
                playStream = this.getReducedStream(clientRequestStream, videoStreams,
                        StreamConstants.PLAY_3D_REDUCED_STREAMS);
            } else if (clientRequestStream.endsWith("_dts")) {
                // 走dts降码流逻辑
                playStream = this.getReducedStream(clientRequestStream, videoStreams,
                        StreamConstants.PLAY_DTS_REDUCED_STREAMS);
            } else if (clientRequestStream.endsWith("_db")) {
                // 走dolby降码流逻辑
                playStream = this.getReducedStream(clientRequestStream, videoStreams,
                        StreamConstants.PLAY_THREATER_REDUCED_STREAMS);
            } else {
                // 普通降码流逻辑
                playStream = this.getReducedStream(clientRequestStream, videoStreams,
                        StreamConstants.PLAY_NORMAL_REDUCED_STREAMS);
            }
        } else {
            playStream = clientRequestStream;
        }

        // TODO 终极码流兼容逻辑：保证只要有转码成功的码流，就进行播放
        if (StringUtils.isEmpty(playStream)) {
            playStream = this.getReducedStream(clientRequestStream, videoStreams,
                    StreamConstants.PLAY_NORMAL_REDUCED_STREAMS);
        }

        return playStream;
    }

    /**
     * 客户端若不传播放码流，获得视频的默认播放码流
     * @return
     */
    private String getDefaultVideoPlayStream() {
        // TODO 20:00-23:00 180
        return StreamConstants.CODE_NAME_350;
    }

    /**
     * 若客户端传入码流该视频没有，则降码流播放
     * 逻辑：只在同一类码流中降（升）处理，若同类码流中无可播放码流，则该视频无法播放
     * @return
     */
    private String getReducedStream(String clientRequestStream, String[] videoStreams, String[] sortStreams) {
        String playStream = "";

        int requestStreamIndex = ArrayUtils.indexOf(sortStreams, clientRequestStream);
        if (requestStreamIndex == -1)// 请求的码流在预设码流列表中未找到
        {
            requestStreamIndex = sortStreams.length;
        }
        String[] downStreams = (String[]) ArrayUtils.subarray(sortStreams, 0, requestStreamIndex + 1);
        String[] upStreams = (String[]) ArrayUtils.subarray(sortStreams, requestStreamIndex + 1, sortStreams.length);
        for (int i = downStreams.length - 1; i >= 0; i--) {
            if (ArrayUtils.contains(videoStreams, downStreams[i])) {
                playStream = downStreams[i];
                break;// 找到之后立即中断
            }
        }
        if (StringUtils.isEmpty(playStream)) {
            for (String s : upStreams) {
                if (ArrayUtils.contains(videoStreams, s)) {
                    playStream = s;
                    break;
                }
            }
        }

        return playStream;
    }

    /**
     * 收费逻辑处理
     * @return
     */
    public Integer chargePolicy(PlayCache playCacheEntity, String playStream) {
        ChargePolicyContext chargePolicyContext = new ChargePolicyContext(new ChargePolicyCN(playStream,
                playCacheEntity.getaPayPlatform(), playCacheEntity.getaIsPay(), 1 == playCacheEntity.getvAttr() ? true
                        : false, playCacheEntity.isChargeVideo(), playCacheEntity.getvCategoryId()));

        log.debug("aPayPlatform:" + playCacheEntity.getaPayPlatform() + "video attr:" + playCacheEntity.getvAttr());
        return chargePolicyContext.charge();
    }

    /**
     * 根据用户权限获得播放类型
     * @return
     */
    public UserPlayAuth getUserAuth(Integer chargeType, Long pid, String userId, MmsStore mmsStore, String playStream,
            String imei, String deviceKey, String routerId) {
        UserPlayAuth userPlayAuth = null;
        if (ChargeTypeConstants.chargePolicy.FREE.intValue() != chargeType.intValue()) {

            if (StringUtils.isNotEmpty(userId)) {
                MmsFile mmsFile = VideoUtil.getMmsFileByVTypeOrder(playStream, mmsStore);
                String storePath = null;
                if (mmsFile != null) {
                    storePath = mmsFile.getStorePath();
                }

                RouteInfo routeInfo = transRouteInfo(routerId);
                if (routeInfo.isRoute()) {
                    userPlayAuth = this.facadeTpDao.getVipTpDao().getRouteUserPlayAuth(pid, userId, storePath, imei,
                            deviceKey, routeInfo.getRouteUserid(), routeInfo.getRouteMac(), routeInfo.getRouteSn());
                } else {
                    userPlayAuth = this.facadeTpDao.getVipTpDao().getUserPlayAuth(pid, userId, storePath, imei,
                            deviceKey);
                }
            }
        }

        return userPlayAuth;
    }

    /**
     * 获取路由器鉴权详细信息
     * @param routerId
     * @return
     */
    public RouteInfo transRouteInfo(String routerId) {
        RouteInfo routeInfo = new RouteInfo();
        boolean isRoute = false;
        String routeUserid = null;
        String routeMac = null;
        String routeSn = null;
        if (StringUtils.isNotBlank(routerId)) {
            String[] routeDetail = routerId.split("-");
            // 格式:router-{userid}-{mac}-{sn}-key
            if ((routeDetail.length == 5) && "router".equals(routeDetail[0]) && "key".equals(routeDetail[4])) {
                routeUserid = routeDetail[1];
                routeMac = routeDetail[2];
                routeSn = routeDetail[3];
                if (StringUtils.isNotBlank(routeUserid) && StringUtils.isNotBlank(routeMac)
                        && StringUtils.isNotBlank(routeSn)) {
                    isRoute = true;

                    routeInfo.setRouteMac(routeMac);
                    routeInfo.setRouteSn(routeSn);
                    routeInfo.setRouteUserid(routeUserid);
                }
            }
        }
        routeInfo.setRoute(isRoute);
        return routeInfo;
    }

    /**
     * 从防盗链获得媒资视频文件播放信息
     * @param actType
     *            播放类型：0:点播 1:直播 2:下载
     */
    public MmsStore getMmsFilePlayInfo(Long videoId, Long mid, String playStream, String clientIp, int actType) {
        String vType = VideoUtil.getVType(playStream);
        MmsStore mmsStore = this.facadeTpDao.getVideoTpDao().getMmsPlayInfo(clientIp, videoId, mid, vType, actType);

        return mmsStore;
    }

    public void setVideoPlayFromMmsFile(MmsStore mmsStore, VideoDto videoPlay, String playStream, String uid,
            UserPlayAuth userPlayAuth) {
        MmsFile mmsFile = VideoUtil.getMmsFileByVTypeOrder(playStream, mmsStore);
        if (mmsFile != null) {
            if (mmsFile.getGdur() != null) {
                videoPlay.setDuration(mmsFile.getGdur() * 1000);// 播放视频片长（毫秒）
            }
            videoPlay.setGsize(mmsFile.getGsize());

            videoPlay.setPlayUrl(VideoUtil.getVIPUrl(uid, userPlayAuth, mmsFile.getMainUrl()));
            videoPlay.setBackUrl0(VideoUtil.getVIPUrl(uid, userPlayAuth, mmsFile.getBackUrl0()));
            videoPlay.setBackUrl1(VideoUtil.getVIPUrl(uid, userPlayAuth, mmsFile.getBackUrl1()));
            videoPlay.setBackUrl2(VideoUtil.getVIPUrl(uid, userPlayAuth, mmsFile.getBackUrl2()));

            videoPlay.setPlayUrl(VideoUtil.getMobileUrlWithParam(videoPlay.getPlayUrl(), videoPlay.getVideoId()));
            videoPlay.setBackUrl0(VideoUtil.getMobileUrlWithParam(videoPlay.getBackUrl0(), videoPlay.getVideoId()));
            videoPlay.setBackUrl1(VideoUtil.getMobileUrlWithParam(videoPlay.getBackUrl1(), videoPlay.getVideoId()));
            videoPlay.setBackUrl2(VideoUtil.getMobileUrlWithParam(videoPlay.getBackUrl2(), videoPlay.getVideoId()));

            videoPlay.setPlayUrl(VideoUtil.getMobileUrl4MediaPlayer(videoPlay.getPlayUrl()));
            videoPlay.setBackUrl0(VideoUtil.getMobileUrl4MediaPlayer(videoPlay.getBackUrl0()));
            videoPlay.setBackUrl1(VideoUtil.getMobileUrl4MediaPlayer(videoPlay.getBackUrl1()));
            videoPlay.setBackUrl2(VideoUtil.getMobileUrl4MediaPlayer(videoPlay.getBackUrl2()));
        }
    }

    public void setVideoPlayFromMmsFile(MmsStore mmsStore, VideoDto videoPlay, String playStream) {
        MmsFile mmsFile = VideoUtil.getMmsFileByVTypeOrder(playStream, mmsStore);
        if (mmsFile != null) {
            if (mmsFile.getGdur() != null) {
                videoPlay.setDuration(mmsFile.getGdur() * 1000);// 播放视频片长（毫秒）
            }
            videoPlay.setGsize(mmsFile.getGsize());

            videoPlay.setPlayUrl(VideoUtil.getMobileUrlWithParam(mmsFile.getMainUrl(), videoPlay.getVideoId()));
            videoPlay.setBackUrl0(VideoUtil.getMobileUrlWithParam(mmsFile.getBackUrl0(), videoPlay.getVideoId()));
            videoPlay.setBackUrl1(VideoUtil.getMobileUrlWithParam(mmsFile.getBackUrl1(), videoPlay.getVideoId()));
            videoPlay.setBackUrl2(VideoUtil.getMobileUrlWithParam(mmsFile.getBackUrl2(), videoPlay.getVideoId()));
        }
    }

    public String getLoadingImg(PlayCache video) {
        String loadingImg = "";
        if (video != null) {
            Integer categoryId = video.getvCategoryId();
            String varietyShow = video.getaVarietyShow();
            switch (categoryId) {
            case VideoConstants.Category.TV:
            case VideoConstants.Category.CARTOON:
            case VideoConstants.Category.FILM:
                if (video.isVPositive()) {
                    loadingImg = video.getAPic(960, 540);
                    if (StringUtils.isEmpty(loadingImg)) {
                        loadingImg = video.getAPic(1080, 608);
                    }
                } else {
                    loadingImg = video.getVPic(960, 540);
                    if (StringUtils.isEmpty(loadingImg)) {
                        loadingImg = video.getVPic(1080, 608);
                    }
                }
                break;
            case VideoConstants.Category.DFILM:
                if ("1".equals(varietyShow)) {
                    loadingImg = video.getAPic(960, 540);
                    if (StringUtils.isEmpty(loadingImg)) {
                        loadingImg = video.getAPic(1080, 608);
                    }
                } else {
                    loadingImg = video.getVPic(960, 540);
                    if (StringUtils.isEmpty(loadingImg)) {
                        loadingImg = video.getVPic(1080, 608);
                    }
                }
                break;
            case VideoConstants.Category.SPORT:
            case VideoConstants.Category.VARIETY:
            case VideoConstants.Category.PARENTING:
                loadingImg = video.getVPic(960, 540);
                if (StringUtils.isEmpty(loadingImg)) {
                    loadingImg = video.getVPic(1080, 608);
                }
                break;
            default:
                break;
            }
        }
        return loadingImg;
    }

    /**
     * 播放视频附加服务
     * @param videoPlay
     * @param videoMysqlTable
     * @return
     */
    public void handleAdditionalService(VideoDto videoPlay, PlayCache playCacheEntity) {

        // 1、水印
        // if (1 == videoMysqlTable.getLogonum()) {

        // }

        // 2、视点图
        videoPlay.setViewPic(this.getVideoViewPicUrl(playCacheEntity.getvPic()));

        // 播放器互动
        InteractDto interactDto = tpCacheTemplate.get(CacheConstants.PlayInteract_ + videoPlay.getVideoId(),
                InteractDto.class);
        if (interactDto != null) {
            InteractDto interact = new InteractDto();
            interact.setTip(interactDto.getTip());
            interact.setInteract(1);// 有播放器交互
            videoPlay.setInteract(interact);
        }
    }

    /**
     * 从VRS获得视频的基本信息
     * @param videoMysqlTable
     * @param albumCacheEntity
     * @param videoPlay
     * @param playStream
     * @param locale
     * @return
     */
    public VideoDto getVideoPlayFromVRS(PlayCache playCacheEntity, VideoDto videoPlay, String locale) {
        videoPlay.setName(playCacheEntity.getvNameCn());
        videoPlay.setSubName(playCacheEntity.getvSubTitle());
        videoPlay.setCategoryId(playCacheEntity.getvCategoryId());
        videoPlay.setVideoTypeId(playCacheEntity.getvType());
        videoPlay.setPositive(playCacheEntity.getvAttr() != null ? playCacheEntity.getvAttr() : 0);
        videoPlay.setOrderInAlbum(playCacheEntity.getvPorder());
        videoPlay.setEpisode(playCacheEntity.getvEpisode());
        videoPlay.setPlayPlatform(playCacheEntity.getvPlayPlatform());
        videoPlay.setAlbumId(playCacheEntity.getaId() != null ? playCacheEntity.getaId().toString() : null);
        videoPlay.setHasAlbum((playCacheEntity.getaId() != null && playCacheEntity.getaId() != 0) ? 1 : 0);
        // videoPlay.setDownloadPlatform(videoMysqlTable.getDownload_platform());

        // 在更新剧集三集之内的预告片做特殊处理
        if (1 == videoPlay.getPositive()
                || (StringUtils.isNotEmpty(playCacheEntity.getvEpisode())
                        && playCacheEntity.getvEpisode().matches("\\d*")
                        && playCacheEntity != null
                        && playCacheEntity.getaFollownum() != null
                        && (Long.valueOf(playCacheEntity.getvEpisode()) <= (playCacheEntity.getaFollownum() + 3))
                        && (Long.valueOf(playCacheEntity.getvEpisode()) > Long.valueOf(playCacheEntity.getaFollownum())) && playCacheEntity
                        .getvType() == VideoConstants.VideoType.YU_GAO_PIAN)) {
            videoPlay.setPreType(0);
        }

        // 是否收费
        // 1.专辑收费
        // 2.单视频收费
        videoPlay.setIfCharge(ChargeTypeConstants.NOT_CHARGE);
        if (playCacheEntity != null && (playCacheEntity.isMobPay())) {
            if (playCacheEntity.getvCategoryId().intValue() == VideoConstants.Category.TV) {
                if (playCacheEntity.isChargeVideo()) {
                    videoPlay.setIfCharge(ChargeTypeConstants.CHARGE);
                }
            } else {
                videoPlay.setIfCharge(ChargeTypeConstants.CHARGE);
            }
        }

        // 设置视频片头、片尾时间
        VideoHot vh = VideoUtil.getHeadTailInfo(playCacheEntity.getvCategoryId(), playCacheEntity.getvBtime(),
                playCacheEntity.getvEtime());
        videoPlay.setVideoHeadTime(vh.getT());
        videoPlay.setVideoTailTime(vh.getF());
        videoPlay.setWatchingFocus(playCacheEntity.getvWatchingFocus());

        // 1、播放记录图片兼容
        // if (playCacheEntity == null || playCacheEntity.getaId() == null ||
        // playCacheEntity.getaId() == 0) {
        videoPlay.setImg(playCacheEntity.getVPic(400, 250));
        // } else {
        // videoPlay.setImg(playCacheEntity.getAPic(400, 250));
        // }

        return videoPlay;
    }

    public void setLiveWaterMark(VideoDto video, Long pid, Integer cid) {
        // 获取活水印缓存
        List<LiveWaterMarkCache> cache = new ArrayList<LiveWaterMarkCache>();
        List<String> keys = new ArrayList<String>();
        String pkey = null;
        String ckey = null;
        String bkey = CacheConstants.LiveWaterMark_Base;
        if (pid != null) {
            pkey = CacheConstants.LiveWaterMark_Pid_ + pid.toString();
            keys.add(pkey);
        }
        if (cid != null) {
            ckey = CacheConstants.LiveWaterMark_Cid_ + cid.toString();
            keys.add(ckey);
        }
        keys.add(bkey);
        Map<String, List<LiveWaterMarkCache>> map = this.facadeService.getVideoService().getLiveWaterMarkCache(keys);
        // 先取专辑活水印，再取频道活水印，都没有，取全网默认活水印
        if (map.get(pkey) != null) {
            cache = map.get(pkey);
        } else {
            if (map.get(ckey) != null) {
                cache = map.get(ckey);
            } else {
                cache = map.get(bkey);
            }
        }
        video.setWaterMark(transLiveWaterMark2Dto(cache));
    }

    public List<LiveWaterMarkDto> transLiveWaterMark2Dto(List<LiveWaterMarkCache> cache) {
        List<LiveWaterMarkDto> waterMarkDto = new ArrayList<LiveWaterMarkDto>();
        if (!CollectionUtils.isEmpty(cache)) {
            for (LiveWaterMarkCache waterMark : cache) {
                LiveWaterMarkDto dto = new LiveWaterMarkDto();
                dto.setLasttime(waterMark.getLasttime());
                dto.setOffset(waterMark.getOffset());
                dto.setPosition(waterMark.getPosition());
                dto.setUrl(waterMark.getUrl());
                waterMarkDto.add(dto);
            }
        }
        return waterMarkDto;
    }

    /**
     * 根据用户权限获得播放类型
     * @param albumMysqlTable
     * @param videoMysqlTable
     * @param userPlayAuth
     * @param videoPlay
     * @param login
     * @param chargeType
     */
    public void getPlayTypeByUserAuth(Integer categoryId, String userId, UserPlayAuth userPlayAuth, VideoDto videoPlay,
            Integer chargeType, String locale, String routerId) {
        Integer playType = 1;
        if (StringUtils.isEmpty(userId) && chargeType.intValue() != ChargeTypeConstants.chargePolicy.FREE.intValue()) {
            videoPlay.setTryPlayTipCode("A-1");// 您可以试看6分钟，观看全片请 开通会员 已是会员请 登录
            videoPlay.setTryPlayTime(new Long(1000 * 60 * 6));
            playType = 6;
        } else {
            RouteInfo routeInfo = transRouteInfo(routerId);
            boolean isRouter = routeInfo.isRoute();
            if (userPlayAuth != null && userPlayAuth.getValues() != null && userPlayAuth.getCode() != null
                    && userPlayAuth.getCode() == 0) {// code为0表示成功返回，若不正常，直接正常播放
                if ("1".equalsIgnoreCase(userPlayAuth.getValues().getStatus())) {
                    playType = 1;
                    if (isRouter) {
                        videoPlay.setVipStatus(1);
                    }
                } else {
                    // 观影券、开通会员逻辑：前提院线收费的影片
                    if (chargeType == ChargeTypeConstants.chargePolicy.CHARGE_YUAN_XIAN) {
                        if (StringUtils.isEmpty(userPlayAuth.getValues().getTicketSize())) {// 鉴权未通过&&没有券
                            if (userPlayAuth.getValues().getIsCellVip()) {// 是VIP
                                long leftTime = userPlayAuth.getValues().getCellVipCancelTime()
                                        - System.currentTimeMillis();
                                if (leftTime > 0) {// 是VIP并且未过期
                                    videoPlay.setTryPlayTipCode("A-3");// 您可以试看6分钟，本月赠送的观影券已用完，请到乐视网购买
                                    videoPlay.setTryPlayTime(new Long(1000 * 60 * 6));
                                    playType = 7;
                                    if (isRouter) {
                                        videoPlay.setVipStatus(1);
                                    }
                                } else {
                                    videoPlay.setTryPlayTipCode("A-5");// 您可以试看6分钟，观看全片请
                                    // 开通会员
                                    playType = 6;// 开通会员
                                    videoPlay.setTryPlayTime(new Long(1000 * 60 * 6));
                                }
                            } else {
                                videoPlay.setTryPlayTipCode("A-5");// 您可以试看6分钟，观看全片请
                                                                   // 开通会员
                                playType = 6;// 开通会员
                                videoPlay.setTryPlayTime(new Long(1000 * 60 * 6));
                            }
                        } else {
                            videoPlay.setUserTicketSize(Integer.valueOf(userPlayAuth.getValues().getTicketSize()));
                            videoPlay.setVideoTicketSize(1);
                            videoPlay.setTicketValideTime(new Long(48 * 60 * 1000));
                            videoPlay.setTryPlayTipCode("A-2");// 您可以试看6分钟，观看全片需使用1张观影券
                                                               // 点击使用
                            videoPlay.setTryPlayTime(new Long(1000 * 60 * 6));
                            playType = 7;// 观影券
                            if (isRouter) {
                                videoPlay.setVipStatus(1);
                            }
                        }
                    } else if (chargeType == ChargeTypeConstants.chargePolicy.CHARGE_BY_STREAM) {
                        // 开通会员流程：前提码流收费
                        playType = 6;// 开通会员
                        videoPlay.setTryPlayTime(new Long(1000 * 60 * 6));
                        videoPlay.setTryPlayTipCode("A-1");
                    }
                }
            }

            if (isRouter && StringUtils.isNotEmpty(userId)
                    && chargeType.intValue() == ChargeTypeConstants.chargePolicy.FREE.intValue()) {
                videoPlay.setVipStatus(1);
            }
        }

        if (categoryId.intValue() == VideoConstants.Category.TV && playType.intValue() != 1) {
            // 开通会员
            playType = 8;// 付费电视剧开通会员
            videoPlay.setTryPlayTime(new Long(0));// 去掉试看
        }

        videoPlay.setPlayType(playType);
    }

    /**
     * 视点图功能
     * @param vrsVideoPic
     * @return
     */
    public String getVideoViewPicUrl(String vrsVideoPic) {
        if (StringUtils.isNotEmpty(vrsVideoPic)) {
            return vrsVideoPic + "/viewpoint/desc_mp.json";
        }

        return null;
    }

    public Response<VideoDto> getDownloadInfo(Long videoId, String clientRequestStream, Long timestamp,
            String signature, Long userId, String clientIp, CommonParam param) {
        Response<VideoDto> response = new Response<VideoDto>();
        VideoDto videoPlay = new VideoDto();

        /**
         * md5(videoId+albumId+timestamp+业务方app密钥)
         */
        // if (validSig(videoId, null, timestamp, signature)) {

        PlayCache playCacheEntity = this.facadeService.getVideoService().getPlayCacheEntityByVideoId(videoId);

        // 如果是付费的或者没有下载版权的均不能下载
        if (playCacheEntity == null || playCacheEntity.getvPayPlatform().contains("141003")
                || StringUtils.isEmpty(playCacheEntity.getvDownloadPlatform())
                || !playCacheEntity.getvDownloadPlatform().contains("290003")) {
            // 无法下载
            videoPlay.setDownload(0);
        } else {
            // 处理码流
            String playStream = getDownloadPlayStream(clientRequestStream, playCacheEntity.getvVideoStreams());

            if (StreamConstants.CHARGE_STREAM_SET.contains(playStream) || playStream.startsWith("3d")
                    || playStream.endsWith("_db") || playStream.endsWith("_dts")) {
                // 无法下载
                videoPlay.setDownload(0);
            } else {
                // 获得媒资视频文件播放信息
                MmsStore mmsStore = this.getMmsFilePlayInfo(videoId, playCacheEntity.getvMidL(), playStream, clientIp,
                        2);
                // 从媒资视频文件解析播放信息
                this.setVideoPlayFromMmsFile(mmsStore, videoPlay, playStream);
                videoPlay.setImg(playCacheEntity.getVPic(400, 250));
                videoPlay.setAlbumImg(playCacheEntity.getAPic(400, 250));
                videoPlay.setName(playCacheEntity.getvNameCn());
                videoPlay.setCurrentStream(playStream);
                videoPlay.setCurrentStreamName(StreamConstants.nameOf(playStream, param.getLangcode()));

                Integer categoryId = playCacheEntity.getvCategoryId();
                Integer videoType = playCacheEntity.getvType();
                String varietyShow = playCacheEntity.getaVarietyShow();
                int hasAlbum = (playCacheEntity.getaId() != null && playCacheEntity.getaId() != 0) ? 1 : 0;

                // 设置视频片头、片尾时间
                VideoHot vh = VideoUtil.getHeadTailInfo(categoryId, playCacheEntity.getvBtime(),
                        playCacheEntity.getvEtime());
                videoPlay.setVideoHeadTime(vh.getT());
                videoPlay.setVideoTailTime(vh.getF());
                videoPlay.setEpisode(playCacheEntity.getvEpisode());
                videoPlay.setPage(playCacheEntity.getvPage());
                videoPlay.setPageNum(getNewPage(playCacheEntity.getvCategoryId(), playCacheEntity.getaVarietyShow(),
                        playCacheEntity.getvPage()));
                videoPlay.setOrderInAlbum(playCacheEntity.getvPorder());
                videoPlay.setEpisode(playCacheEntity.getvEpisode());
                videoPlay.setVideoTypeId(playCacheEntity.getvType());
                videoPlay.setHasSeries(this.facadeService.getVideoService().needSeries(playCacheEntity.getaId(),
                        categoryId, videoType, varietyShow, hasAlbum));
                setLiveWaterMark(videoPlay, playCacheEntity.getaId(), playCacheEntity.getvCategoryId());
                // 下载
                videoPlay.setDownload(1);
            }
        }
        // } else {
        // // 签名校验不通过无法下载
        // }

        response.setData(videoPlay);
        return response;
    }
}
