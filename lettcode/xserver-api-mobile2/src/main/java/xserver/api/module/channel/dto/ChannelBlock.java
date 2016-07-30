package xserver.api.module.channel.dto;

import xserver.common.dto.BaseDto;
import xserver.lib.dto.live.LiveDto;

import java.util.List;

public class ChannelBlock {

    public static final String CHANNEL_BLOCK_STIP_FILTR = "1";
    public static final String CHANNLE_BLOCK_SKIP_PAGE = "2";

    private String name; // 模块名称
    private String cid; // 模块频道id

    private String rectCid; // 模块跳转，需要跳转的频道id
    private String rectCName; // 模块跳转,需要跳转的频道名称
    private String blockSubName;// 版本副标题（cms内容副标题）
    private String rectPageId; // 模块跳转，需要跳转到的pageid(每个导航标签都有一个pageid)
    private String rectType; // 模块跳转类型 1: 跳转条件筛选 2: 跳转导航标签
    private String rectUrl; // 模块跳转，需要跳转到的url
    private String rectVt;
    private String dataUrl; // 跳转频道的URL
    private List<RectRetrieve> rectField; // 跳转条件检索对应的检索条件
    /**副标题跳转相关数据*/
    private String rectSubCid; // 模块跳转，需要跳转的频道id
    private String rectSubPageId; // 模块跳转，需要跳转到的pageid(每个导航标签都有一个pageid)
    private String rectSubType; // 模块跳转类型 1: 跳转条件筛选 2: 跳转导航标签
    private String rectSubUrl; // 模块跳转，需要跳转到的url
    private String rectSubVt;//跳转的视频类型
    private String rectSubCName; // 模块跳转,需要跳转的频道名称
    private String dataSubUrl; // 跳转频道的URL
    private List<RectRetrieve> rectSubField; // 跳转条件检索对应的检索条件
    private Long nativeSubjectStartTime;//目前专门针对鬼吹灯专题，开始时间，客户端据此计算倒计时
    /**
     * 模块展现样式，客户端会根据这个字段，决定以何种样式展示模块数据
     * 1. 焦点图
     * 2. 直播通栏
     * 3. 无标题推荐
     * 4. 有标题有更多推荐
     * 5. 有标题无更多推荐
     * 6. 乐看搜索
     * 7. 图片频道墙
     * 8. icon频道墙
     * 9. 二级导航
     * 10. 视频列表
     * 11. 检索表
     * 12. 检索视频列表
     * 13. 重磅
     * 14. 热词
     * 26. 1大图4小图，有更多---领先版使用
     * 27. 1大图4小图，无更多---领先版使用
     * 28. 图文列表---领先版使用
     * 29. 4张小图，有更多---领先版使用
     * 30. 4张小图，无更多---领先版使用
     */
    private String style;
    private List<ChannelFocus> list; // 模块下的默认数据

    private String recFragId;// 推荐数据上报字段
    private String recReid; // 推荐数据上报字段
    private String recArea;// 推荐数据上报字段
    private String recBucket;// 推荐数据上报字段
    private String src;// 数据来源 0 编辑手动 1 推荐自动
    private String recSrcType;//推荐数据上报字段，数据来自何种推荐 【人工推荐：editor； 自动推荐：auto；混合推荐：mix】

    /**新增直播相关的数据*/
    private Integer liveCount;//直播总场数
    private List<LiveDto> liveList; // 模块下的直播数据

    private List<BaseDto> dataList;//模块下的数据，目前包括乐词、明星

    public String getRectCName() {
        return rectCName;
    }

    public void setRectCName(String rectCName) {
        this.rectCName = rectCName;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getRectCid() {
        return rectCid;
    }

    public void setRectCid(String rectCid) {
        this.rectCid = rectCid;
    }

    public String getRectPageId() {
        return rectPageId;
    }

    public void setRectPageId(String rectPageId) {
        this.rectPageId = rectPageId;
    }

    public String getRectType() {
        return rectType;
    }

    public void setRectType(String rectType) {
        this.rectType = rectType;
    }

    public String getRectUrl() {
        return rectUrl;
    }

    public void setRectUrl(String rectUrl) {
        this.rectUrl = rectUrl;
    }

    public String getRectVt() {
        return rectVt;
    }

    public void setRectVt(String rectVt) {
        this.rectVt = rectVt;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<ChannelFocus> getList() {
        return list;
    }

    public void setList(List<ChannelFocus> list) {
        this.list = list;
    }

    public List<RectRetrieve> getRectField() {
        return rectField;
    }

    public void setRectField(List<RectRetrieve> rectField) {
        this.rectField = rectField;
    }

    public String getBlockSubName() {
        return blockSubName;
    }

    public void setBlockSubName(String blockSubName) {
        this.blockSubName = blockSubName;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getRecFragId() {
        return recFragId;
    }

    public void setRecFragId(String recFragId) {
        this.recFragId = recFragId;
    }

    public String getRecReid() {
        return recReid;
    }

    public void setRecReid(String recReid) {
        this.recReid = recReid;
    }

    public String getRecArea() {
        return recArea;
    }

    public void setRecArea(String recArea) {
        this.recArea = recArea;
    }

    public String getRecBucket() {
        return recBucket;
    }

    public void setRecBucket(String recBucket) {
        this.recBucket = recBucket;
    }

    public String getRectSubCid() {
        return rectSubCid;
    }

    public void setRectSubCid(String rectSubCid) {
        this.rectSubCid = rectSubCid;
    }

    public String getRectSubCName() {
        return rectSubCName;
    }

    public void setRectSubCName(String rectSubCName) {
        this.rectSubCName = rectSubCName;
    }

    public String getRectSubPageId() {
        return rectSubPageId;
    }

    public void setRectSubPageId(String rectSubPageId) {
        this.rectSubPageId = rectSubPageId;
    }

    public String getRectSubType() {
        return rectSubType;
    }

    public void setRectSubType(String rectSubType) {
        this.rectSubType = rectSubType;
    }

    public String getRectSubUrl() {
        return rectSubUrl;
    }

    public void setRectSubUrl(String rectSubUrl) {
        this.rectSubUrl = rectSubUrl;
    }

    public String getRectSubVt() {
        return rectSubVt;
    }

    public void setRectSubVt(String rectSubVt) {
        this.rectSubVt = rectSubVt;
    }

    public String getDataSubUrl() {
        return dataSubUrl;
    }

    public void setDataSubUrl(String dataSubUrl) {
        this.dataSubUrl = dataSubUrl;
    }

    public List<RectRetrieve> getRectSubField() {
        return rectSubField;
    }

    public void setRectSubField(List<RectRetrieve> rectSubField) {
        this.rectSubField = rectSubField;
    }

    public Long getNativeSubjectStartTime() {
        return nativeSubjectStartTime;
    }

    public void setNativeSubjectStartTime(Long nativeSubjectStartTime) {
        this.nativeSubjectStartTime = nativeSubjectStartTime;
    }

    public Integer getLiveCount() {
        return liveCount;
    }

    public void setLiveCount(Integer liveCount) {
        this.liveCount = liveCount;
    }

    public List<LiveDto> getLiveList() {
        return liveList;
    }

    public void setLiveList(List<LiveDto> liveList) {
        this.liveList = liveList;
    }

    public List<BaseDto> getDataList() {
        return dataList;
    }

    public void setDataList(List<BaseDto> dataList) {
        this.dataList = dataList;
    }

    public String getRecSrcType() {
        return recSrcType;
    }

    public void setRecSrcType(String recSrcType) {
        this.recSrcType = recSrcType;
    }

    public static class RectRetrieve {
        private String retrieveKey; // 筛选条件的key
        private String retrieveValue; // 筛选条件的value

        public String getRetrieveKey() {
            return retrieveKey;
        }

        public void setRetrieveKey(String retrieveKey) {
            this.retrieveKey = retrieveKey;
        }

        public String getRetrieveValue() {
            return retrieveValue;
        }

        public void setRetrieveValue(String retrieveValue) {
            this.retrieveValue = retrieveValue;
        }
    }

}
