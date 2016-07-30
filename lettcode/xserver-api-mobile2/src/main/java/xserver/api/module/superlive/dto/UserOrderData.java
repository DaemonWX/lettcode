package xserver.api.module.superlive.dto;

import java.util.Map;

/**
 * 用户定制的频道、类型，cbase中存储的数据
 */
public class UserOrderData {
    private String cids;// 类型排列定制
    private Map<String, String> ccids;// 类似下频道定制

    public String getCids() {
        return cids;
    }

    public void setCids(String cids) {
        this.cids = cids;
    }

    public Map<String, String> getCcids() {
        return ccids;
    }

    public void setCcids(Map<String, String> ccids) {
        this.ccids = ccids;
    }

    @Override
    public String toString() {
        return "UserOrderData [cids=" + cids + ", ccids=" + ccids + "]";
    }

}
