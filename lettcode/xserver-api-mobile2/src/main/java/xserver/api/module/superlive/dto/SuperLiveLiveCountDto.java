package xserver.api.module.superlive.dto;

import java.util.List;


public class SuperLiveLiveCountDto {
    private int liveCount;
    private List<LiveOrderInfo> liveOrderInfo;
    
    public int getLiveCount() {
        return liveCount;
    }

    public void setLiveCount(int liveCount) {
        this.liveCount = liveCount;
    }

    public List<LiveOrderInfo> getLiveOrderInfo() {
        return liveOrderInfo;
    }

    public void setLiveOrderInfo(List<LiveOrderInfo> liveOrderInfo) {
        this.liveOrderInfo = liveOrderInfo;
    }


    public static class LiveOrderInfo{
        private String liveId;
        private int liveOrder;

        public String getLiveId() {
            return liveId;
        }
        public void setLiveId(String liveId) {
            this.liveId = liveId;
        }
        public int getLiveOrder() {
            return liveOrder;
        }
        public void setLiveOrder(int liveOrder) {
            this.liveOrder = liveOrder;
        }
        
    }
}
