package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class CommentInform {

    private Long id;
    private String content;
    private String username;
    private List<CommentInform> recomments;

    public CommentInform(Comment comment) {
        id = comment.getId();
        content = comment.getContent();
        username = comment.getUser().getUsername();
        recomments = comment.getRecomments()
                .stream()
                .map(c -> new CommentInform(c))
                .collect(Collectors.toList());
    }

}
