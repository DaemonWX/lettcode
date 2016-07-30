package xserver.api.module.channel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xserver.api.constant.DataConstants;
import xserver.api.module.BaseService;
import xserver.api.module.channel.ChannelConstant;
import xserver.api.module.channel.constants.SkipTypeConstant;
import xserver.api.module.channel.dto.AssistData;
import xserver.api.module.channel.dto.ChannelBlock;
import xserver.api.module.channel.dto.ChannelFocus;
import xserver.api.module.video.constants.VideoErrorCodeConstants.play;
import xserver.lib.constant.LiveConstants.state;
import xserver.common.dto.BaseDto;
import xserver.lib.tp.cloud.response.HotWordsCountResponse;
import xserver.lib.tp.cloud.response.UserHotWordCount;
import xserver.lib.tp.lines.response.TagInfo;
import xserver.lib.tp.lines.response.TagResponse;
import xserver.lib.tp.rec.response.RecommendTpResponse.RecData;
import xserver.lib.tp.video.response.MmsHotWords;
import xserver.lib.tp.video.response.MmsHotWordsResponse;
import xserver.lib.tp.video.response.MmsStarInfoResponse;
import xserver.lib.tp.video.response.MmsStarInfos;
import xserver.lib.tpcache.cache.ActorCache;
import xserver.lib.tpcache.cache.HotWordsCache;
import xserver.lib.tpcache.cache.PlayCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: xuli3
 * Date：2015/7/31
 * Time: 9:39
 */
@Service
public class ChannelHelper extends BaseService{

    /***
     * 首页和频道页数据跳转类型
     * @param rec
     * @return
     */
    public Integer getSkipType(RecData rec,String contentStyle) {
        Integer type = null;
        if(StringUtils.isNotBlank(contentStyle)){
            switch (contentStyle){
                case ChannelConstant.contentStyle.CONTENT_STYLE_LIVE_LIST1:
                    //直播列表样式1
                    type = SkipTypeConstant.CMS_SKIP_MANUAL_lIVE1;
                    break;
                case ChannelConstant.contentStyle.CONTENT_STYLE_LIVE_LIST2:
                    //直播列表样式2
                    type = SkipTypeConstant.CMS_SKIP_MANUAL_lIVE2;
                    break;
                case ChannelConstant.contentStyle.CONTENT_STYLE_LIVE_PIC:
                    //直播图文样式
                    type = SkipTypeConstant.CMS_SKIP_MANUAL_lIVE3;
                    break;
                case ChannelConstant.contentStyle.CONTENT_STYLE_ATTENTION:
                case ChannelConstant.contentStyle.CONTENT_STYLE_ATTENTION_SLIP:
                    //关注
                    type = SkipTypeConstant.CMS_SKIP_ATTENTION;
                    break;
            }

        } else if(rec != null){
            if ("true".equals(rec.getIs_rec())) {
                // 推荐数据
                if (rec.getVid() != null) {// 视频
                    type = SkipTypeConstant.REC_SKIP_VIDEO;
                } else if (rec.getZid() != null) {// 专题
                    type = SkipTypeConstant.REC_SKIP_SUBJECT;
                } else if (rec.getPid() != null) { // 专辑
                    type = SkipTypeConstant.REC_SKIP_ALBUM;
                }

            } else if (StringUtils.isBlank(rec.getIs_rec()) && rec.getPushflag() != null
                    && (rec.getPushflag().contains(DataConstants.ANDROID_COPYRIGHT) || rec.getPushflag().contains
                    (DataConstants.SUPER_ANDROID_COPYRIGHT))) {
                // 数据来自cms
                if (StringUtils.isNotBlank(rec.getPosition())) {
                    /**
                     * 领先版暂时不支持 cms中position字段不为空时，走特殊跳转逻辑 position
                     * 1:会员频道;2:收银台界面;3:联通流量包套餐订购页;4:我的积分;9:登录界面;
                     */

                } else if (StringUtils.isNotBlank(rec.getSkipPage())) {
                    // 跳转到频道
                    type = SkipTypeConstant.CMS_SKIP_CHANNEL;
                } else if (StringUtils.isNotBlank(rec.getAndroidUrl())) {
                    // 直播流
                    type = SkipTypeConstant.CMS_SKIP_ANDROID_LIVE;
                } else if (StringUtils.isNotBlank(rec.getSkipType()) && StringUtils.isNotBlank(rec.getSkipUrl())) {
                    // URL跳转
                    type = SkipTypeConstant.CMS_SKIP_URL;
                } else if (StringUtils.isNotBlank(rec.getType())) {
                    switch (rec.getType()){
                        case ChannelConstant.CMS_TYPE_VIDEO:// 视频
                            type = SkipTypeConstant.CMS_SKIP_VIDEO;
                            break;
                        case ChannelConstant.CMS_TYPE_ALBUM:// 专辑
                            type = SkipTypeConstant.CMS_SKIP_ALBUM;
                            break;
                        case ChannelConstant.CMS_TYPE_LIVE:// 直播
                            type = SkipTypeConstant.CMS_SKIP_LIVE;
                            break;
                        case ChannelConstant.CMS_TYPE_SUBJECT:// 专题
                            type = SkipTypeConstant.CMS_SKIP_SUBJECT;
                            break;
                        case ChannelConstant.CMS_TYPE_ALL_WEB:// 全网
                            type = SkipTypeConstant.CMS_SKIP_ALL_WEB;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return type;
    }

    public List<ChannelFocus.ShowTagList> parseShowTagList(RecData rec) {
//        if (rec == null || rec.getShowTagList().size() == 0) {
//            return null;
//        }
//        List<ChannelFocus.ShowTagList> tagList = new ArrayList<ChannelFocus.ShowTagList>();
//        ChannelFocus.ShowTagList tag = null;
//        for (CmsBlockContent.ShowTagList cmsTag : rec.getShowTagList()) {
//            /**
//             * 此段逻辑无从追溯，暂时保留移动端老逻辑
//             */
//            if ("3".equals(cmsTag.getTypeId())) {
//                tag = new ChannelFocus.ShowTagList();
//                tag.setId(cmsTag.getId());
//                tag.setValue(cmsTag.getValue());
//                tag.setKey("sc");
//                tagList.add(tag);
//            } else if ("5".equals(cmsTag.getTypeId())) {
//                tag = new ChannelFocus.ShowTagList();
//                tag.setId(cmsTag.getId());
//                tag.setValue(cmsTag.getValue());
//                tag.setKey("area");
//                tagList.add(tag);
//            }
//        }
//        return tagList;
        return null;
    }

    public Map<String, String> getCmsMutiSizeImage(Map<String, String> picList, String mobilePic) {
        Map<String, String> picMap = null;
        picMap = new HashMap<String, String>();
        if(picList != null && picList.size() > 0){
            picMap.put("pic_16_10", StringUtils.isNotEmpty(picList.get("400x250")) ? picList.get("400x250") : mobilePic);
            if (StringUtils.isNotEmpty(picList.get("1440x810"))) {
                picMap.put("pic_16_9", StringUtils.isNotEmpty(picList.get("1440x810")) ? picList.get("1440x810")
                        : mobilePic);
            } else {
                picMap.put("pic_16_9", StringUtils.isNotEmpty(picList.get("960x540")) ? picList.get("960x540") : mobilePic);
            }
            picMap.put("pic_4_3", StringUtils.isNotEmpty(picList.get("400x300")) ? picList.get("400x300") : mobilePic);
        } else {
            picMap.put("pic_16_10",mobilePic);
            picMap.put("pic_16_9",mobilePic);
            picMap.put("pic_4_3",mobilePic);
        }
        return picMap;
    }

    public Map<String, String> getChannelImage(PlayCache play, String defaultPic, String dataType, Integer videoType) {
        Map<String, String> picMap = null;
        if (play != null) {
            picMap = new HashMap<String, String>();
            String pic_16_10 = null;
            String pic_16_9 = null;
            String pic_4_3 = null;
            if (videoType != null && videoType != 180001) {
                pic_16_10 = StringUtils.isNotBlank(defaultPic) ? defaultPic : play.getAPic(400, 250);
                pic_16_9 = StringUtils.isNotBlank(defaultPic) ? defaultPic : play.getAPic(960, 540);
                pic_4_3 = StringUtils.isNotBlank(defaultPic) ? defaultPic : play.getAPic(400, 300);
            } else {
                if (ChannelConstant.DATA_TYPE_ALBUM.endsWith(dataType)) {
                    pic_16_10 = StringUtils.isNotBlank(play.getAPic(400, 250)) ? play.getAPic(400, 250) : defaultPic;
                    pic_16_9 = StringUtils.isNotBlank(play.getAPic(960, 540)) ? play.getAPic(960, 540) : defaultPic;
                    pic_4_3 = StringUtils.isNotBlank(play.getAPic(400, 300)) ? play.getAPic(400, 300) : defaultPic;
                } else if (ChannelConstant.DATA_TYPE_VIDEO.equals(dataType)) {
                    pic_16_10 = StringUtils.isNotBlank(play.getVPic(400, 250)) ? play.getVPic(400, 250) : defaultPic;
                    pic_16_9 = StringUtils.isNotBlank(play.getVPic(960, 540)) ? play.getVPic(960, 540) : defaultPic;
                    pic_4_3 = StringUtils.isNotBlank(play.getVPic(400, 300)) ? play.getVPic(400, 300) : defaultPic;
                }
            }
            picMap.put("pic_16_10",pic_16_10);
            picMap.put("pic_16_9",pic_16_9);
            picMap.put("pic_4_3",pic_4_3);
        }
        return picMap;
    }

    /**
     * 获取频道图片--数据来源为推荐
     * 当前支持 16:9 16:10 4:3比例的图
     * @return
     */
    public Map<String, String> getChannelImage(RecData rec) {
        Map<String, String> picMap = null;
        if (rec != null) {
            picMap = new HashMap<String, String>();
            picMap.put("pic_16_10", rec.getImageByRatio(16, 10));
            picMap.put("pic_16_9", rec.getImageByRatio(16, 9));
            picMap.put("pic_4_3", rec.getImageByRatio(4, 3));
        }
        return picMap;
    }

    /***
     * 通过缓存的数据得到角标
     * @param cache
     *            缓存中的数据
     * @param dType
     *            推荐给的数据类型，专辑还是专辑中的视频
     * @return
     */
    public String fillCornerLabelByCache(PlayCache cache, String dType,String pageId) {
        if (cache == null) {
            return null;
        }
        
        String isPay = null;
        String dataType = null;
        String isPlayMark = null;
        String isHomemade = null;
        String aType = null;
        String playStream = null;

        if (cache.getaId() != null) {
            dataType = ChannelConstant.DATA_TYPE_ALBUM;
        }
        isPay = cache.getaIsyuanxian() == null ? null : String.valueOf(cache.getaIsyuanxian());
        isPlayMark = cache.getaIsPlayMark() == null ? null : String.valueOf(cache.getaIsPlayMark());
        isHomemade = cache.getaIsHomemade() == null ? null : String.valueOf(cache.getaIsHomemade());
        if (ChannelConstant.DATA_TYPE_ALBUM.equals(dType)) {
            // 专辑
            aType = cache.getaType() == null ? null : String.valueOf(cache.getaType());
        }
        if (ChannelConstant.DATA_TYPE_VIDEO.equals(dType)) {
            // 视频
            if (cache.getvType() != null) {
                aType = String.valueOf(cache.getvType());
                // 如果专辑是付费的，但是视频不是正片则不属于付费
                if (isPay != null && "1".equals(isPay) && !"180001".equals(aType)) {
                    isPay = null;
                }
            }
        }

        // 视频的播放码流
        // "180,350,800,1300,720p,1080p,4k,800_db,1300_db,720p_db,1080p_db,4k_db,800_dts,1300_dts,720p_dts,1080p_dts,4k_dts"
        playStream = cache.getvPlayStreams();
        
        return this.getCornerLabel(new AssistData(isPay, dataType, isPlayMark, isHomemade, aType, pageId, playStream));
    }

    /**
     * 视频角标类型
     * @return
     */
    public String getCornerLabel(AssistData aData) {
        // 角标优先级：全网（仅针对乐搜）>付费(仅领先版付费直播模块显示) > 会员 > 独播> 自制> 专题  > 预告
    	// 1.7.1  付费>会员>4k>2k>1080p>影院音>独播>自制>专题>预告>花絮
        String cType = null;
        String isPay = aData.getIsPay() == null ? null : String.valueOf(aData.getIsPay());
        String pageId = aData.getPageid();
        if (StringUtils.isNotBlank(isPay) && "1".equals(isPay)) {
            // 会员
            cType = ChannelConstant.CORNERLABELTYPE.IS_VIP;
            if(StringUtils.isNotBlank(pageId) && ChannelConstant.VIP_CHANNEL_PAGEID.contains(pageId)){
                //会员页面不加会员角标
                cType = null;
            }
        }
        String dataType = aData.getDataType();
        if (StringUtils.isBlank(cType) && StringUtils.isBlank(dataType)) {
            return null;
        }
        if (StringUtils.isBlank(cType) && ChannelConstant.DATA_TYPE_VIDEO.equals(dataType)) {
            // 单视频不展示角标
            return null;
        }

        // 4K 2K 1080P 影院音
        String playStream = aData.getPlayStream();
        if(StringUtils.isBlank(cType) && StringUtils.isNotBlank(playStream)) {
        	if(playStream.contains("db") || playStream.contains("dts")) {
                cType = ChannelConstant.CORNERLABELTYPE.IS_DTS;
        	}
        	
        	if(playStream.contains("1080p")) {
        		cType = ChannelConstant.CORNERLABELTYPE.IS_1080P;
        	} 
        		
			if(playStream.contains("2k")) {
				cType = ChannelConstant.CORNERLABELTYPE.IS_2K;
			} 
			
			if(playStream.contains("4k")) {
				cType = ChannelConstant.CORNERLABELTYPE.IS_4K;
			} 
        }
        
        String isPlayMark = aData.getIsPlayMark();
        String isHomemade = aData.getIsHomemade();
        String aType = aData.getaType(); 
        if (StringUtils.isBlank(cType) && StringUtils.isNotBlank(isPlayMark) && "1".equals(isPlayMark)) {
            // 独播
            cType = ChannelConstant.CORNERLABELTYPE.IS_EXCLUSIVE;
        }
        if (StringUtils.isBlank(cType) && StringUtils.isNotBlank(isHomemade) && "1".equals(isHomemade)) {
            // 自制
            cType = ChannelConstant.CORNERLABELTYPE.IS_HOMEMADE;
            if(StringUtils.isNotBlank(pageId) && ChannelConstant.HOMEMADE_CHANNEL_PAGEID.contains(pageId)){
                //自制页面不加自制角标
                cType = null;
            }
        }
        if (StringUtils.isBlank(cType) && ChannelConstant.DATA_TYPE_SUBJECT.equals(dataType)) {
            // 专题
            cType = ChannelConstant.CORNERLABELTYPE.IS_SPECIAL;
            if(StringUtils.isNotBlank(pageId) && ChannelConstant.SUBJECT_PAGEID.contains(pageId)){
                //专题页面不加专题角标
                cType = null;
            }
        }
        if (StringUtils.isBlank(cType) && StringUtils.isNotBlank(aType) && "180002".equals(aType)) {
            // 预告
            cType = ChannelConstant.CORNERLABELTYPE.IS_PREVIEW;
        }
        
        // 花絮 aType判断
        if (StringUtils.isBlank(cType) && StringUtils.isNotBlank(aType) && "180003".equals(aType)) {
            // 花絮
            cType = ChannelConstant.CORNERLABELTYPE.IS_HUAXU;
        }
        
        return cType;
    }

    // 补充填充非视频和专辑的角标
    public void fillCornerLabelForAll(ChannelFocus focus,AssistData assistData){
        if (assistData != null && StringUtils.isBlank(focus.getCornerLabel())) {
            focus.setCornerLabel(this.getCornerLabel(new AssistData(focus.getPay(),
                    focus.getZid() != null ? ChannelConstant.DATA_TYPE_SUBJECT : null,assistData.getPageid())));
        }
    }


    public ChannelBlock getChannelLiveCache(String cacheKey){
        if(StringUtils.isBlank(cacheKey)){
            return null;
        }
        ChannelBlock blockCache = this.tpCacheTemplate.get(cacheKey,ChannelBlock.class);
        return blockCache;
    }

    /**
     * 获取关注
     * @param attentions
     * @param type
     * @return
     */
    public List<BaseDto> getBaseDataForAttention(String attentions,String type){
        return this.facadeService.getSubjectService().getBaseDataForAttention(attentions,type);
    }

}
