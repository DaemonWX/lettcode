package xserver.api.module.attention;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import xserver.api.constant.ErrorCodeConstants;
import xserver.api.dto.ValueDto;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.attention.dto.AttentionCheckDto;
import xserver.api.response.Response;
import xserver.lib.tp.attention.request.AttentionAddOrDelRequest;
import xserver.lib.tp.attention.request.AttentionCheckRequest;
import xserver.lib.tp.attention.response.AttentionAddOrDelOrCheckTpResponse;

@Service(value = "attentionService")
public class AttentionService extends BaseService {
    
    private final String APPID = "superLive";
    private final String SUCCESS = "10000";
    public Response<ValueDto<Boolean>> add(String token,Long tagid,Integer type,CommonParam param) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        AttentionAddOrDelRequest addOrDelAttentionRequest = new AttentionAddOrDelRequest(token,tagid,type,APPID);
        AttentionAddOrDelOrCheckTpResponse addOrDelOrCheckAttentionTpResponse = facadeTpDao.getAttentionTpDao().add(addOrDelAttentionRequest);
        if (addOrDelOrCheckAttentionTpResponse != null && SUCCESS.equals(addOrDelOrCheckAttentionTpResponse.getErrno())) {
                response.setData(new ValueDto<Boolean>(Boolean.TRUE));
        } else {
            setErrorResponse(response, ErrorCodeConstants.ATTENTION_ADD_FAIL_1,
                    ErrorCodeConstants.ATTENTION_DEL_FAIL_1, param.getLangcode());
        }
        return response;
    }
    public Response<ValueDto<Boolean>> del(String token,Long tagid,Integer type,CommonParam param) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        AttentionAddOrDelRequest addOrDelAttentionRequest = new AttentionAddOrDelRequest(token,tagid,type,null);
        AttentionAddOrDelOrCheckTpResponse addOrDelOrCheckAttentionTpResponse = facadeTpDao.getAttentionTpDao().del(addOrDelAttentionRequest);
        if (addOrDelOrCheckAttentionTpResponse != null && SUCCESS.equals(addOrDelOrCheckAttentionTpResponse.getErrno())) {
                response.setData(new ValueDto<Boolean>(Boolean.TRUE));
        } else {
            setErrorResponse(response, ErrorCodeConstants.ATTENTION_DEL_FAIL_1,
                    ErrorCodeConstants.ATTENTION_DEL_FAIL_1, param.getLangcode());
        }
        return response;
    }
    public Response<List<AttentionCheckDto>> check(String token,String tagid,CommonParam param) {
        Response<List<AttentionCheckDto>> response = new Response<List<AttentionCheckDto>>();
        List<AttentionCheckDto> lstAtn = new ArrayList<AttentionCheckDto>();
        List<String> tagids = Arrays.asList(tagid.split(","));
        for(String t:tagids){
            AttentionCheckDto attentionCheckDto = new AttentionCheckDto();
            attentionCheckDto.setTagID(t);
            AttentionCheckRequest checkAttentionRequest = new AttentionCheckRequest(token,Long.valueOf(t));
            AttentionAddOrDelOrCheckTpResponse addOrDelOrCheckAttentionTpResponse = facadeTpDao.getAttentionTpDao().check(checkAttentionRequest);
            if (addOrDelOrCheckAttentionTpResponse != null && SUCCESS.equals(addOrDelOrCheckAttentionTpResponse.getErrno())) {
                if(addOrDelOrCheckAttentionTpResponse.getData()!=null&&addOrDelOrCheckAttentionTpResponse.getData().getStatus()>0){
                    attentionCheckDto.setIsAttention(Boolean.TRUE);
                }else{
                    attentionCheckDto.setIsAttention(Boolean.FALSE);
                }
            } else {
                setErrorResponse(response, ErrorCodeConstants.ATTENTION_CHECK_FAIL_1,
                        ErrorCodeConstants.ATTENTION_CHECK_FAIL_1, param.getLangcode());
                return response;
            }
            lstAtn.add(attentionCheckDto);
        }
        response.setData(lstAtn);
        return response;
    }
    public Response<ValueDto<Boolean>> list() {
        facadeTpDao.getAttentionTpDao().list(null);
        return null;
    }
}
