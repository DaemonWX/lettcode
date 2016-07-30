package xserver.api.module.channel.skippolicy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import xserver.api.module.channel.ChannelConstant;
import xserver.api.module.channel.constants.SkipTypeConstant;
import xserver.api.module.channel.dto.ChannelBlock;
import xserver.common.dto.BaseDto;
import xserver.lib.dto.live.LiveDto;
import xserver.lib.tpcache.CacheConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xuli
 * Date：15/11/4
 * Time: 下午4:30
 * 页面直播板块-数据来源是服务端从直播大厅取的
 */
@Service
public class SkipNewLivePolicy extends SkipPolicyAdapter{
    @Override
    public ChannelBlock parseSkipPolicy(Object recData, Integer type, Map<String, Object> otherParams) {
        ChannelBlock block = null;
        if(SkipTypeConstant.CMS_SKIP_MANUAL_lIVE1.equals(type) || SkipTypeConstant.CMS_SKIP_MANUAL_lIVE2.equals(type)
                || SkipTypeConstant.CMS_SKIP_MANUAL_lIVE3.equals(type)){
            //直播板块

            String cacheKey = CacheConstants.channel.CHANNEL_PAGE_LIVE_ + ChannelConstant.liveType.LIVE_TYPE_OTHER;
            String cacheType = ChannelConstant.CHANNEL_LIVE_PAGEID_LIVETYPE.get((String)otherParams.get("pageid"));
            if(StringUtils.isNotBlank(cacheType)){
                cacheKey = CacheConstants.channel.CHANNEL_PAGE_LIVE_ + cacheType;
            }
            ChannelBlock blockCache = channelHelper.getChannelLiveCache(cacheKey);
            List<LiveDto> liveDtoList = blockCache.getLiveList();
            if(blockCache != null && liveDtoList != null && liveDtoList.size() > 0){
                block = new ChannelBlock();
                int count = (Integer)otherParams.get("cms_num");
                if(SkipTypeConstant.CMS_SKIP_MANUAL_lIVE1.equals(type)){
                    //列表样式1-只展示两条数据，展示总场次
                    block.setLiveCount(blockCache.getLiveCount());
                    count = ChannelConstant.HOME_PAGE_LIVE_COUNT;
                } else if(SkipTypeConstant.CMS_SKIP_MANUAL_lIVE3.equals(type)){
                    count = count/2 * 2;
                    int liveC = liveDtoList.size()/2 * 2;
                    if(liveC < count){
                        //如果取到的数据比配置的少，以取到的数据为准
                        count = liveC;
                    }
                }
                if(liveDtoList.size() >= count){
                    liveDtoList = liveDtoList.subList(0,count);
                }
                block.setLiveList(liveDtoList);
            }
        }
       return block;
    }
}
