package xserver.api.module.video.constants;

public class VideoErrorCodeConstants {

    public interface play {
        public static final String SIG_ERROR = "";// 播放签名校验错误
        public static final String NO_ALBUM_ERROR = "";// 专辑无版权
        public static final String NO_VIDEO_ERROR = "";// 视频无版权

        public static final String ERROR_CODE_0204 = "0204";// 防盗链URL调度失败
    }

    // 半屏播放页 点赞、点踩
    public interface like {
    	public static final String ERROR_CODE_LIKE_PARAM_ERROR = "like001"; // 参数列表错误
    	public static final String ERROR_CODE_LIKE_REQUEST_ERROR = "like002"; // 点赞、点踩请求失败
    	public static final String ERROR_CODE_LIKE_NUMBER_ERROR = "like002"; // 获取点赞、点踩数量失败
    }
}
