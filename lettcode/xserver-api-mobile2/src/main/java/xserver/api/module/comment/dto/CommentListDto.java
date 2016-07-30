package xserver.api.module.comment.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import xserver.lib.tp.comment.response.Comment;
import xserver.lib.tp.comment.response.CommentBaseTpResponse;

/**
 * 评论Dto
 */
public class CommentListDto {
    private Long total; // 评论总量
    private String rule; // 1 先审核后放;2 先放后审
    private List<CommentDto> comment; // 评论列表
    private List<CommentDto> auth;
    private List<CommentDto> markData; // 精华评论-精评
    private List<CommentDto> godData; // 热评

    public CommentListDto() {
    }

    public CommentListDto(CommentBaseTpResponse tpResponse) {
        if (tpResponse != null) {
            this.total = tpResponse.getTotal();
            this.rule = tpResponse.getRule();
            
            //同一账户未审核的评论自己也可看见  2015811
            if (tpResponse.getAuthData() != null) {
            	 Map<String, Comment> tpAuthMap = tpResponse.getAuthData();
                 Set<String> tpAuthKeys = tpAuthMap.keySet();
                 if (!CollectionUtils.isEmpty(tpAuthKeys)) {
                	 comment = new LinkedList<CommentDto>();
                	 for (String ak : tpAuthKeys) {
                		 comment.add(CommentDto.getInstance(tpAuthMap.get(ak)));
                	 }
                 }
            }
            
            if (tpResponse.getData() != null) {
                Map<String, Comment> tpCommentMap = tpResponse.getData();
                Set<String> tpCommentKeys = tpCommentMap.keySet();
                if (!CollectionUtils.isEmpty(tpCommentKeys)) {
                	if(comment==null){
                		comment = new LinkedList<CommentDto>();
                	}
                    for (String ck : tpCommentKeys) {
                        comment.add(CommentDto.getInstance(tpCommentMap.get(ck)));
                    }
                }
            }

            if (tpResponse.getAuthData() != null) {
                Map<String, Comment> tpAuthMap = tpResponse.getAuthData();
                Set<String> tpAuthKeys = tpAuthMap.keySet();
                if (!CollectionUtils.isEmpty(tpAuthKeys)) {
                    auth = new LinkedList<CommentDto>();
                    for (String ak : tpAuthKeys) {
                        auth.add(CommentDto.getInstance(tpAuthMap.get(ak)));
                    }
                }
            }

            if (tpResponse.getTopData() != null) {
                Map<String, Comment> tpTopMap = tpResponse.getTopData();
                Set<String> tpTopKeys = tpTopMap.keySet();
                if (!CollectionUtils.isEmpty(tpTopKeys)) {
                    markData = new LinkedList<CommentDto>();
                    for (String tk : tpTopKeys) {
                        markData.add(CommentDto.getInstance(tpTopMap.get(tk)));
                    }
                }
            }

            if (tpResponse.getGodData() != null) {
                Map<String, Comment> tpGodMap = tpResponse.getGodData();
                Set<String> tpGodKeys = tpGodMap.keySet();
                if (!CollectionUtils.isEmpty(tpGodKeys)) {
                    godData = new LinkedList<CommentDto>();
                    for (String gk : tpGodKeys) {
                        godData.add(CommentDto.getInstance(tpGodMap.get(gk)));
                    }
                }
            }
        }
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public List<CommentDto> getComment() {
        return comment;
    }

    public void setComment(List<CommentDto> comment) {
        this.comment = comment;
    }

    public List<CommentDto> getAuth() {
        return auth;
    }

    public void setAuth(List<CommentDto> auth) {
        this.auth = auth;
    }

    public List<CommentDto> getMarkData() {
        return markData;
    }

    public void setMarkData(List<CommentDto> markData) {
        this.markData = markData;
    }

    public List<CommentDto> getGodData() {
        return godData;
    }

    public void setGodData(List<CommentDto> godData) {
        this.godData = godData;
    }

}
