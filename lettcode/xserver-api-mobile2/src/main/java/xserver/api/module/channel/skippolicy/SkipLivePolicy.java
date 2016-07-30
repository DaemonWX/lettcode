package xserver.api.module.channel.skippolicy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import xserver.api.module.channel.constants.SkipTypeConstant;
import xserver.api.module.channel.dto.AssistData;
import xserver.api.module.channel.dto.ChannelBlock;
import xserver.api.module.channel.dto.ChannelFocus;
import xserver.lib.tp.rec.response.RecommendTpResponse;
import xserver.lib.tpcache.cache.PlayCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xuli3 on 2015/7/20.
 * 跳转到直播
 */
@Service
public class SkipLivePolicy extends SkipPolicyAdapter {
    @Override
    public ChannelBlock parseSkipPolicy(Object recData, Integer type,Map<String,Object> otherParams) {
        ChannelBlock block = null;
        ChannelFocus focus = null;
        if (SkipTypeConstant.CMS_SKIP_LIVE.equals(type) || SkipTypeConstant.CMS_SKIP_ANDROID_LIVE.equals(type)) {
            RecommendTpResponse.RecData rec = (RecommendTpResponse.RecData) recData;
            focus = new ChannelFocus();
            focus.setCmsid(rec.getId());
            focus.setLiveCode(rec.getRemark());
            focus.setAt(ChannelFocus.HOME_ACTION_TYPE_LIVE);
            focus.setLiveid(rec.getShorDesc()); // cms的shorDesc字段存储直播场次id
            focus.setId(rec.getRemark());

            focus.setName(rec.getTitle());
            focus.setPic(rec.getMobilePic());
            focus.setPicAll(channelHelper.getCmsMutiSizeImage(rec.getPicList(), rec.getMobilePic()));
            focus.setHomeImgUrl(rec.getPic1()); // cms的pic1字段存储主队图标
            focus.setGuestImgUrl(rec.getPic2()); // cms的pic2字段存储客队图标

            focus.setPay("1".equals(rec.getSubtitle()) ? "1" : "0"); // cms的subTitle中存储着是否付费
            long expireTime = this.getLiveExpireTime();
            focus.setTm(String.valueOf(expireTime));
            focus.setLiveUrl(this.getLiveUrl(rec.getAndroidUrl(), 0, expireTime));
            focus.setShowTagList(channelHelper.parseShowTagList(rec));
            this.afterParseSkipPolicy(rec, focus,new AssistData((String)otherParams.get("pageid")));
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
    /**
     * 获取直播过期时间戳
     * @return
     */
    private long getLiveExpireTime() {
        // 直播流有效期为1小时
        return System.currentTimeMillis() / 100 + 3600;
    }

    /**
     * 获取直播地址
     * @param liveUrl
     *            cms中维护的直播地址
     * @param isPay
     *            是否付费 1:付费 0:免费
     * @param expireTime
     *            直播过期时间，单位s
     * @return
     */
    private String getLiveUrl(String liveUrl, int isPay, long expireTime) {
        if (StringUtils.isNotBlank(liveUrl)) {
            StringBuilder sBuilder = new StringBuilder(liveUrl.replace("?", "?bugfix=20140530&"));
            if (sBuilder.charAt(sBuilder.length() - 1) != '&') {
                sBuilder.append("&");
            }
            sBuilder.append("platid=10&playid=1&termid=2&pay=").append(isPay).append("&tm=").append(expireTime)
                    .append("&splatid=1003&ostype=andriod&hwtype=un");
            return sBuilder.toString();
        }

        return liveUrl;
    }
}
