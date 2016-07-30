package xserver.api.response;

/**
 * 单数据Response
 * @param <T>
 */
public class Response<T> extends BaseResponse {

    /**
     * 数据
     */
    private T data;

    public Response() {

    }

    public Response(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
