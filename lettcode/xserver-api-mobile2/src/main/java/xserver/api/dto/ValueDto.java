package xserver.api.dto;


/**
 * 单值返回封装对象
 * 为客户代码混淆
 * 使用例如：
 *   Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
     response.setData(new ValueDto<Boolean>(true)); 
 */
public class ValueDto<T> extends BaseDto{
    private static final long serialVersionUID = -706098127963681244L;
    private T v ;
    public ValueDto(T value){
        this.v=value;
    }
    public T getV() {
        return v;
    }

    public void setV(T v) {
        this.v = v;
    }

    
    
}
