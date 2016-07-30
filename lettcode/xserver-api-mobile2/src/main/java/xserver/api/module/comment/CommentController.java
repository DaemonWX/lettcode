package xserver.api.module.comment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.comment.dto.CommentAddDto;
import xserver.api.module.comment.dto.CommentLikeDto;
import xserver.api.module.comment.dto.CommentListDto;
import xserver.api.response.Response;
import xserver.lib.tp.comment.request.CommentBaseRequest;

/**
 * 评论相关接口相关控制器
 */
@Controller("commentController")
public class CommentController extends BaseController {

    /**
     * 获取评论列表
     * @param pid
     *            专辑id
     * @param vid
     *            视频id
     * @param token
     *            用户登录的token
     * @param page
     * @param pageSize
     * @param commonParam
     * @return
     */
    @RequestMapping("/comment/list")
    public Response<CommentListDto> getCommentList(@RequestParam(value = "pid", required = false) Long pid,
            @RequestParam(value = "vid") Long vid, @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ModelAttribute CommonParam commonParam) {

        pageSize = 30;
        return this.facadeService.getCommentService().getCommentList(pid, vid, token, page, pageSize, commonParam);
    }

    /**
     * 新增评论
     * @param pid
     *            专辑id
     * @param vid
     *            视频id
     * @param cid
     *            频道id
     * @param token
     *            用户登录token
     * @param title
     *            视频标题
     * @param content
     *            评论内容
     * @param commonParam
     * @return
     */
    @RequestMapping("/comment/add")
    public Response<CommentAddDto> addComment(@RequestParam(value = "pid", required = false) Long pid,
            @RequestParam(value = "vid") Long vid, @RequestParam(value = "cid", required = false) Integer cid,
            @RequestParam(value = "token") String token, @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content") String content, @ModelAttribute CommonParam commonParam) {

        CommentBaseRequest request = new CommentBaseRequest();
        request.setPid(pid);
        request.setXid(vid);
        request.setSso_tk(token);
        request.setCid(cid);
        request.setTitle(title);
        request.setContent(content);
        request.setClientIp(commonParam.getIp());
        request.setLang(commonParam.getLangcode());
        return this.facadeService.getCommentService().addComment(request, CommentBaseRequest.COMMENT_OPERATION_ADD);
    }

    /**
     * 新增回复
     * @param pid
     *            专辑id
     * @param vid
     *            视频id
     * @param cid
     *            频道id
     * @param token
     *            用户登录token
     * @param title
     *            视频标题
     * @param content
     *            评论内容
     * @param commentid
     *            回复对应的评论id
     * @param replyid
     *            被回复的回复ID
     * @param commonParam
     * @return
     */
    @RequestMapping("/comment/reply")
    public Response<CommentAddDto> addReply(@RequestParam(value = "pid", required = false) Long pid,
            @RequestParam(value = "vid") Long vid, @RequestParam(value = "cid", required = false) Integer cid,
            @RequestParam(value = "token") String token, @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content") String content, @RequestParam(value = "commentid") String commentid,
            @RequestParam(value = "replyid", required = false) String replyid, @ModelAttribute CommonParam commonParam) {

        CommentBaseRequest request = new CommentBaseRequest();
        request.setPid(pid);
        request.setXid(vid);
        request.setSso_tk(token);
        request.setCid(cid);
        request.setTitle(title);
        request.setContent(content);
        request.setCommentid(commentid);
        request.setReplyid(replyid);
        request.setClientIp(commonParam.getIp());
        request.setLang(commonParam.getLangcode());
        return this.facadeService.getCommentService().addComment(request, CommentBaseRequest.COMMENT_OPERATION_REPLY);
    }

    /**
     * 获取回复列表
     * @param token
     *            用户登录token
     * @param commentid
     *            评论id
     * @param page
     * @param pageSize
     * @param commonParam
     * @return
     */
    @RequestMapping("/comment/replylist")
    public Response<CommentListDto> getReplyList(@RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "commentid") String commentid,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ModelAttribute CommonParam commonParam) {

        CommentBaseRequest request = new CommentBaseRequest();
        request.setCommentid(commentid);
        request.setSso_tk(token);
        request.setPage(page);
        request.setRows(pageSize);
        request.setClientIp(commonParam.getIp());
        request.setLang(commonParam.getLangcode());

        return this.facadeService.getCommentService().getCommentList(request,
                CommentBaseRequest.COMMENT_OPERATION_REPLYLIST);
    }

    /**
     * 喜欢评论
     * @param commentid
     *            评论id
     * @param token
     *            用户登录token
     * @param commonParam
     * @return
     */
    @RequestMapping("/comment/like")
    public Response<CommentLikeDto> commentLike(@RequestParam(value = "commentid") String commentid,
            @RequestParam(value = "token", required = false) String token, @ModelAttribute CommonParam commonParam) {
        CommentBaseRequest request = new CommentBaseRequest();
        request.setCommentid(commentid);
        request.setSso_tk(token);
        request.setMacid(commonParam.getDevId());
        request.setClientIp(commonParam.getIp());
        request.setLang(commonParam.getLangcode());
        return this.facadeService.getCommentService().commentLikeOrUnlike(request,
                CommentBaseRequest.COMMENT_OPERATION_LIKE);
    }

    /**
     * 取消喜欢评论
     * @param commentid
     *            评论id
     * @param token
     *            用户登录token
     * @param commonParam
     * @return
     */
    @RequestMapping("/comment/unlike")
    public Response<CommentLikeDto> commentUnLike(@RequestParam(value = "commentid") String commentid,
            @RequestParam(value = "token", required = false) String token, @ModelAttribute CommonParam commonParam) {
        CommentBaseRequest request = new CommentBaseRequest();
        request.setCommentid(commentid);
        request.setSso_tk(token);
        request.setMacid(commonParam.getDevId());
        request.setClientIp(commonParam.getIp());
        request.setLang(commonParam.getLangcode());
        return this.facadeService.getCommentService().commentLikeOrUnlike(request,
                CommentBaseRequest.COMMENT_OPERATION_UNLIKE);
    }
}
