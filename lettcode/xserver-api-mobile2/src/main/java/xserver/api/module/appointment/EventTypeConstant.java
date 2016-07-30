package xserver.api.module.appointment;

public enum EventTypeConstant {
    LIVE_SPORTS(31,"直播体育"),LIVE_ENT(32,"直播娱乐"),LIVE_MUSIC(30,"直播音乐"),LIVE_OTHER(33,"其他");
    private Integer eventType;
    private String eventTypeName;
    EventTypeConstant(Integer et,String etn){
        eventType=et;
        eventTypeName=etn;
    }
    public Integer getEventType() {
        return eventType;
    }
    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }
    public String getEventTypeName() {
        return eventTypeName;
    }
    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }
    
}
