package xserver.api.module.vip.dto;

import java.util.List;

import xserver.api.dto.BaseDto;

/**
 * 付费活动DTO
 * @author
 */
public class PaymentActivityDto extends BaseDto {

    /**
     * 活动id
     */
    private String id;

    /**
     * 活动类型，1，套餐活动 2:支付活动
     */
    private Integer type;

    /**
     * 套餐包id
     */
    private String monthType;

    /**
     * 活动支持的支付渠道，type为2时有意义
     */
    private List<Integer> payTypeList;

    /**
     * 折扣,代表减少多少钱，单位元
     */
    private Integer discount;

    /**
     * 延长天数
     */
    private Integer prolongDays;

    /**
     * 角标文案
     */
    private String iconText;

    /**
     * 标签文案
     */
    private String lableText;

    /**
     * 主体文案
     */
    private String bodyText;

    /**
     * 是否需要登录
     */
    private Boolean needLogin;

    /**
     * 是否允许乐点支付参与活动
     */
    private Boolean allowPayLepoint;

    /**
     * 活动限制总数
     */
    private Integer quota;

    /**
     * 活动剩余可参与次数
     */
    private Integer leftQuota;

    /**
     * 是否有用户数量限制
     */
    private Boolean hasUserQuota;

    /**
     * 本用户参与数量限制，-1表示无限制
     */
    private Integer userQuota;

    /**
     * 乐点支付文案
     */
    private String letvPointPayText;

    public PaymentActivityDto() {
        super();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMonthType() {
        return this.monthType;
    }

    public void setMonthType(String monthType) {
        this.monthType = monthType;
    }

    public List<Integer> getPayTypeList() {
        return this.payTypeList;
    }

    public void setPayTypeList(List<Integer> payTypeList) {
        this.payTypeList = payTypeList;
    }

    public Integer getDiscount() {
        return this.discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getProlongDays() {
        return this.prolongDays;
    }

    public void setProlongDays(Integer prolongDays) {
        this.prolongDays = prolongDays;
    }

    public String getIconText() {
        return this.iconText;
    }

    public void setIconText(String iconText) {
        this.iconText = iconText;
    }

    public String getLableText() {
        return this.lableText;
    }

    public void setLableText(String lableText) {
        this.lableText = lableText;
    }

    public String getBodyText() {
        return this.bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public Boolean getNeedLogin() {
        return this.needLogin;
    }

    public void setNeedLogin(Boolean needLogin) {
        this.needLogin = needLogin;
    }

    public Boolean getAllowPayLepoint() {
        return this.allowPayLepoint;
    }

    public void setAllowPayLepoint(Boolean allowPayLepoint) {
        this.allowPayLepoint = allowPayLepoint;
    }

    public Boolean getHasUserQuota() {
        return this.hasUserQuota;
    }

    public Integer getQuota() {
        return this.quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public void setHasUserQuota(Boolean hasUserQuota) {
        this.hasUserQuota = hasUserQuota;
    }

    public Integer getLeftQuota() {
        return this.leftQuota;
    }

    public void setLeftQuota(Integer leftQuota) {
        this.leftQuota = leftQuota;
    }

    public Integer getUserQuota() {
        return this.userQuota;
    }

    public void setUserQuota(Integer userQuota) {
        this.userQuota = userQuota;
    }

    public String getLetvPointPayText() {
        return this.letvPointPayText;
    }

    public void setLetvPointPayText(String letvPointPayText) {
        this.letvPointPayText = letvPointPayText;
    }
}
