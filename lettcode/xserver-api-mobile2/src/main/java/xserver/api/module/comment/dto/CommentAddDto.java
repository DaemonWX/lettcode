package xserver.api.module.comment.dto;

import java.util.List;


/**
 * 新增评论的dto
 */
public class CommentAddDto {
    private Boolean status;
    private Long total;
    private List<CommentDto> comment;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<CommentDto> getComment() {
        return comment;
    }

    public void setComment(List<CommentDto> comment) {
        this.comment = comment;
    }

}
