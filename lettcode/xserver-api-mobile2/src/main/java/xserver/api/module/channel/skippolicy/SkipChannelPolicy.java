package xserver.api.module.channel.skippolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import xserver.api.module.channel.constants.SkipTypeConstant;
import xserver.api.module.channel.dto.AssistData;
import xserver.api.module.channel.dto.ChannelBlock;
import xserver.api.module.channel.dto.ChannelFocus;
import xserver.api.module.channel.dto.ChannelSkip;
import xserver.lib.tp.rec.response.RecommendTpResponse;
import xserver.lib.tpcache.cache.PlayCache;
import xserver.lib.util.MessageUtils;

import com.google.common.primitives.Ints;

/**
 * Created by xuli3 on 2015/7/20.
 * 跳转到频道页
 */
@Service
public class SkipChannelPolicy extends SkipPolicyAdapter {
    @Override
    public ChannelBlock parseSkipPolicy(Object recData, Integer type,Map<String,Object> otherParams) {
        ChannelBlock block = null;
        ChannelFocus focus = null;
        if (SkipTypeConstant.CMS_SKIP_CHANNEL.equals(type)) {
            RecommendTpResponse.RecData rec = (RecommendTpResponse.RecData) recData;
            focus = new ChannelFocus();
            focus.setCmsid(rec.getId());
            focus.setPageid(rec.getContent());
            focus.setAt("1002".equals(rec.getSkipPage()) ? ChannelFocus.HOME_ACTION_TYPE_EXCHANGE
                    : ChannelFocus.HOME_ACTION_TYPE_CHANNEL); // 1002为cms配置的"移动精品推荐"
            Integer cid = Ints.tryParse(rec.getSkipPage());
            focus.setCid(cid);// skipPage中存储跳转的频道

            focus.setName(rec.getTitle());
            focus.setSubTitle(rec.getSubTitle());
            focus.setPic(rec.getMobilePic());
            focus.setPicAll(getChannelImage(rec.getMobilePic()));
            focus.setCname(MessageUtils.getMessage(ChannelSkip.CHANNEL_MAP.get(cid), "zh_CN"));

            focus.setShowTagList(channelHelper.parseShowTagList(rec));
            focus.setDataUrl(ChannelSkip.getChannelSkipUrl(rec.getContent(), cid));
            this.afterParseSkipPolicy(rec, focus, new AssistData((String)otherParams.get("pageid")));
        }
        if(focus != null){
            block = new ChannelBlock();
            List<ChannelFocus> focusList = new ArrayList<>();
            focusList.add(focus);
            block.setList(focusList);
        }

        return block;
    }

    /************************************************* private ******************************************/
    /**
     * 获取频道图片--数据来源为cms
     * 当前支持 16:9 16:10 4:3比例的图
     * @return
     */
    private Map<String, String> getChannelImage(String cmsPic) {
        Map<String, String> picMap = null;
        if (StringUtils.isNotBlank(cmsPic)) {
            picMap = new HashMap<String, String>();
            picMap.put("pic_16_10", cmsPic);
            picMap.put("pic_16_9", cmsPic);
            picMap.put("pic_4_3", cmsPic);
        }
        return picMap;
    }
}
