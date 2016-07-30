package xserver.api.module.superlive;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import xserver.api.dto.ValueDto;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.superlive.dto.SuperLivePlayLogDto;
import xserver.api.response.PageCommonResponse;
import xserver.api.response.Response;

@Component(value = "superLivePlayLogService")
public class SuperLivePlayLogService extends BaseService {
    private final Logger log = LoggerFactory.getLogger(SuperLivePlayLogService.class);

    public Response<ValueDto<Boolean>> updatePlayRecord(String token, String type, String id, CommonParam param) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        response.setData(new ValueDto<Boolean>(Boolean.TRUE));
        return response;
    }

    public Response<ValueDto<Boolean>> deletePlayRecord(String token, String type, String id, CommonParam param) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        response.setData(new ValueDto<Boolean>(Boolean.TRUE));
        return response;
    }

    public PageCommonResponse<SuperLivePlayLogDto> getPlayRecord(String token, String types, Integer page,
            Integer pageSize, CommonParam param) {
        PageCommonResponse<SuperLivePlayLogDto> response = new PageCommonResponse<SuperLivePlayLogDto>(page, pageSize,
                0);
       List<SuperLivePlayLogDto> list = new ArrayList<SuperLivePlayLogDto>();
       list.add(new SuperLivePlayLogDto());
        response.setData(list);
        return response;
    }

}
