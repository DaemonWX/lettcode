package xserver.api.module.play.dto;

import java.util.List;

import xserver.lib.tpcache.cache.Stream;

public class Live {
    
    private String liveId;
    
    private String screeningId;

    private String liveName;

    private String playUrl;

    private String currentStream;

    private String currentStreamId;

    private List<Stream> streams;

    /**
     * 播放类型;直播使用
     */
    private Integer playType;

    /**
     * 0 sports
     * 1 entertainment
     * 2 music
     * 3 other
     */
    private Integer liveType;

    private ChargeDto chargeObj;

    /**
     * 播放对象的属性信息，对业务层暴露
     */
    private Live playObj;

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(String screeningId) {
        this.screeningId = screeningId;
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public List<Stream> getStreams() {
        return streams;
    }

    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }

    public ChargeDto getChargeObj() {
        return chargeObj;
    }

    public void setChargeObj(ChargeDto chargeObj) {
        this.chargeObj = chargeObj;
    }

    public String getCurrentStream() {
        return currentStream;
    }

    public void setCurrentStream(String currentStream) {
        this.currentStream = currentStream;
    }

    public Integer getPlayType() {
        return playType;
    }

    public void setPlayType(Integer playType) {
        this.playType = playType;
    }

    public String getCurrentStreamId() {
        return currentStreamId;
    }

    public void setCurrentStreamId(String currentStreamId) {
        this.currentStreamId = currentStreamId;
    }

    public Live getPlayObj() {
        return playObj;
    }

    public void setPlayObj(Live playObj) {
        this.playObj = playObj;
    }

    public Integer getLiveType() {
        return liveType;
    }

    public void setLiveType(Integer liveType) {
        this.liveType = liveType;
    }
}
