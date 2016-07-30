package xserver.api.module.vip.builder;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import xserver.api.module.vip.VipConstants;
import xserver.api.module.vip.dto.VipPackageDto;
import xserver.api.module.vip.util.VipUtil;
import xserver.lib.tp.vip.VipTpConstant;
import xserver.lib.tp.vip.response.VipPackageListTpResponse.VipPackageInfo;

/**
 * 会员套餐包转换类，用于将VipPackageInfo转换为VipPackageDto
 * @author
 */
public class VipPackageBuilder {

    /**
     * 将vipPackageTp转换为VipPackageDto
     * final Locale locale
     * @param vipPackageTp
     * @param locale
     *            语言设置，用于文案获取
     * @param terminal
     *            套餐适用终端，用于数据过滤
     * @return
     */
    public static VipPackageDto build(final VipPackageInfo vipPackageTp, final Integer vipType) {
        VipPackageDto vipPackageDto = null;
        if (isVipPackageAvailable(vipPackageTp, vipType)) {
            vipPackageDto = new VipPackageDto();
            vipPackageDto.setId(vipPackageTp.getMonthType());
            vipPackageDto.setVipType(vipPackageTp.getEnd());
            // 注意直接取Boss字段，不能保证文案的可国际化
            vipPackageDto.setPackageName(vipPackageTp.getName());

            if (vipPackageTp.getCurrentPrice() != null) {
                vipPackageDto.setCurrentPrice(vipPackageTp.getCurrentPrice());
            } else {
                vipPackageDto.setCurrentPrice(vipPackageTp.getOriginPrice());
            }
            vipPackageDto.setOriginPrice(vipPackageTp.getOriginPrice());
            vipPackageDto.setUseCurrentPrice(vipPackageTp.getUseCurrentPrice());
            if (vipPackageTp.getDays() != null) {
                vipPackageDto
                        .setDuration(String.valueOf(VipUtil.parseDayToMonth(vipPackageTp.getDays().intValue(), 31)));
//                vipPackageDto.setPackageText(VipConstants.PACKAGENAME_MAP.get(vipPackageDto.getDuration()));
                // 如果是连续包月，需要单独设置文案
                if(VipTpConstant.ORDER_TYPE_42 == Integer.parseInt(vipPackageTp.getMonthType())
                	|| VipTpConstant.ORDER_TYPE_52 == Integer.parseInt(vipPackageTp.getMonthType())){
                    vipPackageDto.setPackageText(VipConstants.MONTHLY_PACKAGE_INFO);
                    vipPackageDto.setAutoPayInfo(VipConstants.AUTO_PAY_MONTHLY_INFO);
                }else {
                    vipPackageDto.setPackageText(VipConstants.PACKAGENAME_MAP.get(vipPackageDto.getDuration()));
                }
            }
            vipPackageDto.setDiscount(getDiscount(vipPackageDto.getCurrentPrice(), vipPackageDto.getOriginPrice()));
            vipPackageDto.setVipDesc(vipPackageTp.getVipDesc());
            vipPackageDto.setMobileImg(vipPackageTp.getMobileImg());
            vipPackageDto.setSuperMobileImg(vipPackageTp.getSuperMobileImg());
        }
        return vipPackageDto;
    }

    /**
     * 判断vipPackageTp是否可解析
     * @param vipPackageTp
     * @param locale
     * @param vipType
     * @return
     */
    private static boolean isVipPackageAvailable(VipPackageInfo vipPackageTp, Integer vipType) {
        if (vipPackageTp == null || vipType == null) {
            return false;
        }

        // String terminal = VipTpConstantUtils.getTermialByVipType(vipType);
        if (CollectionUtils.isEmpty(vipPackageTp.getTerminals())
                || !vipPackageTp.getTerminals().contains(VipTpConstant.PRICE_ZHIFU_TERMINAL_MOBILE)) {
            return false;
        }

        return true;
    }

    private static String getDiscount(String currentPrice, String originPrice) {
        if (StringUtils.isBlank(currentPrice) || StringUtils.isBlank(originPrice)) {
            return null;
        }
        try {
            return String.format("%.1f", Float.parseFloat(currentPrice) * 10.0f / Float.parseFloat(originPrice));
        } catch (NumberFormatException e) {
            // e.printStackTrace();
            return null;
        }
        /*
        if (format != null && format.endsWith("0")) {
            format = format.substring(0, format.indexOf("."));
        }
        */
    }

    /**
     * 判断两个版本号之间的大小
     * @param version1
     * @param version2
     * @return
     */
    public static boolean compareVersion(String version1, String version2) {
        int temp1 = 0, temp2 = 0;
        int len1 = version1.length(), len2 = version2.length();
        int i = 0,j = 0;
        while(i < len1 || j < len2) {
            temp1 = 0;
            temp2 = 0;
            
            while(i < len1 && version1.charAt(i) != '.') {
                temp1 = temp1 * 10 + version1.charAt(i++) - '0';

            }
            
            while(j < len2 && version2.charAt(j) != '.') {
                temp2 = temp2 * 10 + version2.charAt(j++) - '0';

            }
            
            if(temp1 > temp2) return false;
            else if(temp1 < temp2) return true;
            else {
                i++;
                j++;
            }
        }
        return true;
    }
}
