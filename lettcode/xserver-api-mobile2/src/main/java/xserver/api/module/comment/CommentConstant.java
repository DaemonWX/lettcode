package xserver.api.module.comment;

public class CommentConstant {
    public final static String ERROR_CODE_COMMENT_PARAM_ERROR = "COMMENT_0001"; // 参数不合法
    public final static String ERROR_CODE_COMMENT_CONENT_ERROR = "COMMENT_0002"; // 评论长度最少3个字，最多140字
    public final static String ERROR_CODE_COMMENT__NOTLOGIN = "COMMENT_0003"; // 用户尚未登录
    public final static String ERROR_CODE_COMMENT_NOT_IDENTIFIED = "COMMENT_0004"; // 未实名认证
    public final static String ERROR_CODE_COMMENT_TIME_ERROR = "COMMENT_0005"; // 评论过于频繁
    public final static String ERROR_CODE_COMMENT_MORE_ERROR= "COMMENT_0006"; // 5分钟发评论超过30条
    public final static String ERROR_CODE_COMMENT_REPEAT_ERROR = "COMMENT_0007"; // 评论重复
    public final static String ERROR_CODE_TYPE_ERROR = "COMMENT_0008"; // 评论内容不正确
    public final static String ERROR_CODE_LONG_ERROR = "COMMENT_0009"; // 评论内容过长
    public final static String ERROR_CODE_FAIL_ERROR = "COMMENT_00010"; // 系统错误
    public final static String ERROR_CODE_FORBIDIP_ERROR = "COMMENT_00011"; // 评论IP限制
    public final static String ERROR_CODE_FORBIDUSER_ERROR = "COMMENT_00012"; // ；评论用户限制
    public final static String ERROR_CODE_SIZE_ERROR = "COMMENT_00013"; // 图片尺寸不符合
    public final static String ERROR_CODE_FORMAT_ERROR = "COMMENT_00014"; // 图片格式错误
    public final static String ERROR_CODE_VOTED_ERROR = "COMMENT_00015"; // 已经投过
    public final static String ERROR_CODE_VOTE_EXPIRE_ERROR = "COMMENT_00016"; // 投票已过期
    public final static String ERROR_CODE_ANTICSRF_ERROR = "COMMENT_00017"; // csrf值错误
    public final static String ERROR_CODE_200_SUCCESS = "COMMENT_00018"; // 正确
    public final static String ADD_COMMENT_CUSSCESS = "COMMENT_00019"; // 评论成功
}
