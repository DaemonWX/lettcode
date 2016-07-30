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
 * Created by xuli3 on 2015/7/21.
 * 跳转到专题
 */
@Service
public class SkipSubjectPolicy extends SkipPolicyAdapter {
    @Override
    public ChannelBlock parseSkipPolicy(Object recData, Integer type,Map<String,Object> otherParams) {
        ChannelBlock block = null;
        ChannelFocus focus = null;
        RecommendTpResponse.RecData rec = (RecommendTpResponse.RecData) recData;
        // 来自cms的专题
        if (SkipTypeConstant.CMS_SKIP_SUBJECT.equals(type)) {
            if (rec.getContent() != null && rec.getContent().matches("\\d+")) { // 专题
                focus = new ChannelFocus();
                focus.setCmsid(rec.getId());
                focus.setZid(Long.parseLong(rec.getContent()));
                focus.setType(ChannelConstant.DATA_TYPE_SUBJECT);
                focus.setAt(ChannelFocus.HOME_ACTION_TYPE_FULL);
                if("NEW".equals(rec.getRemark())){
                    focus.setAt(ChannelFocus.HOME_ACTION_TYPE_NATIVE);
                }

                focus.setName(rec.getTitle());
                focus.setSubTitle(rec.getSubTitle());
                focus.setPic(rec.getMobilePic());
                focus.setPicAll(channelHelper.getCmsMutiSizeImage(rec.getPicList(), rec.getMobilePic()));

            }
        }
        // 来自推荐的专题
        if (SkipTypeConstant.REC_SKIP_SUBJECT.equals(type)) {
            if (rec.getZid() != null) { // 专题
                focus = new ChannelFocus();
                focus.setZid(rec.getZid());
                focus.setType(ChannelConstant.DATA_TYPE_SUBJECT);
                focus.setAt(ChannelFocus.HOME_ACTION_TYPE_WEBVIEW);
                focus.setWebViewUrl(rec.getPlayurl());

                focus.setName(rec.getTitle());
                focus.setPic(rec.getPicHT());
                focus.setPicAll(channelHelper.getChannelImage(rec));
            }
        }

        if (focus != null) {
            this.afterParseSkipPolicy(rec, focus,new AssistData((String)otherParams.get("pageid")));
            block = new ChannelBlock();
            List<ChannelFocus> focusList = new ArrayList<>();
            focusList.add(focus);
            block.setList(focusList);
        }

        return block;
    }

    /*************************************************private******************************************/

}
