package xserver.api.module.channel.skippolicy;

import org.springframework.stereotype.Service;
import xserver.api.module.channel.ChannelConstant;
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
 * 跳转到url，包括h5和外跳
 */
@Service
public class SkipURLPolicy extends SkipPolicyAdapter {
    @Override
    public ChannelBlock parseSkipPolicy(Object recData, Integer type,Map<String,Object> otherParams) {
        ChannelBlock block = null;
        ChannelFocus focus = null;
        RecommendTpResponse.RecData rec = (RecommendTpResponse.RecData) recData;
        if (SkipTypeConstant.CMS_SKIP_URL.equals(type)
                || (SkipTypeConstant.CMS_SKIP_ALL_WEB.equals(type) && rec.getContent() != null && rec.getContent()
                        .matches("\\d+"))) {
            // 如果是全网数据，来自cms，content保存着pid
            focus = new ChannelFocus();
            focus.setCmsid(rec.getId());

            focus.setName(rec.getTitle());
            focus.setSubTitle(rec.getSubTitle());
            focus.setPic(rec.getMobilePic());
            focus.setPicAll(channelHelper.getCmsMutiSizeImage(rec.getPicList(), rec.getMobilePic()));

            if (SkipTypeConstant.CMS_SKIP_URL.equals(type)) {
                switch (rec.getSkipType()) {
                case ChannelConstant.CMS_URL_SKIP_TYPE_WEB:// URL外跳
                    focus.setWebUrl(rec.getSkipUrl());
                    focus.setAt(ChannelFocus.HOME_ACTION_TYPE_WEB);
                    break;
                case ChannelConstant.CMS_URL_SKIP_TYPE_WEBVIEW:// URL内嵌webview
                case ChannelConstant.CMS_URL_SKIP_TYPE_SUBJECT:// 专题跳转
                    focus.setWebViewUrl(rec.getSkipUrl());
                    focus.setAt(ChannelFocus.HOME_ACTION_TYPE_WEBVIEW);
                    break;
                default:
                    break;
                }
                focus.setShowTagList(channelHelper.parseShowTagList(rec));
            } else { // 全网数据
                focus.setWebViewUrl(ChannelConstant.WEB_PLAY_URL + rec.getContent());
                focus.setAt(ChannelFocus.HOME_ACTION_TYPE_WEBVIEW);
                focus.setType(ChannelConstant.DATA_TYPE_ALL_WEB);
            }
            this.afterParseSkipPolicy(rec, focus, new AssistData((String) otherParams.get("pageid")));
        }
        if(focus != null){
            block = new ChannelBlock();
            List<ChannelFocus> focusList = new ArrayList<>();
            focusList.add(focus);
            block.setList(focusList);
        }

        return block;
    }
}
