package xserver.api.module.channel.skippolicy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import xserver.api.module.channel.ChannelConstant;
import xserver.api.module.channel.constants.SkipTypeConstant;
import xserver.api.module.channel.dto.AssistData;
import xserver.api.module.channel.dto.ChannelBlock;
import xserver.api.module.channel.dto.ChannelFocus;
import xserver.lib.constant.VideoConstants;
import xserver.lib.tp.rec.response.RecommendTpResponse;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.tpcache.cache.PlayCache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuli3 on 2015/7/20.
 * 跳转到视频
 */
@Service
public class SkipVideoPolicy extends SkipPolicyAdapter {

    @Override
    public ChannelBlock parseSkipPolicy(Object recData, Integer type,Map<String,Object> otherParams) {
        // 跳转到视频，包括cms和rec的视频
        ChannelBlock block = null;
        ChannelFocus focus = null;
        if (SkipTypeConstant.CMS_SKIP_VIDEO.equals(type) || SkipTypeConstant.REC_SKIP_VIDEO.equals(type)) {
            RecommendTpResponse.RecData rec = (RecommendTpResponse.RecData) recData;
//            Long videoId = rec.getVid();
//            if (SkipTypeConstant.CMS_SKIP_VIDEO.equals(type)) {// cms视频
//                if (rec.getContent() != null && rec.getContent().matches("\\d+")) {
//                    videoId = Long.parseLong(rec.getContent());
//                }
//            }
//            PlayCache video = videoService.getPlayCacheEntityByVideoId(videoId);

            String videoId = rec.getVid() != null?String.valueOf(rec.getVid()):rec.getContent();
            PlayCache video = null;
            if(otherParams != null){
                Map<String, PlayCache> caches = (Map<String, PlayCache>)otherParams.get("caches");
                if(caches != null){
                    video = caches.get(CacheConstants.PlayCacheEntity_V_ + videoId);
                } else {
//                    video = videoService.getPlayCacheEntityByVideoId(Long.parseLong(videoId));
                }
            }
            if (video != null) {
                focus = new ChannelFocus();
                focus.setAt(ChannelFocus.HOME_ACTION_TYPE_FULL);
                focus.setType(ChannelConstant.DATA_TYPE_VIDEO);
                focus.setVid(video.getvId());
                focus.setPid(video.getaId());
                focus.setCid(video.getvCategoryId());
                focus.setVideoType(String.valueOf(video.getvType()));

                focus.setTag(video.getvTag());
                focus.setSubCategory(video.getvSubCategoryName());
                focus.setDirector(video.getvDirectory());
                focus.setArea(video.getvAreaName());
                focus.setDuration(String.valueOf(video.getvDuration()));
                focus.setReleaseDate(video.getvReleaseDate());

                String name = rec.getTitle();
                String subTitle = rec.getSubTitle();
                if (SkipTypeConstant.REC_SKIP_VIDEO.equals(type)) {
                    if (rec.getCid() != null && VideoConstants.Category.VARIETY == rec.getCid().intValue()) {
                        // 推荐的综艺视频
                        subTitle = rec.getVidsubtitle();
                        name = rec.getPidname();
                    }
                }
                focus.setName(name);
                focus.setSubTitle(subTitle);
                //填充图片
                this.fillVideoPic(focus,video,rec,type);

                focus.setPay(rec.getIs_pay());

                // 音乐频道取歌手，其他频道取主演
                if (video.getvCategoryId() != null && video.getvCategoryId() == 9) {
                    focus.setSinger(video.getvSinger());
                } else {
                    focus.setSinger(video.getvStarring());
                }
                Date date = video.getvUpdateTime();
                if (date != null) {
                    focus.setUpdateTime(date.getTime());
                }

                String pageId = (String)otherParams.get("pageid");
                if (video.getaId() != null) {
                    focus.setCornerLabel(channelHelper.fillCornerLabelByCache(video, ChannelConstant.DATA_TYPE_VIDEO, pageId));
                }

                // focus.setShowTagList(this.parseShowTagList(rec));
                this.afterParseSkipPolicy(rec, focus,new AssistData(pageId));
            }
        }
        if(focus != null){
            block = new ChannelBlock();
            List<ChannelFocus> focusList = new ArrayList<>();
            focusList.add(focus);
            block.setList(focusList);
        }

        return block;
    }

    /*************************************************private******************************************/
    /***
     * 填充图片
     * @param focus
     * @param video
     * @param rec
     * @param type
     */
    private void fillVideoPic(ChannelFocus focus,PlayCache video,RecommendTpResponse.RecData rec,Integer type){
        String pic = null;
        Map<String, String> picAll = new HashMap<>();
        if (SkipTypeConstant.REC_SKIP_VIDEO.equals(type)) {
            pic = rec.getDefaultPic();
            picAll.putAll(channelHelper.getChannelImage(rec));
        } else {
            pic = StringUtils.isNotBlank(rec.getMobilePic()) ? rec.getMobilePic() : video.getVPic(320, 200);
            picAll.putAll(channelHelper.getChannelImage(video, pic, ChannelConstant.DATA_TYPE_VIDEO, video.getvType()));
        }
        focus.setPic(pic);
        focus.setPicAll(picAll);
    }
}
