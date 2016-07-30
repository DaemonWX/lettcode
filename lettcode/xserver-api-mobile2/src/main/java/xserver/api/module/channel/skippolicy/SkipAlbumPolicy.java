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
 * User: xuli3
 * Date：2015/7/23
 * Time: 15:47
 * 跳转到专辑
 */
@Service
public class SkipAlbumPolicy extends SkipPolicyAdapter {
    @Override
    public ChannelBlock parseSkipPolicy(Object recData, Integer type, Map<String, Object> otherParams) {
        ChannelBlock block = null;
        ChannelFocus focus = null;
        if (SkipTypeConstant.CMS_SKIP_ALBUM.equals(type) || SkipTypeConstant.REC_SKIP_ALBUM.equals(type)) {
            RecommendTpResponse.RecData rec = (RecommendTpResponse.RecData) recData;
//            Long albumId = rec.getPid();
//            if (SkipTypeConstant.CMS_SKIP_ALBUM.equals(type)) {
//                if (rec.getContent() != null && rec.getContent().matches("\\d+")) {
//                    albumId = Long.parseLong(rec.getContent());
//                }
//            }
//            PlayCache album = videoService.getPlayCacheEntityByAlbumId(albumId);
            String albumId = rec.getPid() != null?String.valueOf(rec.getPid()):rec.getContent();
            PlayCache album = null;
            Map<String, PlayCache> caches = (Map<String, PlayCache>)otherParams.get("caches");
            if(caches != null){
                album = caches.get(CacheConstants.PlayCacheEntity_A_ + albumId);
            } else {
//                album = videoService.getPlayCacheEntityByAlbumId(Long.parseLong(albumId));
            }
            if (album != null) {
                focus = new ChannelFocus();
                // 常态属性
                focus.setAt(ChannelFocus.HOME_ACTION_TYPE_FULL);
                focus.setCmsid(rec.getId());
                focus.setPid(album.getaId());
                focus.setCid(album.getaCategoryId());
                focus.setType(ChannelConstant.DATA_TYPE_ALBUM);
                focus.setAlbumType(String.valueOf(album.getaType()));
                Date date = album.getaUpdateTime();
                if (date != null) {
                    focus.setUpdateTime(date.getTime());
                }

                // 经常调整属性
                String name = rec.getTitle();
                String subTitle = rec.getSubTitle();
                if (SkipTypeConstant.REC_SKIP_ALBUM.equals(type)) {
                    subTitle = rec.getPidsubtitle();
                    name = rec.getPidname();
                }
                focus.setName(name);
                focus.setSubTitle(subTitle);
                //填充图片
                this.fillAlbumPic(focus,album,rec,type);

                // 非经常变动属性
                focus.setScore(album.getaScore());
                focus.setSinger(album.getaStarring());
                focus.setDirector(album.getaDirectory());
                focus.setSubCategory(album.getaSubCategoryName());
                focus.setReleaseDate(album.getaReleaseDate());
                focus.setArea(album.getaAreaName());
                focus.setTag(album.getaTag());

                // 剧集更新相关
                focus.setIsEnd(String.valueOf(album.getaIsEnd()));
                focus.setEpisode(String.valueOf(album.getaEpisode()));
                focus.setNowEpisodes(album.getaNowEpisodes());

                if (album.getaIsyuanxian() != null) {
                    focus.setPay(String.valueOf(album.getaIsyuanxian()));
                }

                // 角标
                String pageId = (String)otherParams.get("pageid");
                focus.setCornerLabel(channelHelper.fillCornerLabelByCache(album, ChannelConstant.DATA_TYPE_ALBUM,pageId));
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
    private Map<String, String> getLatestVideoPicMap(String videoPic, String defaultPic) {
        Map<String, String> videoPicMap = new HashMap<String, String>();
        videoPicMap.put("pic_16_10", defaultPic);// 16:10 的图片用在二级导航，仍然取专辑图
        String pic169 = null;
        String pic43 = null;
        if (StringUtils.isNotEmpty(videoPic)) {
            pic169 = videoPic;
            pic43 = videoPic;
            if (!videoPic.endsWith(".jpg")) {
                pic169 = videoPic + "/thumb/2_960_540.jpg";
                pic43 = videoPic + "/thumb/2_400_300.jpg";
            }
        }
        videoPicMap.put("pic_16_9", StringUtils.isNotEmpty(pic169) ? pic169 : defaultPic);
        videoPicMap.put("pic_4_3", StringUtils.isNotEmpty(pic43) ? pic43 : defaultPic);

        return videoPicMap;
    }

    /***
     * 填充图片
     * @param focus
     * @param album
     * @param rec
     * @param type
     */
    private void fillAlbumPic(ChannelFocus focus,PlayCache album,RecommendTpResponse.RecData rec,Integer type){
        if (SkipTypeConstant.REC_SKIP_ALBUM.equals(type)) {
            focus.setPic(rec.getDefaultPic());
            if (rec.getCid() != null && rec.getCid() == VideoConstants.Category.VARIETY) {
                focus.setPic(rec.getLatest_auto_video_pic());
                focus.setPicAll(getLatestVideoPicMap(rec.getLatest_auto_video_pic(), rec.getDefaultPic()));
            } else {
                focus.setPic(rec.getDefaultPic());
                focus.setPicAll(channelHelper.getChannelImage(rec));
            }
        } else {
            String pic = StringUtils.isNotBlank(rec.getMobilePic()) ? rec.getMobilePic() : album.getAPic(320, 200);
            if (album.getaCategoryId() != null && album.getaCategoryId() == VideoConstants.Category.VARIETY) {
//                focus.setPic(rec.getLatest_auto_video_pic());
                focus.setPic(StringUtils.isNotEmpty(rec.getLatest_auto_video_pic()) ? rec
                        .getLatest_auto_video_pic() : pic);
                focus.setPicAll(getLatestVideoPicMap(pic, album.getAPic(320, 200)));
            } else {
                focus.setPic(pic);
                focus.setPicAll(channelHelper.getChannelImage(album, pic,ChannelConstant.DATA_TYPE_ALBUM,album.getaType()));
            }
        }
    }

}
