package xserver.api.module.share;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import xserver.api.module.BaseService;
import xserver.api.module.share.dto.LinkCardDto;
import xserver.api.module.share.dto.LinkCardMediaLink;
import xserver.lib.constant.CommonConstants;
import xserver.lib.constant.VideoConstants;
import xserver.lib.tpcache.cache.PlayCache;

@Service("shareService")
public class ShareService extends BaseService {

    public LinkCardDto linkCardShare(String url) {
        // url:http://m.letv.com/vplay_22886767.html
        log.info("link card share url:" + url);
        Long videoId = getVideoIdFromUrl(url);

        PlayCache playCache = null;
        LinkCardDto linkCardDto = new LinkCardDto();
        if (videoId != null) {
            playCache = this.facadeService.getVideoService().getPlayCacheEntityByVideoId(videoId,
                    CommonConstants.Copyright.PHONE);
            if (playCache != null) {
                linkCardDto.setDisplay_name(getDisPlayName(playCache));
                linkCardDto.setSummary(playCache.getvSubTitle());

                LinkCardMediaLink image = new LinkCardMediaLink();
                image.setUrl(getLinkCardImg(playCache));// 取vrs后台 120*90
                image.setWidth(120);
                image.setHeight(90);
                linkCardDto.setImage(image);

                linkCardDto.setUrl("http://m.letv.com/vplay_" + playCache.getvId() + ".html");
                linkCardDto.setObject_type("video");

                linkCardDto.setEmbed_code("http://i7.imgs.letv.com/player/swfPlayer.swf?autoPlay=1&id=" + videoId);
            } else {

            }
        }

        return linkCardDto;
    }

    private Long getVideoIdFromUrl(String url) {
        Long videoId = null;
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(url);
        while (matcher.find()) {
            videoId = Long.valueOf(matcher.group(0));

            if (videoId != null) {
                break;
            }
        }

        return videoId;
    }

    private String getDisPlayName(PlayCache playCache) {
        String displayName = null;

        Integer categoryId = playCache.getvCategoryId();
        if (categoryId != null) {
            switch (categoryId) {
            case VideoConstants.Category.VARIETY:
                displayName = playCache.getaNameCn() + playCache.getvEpisode();
                break;
            default:
                displayName = playCache.getvNameCn();
                break;
            }
        }

        return displayName;
    }

    private String getLinkCardImg(PlayCache playCache) {
        String vImg = null;
        Integer categoryId = playCache.getvCategoryId();
        if (categoryId != null) {
            switch (categoryId) {
            case VideoConstants.Category.FILM:
                vImg = playCache.getAPic(120, 90);
                break;
            default:
                vImg = playCache.getVPic(120, 90);
                break;
            }
        }

        return vImg;
    }
}
