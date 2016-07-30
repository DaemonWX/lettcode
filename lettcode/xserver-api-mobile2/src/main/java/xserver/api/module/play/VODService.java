package xserver.api.module.play;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xserver.api.constant.DataConstants;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.play.dto.ChargeDto;
import xserver.api.module.play.dto.Live;
import xserver.api.module.play.dto.SuperLiveWaterMarkDto;
import xserver.api.module.play.dto.VODDto;
import xserver.api.module.play.util.LiveUtil;
import xserver.api.module.superlive.SuperLiveConstant;
import xserver.api.module.video.chargepolicy.ChargeTypeConstants;
import xserver.api.module.video.dto.VideoHot;
import xserver.api.module.video.util.VideoUtil;
import xserver.api.response.Response;
import xserver.lib.constant.StreamConstants;
import xserver.lib.constant.VideoConstants;
import xserver.lib.dto.live.LiveDto;
import xserver.lib.tp.live.response.LiveChannelStream;
import xserver.lib.tp.live.response.LiveInfo;
import xserver.lib.tp.live.response.StreamTpResponse;
import xserver.lib.tp.video.response.MmsFile;
import xserver.lib.tp.video.response.MmsStore;
import xserver.lib.tp.video.response.UserLiveAuth;
import xserver.lib.tp.video.response.UserPlayAuth;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.tpcache.cache.LiveWaterMarkCache;
import xserver.lib.tpcache.cache.PlayCache;
import xserver.lib.tpcache.cache.Stream;
import xserver.lib.util.MessageUtils;

import com.fasterxml.jackson.core.type.TypeReference;

@Service(value = "VODService")
public class VODService extends BaseService {

    public VODDto getVideoPlayInfo(Long videoId, Long albumId, String stream, Long timestamp, String sig,
            String businessId, Integer rand, String clientIp, CommonParam param) {
        VODDto videoPlay = new VODDto();
        /**
         * 签名算法
         * 各业务方传入md5(videoId+albumId+timestamp+业务方businessId+业务方app密钥)
         * 生成[0-32)之间的随机整数rand
         * 通用播放器使用md5(videoId+albumId+timestamp+通用播放器businessId+通用播放器密钥).subStr(
         * 0,rand)+md5(videoId+
         * albumId+timestamp+业务方appId+业务方密钥).rand(rand)传入sig值
         */
        // if (!VODUtil.checkSig(videoId, albumId, timestamp, businessId, rand,
        // sig)) {
        // TODO 签名错误
        // log.error("wrong signature, videoid:"+videoId+"albumid:"+albumId+"timestamp:"+timestamp+
        // "businessid:"+businessId+"rand:"+rand+"sig:"+sig);
        // } else {
        PlayCache playCacheEntity = null;
        if (videoId != null) {
            playCacheEntity = this.facadeService.getVideoService().getPlayCacheEntityByVideoId(videoId);
        } else {
            playCacheEntity = this.facadeService.getVideoService().getPlayCacheEntityByAlbumId(albumId);
        }

        if (playCacheEntity != null) {
            // 处理码流
            String playStream = getVideoPlayStream(stream, playCacheEntity.getvVideoStreams(), videoPlay,
                    param.getLangcode());
            videoPlay.setCurrentStream(playStream);

            // 从缓存对象获得定时后台跑出的属性
            setVideoPlayFromCacheEntity(playCacheEntity, videoPlay, param.getWcode(), param.getLangcode());
            // 从VRS获得视频的基本信息
            this.getVideoPlayFromVRS(playCacheEntity, videoPlay, playStream, param.getLangcode());

            // 获得媒资视频文件播放信息
            MmsStore mmsStore = this.getMmsFilePlayInfo(videoId, playCacheEntity.getvMidL(), playStream, clientIp, 0,
                    businessId, param);

            // 播放鉴权
            UserPlayAuth userPlayAuth = this.getUserAuth(ChargeTypeConstants.chargePolicy.FREE,
                    playCacheEntity.getaId(), param.getUid(), mmsStore, playStream, param.getDevId(), "");

            int validateMmsStore = VideoUtil.validateMmsStore(mmsStore);

            if (VideoUtil.validateMmsStore.SUCCESS == validateMmsStore) {
                // 从媒资视频文件解析播放信息
                this.setVideoPlayFromMmsFile(mmsStore, videoPlay, playStream, param.getUid(), userPlayAuth);
            } else if (VideoUtil.validateMmsStore.VIDEO_CN_PLAY_FORBIDDEN == validateMmsStore) {
                // TODO 错误码：大陆地区禁止播放
            } else if (VideoUtil.validateMmsStore.VIDEO_NOT_CN_PLAY_FORBIDDEN == validateMmsStore) {
                // TODO 错误码：大陆外禁止播放
            } else {
            }

            // 设置playType和tipMsg
            this.getPlayTypeByUserAuth(playCacheEntity.getvCategoryId(), userPlayAuth, videoPlay,
                    ChargeTypeConstants.chargePolicy.FREE, param.getLangcode());

            // 播放视频附加服务
            this.handleAdditionalService(videoPlay, playCacheEntity);
        } else {
            // 未找到可以播放的视频/专辑
        }
        // }

        return videoPlay;
    }

    private void setVideoPlayFromCacheEntity(PlayCache playCacheEntity, VODDto videoPlay, String wcode, String langCode) {
        videoPlay.setVideoId(playCacheEntity.getvId().toString());
        videoPlay.setStreams(getI18NStream(playCacheEntity.getvStreams(), wcode, langCode));
        videoPlay.setNormalStreams(getI18NStream(playCacheEntity.getvNormalStreams(), wcode, langCode));
        videoPlay.setTheatreStreams(getI18NStream(playCacheEntity.getvTheatreStreams(), wcode, langCode));
        videoPlay.setThreeDStreams(getI18NStream(playCacheEntity.getvThreeDStreams(), wcode, langCode));
        videoPlay.setPage(playCacheEntity.getvPage());
    }

    /**
     * 对码流名称进行多语言国际化
     * @param streamList
     * @param wcode
     * @param langCode
     * @return
     */
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

    /**
     * 从防盗链获得媒资视频文件播放信息
     * @param actType
     *            播放类型：0:点播 1:直播 2:下载
     */
    public MmsStore getMmsFilePlayInfo(Long videoId, Long mid, String playStream, String clientIp, int actType,
            String businessId, CommonParam param) {
        String vType = VideoUtil.getVType(playStream);
        Integer splatid = VideoUtil.getSplatId(param.getWcode(), param.getTerminalApplication(),
                param.getTerminalSeries(), businessId);
        MmsStore mmsStore = this.facadeTpDao.getVideoTpDao().getMmsPlayInfo(clientIp, videoId, mid, vType, actType,
                splatid);

        log.info(new StringBuffer().append(param.getTerminalApplication()).append("|").append(param.getAppVersion())
                .append("|").append(businessId).append("|").append(splatid).toString());
        return mmsStore;
    }

    /**
     * 获得视频最终播放码流
     * @return
     */
    private String getVideoPlayStream(String clientRequestStream, String[] videoStreams, VODDto videoPlay, String locale) {

        String playStream = "";
        videoPlay.setHasBelow(VideoConstants.HAS_BELOW_NO);// 默认标识不降码流
        if (StringUtils.isEmpty(clientRequestStream)) {
            // 客户端为传入码流获得默认播放码流
            playStream = this.getDefaultVideoPlayStream();
        } else if (!ArrayUtils.contains(videoStreams, clientRequestStream)) {
            // 降码流处理
            playStream = this.getReducedStream(clientRequestStream, videoStreams, StreamConstants.PLAY_SORT_STREAM_T2);
        } else {
            playStream = clientRequestStream;
        }

        return playStream;
    }

    /**
     * 从VRS获得视频的基本信息
     * @param videoMysqlTable
     * @param albumMysqlTable
     * @param videoPlay
     * @param playStream
     * @param locale
     * @return
     */
    public VODDto getVideoPlayFromVRS(PlayCache playCacheEntity, VODDto videoPlay, String playStream, String locale) {
        videoPlay.setName(playCacheEntity.getvNameCn());
        videoPlay.setCategoryId(playCacheEntity.getvCategoryId());
        videoPlay.setVideoTypeId(playCacheEntity.getvType());
        videoPlay.setPositive(playCacheEntity.getvAttr());
        videoPlay.setOrderInAlbum(playCacheEntity.getvPorder());
        videoPlay.setEpisode(playCacheEntity.getvEpisode());
        videoPlay.setPlayPlatform(playCacheEntity.getvPlayPlatform());

        // 是否收费
        if (playCacheEntity != null && playCacheEntity.isMobPay()) {
            videoPlay.setIfCharge(ChargeTypeConstants.CHARGE);
        } else {
            videoPlay.setIfCharge(ChargeTypeConstants.NOT_CHARGE);
        }

        // 设置视频片头、片尾时间
        VideoHot vh = VideoUtil.getHeadTailInfo(playCacheEntity.getvCategoryId(), playCacheEntity.getvBtime(),
                playCacheEntity.getvEtime());
        videoPlay.setVideoHeadTime(vh.getT());
        videoPlay.setVideoTailTime(vh.getF());

        // DRM标识
        videoPlay.setDrmFlagId(playCacheEntity.getvDrmFlagId());

        // 家长锁功能
        videoPlay.setIsPlayLock(playCacheEntity.getvIsPlayLock());
        // 以下为兼容处理
        // 1、播放记录图片兼容
        if (playCacheEntity.getaId() == null || playCacheEntity.getaId() == 0 || playCacheEntity == null) {
            videoPlay.setImg(playCacheEntity.getAPic(400, 300));
        } else {
            videoPlay.setImg(playCacheEntity.getVPic(400, 300));
        }

        // 2、专辑名称兼容
        String albumName = playCacheEntity.getvNameCn();
        if (playCacheEntity != null) {
            albumName = playCacheEntity.getaNameCn();
        }
        videoPlay.setAlbumId(playCacheEntity.getaId() != null ? playCacheEntity.getaId().toString() : null);
        videoPlay.setHasAlbum((playCacheEntity.getaId() != null && playCacheEntity.getaId() != 0) ? 1 : 0);

        return videoPlay;
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
     * @return
     */
    private String getReducedStream(String clientRequestStream, String[] videoStreams, String sortStreams) {
        String playStream = "";

        int clientRequestIndex = xserver.lib.constant.StreamConstants.PLAY_STREAM_CODE2ORDER.get(clientRequestStream);
        int videoMinIndex = xserver.lib.constant.StreamConstants.PLAY_STREAM_CODE2ORDER.get(videoStreams[0]);
        if (clientRequestIndex < videoMinIndex) {
            playStream = videoStreams[0];
        } else {
            playStream = videoStreams[videoStreams.length - 1];
        }
        return playStream;
    }

    public void setVideoPlayFromMmsFile(MmsStore mmsStore, VODDto videoPlay, String playStream, String uid,
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

    /**
     * 播放视频附加服务
     * @param videoPlay
     * @param videoMysqlTable
     * @return
     */
    public void handleAdditionalService(VODDto videoPlay, PlayCache playCacheEntity) {

        // 1、水印
        // if (1 == videoMysqlTable.getLogonum()) {

        // }

        // 2、视点图
        videoPlay.setViewPic(this.getVideoViewPicUrl(playCacheEntity.getvPic()));
    }

    /**
     * 视点图功能
     * @param vrsVideoPic
     * @return
     */
    public String getVideoViewPicUrl(String vrsVideoPic) {
        if (StringUtils.isNotEmpty(vrsVideoPic)) {
            return vrsVideoPic + "/viewpoint/desc_200x170.json";
        }

        return null;
    }

    /**
     * 根据用户权限获得播放类型
     * @return
     */
    public UserPlayAuth getUserAuth(Integer chargeType, Long pid, String userId, MmsStore mmsStore, String playStream,
            String imei, String deviceKey) {
        UserPlayAuth userPlayAuth = null;
        if (ChargeTypeConstants.chargePolicy.FREE != chargeType) {
            MmsFile mmsFile = VideoUtil.getMmsFileByVTypeOrder(playStream, mmsStore);
            String storePath = null;
            if (mmsFile != null) {
                storePath = mmsFile.getStorePath();
            }
            userPlayAuth = this.facadeTpDao.getVipTpDao().getUserPlayAuth(pid, userId, storePath, imei, deviceKey);
        }

        return userPlayAuth;
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
    public void getPlayTypeByUserAuth(Integer categoryId, UserPlayAuth userPlayAuth, VODDto videoPlay,
            Integer chargeType, String locale) {
        // if (userPlayAuth != null &&
        // VideoTpConstants.UserPlayAuth.PLAY_SERVICE_OPEN ==
        // userPlayAuth.getStatus()) {
        // chargeType = ChargeTypeConstants.chargePolicy.FREE;
        // }

        // 根据不同的收费策略展示不同的试看提示
        if (ChargeTypeConstants.chargePolicy.CHARGE_BY_STREAM == chargeType
                || ChargeTypeConstants.chargePolicy.CHARGE_YUAN_XIAN == chargeType) {
            videoPlay.setTryPlayTime(new Long(1000 * 60 * 6));
            videoPlay.setTryPlayTipMsg(MessageUtils.getMessage("VIDEO.SHIKAN.SIXMIN", locale));
        } else if (ChargeTypeConstants.chargePolicy.CHARGE_BY_CONTENT == chargeType) {
            videoPlay.setTryPlayTime(VideoUtil.getTryPlayTime(categoryId, videoPlay.getVideoTailTime(),
                    videoPlay.getDuration()));
            videoPlay
                    .setTryPlayTipMsg((categoryId != null && (VideoConstants.Category.FILM == categoryId)) ? MessageUtils
                            .getMessage("VIDEO.SHIKAN.SIXMIN", locale) : MessageUtils.getMessage("VIDEO.SHIKAN.DIYIJI",
                            locale));
        }

        videoPlay.setPlayType(ChargeTypeConstants.chargePolicy.FREE);
    }

    /**
     * @param liveId
     * @param stream
     * @param type
     * @return
     */
    public Live getLiveInfo(String liveId, String liveType, String stream, Integer type) {
        Live live = new Live();
        switch (type) {
        case 1:
            LiveDto response = this.facadeService.getLiveService().liveDetail(liveType, liveId, "1046", "zh_cn");
            List<LiveChannelStream> liveStreamList = this.facadeTpDao.getLiveTpDao()
                    .getStreamResponseTp("1046", liveId);

            live.setLiveName(response.getLiveName());
            for (LiveChannelStream liveChannelStream : liveStreamList) {
                if (liveChannelStream.getRateType().contains(stream)) {

                }

            }
            // live.setPlayUrl(playUrl);
            break;

        default:
            break;
        }

        return null;
    }

    /**
     * 由直播id拿到直播流id，再由直播流id拿到所有直播流列表
     * 直播防盗链策略:streamId,splatid
     * @param liveId
     * @param requestStream
     * @return
     */
    public Live getLivePlayInfo(String liveId, String requestStream, String userId, String imei, String businessId,
            CommonParam commonParam) {
        Live resp = new Live();
        String langcode = commonParam != null ? commonParam.getLangcode() : DataConstants.LANGCODE_CN;
        /**
         * clientId:由直播部门定义，请见 http://api.live.letv.com/v1/dictionary/00013
         * channelid：频道id
         */
        String clientId = LiveUtil.business2SplatidMap.get(businessId);
        if (StringUtils.isEmpty(clientId)) {
            clientId = "1036";
            log.error("businessId:" + businessId + " not registed! Default:1036");
        }
        LiveInfo tpLiveInfo = this.facadeTpDao.getLiveTpDao().getSingleLiveInfo(liveId, clientId);

        if (tpLiveInfo != null) {
            resp.setLiveName(tpLiveInfo.getTitle());
            String channelId = tpLiveInfo.getSelectId();// 获得直播节目的频道信息，在某个频道上有流信息

            Live playObj = new Live();
            playObj.setLiveType(LiveUtil.liveTypeMap.get(tpLiveInfo.getLiveType()));
            playObj.setLiveId(tpLiveInfo.getId());
            playObj.setScreeningId(tpLiveInfo.getScreenings());
            resp.setPlayObj(playObj);

            StreamTpResponse streamTpResponse = this.facadeTpDao.getLiveTpDao().getSingleLiveStreams(channelId,
                    clientId);
            if (streamTpResponse != null) {
                List<LiveChannelStream> tpLiveStreams = streamTpResponse.getRows();
                Map<String, LiveChannelStream> tmpTpStreamMap = new HashMap<String, LiveChannelStream>();
                if (!CollectionUtils.isEmpty(tpLiveStreams)) {
                    List<Stream> liveStreams = new ArrayList<Stream>();
                    for (LiveChannelStream tpStream : tpLiveStreams) {
                        Stream s = new Stream();
                        s.setCode(LiveUtil.transTpLiveStream2PlayStream(tpStream.getRateType()));
                        s.setName(LiveUtil.getLiveStreamName(s.getCode(), langcode));

                        liveStreams.add(s);
                        tmpTpStreamMap.put(s.getCode(), tpStream);
                        if (StringUtils.isNotEmpty(requestStream)) {
                            if (requestStream.equalsIgnoreCase(s.getCode())) {
                                resp.setPlayUrl(tpStream.getStreamUrl() + "&platid=10&splatid=" + clientId
                                        + "&expect=3&format=1");

                                resp.setCurrentStream(s.getCode());
                                resp.setCurrentStreamId(tpStream.getStreamName());
                            }
                        } else if ("1300".equalsIgnoreCase(s.getCode())) {// 默认码流
                            resp.setPlayUrl(tpStream.getStreamUrl() + "&platid=10&splatid=" + clientId
                                    + "&expect=3&format=1");

                            resp.setCurrentStream("1300");
                            resp.setCurrentStreamId(tpStream.getStreamName());
                        }
                    }
                    // 码流排序
                    Collections.sort(liveStreams, new Comparator<Stream>() {
                        @Override
                        public int compare(Stream o1, Stream o2) {
                            return (StreamConstants.STREAM_CODE_SORT_VSLUE.get(o2.getCode()) != null ? StreamConstants.STREAM_CODE_SORT_VSLUE
                                    .get(o2.getCode()) : new Integer(0)).compareTo(StreamConstants.STREAM_CODE_SORT_VSLUE
                                    .get(o1.getCode()) != null ? StreamConstants.STREAM_CODE_SORT_VSLUE.get(o1
                                    .getCode()) : new Integer(0));
                        }
                    });
                    resp.setStreams(liveStreams);

                    // 请求的码流不在直播支持的码流之内，走降码流逻辑
                    if (StringUtils.isEmpty(resp.getCurrentStream())) {
                        for (Stream sortedStream : liveStreams) {
                            if (StreamConstants.STREAM_CODE_SORT_VSLUE.get(requestStream) != null) {
                                if (StreamConstants.STREAM_CODE_SORT_VSLUE.get(sortedStream.getCode()) <= StreamConstants.STREAM_CODE_SORT_VSLUE
                                        .get(requestStream)) {
                                    resp.setPlayUrl(tmpTpStreamMap.get(sortedStream.getCode()).getStreamUrl()
                                            + "&platid=10&splatid=" + clientId + "&expect=3&format=1");

                                    resp.setCurrentStream(sortedStream.getCode());
                                    resp.setCurrentStreamId(tmpTpStreamMap.get(sortedStream.getCode()).getStreamName());
                                }
                            } else {
                                resp.setPlayUrl(tmpTpStreamMap.get(sortedStream.getCode()).getStreamUrl()
                                        + "&platid=10&splatid=" + clientId + "&expect=3&format=1");

                                resp.setCurrentStream(sortedStream.getCode());
                                resp.setCurrentStreamId(tmpTpStreamMap.get(sortedStream.getCode()).getStreamName());

                                break;
                            }
                        }
                    }
                } else {
                    this.log.error("getLivePlayInfo:no streams, liveId:" + liveId + ", channelId:" + channelId);
                }
            }

            // 付费直播，走鉴权
            if (tpLiveInfo != null && tpLiveInfo.getIsPay() != null && tpLiveInfo.getIsPay().intValue() == 1) {
                if (StringUtils.isNotEmpty(userId)) {
                    UserLiveAuth liveAuth = this.facadeTpDao.getVipTpDao().getUserLiveAuth(liveId,
                            tpLiveInfo.getScreenings(), resp.getCurrentStreamId(), clientId, userId, imei);
                    if (liveAuth != null) {
                        if (liveAuth.getStatus().equalsIgnoreCase("1")) {// 鉴权通过，正常播放
                            ChargeDto charge = new ChargeDto();
                            charge.setOrdertype(liveAuth.getOrdertype());
                            charge.setTeamName(liveAuth.getTeamName());
                            charge.setLiveStatus(liveAuth.getLiveStatus());
                            charge.setStatus(liveAuth.getStatus());
                            resp.setChargeObj(charge);

                            if (liveAuth.getLiveStatus() != null && liveAuth.getLiveStatus() == 1)// 直播未开始
                            {
                                resp.setPlayType(0);// 直播未开始，也不能播放
                            } else {
                                resp.setPlayType(1);
                            }
                            // 返回token
                            String token = liveAuth.getToken();
                            resp.setPlayUrl(resp.getPlayUrl() + "&uid=" + userId + "&token=" + token);

                            if (StringUtils.isNotEmpty(liveAuth.getUinfo())) {
                                resp.setPlayUrl(resp.getPlayUrl() + "&uinfo=" + liveAuth.getUinfo());
                            }
                        } else if (liveAuth.getStatus().equalsIgnoreCase("0") && liveAuth.getInfo() != null) {// 不能直接播放
                            ChargeDto charge = new ChargeDto();
                            charge.setCode(liveAuth.getInfo().getCode());
                            charge.setMsg(liveAuth.getInfo().getMsg());
                            charge.setLiveStatus(liveAuth.getInfo().getLiveStatus());// 返回直播状态
                            charge.setUserTicketSize(liveAuth.getInfo().getCount());// 直播卷数量
                            charge.setTeam(liveAuth.getInfo().getTeam());
                            charge.setSeason(liveAuth.getInfo().getSeason());

                            resp.setPlayType(0);
                            resp.setChargeObj(charge);
                        } else if (liveAuth.getStatus().equalsIgnoreCase("0") && liveAuth.getError() != null) {
                            ChargeDto charge = new ChargeDto();
                            charge.setCode(liveAuth.getError().getCode());
                            charge.setMsg(liveAuth.getError().getMsg());

                            resp.setPlayType(0);
                            resp.setChargeObj(charge);
                        }
                    } else {
                        // 鉴权出异常返回空，直接正常播放
                        resp.setPlayType(1);
                    }
                } else {
                    // 付费直播、未登录用户试看
                    ChargeDto charge = new ChargeDto();
                    charge.setCode("PL001");// 未登录用户无法观看付费直播

                    resp.setPlayType(0);
                    resp.setChargeObj(charge);
                }
            } else {// 免费直播，直接正常看
                resp.setPlayType(1);
            }
        } else {
            this.log.error("getLivePlayInfo:no liveInfo, liveId:" + liveId);
        }

        return resp;
    }

    /**
     * 获取直播水印
     * @return
     */
    public Response<SuperLiveWaterMarkDto> getLiveWaterMark(String channelId, String type) {
        Response<SuperLiveWaterMarkDto> response = new Response<SuperLiveWaterMarkDto>();
        // 暂时只有轮播加水印
        if (SuperLiveConstant.LIVE_TYPE_CHANNEL_LUNBO.equals(type)) {
            List<LiveWaterMarkCache> liveWaterMarkList = this.tpCacheTemplate.get(CacheConstants.LiveWaterMark_Base,
                    new TypeReference<List<LiveWaterMarkCache>>() {
                    });
            if (liveWaterMarkList != null) {
                SuperLiveWaterMarkDto markDto = new SuperLiveWaterMarkDto();
                markDto.setChannelId(channelId);
                markDto.setType(type);
                markDto.setWartermark(this.facadeService.getPlayService().transLiveWaterMark2Dto(liveWaterMarkList));
                response.setData(markDto);
            }
        }
        return response;
    }

}
