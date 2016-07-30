package xserver.api.module.vote;

import org.springframework.stereotype.Service;

import xserver.api.constant.ErrorCodeConstants;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.vote.dto.VoteResultDto;
import xserver.api.response.Response;
import xserver.lib.tp.vote.response.VoteTpResponse;

/**
 * 点赞相关 业务逻辑
 */
@Service
public class VoteService extends BaseService {
    /**
     * 根据id获取点赞，目前第三方仅支持最多50个
     * @param ids
     * @param commonParam
     * @return
     */
    public Response<VoteResultDto> getVoteResultByIds(String ids, CommonParam commonParam) {
        Response<VoteResultDto> response = new Response<VoteResultDto>();
        // 服务端定义一次可获取20个视频赞数
        if (ids == null || ids.split(",").length > 20) {
            setErrorResponse(response, ErrorCodeConstants.USER_VIDEO_VOTE_COMMIT,
                    ErrorCodeConstants.USER_VIDEO_VOTE_COMMIT, commonParam.getLangcode());
            return response;
        }
        // 第三方请求点赞
        VoteTpResponse voteTpResponse = facadeTpDao.getVoteTpDao().getVoteByIds(ids);
        // 校验请求合法性
        if (voteTpResponse != null && voteTpResponse.getCode() != null && voteTpResponse.getCode() == 200
                && voteTpResponse.getData() != null && !voteTpResponse.getData().isEmpty()) {
            VoteResultDto voteResultDto = new VoteResultDto();
            // 封装赞id与赞数，map结构
            voteResultDto.setVresult(voteTpResponse.getData());
            response.setData(voteResultDto);
        } else {
            // 异常处理
            setErrorResponse(response, ErrorCodeConstants.USER_VIDEO_VOTE_COMMIT,
                    ErrorCodeConstants.USER_VIDEO_VOTE_COMMIT, commonParam.getLangcode());
        }
        return response;
    }

    /**
     * 用户提交赞
     * @param ids
     * @param token
     * @param commonParam
     * @return
     */
    public Response<VoteResultDto> commitVote(String ids, String token,String sign, CommonParam commonParam) {
        Response<VoteResultDto> response = new Response<VoteResultDto>();
        // 服务端定义一次可获取20个视频赞数
        if (ids == null || ids.split(",").length > 20) {
            // TODO 错误码待产品给出
            setErrorResponse(response, ErrorCodeConstants.USER_VIDEO_VOTE_COMMIT,
                    ErrorCodeConstants.USER_VIDEO_VOTE_COMMIT, commonParam.getLangcode());
            return response;
        }
        // getIp
        String ip = commonParam.getIp();
        // 提交第三方赞
        VoteTpResponse voteTpResponse = facadeTpDao.getVoteTpDao().commitVote(ids, token, sign, ip);
        if (voteTpResponse != null && voteTpResponse.getCode() != null && voteTpResponse.getCode() == 200
                && voteTpResponse.getData() != null && !voteTpResponse.getData().isEmpty()) {
            VoteResultDto voteResultDto = new VoteResultDto();
            // 封装返回赞
            voteResultDto.setVresult(voteTpResponse.getData());
            response.setData(voteResultDto);
        } else {
            // TODO 错误码待产品给出
            if(voteTpResponse!=null&&voteTpResponse.getCode()!=null&&voteTpResponse.getCode()==401){
                setErrorResponse(response, ErrorCodeConstants.USER_VIDEO_VOTE_DONE,
                        ErrorCodeConstants.USER_VIDEO_VOTE_DONE, commonParam.getLangcode());
            }else {
                setErrorResponse(response, ErrorCodeConstants.USER_VIDEO_VOTE_COMMIT,
                        ErrorCodeConstants.USER_VIDEO_VOTE_COMMIT, commonParam.getLangcode());
            }
        }
        return response;
    }

}
