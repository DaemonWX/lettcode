package xserver.api.constant;

public class ErrorCodeConstants {
    /**
     * 业务正常状态码
     */
    public static final int RESPONSE_SUC_CODE = 1;
    /**
     * 业务异常状态码
     */
    public static final int RESPONSE_FAIL_CODE = 0;
    /**
     * 获取详情页数据失败[抱歉，已偏离航道，请归位重试。错误：018]
     */
    public static final String DETAIL_PAGE_FAIL = "STV002";

    /**
     * 获取详情页数据为空[暂无内容，错误：017]
     */
    public static final String DETAIL_PAGE_NULL = "STV001";

    /**
     * 下载鉴权失败[抱歉，获取下载资源失败，请火速联系我们。错误：025]
     */
    public static final String VIDEO_DOWNLOAD_AUTHFAIL = "SVP005";

    /**
     * 无可下载视频[未找到可下载资源]
     */
    public static final String VIDEO_DOWNLOAD_NULL = "SVP004";

    /**
     * 无可播放码流[未找到相关视频，请火速联系我们。错误：024]
     */
    public static final String VIDEO_PLAY_NULL = "SVP003";

    /**
     * 验证加密失败[签名错误]
     */
    public static final String VIDEO_SIG_ERROR = "SVP001";

    public static final String VIDEO_CN_PLAY_FORBIDDEN = "SVP006";

    public static final String VIDEO_NOT_CN_PLAY_FORBIDDEN = "SVP007";

    public static final String VIDEO_STREAM_NOT_FOUND = "SVP008";

    /**
     * 用户相关错误码
     */
    public static final String USER_COLLECTION_NULL = "SUC001"; // 收藏追剧为空
    public static final String USER_PLAYLOG_NULL = "SUC002"; // 播放记录为空
    public static final String USER_PLAYLOG_DEL_FALURE = "SUC003"; // 播放记录删除失败
    public static final String USER_NOT_LOGIN = "SUC004"; // 用户未登录
    public static final String USER_NOT_EXIST = "SUC005"; // 用户不存在
    public static final String USER_PASSWORD_ERROR = "SUC006"; // 密码错误
    public static final String USER_TOKEN_EXPIRE = "SUC007"; // 用户token过期
    public static final String USER_TOKEN_FAILURE = "SUC008"; // 用户token失效
    public static final String USER_MUTIL_LOGIN = "SUC009"; // 用户异地登录-"您的账号已在另一台电视设备上登录，请重新进行登录"
    public static final String USER_GET_PLAY_FAVORITE_FAILURE = "SUC010"; // 获取收藏追剧失败
    public static final String USER_ADD_PLAY_FAVORITE_FALIURE = "SUC011"; // 添加追剧或收藏失败
    public static final String USER_CHECK_PLAY_FAVORITE_FAILURE = "SUC012"; // 检查追剧收藏状态失败
    public static final String USER_DELETE_PLAY_FAVORITE_FAILURE = "SUC013"; // 删除追剧和收藏失败
    public static final String USER_GET_PLAY_RECORD_FAILURE = "SUC014"; // 获取播放记录失败
    public static final String USER_DELETE_PLAY_RECORD_FAILURE = "SUC015"; // 删除播放记录失败
    public static final String USER_UPDATE_PLAY_RECORD_FAILURE = "SUC016"; // 更新播放记录失败
    public static final String USER_FREE_VIP_REPEAT = "SUC017"; // 用户或者设备已经领取过会员试用，无法再次领取
    public static final String USER_FREE_VIP_ORDER_EXIST = "SUC018"; // 用户开通过套餐或有过消费记录，无法领取会员试用
    public static final String USER_FREE_VIP_GET_FAILURE = "SUC019"; // 领取会员试用失败
    public static final String USER_LOTTERY_EXPIRE = "SUC020"; // 活动尚未开始或者已经结束
    public static final String USER_CREATE_SSO_QRCODE_FAILURE = "SUC021"; // 创建会员中心认证URL失败
    public static final String USER_CHECK_DIRECTIONAL_PUSH_FAILURE = "SUC022"; // （检查当前用户是否符合某定点投放活动条件）定点投放用户查询失败
    public static final String USER_LOGIN_FAILURE = "SUC023"; // 登录到用户中心失败
    public static final String USER_UPDATE_LOGIN_INFO_FAILURE = "SUC024"; // 更新用户登录信息失败
    public static final String USER_ILLEGAL_PARAMETER = "SUC031"; // 请求参数不合法

    /**
     * 支付付费相关错误码
     */
    public static final String PAY_GET_VIP_PACKAGE_LIST_FAILURE = "SPC001"; // 套餐信息获取失败
    public static final String PAY_GET_QRCODE_FAILURE = "SPC002"; // 支付获取二维码失败
    public static final String PAY_COMMIT_ORDER_FAILURE = "SPC003"; // 提交订单失败
    public static final String PAY_FAILURE = "SPC004"; // 支付失败
    public static final String PAY_VIPINFO_SYNC_FAILURE = "SPC005"; // 会员套餐信息同步失败
    public static final String PAY_GET_CONSUMPTION_RECORD_FAILURE = "SPC006"; // 获取消费记录失败
    public static final String PAY_CONSUMPTION_RECORD_NULL = "SPC007"; // 消费记录为空
    public static final String PAY_USER_ORDER_NULL = "SPC008"; // 订单记录为空
    public static final String PAY_ORDER_FAILURE = "SPC009"; // 获取订单失败
    public static final String PAY_GET_RECHARGE_RECORD_FAILURE = "SPC010"; // 获取充值记录失败
    public static final String PAY_RECHARGE_RECORD_NULL = "SPC011"; // 充值记录为空
    public static final String PAY_GET_PRICE_PARCKAGE_LIST_FAILURE = "SPC012"; // 获取产品包列表失败
    public static final String PAY_GET_VIP_PACKAGE_LIST_EMPTY = "SPC013"; // 产品包列表为空
    public static final String PAY_ILLEGAL_PRODUCT_TYPE = "SPC014"; // 不合法的产品类型
    public static final String PAY_GET_PURCHASE_ORDER_ID_FAILURE = "SPC015"; // 获取订单号失败
    public static final String PAY_GET_ALI_PAYCODE_FAILURE = "SPC016"; // 获取阿里支付二维码失败
    public static final String PAY_GET_WX_PAYCODE_FALIURE = "SPC017"; // 获取微信支付二维码失败
    public static final String PAY_ILLEGATE_PAYMENT_CHANNEL = "SPC018"; // 非法的支付方式
    public static final String PAY_PURCHASE_VIP_PHONE_PAY_FAILURE = "SPC019"; // 手机支付失败
    public static final String PAY_LAKALA_PAY_FAILURE = "SPC020"; // 拉卡拉支付失败
    public static final String PAY_CHECK_PHONE_FAILURE = "SPC021"; // 检查手机套餐信息失败
    public static final String PAY_CHECK_USER_ORDER_FAILURE = "SPC022"; // 获取订单详情失败
    public static final String PAY_CONSUME_VIP_BY_LETVPOINT_FAILURE = "SPC023"; // 乐点开通VIP失败
    public static final String PAY_GET_PAYMENT_ACTIVITY_FAILURE = "SPC024"; // 获取付费活动信息失败
    public static final String PAY_GET_LETV_POINT_FAILURE = "SPC025"; // 查询乐点失败
    public static final String PAY_GET_DEVICE_BIND_FAILURE = "SPC026"; // 查询机卡绑定套餐信息失败
    public static final String PAY_ORDER_PAYCODE_ILLEGAL_PARAMETER = "SPC027"; // 单点支付请求参数不合法
    public static final String PAY_ORDER_PAYCODE_ALI_FAILURE = "SPC028"; // 单点支付--获取支付宝二维码图片失败
    public static final String PAY_ORDER_PAYCODE_WEIXIN_FAILURE = "SPC029"; // 单点支付--获取微信二维码图片失败
    public static final String PAY_ORDER_PAYCODE_LAKALA_FAILURE = "SPC030"; // 单点支付--获取拉卡拉二维码图片失败
    public static final String PAY_ILLEGAL_PARAMETER = "SPC031"; // 请求参数不合法
    public static final String PAY_LETVPOINT_NOT_SUPPORT_919 = "SPC032"; // 919活动当天不接收乐点支付，敬请谅解
    public static final String PAY_GET_PRICE_PARCKAGE_FAILURE = "SPC033"; // 获取（单个）产品包失败
    public static final String PAY_LETVPOINT_AVAILABLE_INSUFFICIENT = "SPC034"; // 可用乐点不足
    public static final String PAY_PURCHASE_VIP_ALI_APP_PAY_FAILURE = "SPC035"; // 支付宝支付失败（升级后的收银台）
    public static final String PAY_PURCHASE_VIP_WEIXIN_APP_PAY_FAILURE = "SPC036"; // 微信支付失败（升级后的收银台）
    public static final String PAY_PURCHASE_VIP_LAKALA_PAY_FAILURE = "SPC037"; // 拉卡拉支付失败（升级后的收银台）
    public static final String PAY_PURCHASE_VIP_LETVPOINT_PAY_FAILURE = "SPC038"; // 乐点支付失败（升级后的收银台）
    public static final String PAY_CHECK_VOUCHER_STATUS_FAILURE = "SPC039"; // 校验代金券状态失败
    public static final String PAY_VOUCHER_NOT_AVAILABLE = "SPC040"; // 代金券不可用
    public static final String PAY_VOUCHER_NOT_APPLICATIVE = "SPC041"; // 代金券不适用
    public static final String PAY_GET_USER_AGGREMENT_FAILURE = "SPC042"; // 获取收银台用户协议文案失败
    public static final String PAY_CHECK_LIVE_FAILURE = "SPC043"; // 直播鉴权失败
    public static final String PAY_CHECK_ONE_KEY_PAY_FAILURE = "SPC044"; // 一键支付绑定查询失败
    public static final String PAY_PURCHASE_VIP_PAYPAL_PAY_FAILURE = "SPC045"; // paypal二维码生成失败
    public static final String PAY_PURCHASE_VIP_ONE_KEY_QUICK_PAY_FAILURE = "SPC046"; // 一键支付请求失败
    public static final String PAY_GET_MOVIE_TICKET_LIST_FAILURE = "SPC047"; // 获取观影券失败
    public static final String PAY_RECEIVE_PRESENT_DEVICE_BIND_FAILURE = "SPC048"; // 领取超级手机赠送会员时长失败
    public static final String PAY_GET_PRESENT_DEVICE_BIND_FAILURE = "SPC049"; // 查询超级手机赠送会员时长失败
    public static final String PAY_RECEIVE_DEVICE_BIND_FAILURE = "SPC050"; // 在超级手机上领取赠送会员时长失败
    public static final String PAY_ACTIVE_MOVIE_TICKET_FAILURE = "SPC051"; // 使用观影券失败
    public static final String PAY_GET_VIP_ACCOUNT_INFO_FAILURE = "SPC052"; // 获取会员账户信息失败
    public static final String PAY_GET_USER_LIVE_TICKET_FAILURE = "SPC053"; // 查询用户直播券失败
    public static final String PAY_ACTIVE_LIVE_TICKET_FAILURE = "SPC054"; // 激活用户直播券失败
    public static final String PAY_GET_LIVE_TICKETINFO_FAILURE = "SPC055"; // 查询用户直播券详细信息失败
    /**
     * 需要由产品最终确认错误友及错误方案
     */
    public static final String CLOSE_SERVER_TP = "SPC046"; // 临时码
    /**
     * 需要由产品最终确认错误友及错误方案
     */
    public static final String EXCEPION_SERVER_TP = "SPC047"; // 临时码
    /**
     * 需要由产品最终确认错误友及错误方案
     */
    public static final String CLOSE_SERVER_TP_SUBJECT = "SPC048"; // 临时码
    /**
     * 需要由产品最终确认错误友及错误方案
     */
    public static final String USER_VIDEO_VOTE_DONE = "VVC001"; // 临时码
    public static final String USER_VIDEO_VOTE_COMMIT = "VVC002"; // 临时码
    /**
     * 需要由产品最终确认错误友及错误方案
     */
    public static final String USER_VIDEO_VOTE_GET = "SPC048"; // 临时码
    /**
     * 预约相关错误码
     */
    public static final String Appointment_ADD_FAIL_1 = "AAF001"; // 临时码
    public static final String Appointment_ADD_FAIL_2 = "AAF002"; // 临时码
    public static final String Appointment_DEL_FAIL_1 = "ADF001"; // 临时码
    public static final String Appointment_DEL_FAIL_2 = "ADF002"; // 临时码
    public static final String Appointment_CHECK_FAIL_1 = "ACF001"; // 临时码
    public static final String Appointment_CHECK_FAIL_2 = "ACF002"; // 临时码
    public static final String Appointment_USER_LIST_FAIL_1 = "AULF001"; // 临时码
    public static final String Appointment_USER_LIST_FAIL_2 = "AULF002"; // 临时码

    /**
     * 超级Live 预约相关错误码
     */
    public static final String SUBSCRIBE_ADD_FAIL_1 = "SAF001"; // 临时码
    public static final String SUBSCRIBE_ADD_FAIL_2 = "SAF002"; // 临时码
    public static final String SUBSCRIBE_DEL_FAIL_1 = "SDF001"; // 临时码
    public static final String SUBSCRIBE_DEL_FAIL_2 = "SDF002"; // 临时码
    public static final String SUBSCRIBE_MULDEL_FAIL_1 = "SMDF001"; // 临时码
    public static final String SUBSCRIBE_MULDEL_FAIL_2 = "SMDF002"; // 临时码
    public static final String SUBSCRIBE_CHECK_FAIL_1 = "SCF001"; // 临时码
    public static final String SUBSCRIBE_CHECK_FAIL_2 = "SCF002"; // 临时码
    public static final String SUBSCRIBE_USER_LIST_FAIL_1 = "SULF001"; // 临时码
    public static final String SUBSCRIBE_USER_LIST_FAIL_2 = "SULF002"; // 临时码
    public static final String SUBSCRIBE_LIVE_NOT_FOUND = "SLNF001"; //预约的直播不存在
    /**
     * 超级Live 关注相关错误码
     */
    public static final String ATTENTION_ADD_FAIL_1 = "AAF001"; // 临时码
    public static final String ATTENTION_DEL_FAIL_1 = "ADF001"; // 临时码
    public static final String ATTENTION_CHECK_FAIL_1 = "ACF001"; // 临时码
    
    // 直播相关错误码
    public static final String LIVE_CHANNEL_PROGRAM_WITH_DATE_GET_FAIL = "LCP001"; // 临时码
    // 直播相关错误码
    public static final String LIVE_CHANNEL_PROGRAM_WITH_INC_GET_FAIL = "LCP002"; // 临时码

    /**
     * 超级live相关错误码
     */
    public static final String SUPERLIVE_PARAM_ERROR = "SL001"; // 参数不合法

}
