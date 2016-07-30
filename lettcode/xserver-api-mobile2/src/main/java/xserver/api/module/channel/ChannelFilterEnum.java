package xserver.api.module.channel;

public enum ChannelFilterEnum {
    FILM(1, "电影", "1"),
    TV(2, "电视剧", "1"),
    ENT(3, "娱乐", "2"),
    SPORT(4, "体育", "2"),
    CARTOON(5, "动漫", "1"),
    MUSIC(9, "音乐", "2"),
    VARIETY(11, "综艺", "1"),
    CAR(14, "汽车", "2"),
    DFILM(16, "纪录片", "1"),
    FASHION(20, "风尚", "2"),
    CAIJING(22, "财经", "2"),
    TRAVEL(23, "旅游", "2"),
    VIP(1000, "会员", "1");

    private final int cid;
    private final String name;
    private final String dataType;

    ChannelFilterEnum(int cid, String name, String dataType) {
        this.cid = cid;
        this.name = name;
        this.dataType = dataType;
    }

    public int getCid() {
        return cid;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

}
