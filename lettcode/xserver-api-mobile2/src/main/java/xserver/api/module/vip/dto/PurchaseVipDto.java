package xserver.api.module.vip.dto;

import xserver.api.dto.BaseDto;

/**
 * 会员消费统一DTO封装类，封装不同支付方式的支付结果
 * @author
 */
public class PurchaseVipDto extends BaseDto {

    /**
     * 订单号，目前所有支付方式均会返回
     */
    private String corderid;

    /**
     * 支付宝app、微信app支付时，支付中心下单接口返回，直接透传给客户端
     */
    private String parentId;

    /**
     * 支付宝app支付时，支付中心下单接口返回，直接透传给客户端
     */
    private String info;

    /**
     * 微信app支付时，支付中心下单接口返回，直接透传给客户端
     */
    private String price;

    /**
     * 微信app支付时，支付中心下单接口返回，直接透传给客户端
     */
    private String ordernumber;

    /**
     * 微信app支付时，支付中心下单接口返回，直接透传给客户端
     */
    // private String data;

    /**
     * 微信app支付时，支付中心下单接口返回，直接透传给客户端
     */
    private String timestamp;

    /**
     * 微信app支付时，支付中心下单接口返回，直接透传给客户端
     */
    private String noncestr;

    /**
     * 微信app支付时，支付中心下单接口返回，直接透传给客户端
     */
    private String partnerid;

    /**
     * 微信app支付时，支付中心下单接口返回，直接透传给客户端
     */
    private String prepayid;

    /**
     * 微信app支付时，支付中心下单接口返回，直接透传给客户端
     */
    private String weixinPackage;

    /**
     * 微信app支付时，支付中心下单接口返回，直接透传给客户端
     */
    private String appid;

    /**
     * 微信app支付时，支付中心下单接口返回，直接透传给客户端
     */
    private String sign;

    /**
     * 电信话费支付专用，表示发送指令
     */
    private String command;

    /**
     * 电信话费支付专用，表示服务代码
     */
    private String servicecode;

    /**
     * 连续包月一键支付时是否签约
     */
    private Boolean onekeySign;
    
    public String getCorderid() {
        return this.corderid;
    }

    public void setCorderid(String corderid) {
        this.corderid = corderid;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrdernumber() {
        return this.ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return this.noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPartnerid() {
        return this.partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return this.prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getWeixinPackage() {
        return this.weixinPackage;
    }

    public void setWeixinPackage(String weixinPackage) {
        this.weixinPackage = weixinPackage;
    }

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getServicecode() {
        return this.servicecode;
    }

    public void setServicecode(String servicecode) {
        this.servicecode = servicecode;
    }

	public Boolean getOnekeySign() {
		return onekeySign;
	}

	public void setOnekeySign(Boolean onekeySign) {
		this.onekeySign = onekeySign;
	}
    
}
