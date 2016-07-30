package xserver.api.module.user;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xserver.lib.constant.ApplicationConstants;
import xserver.lib.util.ApplicationUtils;

public class UserConstants {

    /**
     * 用户模块的业务码，1--播放记录，2--收藏
     */
    public static final int PLAY_RECORD = 1;
    public static final int PLAY_FAVORITE = 2;

    /**
     * 播放记录上传来源名称；当前固定文案，后期应当修改为从配置文件读取
     * TODO
     */
    public static final String[] PLAY_RECORD_FROM_NAME = { "", "USER_PLAY_RECORD_FROM_1", "USER_PLAY_RECORD_FROM_2",
            "USER_PLAY_RECORD_FROM_3", "USER_PLAY_RECORD_FROM_4", "USER_PLAY_RECORD_FROM_5" };

    /**
     * 播放记录状态名称；当前固定文案，后期应当修改为从配置文件读取
     * TODO
     */
    public static final String[] PLAY_STATUS_NAME = { "USER_PLAY_RECORD_PLAY_STAUTS_0",
            "USER_PLAY_RECORD_PLAY_STAUTS_1", "USER_PLAY_RECORD_PLAY_STAUTS_2" };

    /**
     * 播放记录文案前缀
     */
    public static final String USER_PLAY_RECORD_PROFIX = "USER_PLAY_RECORD_STATUS_NAME_";

    /**
     * 播放记录上传来源中，超级设备里诶包；当前固定文案，后期应当修改为从配置文件读取
     * TODO
     */
    public static final String[] PLAY_RECORD_FROM_PRODUCT_NAME = { "", "USER_PLAY_RECORD_FROM_PRODUCT_NAME_1",
            "USER_PLAY_RECORD_FROM_PRODUCT_NAME_2", "USER_PLAY_RECORD_FROM_PRODUCT_NAME_3" };

    /**
     * 在播放记录中填充直播、卫视、轮播台信息的业务开关
     */
    public static String PLAY_RECORD_FILL_SUPER_LIVE_SWITCH_ON = "1";
    public static boolean PLAY_RECORD_NEED_FILL_SUPER_LIVE_INFO = StringUtils.equals(
            PLAY_RECORD_FILL_SUPER_LIVE_SWITCH_ON,
            ApplicationUtils.get(ApplicationConstants.USER_PLAY_RECORD_FILL_SUPER_LIVE_SWITCH));

    /**
     * 用户播放记录来源设备硬件型号文案的key值前缀，这里暂只定义超级手机
     */
    public static String USER_PLAY_RECORD_FROM_PRODUCT_NAME_MOBILE = "USER_PLAY_RECORD_FROM_PRODUCT_NAME_3";

    /**
	 * 用户播放记录对应设备名称
	 * X600 乐视超级手机1
	 * X800 乐视超级手机1pro
	 * X900 乐视超级手机Max
	 */
	public static Map<String, String> USER_PLAY_RECORD_PRODUCT = new HashMap<String, String>();
	public static final String LE_1 = "Le_1";
	public static final String LE_1_PRO = "Le_1_Pro";
	public static final String LE_MAX = "Le_Max";

	static {
		USER_PLAY_RECORD_PRODUCT.put(LE_1, "X600");
		USER_PLAY_RECORD_PRODUCT.put(LE_1_PRO, "X800");
		USER_PLAY_RECORD_PRODUCT.put(LE_MAX, "X900");
	}
}
