package xserver.api.module.play.util;

import java.util.HashMap;
import java.util.Map;

import xserver.lib.util.MessageDigestUtil;

public class VODUtil {

    public static final Map<String, String> appMap = new HashMap<String, String>();

    static {
        appMap.put("100", "8Dz&P$UWb^S#PNPw");// 通用播放器
        appMap.put("101", "HrDyWwCx4yZuJJ^t");
        appMap.put("102", "^#Cb9BrEn6l1vXl3");
        appMap.put("103", "OgaC0Cysvjs&hatn");

        appMap.put("3004", "1wUH7H*0bTM$7o$q");// 乐视音乐--乐视音乐app Android
        appMap.put("3005", "1wUH7H*0bTM$7o$q");// 乐视音乐--乐视音乐app iOS

        appMap.put("", "lfuI!2hG@obxbJWq");
    }

    public static boolean checkSig(Long videoId, Long albumId, Long timestamp, String businessId, Integer rand,
            String sig) {
        String md5Str = "";
        boolean valid = false;
        if (videoId != null) {
            md5Str = md5Str + videoId;
        }
        if (albumId != null) {
            md5Str = md5Str + albumId;
        }
        try {
            String commonPlayer = MessageDigestUtil.md5(
                    (md5Str + timestamp + "100" + appMap.get("100")).getBytes("UTF-8")).substring(0, rand);
            String app = MessageDigestUtil.md5(
                    (md5Str + timestamp + businessId + appMap.get(businessId)).getBytes("UTF-8")).substring(rand);

            String validSig = commonPlayer + app;
            valid = validSig.equalsIgnoreCase(sig);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return valid;
    }

}
