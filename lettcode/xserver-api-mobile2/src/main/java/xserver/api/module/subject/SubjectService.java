package xserver.api.module.subject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Ints;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xserver.api.constant.ErrorCodeConstants;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.channel.ChannelConstant;
import xserver.api.module.subject.constants.SubjectConstants;
import xserver.api.module.subject.dto.BaseSubjectDataDto;
import xserver.api.module.subject.dto.Block;
import xserver.api.module.subject.dto.CmsSubjectDto;
import xserver.api.module.subject.dto.CmsSubjectListDto;
import xserver.api.module.subject.dto.SubjectDto;
import xserver.api.module.subject.dto.SubjectInfoDto;
import xserver.api.module.video.dto.AlbumDto;
import xserver.api.module.video.dto.VideoDto;
import xserver.api.response.Response;
import xserver.common.dto.BaseDto;
import xserver.common.dto.superlive.v2.SuperLiveChannelDto;
import xserver.common.dto.superlive.v2.SuperLiveProgramDto;
import xserver.common.util.TimeUtil;
import xserver.lib.dto.live.LiveDto;
import xserver.lib.dto.subject.H5Activity;
import xserver.lib.tp.TpErrorCodeConstant;
import xserver.lib.tp.cloud.response.HotWordsCountResponse;
import xserver.lib.tp.cloud.response.UserHotWordCount;
import xserver.lib.tp.cloud.response.WallPageBatchResponse;
import xserver.lib.tp.cloud.response.WallPaperDigestInfo;
import xserver.lib.tp.cms.response.CmsBlockContent;
import xserver.lib.tp.cms.response.CmsBlockTpResponse;
import xserver.lib.tp.cms.response.ContentPackage;
import xserver.lib.tp.cms.response.ContentPackage.ContentItem;
import xserver.lib.tp.cms.response.SubjectContent;
import xserver.lib.tp.cms.response.SubjectContentTpResponse;
import xserver.lib.tp.lines.response.TagInfo;
import xserver.lib.tp.lines.response.TagResponse;
import xserver.lib.tp.video.response.MmsAudioResponse;
import xserver.lib.tp.video.response.MmsAudioResponse.XiamiData;
import xserver.lib.tp.video.response.MmsHotWords;
import xserver.lib.tp.video.response.MmsOstInfo;
import xserver.lib.tp.video.response.MmsResponse;
import xserver.lib.tp.vote.response.QRCode;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.tpcache.cache.ActorCache;
import xserver.lib.tpcache.cache.HotWordsCache;
import xserver.lib.tpcache.cache.MusicCache;
import xserver.lib.tpcache.cache.OstCache;
import xserver.lib.tpcache.cache.WallPaperDto;
import xserver.lib.util.CommonUtil;
import xserver.lib.util.MessageDigestUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专题相关业务逻辑
 */
@Service
public class SubjectService extends BaseService implements SubjectServiceInterface {
    @Resource
    private SubjectServiceInterface videoSubjectService;
    @Resource
    private SubjectServiceInterface albumSubjectService;
    @Resource
    private SubjectServiceInterface liveSubjectService;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 专题下，不同数据包service集合
     */
    private final Map<String, GetService> services = new HashMap<String, GetService>();
    {
        services.put(SubjectConstants.SUBJECT_PACKAGE_TYPE_VIDEO, new GetService() {
            @Override
            public SubjectServiceInterface getSerivice() {
                return videoSubjectService;
            }
        });
        services.put(SubjectConstants.SUBJECT_PACKAGE_TYPE_ALBUM, new GetService() {
            @Override
            public SubjectServiceInterface getSerivice() {
                return albumSubjectService;
            }
        });
        services.put(SubjectConstants.SUBJECT_PACKAGE_TYPE_LIVE, new GetService() {
            @Override
            public SubjectServiceInterface getSerivice() {
                return liveSubjectService;
            }
        });
    }

    private interface GetService {
        public SubjectServiceInterface getSerivice();
    }

    /**
     * 热点是由CMS中的小专题提供数据
     * @param zid
     * @return
     */
    public Response<SubjectInfoDto> getHotSubjectById(String zid, CommonParam commonParam) {
        Response<SubjectInfoDto> response = new Response<SubjectInfoDto>();
        // 根据专题id获取专题信息，包括其下的数据包
        SubjectContentTpResponse subjectContentTpResponse = facadeTpDao.getCmsTpDao().getSubjectContent(zid);
        // 校验请求是否ok
        if (subjectContentTpResponse != null && subjectContentTpResponse.getCode() != null
                && subjectContentTpResponse.getCode() == 200) {
            // 取出response中专题信息
            SubjectContent subjectContent = subjectContentTpResponse.getData();// 获取专题包
            // 从专题包中取出数据名，可能包含：视频包、专辑包、直播包。对于热点频道，仅取视频包数据
            List<ContentPackage> contentPackage = subjectContent.getTjPackages();
            if (!subjectContent.getTjPackages().isEmpty()) {
                for (ContentPackage c : contentPackage) {
                    // 取出视频包数据，跳出循环
                    if (c.getPtype() != null && SubjectConstants.SUBJECT_PACKAGE_TYPE_VIDEO.equals(c.getPtype())) {
                        SubjectInfoDto subjectInfoDto = getSubjectInfo(c, commonParam);
                        response.setData(subjectInfoDto);
                        break;
                    }
                }
            }
        } else {
            // 请求不合法，异常处理。1.第三方故障，2本地切服
            if (subjectContentTpResponse.getInnerCode() != null
                    && TpErrorCodeConstant.CLOSE_SERVER_CODE.equals(subjectContentTpResponse.getInnerCode())) {
                setErrorResponse(response, ErrorCodeConstants.CLOSE_SERVER_TP,
                        ErrorCodeConstants.CLOSE_SERVER_TP_SUBJECT, commonParam.getLangcode());
            } else {
                setErrorResponse(response, ErrorCodeConstants.EXCEPION_SERVER_TP,
                        ErrorCodeConstants.CLOSE_SERVER_TP_SUBJECT, commonParam.getLangcode());
            }
        }
        return response;
    }

    /**
     * 专题列表数据，是由CMS版块中获取出来的。
     * @param bid
     * @return
     */
    public Response<CmsSubjectListDto> getSubjectListByBlockId(String bid, CommonParam commonParam) {
        Response<CmsSubjectListDto> response = new Response<CmsSubjectListDto>();
        // 获取版块id数据，专题列表是挂在某一版块下的
        CmsBlockTpResponse cmsBlockTpResponse = facadeTpDao.getCmsTpDao().getCmsBlock(bid);
        // 校验请求数据合法性
        if (cmsBlockTpResponse != null && cmsBlockTpResponse.getBlockContent() != null) {
            List<CmsBlockContent> list = cmsBlockTpResponse.getBlockContent();
            CmsSubjectListDto cmsSubjectListDto = new CmsSubjectListDto();
            List<CmsSubjectDto> subjects = new ArrayList<CmsSubjectDto>();
            // 遍历版块下的数据列表，每一条数据对应一个专题
            for (CmsBlockContent cmsBlockContent : list) {
                CmsSubjectDto cmsSubjectDto = new CmsSubjectDto();
                // 专题名称
                cmsSubjectDto.setNameCn(cmsBlockContent.getTitle());
                // 专题图片
                cmsSubjectDto.setPic(cmsBlockContent.getMobilePic());
                // 1：视频，2：专辑，3：直播Code，4：用户ID，5：小专题ID，6：轮播台ID，7：直播专题ID,在专题列表应该只有5或7
                cmsSubjectDto.setType(cmsBlockContent.getType());
                // 专题id,type为5或7，为专题id
                cmsSubjectDto.setContentId(cmsBlockContent.getContent());
                // webView跳转地址
                cmsSubjectDto.setWebViewUrl(cmsBlockContent.getSkipUrl());
                // 跳转类型 1移动外跳，2移动内跳，3，小专题地址，4tv外跳，5tv内跳
                cmsSubjectDto.setSkipType(cmsBlockContent.getSkipType());
                subjects.add(cmsSubjectDto);
            }
            cmsSubjectListDto.setSubjects(subjects);
            response.setData(cmsSubjectListDto);
        } else {
            // 异常处理，1切服、2第三方请求故障
            if (cmsBlockTpResponse.getInnerCode() != null) {
                if (TpErrorCodeConstant.CLOSE_SERVER_CODE.equals(cmsBlockTpResponse.getInnerCode())) {
                    setErrorResponse(response, ErrorCodeConstants.CLOSE_SERVER_TP,
                            ErrorCodeConstants.CLOSE_SERVER_TP_SUBJECT, commonParam.getLangcode());
                }
            }
            setErrorResponse(response, ErrorCodeConstants.EXCEPION_SERVER_TP,
                    ErrorCodeConstants.CLOSE_SERVER_TP_SUBJECT, commonParam.getLangcode());
        }
        return response;
    }

    /**
     * 根据专题id返回其下所有数据包
     * 一个专题算是一个大包，其有视频包、专辑包、直播包的组合。
     * @param zid
     * @param commonParam
     * @return
     */
    public Response<SubjectInfoDto> getSubjectContentById(String zid, CommonParam commonParam) {
        Response<SubjectInfoDto> response = new Response<SubjectInfoDto>();
        // 获取专题整体数据
        SubjectContentTpResponse subjectContentTpResponse = facadeTpDao.getCmsTpDao().getSubjectContent(zid);
        // 校验请求合法性
        if (subjectContentTpResponse != null && subjectContentTpResponse.getCode() != null
                && subjectContentTpResponse.getCode() == 200) {
            // 获取专题包数据
            SubjectContent subjectContent = subjectContentTpResponse.getData();// 获取专题包
            // 获取专题包下面的所有数据包
            List<ContentPackage> contentPackage = subjectContent.getTjPackages();
            // 判断是否有包
            if (!subjectContent.getTjPackages().isEmpty()) {
                SubjectInfoDto subjectInfoDto = new SubjectInfoDto();
                List<BaseSubjectDataDto> list = new ArrayList<BaseSubjectDataDto>();
                String pType = null;
                // 封装数据列表
                for (ContentPackage c : contentPackage) {
                    // 封装其中某一包数据、例如、视频包、专辑包，直播包
                    SubjectInfoDto subjectInfoDtoTmp = getSubjectInfo(c, commonParam);
                    // 数据合并
                    if (subjectInfoDtoTmp != null && subjectInfoDtoTmp.getDataList() != null
                            && subjectInfoDtoTmp.getDataList().size() > 0) {
                        list.addAll(subjectInfoDtoTmp.getDataList());
                        pType = subjectInfoDtoTmp.getType();
                    }
                }
                subjectInfoDto.setDataList(list);
                subjectInfoDto.setName(subjectContent.getName());
                subjectInfoDto.setType(pType);
                subjectInfoDto.setCtime(subjectContent.getCtime());
                subjectInfoDto.setTag(subjectContent.getTag());
                subjectInfoDto.setDesc(subjectContent.getDescription());
                response.setData(subjectInfoDto);
            }
            return response;
        } else {
            // 异常处理
            if (subjectContentTpResponse != null && subjectContentTpResponse.getInnerCode() != null
                    && TpErrorCodeConstant.CLOSE_SERVER_CODE.equals(subjectContentTpResponse.getInnerCode())) {
                setErrorResponse(response, ErrorCodeConstants.CLOSE_SERVER_TP,
                        ErrorCodeConstants.CLOSE_SERVER_TP_SUBJECT, commonParam.getLangcode());
            } else {
                setErrorResponse(response, ErrorCodeConstants.EXCEPION_SERVER_TP,
                        ErrorCodeConstants.CLOSE_SERVER_TP_SUBJECT, commonParam.getLangcode());
            }
        }
        return response;
    }

    /**
     * 获取专题名数据
     */
    @Override
    public SubjectInfoDto getSubjectInfo(ContentPackage contentPackage, CommonParam commonParam) {
        // 根据专题数据包类型，获取service
        SubjectServiceInterface subjectServiceInterface = services.get(contentPackage.getPtype()).getSerivice();
        // service未找到，说明类型不合法。造成数据缺少不影响正常运行，不抛异常
        if (subjectServiceInterface == null) {
            log.warn("subject type not found .ptype:" + contentPackage.getPtype() + ",id:" + contentPackage.getId());
            return null;
        }
        // 根据具体service获取专题下具体包数据
        SubjectInfoDto subjectInfoDto = subjectServiceInterface.getSubjectInfo(contentPackage, commonParam);
        return subjectInfoDto;
    }

    public SubjectServiceInterface getVideoSubjectService() {
        return videoSubjectService;
    }

    public SubjectServiceInterface getAlbumSubjectService() {
        return albumSubjectService;
    }

    public SubjectServiceInterface getLiveSubjectService() {
        return liveSubjectService;
    }

    public QRCode submitQRCode(String guid, String token, CommonParam commonParam) {
        String key = "";
        try {
            key = MessageDigestUtil.md5((guid + commonParam.getUid() + "letv_qrcode").getBytes(CommonUtil.UTF8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.facadeTpDao.getQRTpDao().submitQRCode(guid, token, key);
    }

    public SubjectDto getSubject(String subjectId) {
        SubjectContentTpResponse tpResponse = facadeTpDao.getCmsTpDao().getSubjectContent(subjectId);
        SubjectDto subject = null;
        SubjectContent subjectContent = tpResponse.getData();
        if (tpResponse != null && tpResponse.getCode() != null && tpResponse.getCode().intValue() == 200
                && subjectContent != null) {
            subject = new SubjectDto();
            List<Block> blockList = new ArrayList<Block>();

            // 填充视频、专辑和直播包
            List<Block> pBlockList = this.fillPackageBlock(subjectContent);
            if (pBlockList != null && pBlockList.size() > 0) {
                blockList.addAll(pBlockList);
            }

            // 填充专用板块，包括明星、音乐、乐词关注、活动
            CmsBlockTpResponse cmsTpResponse = this.facadeTpDao.getCmsTpDao().getCmsBlock(
                    SubjectConstants.ALL_SUBJECT_CMS_BLOCK_ID);
            CmsBlockContent thisSubjectBlock = null;
            if (cmsTpResponse != null && !CollectionUtils.isEmpty(cmsTpResponse.getBlockContent())) {
                List<CmsBlockContent> allSubjectIds = cmsTpResponse.getBlockContent();
                for (CmsBlockContent cmsBlockContent : allSubjectIds) {
                    if (StringUtils.isNotEmpty(cmsBlockContent.getContent())
                            && cmsBlockContent.getContent().equalsIgnoreCase(subjectId)) {
                        thisSubjectBlock = cmsBlockContent;
                        break;
                    }
                }
            }
            if (thisSubjectBlock != null) {
                // 填充明星block
                Block sBlock = this.fillActorBlock(thisSubjectBlock);
                if (sBlock != null) {
                    blockList.add(sBlock);
                }

                // 填充音乐block
                Block mBlock = this.fillMusicBlock(thisSubjectBlock);
                if (mBlock != null) {
                    blockList.add(mBlock);
                }

                // 填充关注乐词block
                Block hwBlock = this.fillHotWordsBlock(thisSubjectBlock);
                if (hwBlock != null) {
                    blockList.add(hwBlock);
                }

                // 填充H5活动block
                Block h5Block = this.fillH5ActivitysBlock(thisSubjectBlock);
                if (h5Block != null) {
                    blockList.add(h5Block);
                }

                //填充壁纸
                Block wpBlock = this.fillWallpapersBlock(thisSubjectBlock);
                if(wpBlock != null){
                    blockList.add(wpBlock);
                }

            }
            // 按照特定的顺序排序,如有新增style，SubjectConstants.sortMap加入一条
            Collections.sort(blockList);

            subject.setBlock(blockList);
            subject.setName(subjectContent.getName());
            subject.setDesc(subjectContent.getDescription());
            subject.setPic(subjectContent.getPic169());
        }

        return subject;
    }

    public VideoDto parseSubject2Video(ContentItem item) {
        VideoDto videoDto = null;
        if (item != null) {
            videoDto = new VideoDto();

            videoDto.setVideoId(item.getId());
            videoDto.setName(item.getNameCn());
            videoDto.setImg(item.getPicAll().get("400*300"));
            videoDto.setType(Integer.valueOf(ChannelConstant.DATA_TYPE_VIDEO));
        }

        return videoDto;
    }

    public AlbumDto parseSubject2Album(ContentItem item) {
        AlbumDto albumDto = null;
        if (item != null) {
            albumDto = new AlbumDto();

            albumDto.setAlbumId(item.getId());
            albumDto.setName(item.getNameCn());
            albumDto.setImg(item.getPicCollections().get("400*300"));
            albumDto.setType(Integer.valueOf(ChannelConstant.DATA_TYPE_ALBUM));
        }

        return albumDto;
    }

    public LiveDto parseSubject2Live(ContentItem item) {
        LiveDto liveDto = null;
        if (item != null) {
            String liveId = item.getRid();
            SuperLiveChannelDto channelCache = this.tpCacheTemplate.get(CacheConstants.SUPERLIVE_LIVEROOM_LIVEID
                    + liveId, SuperLiveChannelDto.class);
            if (channelCache != null) {
                SuperLiveProgramDto liveProgram = channelCache.getCur();
                if (liveProgram != null && !"3".equals(liveProgram.getStatus())) {
                    liveDto = new LiveDto();
                    liveDto.setId(liveId);
                    liveDto.setLiveName(item.getRname());
                    liveDto.setLiveSubName(item.getSubTitle());
                    liveDto.setStartTime(TimeUtil.string2timestamp(liveProgram.getPlayTime()));
                    liveDto.setEndTime(TimeUtil.string2timestamp(liveProgram.getEndTime()));
                    liveDto.setState(Integer.valueOf(liveProgram.getStatus()));
                    liveDto.setType(ChannelConstant.DATA_TYPE_LIVE);
                    liveDto.setPlayerType(ChannelConstant.playerType.PLAYER_TYPE_LIVE);
                    if (StringUtils.isNotBlank(channelCache.getIsPay())) {
                        liveDto.setIsPay(Ints.tryParse(channelCache.getIsPay()));
                    }

                }
            }
        }

        return liveDto;
    }

    public MusicCache parseAudio2Music(Long id) {

        MusicCache musicCache = null;
        MmsAudioResponse audio = this.facadeTpDao.getMmsTpDao().getMmsAudioResponse(id);
        if (audio != null) {
            musicCache = new MusicCache();
            musicCache.setSongId(audio.getId().toString());
            musicCache.setSongName(audio.getNameCn());
            musicCache.setSingerName(null);
            musicCache.setDesc(audio.getDescription());
            musicCache.setType(Integer.valueOf(ChannelConstant.DATA_TYPE_MUSIC));
            String xiamiData = audio.getXiamiData();
            if (xiamiData != null) {
                try {
                    XiamiData d = OBJECT_MAPPER.readValue(xiamiData, new TypeReference<XiamiData>() {
                    });
                    if (d != null) {
                        musicCache.setSingerName(d.getSingers());
                    }
                } catch (Exception e) {
                    log.error("parseAudio2Music 获取歌手失败");
                }
            }
        }

        return musicCache;
    }

    public HotWordsCache parseMmsHotWords2HotWordsCache(MmsHotWords hotWords) {
        HotWordsCache hotWordsCache = null;
        if (hotWords != null) {
            hotWordsCache = new HotWordsCache();

            hotWordsCache.setId(hotWords.getId());
            hotWordsCache.setName(hotWords.getName());
            hotWordsCache.setCategoryType(hotWords.getType());
            if (!CollectionUtils.isEmpty(hotWords.getData())) {
                // 比例1:1，展示尺寸40 x 40dp，取图尺寸：300x300
                hotWordsCache.setImg(getRealImgUrl(hotWords.getData().get(0).getHeadPicWeb(), "11", 300, 300));
            }

            // 调用乐视云查询此剧集乐词关注度
            HotWordsCountResponse response = this.facadeTpDao.getAttTpDao().getUserHotWordsCount(
                    hotWords.getId().toString());
            if (response != null && "10000".equals(response.getErrno()) && response.getData() != null) {
                for (String key : response.getData().keySet()) {
                    if (hotWordsCache.getId() == NumberUtils.toInt(key)) {
                        UserHotWordCount userHotWordCount = response.getData().get(key);
                        hotWordsCache.setAttention(userHotWordCount.getCount().toString());
                    }
                }
            }
            hotWordsCache.setType(Integer.valueOf(ChannelConstant.DATA_TYPE_HOTWORDS));
        }

        return hotWordsCache;
    }

    private String getRealImgUrl(String imgUrl, String scale, Integer width, Integer height) {
        if (StringUtils.isNotBlank(imgUrl)) {
            if (StringUtils.isNotBlank(scale)) {
                imgUrl = imgUrl + "/" + scale;
            }
            if (width != null && height != null) {
                imgUrl = imgUrl + "_" + width + "_" + height;
            }
            imgUrl = imgUrl + ".jpg";
        }
        return imgUrl;
    }

    private List<Block> fillPackageBlock(SubjectContent subjectContent) {
        List<ContentPackage> tjPackages = subjectContent.getTjPackages();
        List<Block> pBlockList = null;
        if (!tjPackages.isEmpty()) {
            pBlockList = new ArrayList<>();
            // 视频、专辑和直播包
            for (ContentPackage c : tjPackages) {
                Block block = new Block();
                block.setName(c.getName());
                block.setStyle("30");
                List<BaseDto> dataList = new ArrayList<BaseDto>();
                List<ContentItem> itemList = c.getDataList();
                if (!itemList.isEmpty()) {
                    switch (c.getPtype()) {
                    case SubjectConstants.SUBJECT_PACKAGE_TYPE_VIDEO:
                        for (ContentItem item : itemList) {
                            VideoDto v = parseSubject2Video(item);
                            if (v != null) {
                                dataList.add(v);
                            }
                        }

                        if (!CollectionUtils.isEmpty(dataList)) {
                            if ((dataList.size() & 1) != 0) {// 奇数
                                dataList = dataList.subList(0, dataList.size() - 1);
                            }
                        }
                        break;
                    case SubjectConstants.SUBJECT_PACKAGE_TYPE_ALBUM:
                        for (ContentItem item : itemList) {
                            AlbumDto a = parseSubject2Album(item);
                            if (a != null) {
                                dataList.add(a);
                            }
                        }

                        if (!CollectionUtils.isEmpty(dataList)) {
                            if ((dataList.size() & 1) != 0) {// 奇数
                                dataList = dataList.subList(0, dataList.size() - 1);
                            }
                        }
                        break;
                    case SubjectConstants.SUBJECT_PACKAGE_TYPE_LIVE:
                        block.setStyle("52");
                        for (ContentItem item : itemList) {
                            LiveDto l = parseSubject2Live(item);
                            if (l != null) {
                                dataList.add(l);
                            }
                        }
                        break;
                    default:
                        break;
                    }
                }
                block.setList(dataList);
                pBlockList.add(block);
            }
        }
        return pBlockList;
    }

    private Block fillActorBlock(CmsBlockContent thisSubjectBlock) {
        Block block = null;
        // 标题存储取明星id，逗号隔开(名称|id1,id2,id3)
        String[] actorBlockNameIds = thisSubjectBlock.getTitle().split("\\|");
        if (ArrayUtils.isNotEmpty(actorBlockNameIds) && actorBlockNameIds.length > 1) {
            List<BaseDto> dataList = this.getBaseDataForAttention(actorBlockNameIds[1],
                    ChannelConstant.AttentionType.ATTENTION_TYPE_ACTOR);
            if (dataList != null && dataList.size() > 0) {
                block = new Block();
                block.setList(dataList);
                block.setName(actorBlockNameIds[0]);
                block.setStyle("48");
            }
        }
        return block;
    }

    private Block fillMusicBlock(CmsBlockContent thisSubjectBlock) {
        Block block = null;
        // 副标题存储取音乐id，逗号隔开(名称|id1,id2,id3)
        String[] musicBlockNameIds = thisSubjectBlock.getSubTitle().split("\\|");
        if (ArrayUtils.isNotEmpty(musicBlockNameIds) && musicBlockNameIds.length > 1) {
            block = new Block();
            List<BaseDto> dataList = new ArrayList<BaseDto>();
            String[] audioIds = musicBlockNameIds[1].split(",");
            for (String id : audioIds) {
                if(id.startsWith("a")){
                    id = id.substring(1,id.length());
                    OstCache cache = this.getMusicAlbumInfo(Long.parseLong(id));
                    if(cache != null){
                        dataList.add(cache);
                    }
                } else {
                    MusicCache cache = this.parseAudio2Music(Long.parseLong(id));
                    if(cache != null){
                        dataList.add(cache);
                    }
                }
            }
            block.setList(dataList);
            block.setName(musicBlockNameIds[0]);
            block.setStyle("50");

        }
        return block;
    }

    private Block fillHotWordsBlock(CmsBlockContent thisSubjectBlock) {
        Block block = null;
        // 简介存储取关注乐词id，逗号隔开(名称|id1,id2,id3)
        String[] hotWordsAlbumIds = thisSubjectBlock.getShorDesc().split("\\|");
        if (ArrayUtils.isNotEmpty(hotWordsAlbumIds) && hotWordsAlbumIds.length > 1) {
            List<BaseDto> dataList = this.getBaseDataForAttention(hotWordsAlbumIds[1],
                    ChannelConstant.AttentionType.ATTENTION_TYPE_HOTWORDS);
            if (dataList != null && dataList.size() > 0) {
                block = new Block();
                block.setList(dataList);
                block.setName(hotWordsAlbumIds[0]);
                block.setStyle("49");
            }
        }
        return block;
    }

    private Block fillH5ActivitysBlock(CmsBlockContent thisSubjectBlock) {
        Block block = null;
        // PIC1存储取H5活动，逗号隔开(名称|IMGURL)
        String[] h5Activitys = thisSubjectBlock.getPic1().split("\\|");
        if (ArrayUtils.isNotEmpty(h5Activitys) && h5Activitys.length > 1) {
            block = new Block();
            List<BaseDto> dataList = new ArrayList<BaseDto>();

            H5Activity h5Activity = new H5Activity();
            h5Activity.setImg(h5Activitys[1]);
            h5Activity.setUrl(thisSubjectBlock.getUrl());// URL存储活动的url
            h5Activity.setType(Integer.valueOf(ChannelConstant.DATA_TYPE_H5ACTIVITY));

            dataList.add(h5Activity);

            block.setList(dataList);
            block.setName(h5Activitys[0]);
            block.setStyle("54");
        }
        return block;
    }

    private Block fillWallpapersBlock(CmsBlockContent thisSubjectBlock) {
        Block block = null;
        String wp = thisSubjectBlock.getRemark();
        if(StringUtils.isNotBlank(wp)) {
            String[] wallPapers = wp.split("\\|");
            if (ArrayUtils.isNotEmpty(wallPapers) && wallPapers.length > 1) {
                block = new Block();
                String[] ids = wallPapers[1].split(",");
                List<String> wpIds = Arrays.asList(ids);
                List<BaseDto> dataList = this.getBaseDataForWallPaper(wpIds);
                block.setList(dataList);
                block.setName(wallPapers[0]);
                block.setStyle("55");
            }
        }
        return block;
    }
    /***
     * 获取明星和乐词
     * @param attentions
     * @param type
     * @return
     */
    public List<BaseDto> getBaseDataForAttention(String attentions, String type) {
        List<BaseDto> dataList = new ArrayList<BaseDto>();
        // 老接口
        // if(StringUtils.isNotBlank(attentions)){
        // if(ChannelConstant.AttentionType.ATTENTION_TYPE_HOTWORDS.equals(type)){
        // MmsHotWordsResponse response =
        // this.facadeTpDao.getMmsTpDao().getHotWords(attentions);
        // if (response != null && !CollectionUtils.isEmpty(response.getData()))
        // {
        // List<MmsHotWords> hotWords = response.getData();
        // for (MmsHotWords hotWord : hotWords) {
        // dataList.add(parseMmsHotWords2HotWordsCache(hotWord));
        // }
        // }
        // } else
        // if(ChannelConstant.AttentionType.ATTENTION_TYPE_ACTOR.equals(type)){
        // MmsStarInfoResponse starInfo =
        // this.facadeTpDao.getMmsTpDao().getStarList(attentions);
        // if (starInfo != null && !CollectionUtils.isEmpty(starInfo.getData()))
        // {
        // ActorCache actorCache = null;
        // for (MmsStarInfos star : starInfo.getData()) {
        // actorCache = new ActorCache();
        // actorCache.setId(star.getId());
        // actorCache.setName(star.getName());
        // if (!CollectionUtils.isEmpty(star.getData())) {
        // // 比例1:1，展示尺寸40 x 40dp，取图尺寸：300x300
        // actorCache.setImg(getRealImgUrl(star.getData().get(0).getPostS1(),
        // "11", 300, 300));
        // }
        // actorCache.setType(Integer.valueOf(ChannelConstant.DATA_TYPE_STAR));
        // dataList.add(actorCache);
        // }
        // }
        //
        // }
        //
        // }

        // 新接口
        if (StringUtils.isNotBlank(attentions)) {
            TagResponse response = this.facadeTpDao.getLinesTpDao().getTagList(attentions);
            if (response != null && response.getData() != null && !CollectionUtils.isEmpty(response.getData())) {
                dataList = new ArrayList<>();
                List<TagInfo> tagInfos = response.getData();
                for (TagInfo tag : tagInfos) {

                    if (ChannelConstant.AttentionType.ATTENTION_TYPE_HOTWORDS.equals(type)) {
                        // 乐词
                        HotWordsCache cache = new HotWordsCache();
                        cache.setId(tag.getTag_id().intValue());
                        cache.setImg(tag.getIcon());
                        HotWordsCountResponse res = this.facadeTpDao.getAttTpDao().getUserHotWordsCount(
                                tag.getTag_id().toString());
                        if (res != null && "10000".equals(res.getErrno()) && res.getData() != null) {
                            for (String key : res.getData().keySet()) {
                                if (cache.getId() == NumberUtils.toInt(key)) {
                                    UserHotWordCount userHotWordCount = res.getData().get(key);
                                    cache.setAttention(userHotWordCount.getCount().toString());
                                }
                            }
                        }
                        cache.setName(tag.getName());
                        // cache.setCategoryType();
                        cache.setType(Integer.valueOf(ChannelConstant.DATA_TYPE_HOTWORDS));
                        dataList.add(cache);
                    } else if (ChannelConstant.AttentionType.ATTENTION_TYPE_ACTOR.equals(type)) {
                        // 明星
                        ActorCache cache = new ActorCache();
                        cache.setId(tag.getTag_id().intValue());
                        cache.setImg(tag.getIcon());
                        cache.setName(tag.getName());
                        cache.setType(Integer.valueOf(ChannelConstant.DATA_TYPE_STAR));
                        dataList.add(cache);
                    }
                }
            }
        }
        return dataList;
    }

    public List<BaseDto> getBaseDataForWallPaper(List<String> ids){
        List<BaseDto> dataList = null;
        WallPageBatchResponse response = this.facadeTpDao.getWallPaperTpDao().mgetWallPaperByIds(ids);
        if(response != null && response.getData() != null){
            dataList = new ArrayList<>();
            for(WallPaperDigestInfo info:response.getData()){
                WallPaperDto cache = new WallPaperDto();
                cache.setId(info.getId());
                cache.setImg(info.getThumbnail());
                cache.setType(Integer.valueOf(ChannelConstant.DATA_TYPE_WALLPAPER));
                dataList.add(cache);
            }
        }
        return dataList;
    }


    public OstCache getMusicAlbumInfo(Long id){
        OstCache cache = null;
        MmsResponse<MmsOstInfo> response = this.facadeTpDao.getMmsTpDao().getMmsOstInfoById(id);
        if (response != null && response.getData() != null) {
            MmsOstInfo ost = response.getData();
            cache = new OstCache();
            if (ost.getPicCollections() != null) {
                cache.setImg(ost.getPicCollections().get("300*300"));
            }
            cache.setId(Long.valueOf(ost.getXiamiId()));
            cache.setName(ost.getNameCn());
            Map<Integer, String> artistName = ost.getArtistName();
            String aName = "";
            if(artistName != null){
                for(Map.Entry<Integer, String> entry:artistName.entrySet()){
                    if(StringUtils.isNotBlank(entry.getValue())){
                        aName = entry.getValue();
                        break;
                    }
                }
            }
            cache.setArtist(aName);
            cache.setType(ChannelConstant.DATA_TYPE_MUSIC_ALBUM);
        }
        return cache;
    }
}