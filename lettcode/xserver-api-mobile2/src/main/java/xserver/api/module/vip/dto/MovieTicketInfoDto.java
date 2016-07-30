package xserver.api.module.vip.dto;

import xserver.api.dto.BaseDto;
import xserver.lib.tp.vip.response.GetMovieTicketListTpResponse.MovieTicketInfoTpResponse;
import xserver.lib.util.CalendarUtil;

/**
 * 观影券信息
 * @author
 */
public class MovieTicketInfoDto extends BaseDto {

    /**
     * 观影券id
     */
    private String id;

    /**
     * 观影券名称
     */
    private String ticketName;

    /**
     * 观影券总张数
     */
    private Integer totalNumber;

    /**
     * 观影券已用张数
     */
    private Integer usedNumber;

    /**
     * 观影券开始时间，格式为“yyyy-MM-hh”
     */
    private String beginTime;

    /**
     * 观影券到期时间，格式为“yyyy-MM-hh”
     */
    private String endTime;

    /**
     * 观影券来源
     */
    private String ticketSource;

    /**
     * 是否过期，0--未过期，1--已过期
     */
    private Integer isExpired;

    /**
     * 专辑ID，如果是特定于电影的观影券，则有值
     */
    private String albumId;

    public MovieTicketInfoDto(MovieTicketInfoTpResponse moiveTicket) {
        super();
        if (moiveTicket != null) {
            this.id = moiveTicket.getTicketCode();
            this.ticketName = moiveTicket.getTicketName();
            this.totalNumber = moiveTicket.getTotalNumber();
            this.usedNumber = moiveTicket.getUsedNumber();
            this.beginTime = CalendarUtil.getDateStringFromLong(moiveTicket.getBeginTime(),
                    CalendarUtil.SHORT_DATE_FORMAT);
            this.endTime = CalendarUtil.getDateStringFromLong(moiveTicket.getEndTime(), CalendarUtil.SHORT_DATE_FORMAT);
            this.ticketSource = moiveTicket.getTicketSource();
            this.isExpired = moiveTicket.getIsExpired();
            this.albumId = String.valueOf(moiveTicket.getPid());
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicketName() {
        return this.ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Integer getTotalNumber() {
        return this.totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Integer getUsedNumber() {
        return this.usedNumber;
    }

    public void setUsedNumber(Integer usedNumber) {
        this.usedNumber = usedNumber;
    }

    public String getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTicketSource() {
        return this.ticketSource;
    }

    public void setTicketSource(String ticketSource) {
        this.ticketSource = ticketSource;
    }

    public Integer getIsExpired() {
        return this.isExpired;
    }

    public void setIsExpired(Integer isExpired) {
        this.isExpired = isExpired;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

}
