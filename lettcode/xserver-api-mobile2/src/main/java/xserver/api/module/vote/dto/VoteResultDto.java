package xserver.api.module.vote.dto;

import java.util.Map;

import xserver.api.dto.BaseDto;

/**
 * 返回投票或赞结构
 */
public class VoteResultDto extends BaseDto {

    private static final long serialVersionUID = 3224617894082577030L;
    private Map<String, String> vresult;// 赞id->赞数

    public Map<String, String> getVresult() {
        return vresult;
    }

    public void setVresult(Map<String, String> vresult) {
        this.vresult = vresult;
    }

}
