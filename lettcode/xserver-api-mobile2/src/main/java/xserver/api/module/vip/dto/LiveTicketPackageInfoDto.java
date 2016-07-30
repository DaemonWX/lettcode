package xserver.api.module.vip.dto;

import java.util.List;

import xserver.api.dto.BaseDto;

/**
 * 查询用户直播券详细信息封装类
 */
public class LiveTicketPackageInfoDto extends BaseDto {

    /**
     * 频道id
     */
    private String matchId;

    /**
     * 赛事id
     */
    private String itemId;

    /**
     * 赛季id
     */
    private String sessionId;

    /**
     * 赛季结束时间
     */
    private String sessionEndDate;

    /**
     * 直播券详细信息列表
     */
    List<LiveTicketInfoDto> packages;

    public String getMatchId() {
        return this.matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionEndDate() {
        return this.sessionEndDate;
    }

    public void setSessionEndDate(String sessionEndDate) {
        this.sessionEndDate = sessionEndDate;
    }

    public List<LiveTicketInfoDto> getPackages() {
        return this.packages;
    }

    public void setPackages(List<LiveTicketInfoDto> packages) {
        this.packages = packages;
    }

    public static class LiveTicketInfoDto extends BaseDto {

        /**
         * 数量
         */
        private Integer counts;

        /**
         * 结束时间，格式为"yyyy-MM-dd hh:mm:ss"
         */
        private String endTime;

        /**
         * 移动端图片URL
         */
        private String mobileImg;

        /**
         * 直播券名称
         */
        private String name;

        /**
         * 描述显示
         */
        private String playDesc;

        /**
         * 名称显示
         */
        private String playName;

        /**
         * 非会员价格
         */
        private String originalPrice;

        /**
         * 赛季名称
         */
        private String sessionName;

        /**
         * 可用状态，0--可用，1--不可用
         */
        private String status;

        /**
         * 超级手机图片URL
         */
        private String superMobileImg;

        /**
         * 直播券类型，1--直播卷，2--轮次包 暂时没有，3--赛季包，4--球队包
         */
        private Integer type;

        /**
         * 有效时间
         */
        private Integer validateDays;

        /**
         * 会员价格
         */
        private String vipPrice;

        public Integer getCounts() {
            return this.counts;
        }

        public void setCounts(Integer counts) {
            this.counts = counts;
        }

        public String getEndTime() {
            return this.endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getMobileImg() {
            return this.mobileImg;
        }

        public void setMobileImg(String mobileImg) {
            this.mobileImg = mobileImg;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlayDesc() {
            return this.playDesc;
        }

        public void setPlayDesc(String playDesc) {
            this.playDesc = playDesc;
        }

        public String getPlayName() {
            return this.playName;
        }

        public void setPlayName(String playName) {
            this.playName = playName;
        }

        public String getOriginalPrice() {
            return this.originalPrice;
        }

        public void setOriginalPrice(String originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getSessionName() {
            return this.sessionName;
        }

        public void setSessionName(String sessionName) {
            this.sessionName = sessionName;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSuperMobileImg() {
            return this.superMobileImg;
        }

        public void setSuperMobileImg(String superMobileImg) {
            this.superMobileImg = superMobileImg;
        }

        public Integer getType() {
            return this.type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getValidateDays() {
            return this.validateDays;
        }

        public void setValidateDays(Integer validateDays) {
            this.validateDays = validateDays;
        }

        public String getVipPrice() {
            return this.vipPrice;
        }

        public void setVipPrice(String vipPrice) {
            this.vipPrice = vipPrice;
        }

    }
}
