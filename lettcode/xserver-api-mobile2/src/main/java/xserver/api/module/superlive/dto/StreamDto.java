package xserver.api.module.superlive.dto;

import xserver.lib.constant.StreamConstants;
import xserver.lib.tp.live.response.LiveChannelStream;

public class StreamDto extends LiveChannelStream implements Comparable<StreamDto> {

    private String streamId;// 流ID
    private String streamName;// 流名称
    private String rateType;// 码率类型，参考《码率类型词典编码》http://st.live.letv.com/live/code/00014.json
    private String streamUrl;// 对该客户端有效的直播流播放地址
    private String name;
    private String code;
    private Integer order; // 码流清晰度的顺序

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String getStreamId() {
        return streamId;
    }

    @Override
    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    @Override
    public String getStreamName() {
        return streamName;
    }

    @Override
    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    @Override
    public String getRateType() {
        return rateType;
    }

    @Override
    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    @Override
    public String getStreamUrl() {
        return streamUrl;
    }

    @Override
    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int compareTo(StreamDto s) {
    	Integer otherCodeValue = StreamConstants.STREAM_CODE_SORT_VSLUE.get(s.getCode());
        otherCodeValue = otherCodeValue == null ? 0 : otherCodeValue.intValue();
        Integer thisCodeValue = StreamConstants.STREAM_CODE_SORT_VSLUE.get(this.getCode());
        thisCodeValue = thisCodeValue == null ? 0 : thisCodeValue.intValue();
        if (otherCodeValue > thisCodeValue) {
            return 1;
        } else if (otherCodeValue < thisCodeValue) {
            return -1;
        }
        // 以上都规则都无法区分的数据，向后追加
        return 1;
    }

    @Override
    public String toString() {
        return "LiveChannelStream [streamId=" + streamId + ", streamName=" + streamName + ", rateType=" + rateType
                + ", streamUrl=" + streamUrl + "]";
    }

}
