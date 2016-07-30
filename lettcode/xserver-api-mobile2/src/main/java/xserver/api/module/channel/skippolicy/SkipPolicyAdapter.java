package xserver.api.module.channel.skippolicy;

import org.springframework.beans.factory.annotation.Autowired;
import xserver.api.module.channel.ChannelHelper;
import xserver.api.module.channel.ChannelConstant;
import xserver.api.module.channel.dto.AssistData;
import xserver.api.module.channel.dto.ChannelBlock;
import xserver.api.module.channel.dto.ChannelFocus;
import xserver.api.module.video.VideoService;
import xserver.common.dto.BaseDto;
import xserver.lib.tp.rec.response.RecommendTpResponse.RecData;
import xserver.lib.tpcache.cache.PlayCache;

import java.util.List;
import java.util.Map;

/**
 * Created by xuli3 on 2015/7/20.
 */
public abstract class SkipPolicyAdapter implements ISkipPolicy{

    @Autowired
    protected VideoService videoService;

    @Autowired
    protected ChannelHelper channelHelper;

    @Override
    public ChannelBlock parseSkipPolicy(Object recData, Integer type, Map<String, Object> otherParams) {
        return null;
    }


    /***
     * 解析后处理
     * @param src
     * @param dst
     */
    public void afterParseSkipPolicy(RecData src,ChannelFocus dst,AssistData assistSrc){
        //填充通用的角标，会员
        channelHelper.fillCornerLabelForAll(dst,assistSrc);
        //填充数据来源
        if("true".equals(src.getIs_rec())){
            dst.setSource(ChannelConstant.dataSource.DATA_SOURCE_REC);
        } else {
            dst.setSource(ChannelConstant.dataSource.DATA_SOURCE_CMS);
        }
        //还有后续通用操作可增加
    }

}
