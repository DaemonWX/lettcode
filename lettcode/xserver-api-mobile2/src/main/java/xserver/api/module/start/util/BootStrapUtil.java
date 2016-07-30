package xserver.api.module.start.util;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

import xserver.api.module.start.BootStrapConstant;
import xserver.lib.util.MessageDigestUtil;

public class BootStrapUtil {

    private static Calendar cal = Calendar.getInstance();

    /**
     * 水货手机破解key
     * @param brand
     * @param series
     * @param application
     * @param imei
     * @return
     */
    public static String getCrackKey(String brand, String series, String application, String imei) {
        StringBuffer key = new StringBuffer(BootStrapConstant.CRACK_KEY);

        String sKey = "";
        if (StringUtils.isNotEmpty(brand)) {
            key.append(brand);
        }
        if (StringUtils.isNotEmpty(series)) {
            key.append(series);
        }
        if (StringUtils.isNotEmpty(application)) {
            key.append(application);
        }
        if (StringUtils.isNotEmpty(imei)) {
            key.append(imei);
        }
        key.append(cal.get(Calendar.DAY_OF_YEAR));

        try {
            sKey = MessageDigestUtil.md5((key.toString()).getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sKey;
    }
}
