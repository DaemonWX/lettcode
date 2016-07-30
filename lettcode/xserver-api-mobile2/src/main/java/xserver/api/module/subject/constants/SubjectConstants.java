package xserver.api.module.subject.constants;

import java.util.HashMap;
import java.util.Map;

public class SubjectConstants {

    public static final String VOTE_PREFIX = "1000004_1_";// 投票、点赞，前缀
    public static final String SUBJECT_PACKAGE_TYPE_ALBUM = "1";// 专题，专辑包
    public static final String SUBJECT_PACKAGE_TYPE_VIDEO = "2";// 专题，视频包
    public static final String SUBJECT_PACKAGE_TYPE_LIVE = "3";// 专题，直播包

    public static final String ALL_SUBJECT_CMS_BLOCK_ID = "8081"; //test
//    public static final String ALL_SUBJECT_CMS_BLOCK_ID = "4392";

    public static final Map<String, Integer> sortMap = new HashMap<>();
    static {
        // 由此控制native专题模块顺序：专题名称-头图-导语-直播-活动-专辑或者视频-明星-音乐-关注
        sortMap.put("52", 1);// 直播模块
        sortMap.put("54", 2);// 活动模块
        sortMap.put("30", 3);// 视频、专辑模块
        sortMap.put("48", 4);// 明星模块
        sortMap.put("50", 5);// 音乐模块
        sortMap.put("49", 6);// 乐词关注模块
        sortMap.put("51", 7);//壁纸模块

    }
}
