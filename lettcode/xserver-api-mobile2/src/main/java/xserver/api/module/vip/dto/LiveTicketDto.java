package xserver.api.module.vip.dto;

import xserver.api.dto.BaseDto;

/**
 * 获取体育直播券信息封装类
 * @author liyunlong
 */
public class LiveTicketDto extends BaseDto {

    /**
     * 直播券类型，1--直播卷，2--轮次包 暂时没有，3--赛季包，4--球队包
     */
    private Integer type;

    /**
     * 可用总数量
     */
    private Integer count;

    /**
     * 状态，0--不可用，1--可用
     */
    private Integer status;

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
