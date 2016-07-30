package xserver.api.module.superlive.request;

public class CategoryDetailDataRequest {

    public final static String DIRECTION_UP = "0"; // 向上翻页取数据
    public final static String DIRECTION_DOWN = "1"; // 向下翻页取数据

    private String cid;
    private String subCid;
    private String direction;
    private String value;
    private Integer size;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSubCid() {
        return subCid;
    }

    public void setSubCid(String subCid) {
        this.subCid = subCid;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}
