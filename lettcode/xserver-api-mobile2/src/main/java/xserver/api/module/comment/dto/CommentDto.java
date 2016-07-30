package xserver.api.module.comment.dto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import xserver.lib.tp.comment.response.Comment;
import xserver.lib.tp.comment.response.Comment.CommentSource;
import xserver.lib.tp.comment.response.CommentUser;
import xserver.lib.tp.comment.response.CommentUser.Cooperation;

public class CommentDto {
    private String id; // 代表评论的唯一id
    private String commentid; // 此评论为回复时，对应的评论id
    private String content; // 评论内容
    private String vtime; // 评论发表时间 例如 10秒前
    private String ctime; // 评论发表时间
    private String city; // 发表评论用户的城市
    private Long replynum; // 评论恢复数量
    private String share;
    private String like; // 评论的获赞数量
    private Long pid; // 专辑id
    private Long xid; // 视频id
    private Integer cid; // 频道id
    private String title; // 视频title
    private List<CommentDto> replys; // 评论回复
    private CommentSource source; // 评论来源
    private CommentUserDto user; // 发表评论的用户信息
    private String isLike;
    private String isVoted;

    public CommentDto() {
    }

    public static CommentDto getInstance(Comment comment) {
        CommentDto dto = new CommentDto();
        if (comment != null) {
            dto.setId(comment.get_id());
            dto.setCommentid(comment.getCommentid());
            dto.setContent(comment.getContent());
            dto.setVtime(comment.getVtime());
            dto.setCtime(comment.getCtime());
            dto.setCity(comment.getCity());
            dto.setReplynum(comment.getReplynum());
            dto.setPid(comment.getPid());
            dto.setXid(comment.getXid());
            dto.setCid(comment.getCid());
            dto.setTitle(comment.getTitle());
            dto.setIsLike(comment.getIsLike());
            dto.setIsVoted(comment.getIsVoted());
            dto.setSource(comment.getSource());
            dto.setUser(transCommentUser2Dto(comment.getUser()));
            dto.setLike(comment.getLike());
            if (!CollectionUtils.isEmpty(comment.getReplys())) {
                Map<String, Comment> replysMap = comment.getReplys();
                Set<String> replyKeys = replysMap.keySet();
                List<CommentDto> replyDto = new LinkedList<CommentDto>();
                if (!CollectionUtils.isEmpty(replyKeys)) {
                    for (String rk : replyKeys) {
                        replyDto.add(CommentDto.getInstance(replysMap.get(rk)));
                    }
                }
                dto.setReplys(replyDto);
            }
        }
        return dto;
    }

    private static CommentUserDto transCommentUser2Dto(CommentUser user) {
        CommentUserDto dto = null;
        if (user != null) {
            dto = new CommentUserDto();
            dto.setUsername(user.getUsername());
            dto.setUid(user.getUid());
            dto.setPhoto(user.getPhoto());
            dto.setIsVip(user.getIsvip());
            List<Cooperation> cooperList = new ArrayList<Cooperation>();
            if (user.getCooperation() != null) {
                cooperList.add(user.getCooperation());
            }
            dto.setCooperation(cooperList);
        }

        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVtime() {
        return vtime;
    }

    public void setVtime(String vtime) {
        this.vtime = vtime;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getReplynum() {
        return replynum;
    }

    public void setReplynum(Long replynum) {
        this.replynum = replynum;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getXid() {
        return xid;
    }

    public void setXid(Long xid) {
        this.xid = xid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommentDto> getReplys() {
        return replys;
    }

    public void setReplys(List<CommentDto> replys) {
        this.replys = replys;
    }

    public CommentSource getSource() {
        return source;
    }

    public void setSource(CommentSource source) {
        this.source = source;
    }

    public CommentUserDto getUser() {
        return user;
    }

    public void setUser(CommentUserDto user) {
        this.user = user;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getIsVoted() {
        return isVoted;
    }

    public void setIsVoted(String isVoted) {
        this.isVoted = isVoted;
    }
}
