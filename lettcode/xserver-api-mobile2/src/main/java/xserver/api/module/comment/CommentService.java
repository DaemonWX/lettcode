package xserver.api.module.comment;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.comment.dto.CommentAddDto;
import xserver.api.module.comment.dto.CommentLikeDto;
import xserver.api.module.comment.dto.CommentListDto;
import xserver.api.response.Response;
import xserver.lib.constant.CommonConstants;
import xserver.lib.constant.VideoConstants;
import xserver.lib.tp.comment.request.CommentBaseRequest;
import xserver.lib.tp.comment.response.CommentBaseTpResponse;
import xserver.lib.tp.comment.response.CommentLikeTpResponse;
import xserver.lib.tpcache.cache.PlayCache;
import xserver.lib.util.MessageUtils;

@Service("commentService")
public class CommentService extends BaseService {

    /**
     * 获取评论列表
     * @return
     */
    public Response<CommentListDto> getCommentList(CommentBaseRequest request, int operation) {
        Response<CommentListDto> response = new Response<CommentListDto>();
        if (request != null) {
            CommentBaseTpResponse tpResponse = this.facadeTpDao.getCommentTpDao().comment(request, operation);
            CommentListDto dto = null;
            if (tpResponse != null && tpResponse.success()) {
                dto = new CommentListDto(tpResponse);
                response.setData(dto);
            } else {
                dto = new CommentListDto();
                dto.setTotal(0l);
            }
        }
        return response;
    }

    /**
     * 获取评论列表
     * @return
     */
    public Response<CommentListDto> getCommentList(Long pid, Long vid, String token, int page, int pageSize,
            CommonParam commonParam) {
        Response<CommentListDto> response = new Response<CommentListDto>();

        PlayCache video = this.facadeService.getVideoService().getPlayCacheEntityByVideoId(vid,
                CommonConstants.Copyright.PHONE);

        CommentBaseRequest request = new CommentBaseRequest();
        request.setXid(vid);
        request.setSso_tk(token);
        request.setMacid(commonParam.getDevId());
        request.setPage(page);
        request.setRows(pageSize);
        request.setClientIp(commonParam.getIp());
        request.setLang(commonParam.getLangcode());
        if (video != null) {
            Integer category = video.getvCategoryId();
            if (category != null && VideoConstants.Category.FILM == category) {
                request.setPid(video.getaId());// 不信任客户端传的pid，不准确
            }
        }

        if (request != null) {
            CommentBaseTpResponse tpResponse = this.facadeTpDao.getCommentTpDao().comment(request,
                    CommentBaseRequest.COMMENT_OPERATION_LIST);
            CommentListDto dto = null;
            if (tpResponse != null && tpResponse.success()) {
                dto = new CommentListDto(tpResponse);
                response.setData(dto);
            } else {
                dto = new CommentListDto();
                dto.setTotal(0l);
            }
        }

        return response;
    }

	/**
	 * 新增评论
	 * 
	 * @param request
	 * @return
	 */
	public Response<CommentAddDto> addComment(CommentBaseRequest request, int operation) {
		Response<CommentAddDto> response = new Response<CommentAddDto>();

		String errCode = null;
//		if (request == null) {
//			errCode = CommentConstant.ERROR_CODE_COMMENT_PARAM_ERROR;
//		} else {
		if (request.getContent() == null || request.getContent().length() < 3
				|| request.getContent().length() > 140) {
			errCode = CommentConstant.ERROR_CODE_COMMENT_CONENT_ERROR;
		}
//			if (StringUtils.isBlank(request.getSso_tk())) {
//				errCode = CommentConstant.ERROR_CODE_COMMENT__NOTLOGIN;
//			}
//		}
		
		if (errCode == null) {
			CommentBaseTpResponse tpResponse = this.facadeTpDao.getCommentTpDao().comment(request, operation);
			CommentAddDto dto = new CommentAddDto();

			if (tpResponse != null && tpResponse.success()) {
				dto.setStatus(true);
				dto.setTotal(tpResponse.getTotal());
				response.setErrorMessage(MessageUtils.getMessage(CommentConstant.ADD_COMMENT_CUSSCESS, request != null ? request.getLang() : null));
				response.setData(dto);
			} else {
				errCode = getErrorCode(tpResponse);
				if (errCode != null) {
					this.setErrorResponse(response, errCode, errCode, request != null ? request.getLang() : null);
				} else {
					// 除了上述几种错误以外，都提示评论成功
					dto.setStatus(true);
					dto.setTotal(tpResponse.getTotal());
					response.setErrorMessage(MessageUtils.getMessage(CommentConstant.ADD_COMMENT_CUSSCESS, request != null ? request.getLang() : null));
					response.setData(dto);
				}
			}

		} else {
			this.setErrorResponse(response, errCode, errCode, request != null ? request.getLang() : null);
		}
		return response;
	}

	/**
	 * 获取评论错误码
	 * 
	 * @param tpResponse
	 * @return
	 */
	private String getErrorCode(CommentBaseTpResponse tpResponse) {
		/*if (tpResponse != null && "not_identified".equalsIgnoreCase(tpResponse.getResult())) {
			// 没有进行实名认证
			return CommentConstant.ERROR_CODE_COMMENT_NOT_IDENTIFIED;
		} else*/
			
		if (tpResponse != null && "more".equalsIgnoreCase(tpResponse.getResult())) {
		// 5分钟发评论超过30条
			return CommentConstant.ERROR_CODE_COMMENT_MORE_ERROR;
		} else if (tpResponse != null && "time".equalsIgnoreCase(tpResponse.getResult())) {
			// 评论过于频繁
			return CommentConstant.ERROR_CODE_COMMENT_TIME_ERROR;
		} else if (tpResponse != null && "repeat".equalsIgnoreCase(tpResponse.getResult())) {
			// 评论重复
			return CommentConstant.ERROR_CODE_COMMENT_REPEAT_ERROR;
		}
		return null;
	}

	/**
	 * 喜欢或者取消喜欢评论
	 * 
	 * @param request
	 * @param operation
	 * @return
	 */
	public Response<CommentLikeDto> commentLikeOrUnlike(CommentBaseRequest request, int operation) {
		Response<CommentLikeDto> response = new Response<CommentLikeDto>();
		if (request == null || StringUtils.isBlank(request.getCommentid())) {
			this.setErrorResponse(response, CommentConstant.ERROR_CODE_COMMENT_PARAM_ERROR,
					CommentConstant.ERROR_CODE_COMMENT_PARAM_ERROR, request != null ? request.getLang() : null);
		} else {
			CommentLikeTpResponse tpResponse = this.facadeTpDao.getCommentTpDao().commentLikeOrUnlike(request,
					operation);
			if (tpResponse != null && tpResponse.success() && tpResponse.getData() != null) {
				CommentLikeDto dto = new CommentLikeDto();
				dto.setLike(tpResponse.getData().getLike());
				response.setData(dto);
			}
		}
		return response;
	}
}
