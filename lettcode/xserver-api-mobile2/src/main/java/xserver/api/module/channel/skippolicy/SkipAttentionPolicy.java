package xserver.api.module.channel.skippolicy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import xserver.api.module.channel.ChannelConstant;
import xserver.api.module.channel.constants.SkipTypeConstant;
import xserver.api.module.channel.dto.ChannelBlock;
import xserver.common.dto.BaseDto;
import xserver.lib.tp.rec.response.RecommendTpResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xuli
 * Date：15/11/4
 * Time: 下午2:57
 * 关注板块的数据
 */
@Service
public class SkipAttentionPolicy extends SkipPolicyAdapter{

    @Override
    public ChannelBlock parseSkipPolicy(Object recData, Integer type, Map<String, Object> otherParams) {
        ChannelBlock block = null;
        List<BaseDto> baseDtos = null;
        if(SkipTypeConstant.CMS_SKIP_ATTENTION.equals(type)){
            //关注
            RecommendTpResponse.RecData rec = (RecommendTpResponse.RecData) recData;
            String attentionsIds = null;
            String attentionType = null;
            if(StringUtils.isNotBlank(rec.getTitle())){
                //配置的明星
                String[] actorIds = rec.getTitle().split("\\|");
                if(actorIds.length > 1){
                   attentionsIds = actorIds[1];
                   attentionType = ChannelConstant.AttentionType.ATTENTION_TYPE_HOTWORDS;
                }
            } else if(StringUtils.isNotBlank(rec.getShorDesc())){
                //配置的乐词
                String[] hotWords = rec.getShorDesc().split("\\|");
                if(hotWords.length > 1){
                    attentionsIds = hotWords[1];
                    attentionType = ChannelConstant.AttentionType.ATTENTION_TYPE_ACTOR;
                }
            }
            if(StringUtils.isNotBlank(attentionsIds)){
                baseDtos = channelHelper.getBaseDataForAttention(attentionsIds,attentionType);
            }
        }

        if(baseDtos != null){
            block = new ChannelBlock();
            block.setDataList(baseDtos);
        }

        return block;
    }


}
