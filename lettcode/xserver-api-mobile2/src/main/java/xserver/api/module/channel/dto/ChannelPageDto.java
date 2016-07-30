package xserver.api.module.channel.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xserver.api.constant.DataConstants;
import xserver.api.constant.TerminalConstants;
import xserver.lib.tp.cms.response.CmsBlockContent;
import xserver.lib.tp.cms.response.CmsBlockTpResponse;

/**
 * 频道墙返回格式
 */
public class ChannelPageDto {
    private String name; // 频道组名称
    private List<Channel> channels; // 频道组下的频道数据

    public ChannelPageDto() {
    }

    public ChannelPageDto(CmsBlockTpResponse channelPage) {
        if (channelPage != null) {
            this.name = channelPage.getName();
            List<CmsBlockContent> blocks = channelPage.getBlockContent();
            if (blocks != null) {
                channels = new ArrayList<ChannelPageDto.Channel>();
                for (CmsBlockContent block : blocks) {
                    // 过滤版权
                    if (block.getPushflag() != null && block.getPushflag().contains(DataConstants.ANDROID_COPYRIGHT)) {
                        channels.add(new Channel(block));
                    }
                }
            }
        }
    }

    // 因x1和max rom不支持影院声，暂屏蔽
    public ChannelPageDto(CmsBlockTpResponse channelPage, String terminalSeries) {
        if (channelPage != null) {
            this.name = channelPage.getName();
            List<CmsBlockContent> blocks = channelPage.getBlockContent();
            if (blocks != null) {
                channels = new ArrayList<ChannelPageDto.Channel>();
                for (CmsBlockContent block : blocks) {
                    // 过滤版权
                    if (block.getPushflag() != null && block.getPushflag().contains(DataConstants.ANDROID_COPYRIGHT)) {
                        if (StringUtils.isNotEmpty(terminalSeries)
                                && (TerminalConstants.NOT_SUPPORT_DOLBY_DTS_SERIES.contains(terminalSeries))
                                && StringUtils.isNotEmpty(block.getContent())
                                && "1002905344".equalsIgnoreCase(block.getContent())) {
                            // 不支持影院声播放的机型过滤该频道（暂时）
                            continue;
                        }
                        channels.add(new Channel(block));
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    static class Channel {
        private String pageid; // 对应频道的cms pageid
        private String name; // 频道名称
        private String mzcid; // 频道id
        private String type; // 频道跳转类型
        private String pic; // 频道背景大图
        private String pic1; // 频道背景图标
        private String pic2;
        private String url; // 频道需要跳转网页时的URL
        private String dataUrl; // 频道首页数据地址

        private String cmsId;//cms对应的频道id,用于数据上报
        private final static String SEARCH_URL = "http://api.m.letv.com/mobile/channel/search.json";
        private final static String REC_URL = "http://api.m.letv.com/mobile/channel/data.json";

        public Channel() {
        }

        public Channel(CmsBlockContent block) {
            /**
             * CMS的字段固定，编辑将对应的数据填入，CMS字段名称无实际含义
             */
            if (block != null) {
                this.pageid = block.getContent();
                this.name = block.getTitle();
                this.mzcid = block.getSubTitle();
                this.type = StringUtils.isBlank(block.getUrl()) ? "1" : block.getUrl();
                this.pic = block.getMobilePic();
                this.pic1 = block.getPic1();
                this.pic2 = block.getPic2();
                this.url = block.getShorDesc();
                this.cmsId = block.getId();
                this.dataUrl = parseDataUrl(block);
            }
        }

        private String parseDataUrl(CmsBlockContent block) {
            // 临时使用pageid判断是否要走搜索供客户端开发联调，稍后需要CMS提供搜索跳转类型
            // if (block != null) {
            // if ("1002798160".equals(block.getContent())) { // 3D频道
            // return SEARCH_URL +
            // "?filter=playStreamFeatures:3d;dt:1;cg:1;or:1;vt=180001&page=1&pageSize=20";
            // } else if ("1002798161".equals(block.getContent())) { // 4K频道
            // return SEARCH_URL +
            // "?filter=playStreamFeatures:4k;dt:1;cg:1;or:1;vt=180001&page=1&pageSize=20";
            // } else if ("1002905344".equals(block.getContent())) { // 影院声频道
            // return SEARCH_URL +
            // "?filter=playStreamFeatures:db;dt:1;cg:1;or:1;vt=180001&page=1&pageSize=20";
            // } else if ("1013".equals(block.getSubTitle())) { // 1080p
            // return SEARCH_URL +
            // "?filter=playStreamFeatures:1080p;dt:1;cg:1;or:1;vt=180001&page=1&pageSize=20";
            // } else if ("1024".equals(block.getSubTitle())) {
            // return SEARCH_URL +
            // "?filter=playStreamFeatures:2k;dt:1;cg:1;or:1;vt=180001&page=1&pageSize=20";
            // } else {
            // return REC_URL + "?pageid=" + block.getContent();
            // }
            // }
            Integer cid = null;
            if (StringUtils.isNotBlank(block.getSubTitle())) {
                try {
                    cid = Integer.parseInt(block.getSubTitle());
                } catch (Exception e) {
                }
            }
            return ChannelSkip.getChannelSkipUrl(block.getContent(), cid);
        }

        public String getPageid() {
            return pageid;
        }

        public void setPageid(String pageid) {
            this.pageid = pageid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMzcid() {
            return mzcid;
        }

        public void setMzcid(String mzcid) {
            this.mzcid = mzcid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getPic1() {
            return pic1;
        }

        public void setPic1(String pic1) {
            this.pic1 = pic1;
        }

        public String getPic2() {
            return pic2;
        }

        public void setPic2(String pic2) {
            this.pic2 = pic2;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDataUrl() {
            return dataUrl;
        }

        public void setDataUrl(String dataUrl) {
            this.dataUrl = dataUrl;
        }
    }
}
