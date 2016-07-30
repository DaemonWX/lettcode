package xserver.api.module.comment.dto;

public class CommentLikeDto {
    private Long like; // 当前有多少人喜欢

    public Long getLike() {
        return like;
    }

    public void setLike(Long like) {
        this.like = like;
    }

}
