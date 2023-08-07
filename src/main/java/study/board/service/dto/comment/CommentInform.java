package study.board.service.dto.comment;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.comment.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class CommentInform {

    private Long id;
    private String content;
    private String username;
    private List<RecommentInform> recomments;

    public CommentInform(Comment comment) {
        id = comment.getId();
        content = comment.getContent();
        username = comment.getUser().getUsername();
        recomments = comment.getRecomments()
                .stream()
                .map(rc -> new RecommentInform(rc))
                .collect(Collectors.toList());
    }

}
