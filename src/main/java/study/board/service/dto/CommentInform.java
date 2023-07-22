package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.Comment;

@Getter @Setter
public class CommentInform {

    private Long id;
    private String content;
    private String username;

    public CommentInform(Comment comment) {
        id = comment.getId();
        content = comment.getContent();
        username = comment.getUser().getUsername();
    }

}
