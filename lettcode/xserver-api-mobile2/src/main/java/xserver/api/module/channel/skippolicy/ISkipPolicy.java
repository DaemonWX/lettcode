package xserver.api.module.channel.skippolicy;

import xserver.api.module.channel.dto.AssistData;
import xserver.api.module.channel.dto.ChannelBlock;
import xserver.api.module.channel.dto.ChannelFocus;
import xserver.common.dto.BaseDto;
import xserver.lib.tpcache.cache.PlayCache;

import java.util.List;
import java.util.Map;

/**
 * Created by xuli3 on 2015/7/20.
 */
public interface ISkipPolicy {
    /**
     * 处理各种跳转
     * @param recData  源数据
     * @param type     数据所属类型，处理过程根据type值区分不同逻辑
     * @param otherParams     处理时需要的一些其它参数，为了避免经常添加这里设定为map
     * @return
     */
    public ChannelBlock parseSkipPolicy(Object recData, Integer type,Map<String,Object> otherParams);

}
