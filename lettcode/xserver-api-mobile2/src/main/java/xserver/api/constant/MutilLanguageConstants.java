package xserver.api.constant;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import xserver.api.module.CommonParam;

/**
 * 多语言常量类
 * @author dunhongqin
 */
public class MutilLanguageConstants {

    /**
     * 客户端传递的语言参数名称
     */
    public static final String CLIENT_LANGUAGE = "langcode";

    /**
     * 客户端传递的国家参数名称
     */
    public static final String CLIENT_COUNTRY = "wcode";

    /**
     * 默认区域语言
     */
    public static final Locale DEFAULT_LOCALE_ZH_CN = new Locale("zh", "CN");

    /**
     * 获取Locale环境
     * 如果参数错误或者出现异常，默认返回中文简体的Locale
     * @param request
     *            HttpRequest对象
     * @return
     */
    public static Locale getLocale(HttpServletRequest request) {
        if (request != null) {
            String langcode = request.getParameter(CLIENT_LANGUAGE);
            String wcode = request.getParameter(CLIENT_COUNTRY);
            return getLocale(langcode, wcode);
        }

        return new Locale("zh", "CN");
    }

    /**
     * 根据语言langcode和地域wcode获取语言-地域信息封装类Locale
     * @param langcode
     * @param wcode
     * @return
     */
    public static Locale getLocale(String langcode, String wcode) {

        try {
            if (langcode != null && langcode.indexOf("-") > 0) {
                langcode = langcode.replaceAll("-", "_");
            }
            if (langcode != null && langcode.indexOf("_") > 0) {
                String language = langcode.substring(0, langcode.indexOf("_")).toLowerCase();
                String country = langcode.substring(langcode.indexOf("_") + 1).toUpperCase();
                return new Locale(language, country);
            } else {
                return new Locale(langcode.toLowerCase(), wcode.toUpperCase());
            }
        } catch (Exception e) {
        }

        return new Locale("zh", "CN");
    }

    public static Locale getLocale(CommonParam commonParam) {

        if (commonParam == null || StringUtils.isEmpty(commonParam.getLangcode())) {
            return DEFAULT_LOCALE_ZH_CN;
        }
        String langcode = commonParam.getLangcode();
        try {
            if (langcode != null && langcode.indexOf("-") > 0) {
                langcode = langcode.replaceAll("-", "_");
            }
            if (langcode != null && langcode.indexOf("_") > 0) {
                String language = langcode.substring(0, langcode.indexOf("_")).toLowerCase();
                String country = langcode.substring(langcode.indexOf("_") + 1).toUpperCase();
                return new Locale(language, country);
            } else {
                return StringUtils.isEmpty(commonParam.getWcode()) ? DEFAULT_LOCALE_ZH_CN : new Locale(
                        langcode.toLowerCase(), commonParam.getWcode().toUpperCase());
            }
        } catch (Exception e) {
        }

        return DEFAULT_LOCALE_ZH_CN;
    }
}
