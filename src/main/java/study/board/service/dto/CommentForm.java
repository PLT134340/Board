package study.board.service.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentForm {

    private Long userId;
    private Long postId;
    private String content;

    public void setId(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

}
