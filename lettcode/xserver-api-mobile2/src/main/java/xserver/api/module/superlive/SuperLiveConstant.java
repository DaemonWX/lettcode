package xserver.api.module.superlive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import xserver.api.module.superlive.dto.SuperLiveCategoryDto;
import xserver.lib.constant.ApplicationConstants;
import xserver.lib.util.ApplicationUtils;

public class SuperLiveConstant {
    public static final String LUNBO_SOURCE = "7";
    public static final String HOT_CATEGORY_CMS_BLOCK_ID = "2951";// 超级Live推荐台
    public static final String SUPERLIVE_VOTE_BLOCK_ID = "3582"; // 超级live直播投票版块
    public static final String WEISHI_SOURCE = "2";
    public static final String LIVE_TYPE_LIVEROOM = "2";
    public static final String LIVE_TYPE_CHANNEL_LUNBO = "3";
    public static final String LIVE_TYPE_CHANNEL_WEISHI = "4";
    public static final String LIVE_ROOM_SPORTS = "sports";
    public static final String LIVE_ROOM_ENTERTAINMENT = "entertainment";
    public static final String LIVE_ROOM_MUSIC = "music";
    public static final String LIVE_ROOM_OTHER = "other";
    public static final String LIVE_ROOM_INFORMATION = "information";
    public static final String LIVE_ROOM_GAME = "game";
    public static final String LIVE_ROOM_BRAND = "brand";
    public static final int SUPERLIVE_CATEGORY_DATA_DEF_SIZE = 6;
    public static final String SUPERLIVE_CLIENTID = ApplicationUtils.get(ApplicationConstants.LIVE_CLIENTID);
    public static final String SPUERLIVE_BELONG_AREA = ApplicationUtils.get(ApplicationConstants.LIVE_BELONGAREA);// 大陆
    public static final SuperLiveCategoryDto SUPERLIVE_CATEGORY_REC = new SuperLiveCategoryDto("000", "推荐");
    public static final SuperLiveCategoryDto SUPERLIVE_CATEGORY_FOCUS = new SuperLiveCategoryDto("003", "焦点");
    public static final SuperLiveCategoryDto SUPERLIVE_CATEGORY_LIVE = new SuperLiveCategoryDto("001", "直播");
    public static final SuperLiveCategoryDto SUPERLIVE_CATEGORY_OTHER = new SuperLiveCategoryDto("790", "更多");
    private static final Map<String, String[]> LIVE_CATEGORY_PIC_AND_COLOR = new HashMap<String, String[]>();
    public static final String LIVECHANNEL_ALL = "1";
    public static final Map<String, String> DEFAULT_LIVE_CHANNELS = new LinkedHashMap<String, String>();
    public static final Map<String, String> DEFAULT_LIVE_INDEX_CHANNELS = new LinkedHashMap<String, String>();

    public static final String SUPERLIVE_SHARE_URL_REPLACE = "{REPLACE_CONTENT}";
    public static final String SUPERLIVE_SHARE_URL_DEFAULT = "http://m.letv.com/live";
    // public static final String SUPERLIVE_SHARE_URL_LIVE =
    // "http://m.letv.com/live/list/channel/{REPLACE_CONTENT}/";
    public static final String SUPERLIVE_SHARE_URL_LUNBO = "http://m.letv.com/live/play?channel="
            + SUPERLIVE_SHARE_URL_REPLACE + "id&ename=lunbo";
    public static final String SUPERLIVE_SHARE_URL_WEISHI = "http://m.letv.com/live/play?channel="
            + SUPERLIVE_SHARE_URL_REPLACE + "id&ename=weishi";

    public static final Map<String, String> SUPERLIVE_SHARE_LIVE_MAP = new HashMap<String, String>();

    static {

        SUPERLIVE_SHARE_LIVE_MAP.put("sports", "http://m.letv.com/live/play_sports.html?id="
                + SUPERLIVE_SHARE_URL_REPLACE);
        SUPERLIVE_SHARE_LIVE_MAP.put("music", "http://m.letv.com/live/play_music.html?id="
                + SUPERLIVE_SHARE_URL_REPLACE);
        SUPERLIVE_SHARE_LIVE_MAP.put("entertainment", "http://m.letv.com/live/play_ent.html?id="
                + SUPERLIVE_SHARE_URL_REPLACE);
        SUPERLIVE_SHARE_LIVE_MAP.put("other", "http://m.letv.com/live/play_other.html?id="
                + SUPERLIVE_SHARE_URL_REPLACE);

        // 明星
        DEFAULT_LIVE_INDEX_CHANNELS.put("725", "620-575-537-857-743-658-366");
        // 卫视
        DEFAULT_LIVE_INDEX_CHANNELS.put("220", "458-463-86-469-474-467");
        // 央视
        DEFAULT_LIVE_INDEX_CHANNELS.put("210", null);
        // 地方
        DEFAULT_LIVE_INDEX_CHANNELS.put("230", null);
        // 体育
        DEFAULT_LIVE_INDEX_CHANNELS.put("740", "227-678-774-392-272-369");
        // 电影
        DEFAULT_LIVE_INDEX_CHANNELS.put("710", "224-317-352-365-378-380");
        // 电视剧
        DEFAULT_LIVE_INDEX_CHANNELS.put("720", "225-249-262-719-732-724");
        // 综艺
        DEFAULT_LIVE_INDEX_CHANNELS.put("760", "248-541-876-670-299-308");
        // 动漫
        DEFAULT_LIVE_INDEX_CHANNELS.put("750", "226-315-650-652-268-716");
        // 音乐
        DEFAULT_LIVE_INDEX_CHANNELS.put("770", "235-282-295-304-353-357");
        // 综合
        DEFAULT_LIVE_INDEX_CHANNELS.put("730", "71-349-449");
        // 纪录片
        DEFAULT_LIVE_INDEX_CHANNELS.put("765", "228-372-745-776-780-777");
        // 娱乐
        DEFAULT_LIVE_INDEX_CHANNELS.put("780", "290-302-342-593-856");

        DEFAULT_LIVE_CHANNELS
                .put("725",
                        "575-537-857-743-658-366-874-875-689-695-447-898-685-920-873-687-688-897-868-867-912-911-862-665-871-914-686-692-696-697-820-825-878-896-817-823-826-833-835-839-845-842-838-893-837-858-683-684-693-694-827-828-829-830-831-832-818-819-824-822-834-836-840-841-843-844-846-847-859-860-861-863-864-865-866-869-870-872-879-880-881-882-883-884-885-886-887-888-889-890-891-892-895-894");
        DEFAULT_LIVE_CHANNELS.put("220",
                "458-463-86-469-474-467-100-96-97-465-470-171-102-103-104-170-105-114-116-118-119-475-476");
        // 央视
        DEFAULT_LIVE_CHANNELS.put("210", null);
        // 地方
        DEFAULT_LIVE_CHANNELS.put("230", null);
        DEFAULT_LIVE_CHANNELS.put("740", "227-678-774-392-272-369-676-677-289-256-255-547");
        DEFAULT_LIVE_CHANNELS.put("710", "224-317-352-365-378-380-230-247-631-760-761-762");
        DEFAULT_LIVE_CHANNELS
                .put("720",
                        "225-249-262-719-732-724-381-506-720-589-723-730-725-726-727-265-600-637-662-625-671-254-789-368-722-633-636-663-664-681-721-690-691-728-729-734-542-622-550-570-735-736-437-737-738-739-740-741-742-744-746-747-748-749-750-751-752-753-754-755-756-757-758-791-793-813-797-799-801-802-804-806-808-795-809-811-815");
        DEFAULT_LIVE_CHANNELS.put("760", "248-541-876-670-299-308-360-385-702-698-699-700-701-703-704-909");
        DEFAULT_LIVE_CHANNELS.put("750",
                "226-315-650-652-268-716-792-707-706-388-496-790-705-708-709-710-711-712-713-714-715-717-718-787");
        DEFAULT_LIVE_CHANNELS
                .put("770",
                        "235-282-295-304-353-357-812-850-851-848-849-852-853-632-599-635-759-794-796-798-800-803-805-807-810-814-816-306");

        DEFAULT_LIVE_CHANNELS.put("730", "71-349-449");
        DEFAULT_LIVE_CHANNELS.put("765", "228-372-745-776-780-777-779-781-782-778");
        DEFAULT_LIVE_CHANNELS.put("780", "290-302-342-593-856");

        LIVE_CATEGORY_PIC_AND_COLOR.put("000", new String[] {
                "http://i0.letvimg.com/lc03_tdv/201504/04/12/13/tuijian.png", "#c54242" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("003", new String[] {
                "http://i0.letvimg.com/lc01_webtv/201504/28/18/55/1430218554197.png", "#EF4444" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("001", new String[] {
                "http://i1.letvimg.com/lc03_tdv/201504/03/19/39/zhibo.png", "#716fdb" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("002", new String[] {
                "http://i1.letvimg.com/lc03_tdv/201504/04/12/14/wode.png", "#1b8ec3" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("740", new String[] {
                "http://i3.letvimg.com/lc03_tdv/201504/04/12/13/tiyu.png", "#bf7139" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("750", new String[] {
                "http://i3.letvimg.com/lc01_tdv/201504/04/12/12/dongman.png", "#716fdb" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("780", new String[] {
                "http://i1.letvimg.com/lc01_tdv/201504/04/12/16/yule.png", "#1b8ec3" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("770", new String[] {
                "http://i1.letvimg.com/lc03_tdv/201504/04/12/16/yinyue.png", "#c54242" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("210", new String[] {
                "http://i3.letvimg.com/lc02_tdv/201504/04/12/15/yangshi.png", "#c54242" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("760", new String[] {
                "http://i0.letvimg.com/lc03_tdv/201504/04/12/17/zongyi.png", "#1b8ec3" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("765", new String[] {
                "http://i3.letvimg.com/lc02_tdv/201504/03/19/40/jilupian.png", "#bf7139" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("710", new String[] {
                "http://i1.letvimg.com/lc03_tdv/201504/04/12/11/dianying.png", "#1c8666" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("730", new String[] {
                "http://i0.letvimg.com/lc03_tdv/201504/04/12/16/zonghe.png", "#716fdb" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("725", new String[] {
                "http://i2.letvimg.com/lc03_tdv/201504/04/12/12/mingxing.png", "#bf7139" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("720", new String[] {
                "http://i3.letvimg.com/lc03_tdv/201504/04/12/10/dianshiju.png", "#c54242" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("230", new String[] {
                "http://i1.letvimg.com/lc03_tdv/201504/04/12/11/difang.png", "#1c8666" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("220", new String[] {
                "http://i1.letvimg.com/lc02_tdv/201504/04/12/14/weishi.png", "#1b8ec3" });
        LIVE_CATEGORY_PIC_AND_COLOR.put("XXX", new String[] {
                "http://i1.letvimg.com/lc01_tdv/201504/04/12/16/yule.png", "#1b8ec3" });
        String[] pc = getLiveCategoryPicAndColor(SUPERLIVE_CATEGORY_FOCUS.getCode());
        SUPERLIVE_CATEGORY_FOCUS.setCategoryPic(pc[0]);
        SUPERLIVE_CATEGORY_FOCUS.setColor(pc[1]);

    }

    public static final ArrayList<String> SUPERLIVE_CATEGORY_LIST = new ArrayList<String>();
    static {
        // SUPERLIVE_CATEGORY_LIST.add("我的");
        // SUPERLIVE_CATEGORY_LIST.add("直播");
        SUPERLIVE_CATEGORY_LIST.add("明星");
        SUPERLIVE_CATEGORY_LIST.add("卫视");
        SUPERLIVE_CATEGORY_LIST.add("央视");
        SUPERLIVE_CATEGORY_LIST.add("地方");
        SUPERLIVE_CATEGORY_LIST.add("体育");
        SUPERLIVE_CATEGORY_LIST.add("电影");
        SUPERLIVE_CATEGORY_LIST.add("电视剧");
        SUPERLIVE_CATEGORY_LIST.add("综艺");
        SUPERLIVE_CATEGORY_LIST.add("动漫");
        SUPERLIVE_CATEGORY_LIST.add("音乐");
        SUPERLIVE_CATEGORY_LIST.add("综合");
        SUPERLIVE_CATEGORY_LIST.add("纪录片");
        SUPERLIVE_CATEGORY_LIST.add("娱乐");
    }
    public static final Map<String, String> MUSIC_TYPE = new LinkedHashMap<String, String>();
    static {
        MUSIC_TYPE.put("1", "颁奖礼");
        MUSIC_TYPE.put("2", "演唱会");
        MUSIC_TYPE.put("3", "音乐会");
        MUSIC_TYPE.put("4", "音乐节");
    }

    public static final Map<String, String> ENT_TYPE = new LinkedHashMap<String, String>();
    static {
        ENT_TYPE.put("101", "颁奖礼");
        ENT_TYPE.put("102", "电影节");
        ENT_TYPE.put("103", "发布会");
        ENT_TYPE.put("104", "首映式");
        ENT_TYPE.put("105", "脱口秀");
        ENT_TYPE.put("106", "演唱会");
        ENT_TYPE.put("107", "电视节目");
    }

    public static final Map<String, String> BRAND_TYPE = new LinkedHashMap<String, String>();

    static {
        BRAND_TYPE.put("101", "颁奖礼");
        BRAND_TYPE.put("102", "电影节");
        BRAND_TYPE.put("103", "时尚");
        BRAND_TYPE.put("104", "汽车");
        BRAND_TYPE.put("105", "生活");
        BRAND_TYPE.put("106", "财经");
        BRAND_TYPE.put("107", "游戏");
        BRAND_TYPE.put("108", "健康");
        BRAND_TYPE.put("109", "IT");
        BRAND_TYPE.put("110", "教育");
        BRAND_TYPE.put("111", "电商");
        BRAND_TYPE.put("112", "其他");
    }

    public static final Map<String, String> OTHER_TYPE = new LinkedHashMap<String, String>();

    static {
        OTHER_TYPE.put("1", "电视节目");
    }

    public static final String[] getLiveCategoryPicAndColor(String code) {
        String[] pc = LIVE_CATEGORY_PIC_AND_COLOR.get(code);
        if (pc == null) {
            pc = LIVE_CATEGORY_PIC_AND_COLOR.get("XXX");
        }
        return pc;
    }

    public static final Map<String, String> CUSTOMIZE_STARS = new HashMap<String, String>(); // 明星手机列表
    static {
        CUSTOMIZE_STARS.put("866479020013362", "688");
        CUSTOMIZE_STARS.put("866479020049903", "822");
        CUSTOMIZE_STARS.put("866479020050554", "829");
        // CUSTOMIZE_STARS.put("866479020054986", "818");
        // CUSTOMIZE_STARS.put("866479020059985", "841");
        CUSTOMIZE_STARS.put("866479020069703", "1136");
        // CUSTOMIZE_STARS.put("866479020072111", "843");
        CUSTOMIZE_STARS.put("866479020077656", "658");
        CUSTOMIZE_STARS.put("866479020090659", "912");
        // CUSTOMIZE_STARS.put("866479020092655", "830");
        CUSTOMIZE_STARS.put("866479020101290", "898");
        // CUSTOMIZE_STARS.put("866479020082953", "870");
        CUSTOMIZE_STARS.put("866479020088182", "366");
        // CUSTOMIZE_STARS.put("866479020088612", "823");
        CUSTOMIZE_STARS.put("866479020160601", "834");
        CUSTOMIZE_STARS.put("866479020165709", "872");
        CUSTOMIZE_STARS.put("866479020166194", "575");
        CUSTOMIZE_STARS.put("866479020065602", "575");
        CUSTOMIZE_STARS.put("866479020167945", "864");
        // CUSTOMIZE_STARS.put("866479020175229", "860");
        CUSTOMIZE_STARS.put("866479020175351", "866");
        // CUSTOMIZE_STARS.put("866479020176698", "827");
        CUSTOMIZE_STARS.put("866479020056379", "863");
        CUSTOMIZE_STARS.put("866479020070149", "836");
        CUSTOMIZE_STARS.put("866479020070842", "743");
        CUSTOMIZE_STARS.put("866479020075049", "832");
        CUSTOMIZE_STARS.put("866479020083258", "689");
        CUSTOMIZE_STARS.put("866479020140017", "820");
        // CUSTOMIZE_STARS.put("866479020168604", "861");
        CUSTOMIZE_STARS.put("866479020172366", "838");
        CUSTOMIZE_STARS.put("866479020172440", "847");
        CUSTOMIZE_STARS.put("866479020175948", "665");
        CUSTOMIZE_STARS.put("866479020076534", "893");
        CUSTOMIZE_STARS.put("866479020088570", "447");
        CUSTOMIZE_STARS.put("866479020154430", "828");
        CUSTOMIZE_STARS.put("866479020163225", "833");
        CUSTOMIZE_STARS.put("866479020165915", "920");
        CUSTOMIZE_STARS.put("866479020166772", "688");
        CUSTOMIZE_STARS.put("866479020167150", "686");
        CUSTOMIZE_STARS.put("866479020168893", "683");
        // CUSTOMIZE_STARS.put("866479020178827", "889");
        CUSTOMIZE_STARS.put("866479020178983", "862");
        // CUSTOMIZE_STARS.put("866479020053210", "883");
        CUSTOMIZE_STARS.put("866479020083449", "865");
        CUSTOMIZE_STARS.put("866479020086715", "879");
        CUSTOMIZE_STARS.put("866479020161286", "824");
        // CUSTOMIZE_STARS.put("866479020161948", "840");
        // CUSTOMIZE_STARS.put("866479020162078", "837");
        CUSTOMIZE_STARS.put("866479020168166", "858");
        CUSTOMIZE_STARS.put("866479020172101", "911");
        // CUSTOMIZE_STARS.put("866479020173315", "880");
        CUSTOMIZE_STARS.put("866479020179122", "871");
        CUSTOMIZE_STARS.put("866479020077789", "874");
        CUSTOMIZE_STARS.put("866479020081641", "842");
        // CUSTOMIZE_STARS.put("866479020082870", "869");
        CUSTOMIZE_STARS.put("866479020101332", "859");
        CUSTOMIZE_STARS.put("866479020103791", "875");
        CUSTOMIZE_STARS.put("866479020104195", "868");
        // CUSTOMIZE_STARS.put("866479020105341", "895");
        CUSTOMIZE_STARS.put("866479020105424", "857");
        CUSTOMIZE_STARS.put("866479020110077", "878");
        CUSTOMIZE_STARS.put("866479020128186", "914");
        // CUSTOMIZE_STARS.put("866479020069737", "892");
        CUSTOMIZE_STARS.put("866479020070685", "888");
        CUSTOMIZE_STARS.put("866479020071857", "882");
        CUSTOMIZE_STARS.put("866479020073598", "897");
        CUSTOMIZE_STARS.put("866479020074596", "896");
        CUSTOMIZE_STARS.put("866479020075684", "825");
        // CUSTOMIZE_STARS.put("866479020076872", "819");
        // CUSTOMIZE_STARS.put("866479020086749", "887");
        CUSTOMIZE_STARS.put("866479020087978", "867");
        CUSTOMIZE_STARS.put("866479020089008", "826");
        CUSTOMIZE_STARS.put("866479020156435", "537");
        CUSTOMIZE_STARS.put("866479020167499", "687");
        CUSTOMIZE_STARS.put("866479020172275", "886");
        CUSTOMIZE_STARS.put("866479020172754", "846");
        // CUSTOMIZE_STARS.put("866479020173588", "844");
        CUSTOMIZE_STARS.put("866479020174693", "831");
        CUSTOMIZE_STARS.put("866479020175344", "839");
        // CUSTOMIZE_STARS.put("866479020175450", "873");
        CUSTOMIZE_STARS.put("866479020176201", "885");
        CUSTOMIZE_STARS.put("866479020177134", "845");

        CUSTOMIZE_STARS.put("866479020069919", "1074");
        CUSTOMIZE_STARS.put("866479020076666", "1113");
        CUSTOMIZE_STARS.put("866479020078142", "1037");
        CUSTOMIZE_STARS.put("866479020079264", "1070");
        CUSTOMIZE_STARS.put("866479020082656", "1036");
        CUSTOMIZE_STARS.put("866479020083266", "1045");
        CUSTOMIZE_STARS.put("866479020085188", "1043");
        CUSTOMIZE_STARS.put("866479020085337", "1118");
        CUSTOMIZE_STARS.put("866479020087721", "1031");
        CUSTOMIZE_STARS.put("866479020091269", "1030");
        CUSTOMIZE_STARS.put("866479020069034", "1055");
        CUSTOMIZE_STARS.put("866479020070701", "1038");
        CUSTOMIZE_STARS.put("866479020074927", "1082");
        CUSTOMIZE_STARS.put("866479020163696", "1087");
        CUSTOMIZE_STARS.put("866479020167820", "905");
        CUSTOMIZE_STARS.put("866479020171806", "1020");
        CUSTOMIZE_STARS.put("866479020172143", "1049");
        CUSTOMIZE_STARS.put("866479020175005", "1080");
        CUSTOMIZE_STARS.put("866479020178603", "1125");
        CUSTOMIZE_STARS.put("866479020178710", "1028");
        CUSTOMIZE_STARS.put("866479020049820", "1032");
        CUSTOMIZE_STARS.put("866479020051123", "1085");
        CUSTOMIZE_STARS.put("866479020052733", "1106");
        CUSTOMIZE_STARS.put("866479020057302", "1042");
        CUSTOMIZE_STARS.put("866479020057864", "1018");
        CUSTOMIZE_STARS.put("866479020059290", "1041");
        CUSTOMIZE_STARS.put("866479020061148", "1119");
        CUSTOMIZE_STARS.put("866479020094115", "1091");
        CUSTOMIZE_STARS.put("866479020095302", "1065");
        CUSTOMIZE_STARS.put("866479020095575", "1048");
        CUSTOMIZE_STARS.put("866479020050315", "1088");
        CUSTOMIZE_STARS.put("866479020167846", "1084");
        CUSTOMIZE_STARS.put("866479020173810", "1105");
        CUSTOMIZE_STARS.put("866479020174909", "1057");
        CUSTOMIZE_STARS.put("866479020176425", "1027");
        CUSTOMIZE_STARS.put("866479020176623", "1064");
        CUSTOMIZE_STARS.put("866479020176797", "1086");
        CUSTOMIZE_STARS.put("866479020177100", "1117");
        CUSTOMIZE_STARS.put("866479020177126", "1121");
        CUSTOMIZE_STARS.put("866479020178611", "1044");
        CUSTOMIZE_STARS.put("866479020051156", "1023");
        CUSTOMIZE_STARS.put("866479020071790", "1114");
        CUSTOMIZE_STARS.put("866479020089065", "1093");
        CUSTOMIZE_STARS.put("866479020089917", "1052");
        CUSTOMIZE_STARS.put("866479020093042", "1033");
        CUSTOMIZE_STARS.put("866479020093091", "1107");
        CUSTOMIZE_STARS.put("866479020172416", "1060");
        CUSTOMIZE_STARS.put("866479020173927", "1047");
        CUSTOMIZE_STARS.put("866479020174206", "1035");
        CUSTOMIZE_STARS.put("866479020047881", "1046");
        CUSTOMIZE_STARS.put("866479020137427", "1101");
        CUSTOMIZE_STARS.put("866479020149570", "1115");
        CUSTOMIZE_STARS.put("866479020149810", "1039");
        CUSTOMIZE_STARS.put("866479020153960", "1034");
        CUSTOMIZE_STARS.put("866479020157011", "1099");
        CUSTOMIZE_STARS.put("866479020174701", "1100");
        CUSTOMIZE_STARS.put("866479020176730", "1075");
        CUSTOMIZE_STARS.put("866479020176847", "1094");
        CUSTOMIZE_STARS.put("866479020177290", "1021");
        CUSTOMIZE_STARS.put("866479020070891", "1061");
        CUSTOMIZE_STARS.put("866479020083662", "1090");
        CUSTOMIZE_STARS.put("866479020156229", "1056");
        CUSTOMIZE_STARS.put("866479020171525", "1103");
        CUSTOMIZE_STARS.put("866479020178678", "1014");
        CUSTOMIZE_STARS.put("866479020178694", "1067");
        CUSTOMIZE_STARS.put("866479020178751", "1108");
        CUSTOMIZE_STARS.put("866479020178785", "1015");
        CUSTOMIZE_STARS.put("866479020178793", "1040");
        CUSTOMIZE_STARS.put("866479020049754", "1112");
        CUSTOMIZE_STARS.put("866479020049804", "1054");
        CUSTOMIZE_STARS.put("866479020050638", "1066");
        CUSTOMIZE_STARS.put("866479020051719", "1110");
        CUSTOMIZE_STARS.put("866479020054762", "1017");
        CUSTOMIZE_STARS.put("866479020056890", "1092");
        CUSTOMIZE_STARS.put("866479020061858", "1029");
        CUSTOMIZE_STARS.put("866479020063144", "1050");
        CUSTOMIZE_STARS.put("866479020063466", "1013");
        CUSTOMIZE_STARS.put("866479020066758", "1098");
        CUSTOMIZE_STARS.put("866479020076302", "1019");
        CUSTOMIZE_STARS.put("866479020111893", "1053");
        CUSTOMIZE_STARS.put("866479020160353", "1120");
        CUSTOMIZE_STARS.put("866479020162318", "1122");
        CUSTOMIZE_STARS.put("866479020166426", "1083");
        CUSTOMIZE_STARS.put("866479020167184", "1022");
        CUSTOMIZE_STARS.put("866479020168356", "1059");
        CUSTOMIZE_STARS.put("866479020174305", "1051");
        CUSTOMIZE_STARS.put("866479020174958", "1089");
        CUSTOMIZE_STARS.put("866479020177050", "1109");
        CUSTOMIZE_STARS.put("866479020053541", "1104");
        CUSTOMIZE_STARS.put("866479020053970", "1078");
        CUSTOMIZE_STARS.put("866479020062633", "1097");
        CUSTOMIZE_STARS.put("866479020062690", "1058");
        CUSTOMIZE_STARS.put("866479020062773", "1069");
        CUSTOMIZE_STARS.put("866479020053830", "1062");
        CUSTOMIZE_STARS.put("866479020063078", "1111");
        CUSTOMIZE_STARS.put("866479020064340", "1127");
        CUSTOMIZE_STARS.put("866479020174172", "906");
        CUSTOMIZE_STARS.put("866479020169933", "1012");
        CUSTOMIZE_STARS.put("866479020068093", "933");
        CUSTOMIZE_STARS.put("866479020176227", "828");
        CUSTOMIZE_STARS.put("866479020176789", "828");
        CUSTOMIZE_STARS.put("866479020167796", "687");

    }
}
