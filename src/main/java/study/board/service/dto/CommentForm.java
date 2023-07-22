package study.board.service.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentForm {

    private Long userId;
    private Long postId;
    private String content;

}
