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
    private Boolean isRemoved;

    public CommentInform(Comment comment) {
        id = comment.getId();
        username = comment.getUsername();
        content = comment.getContent();

        recomments = comment.getRecomments()
                .stream()
                .filter(rc -> !rc.getIsRemoved())
                .map(rc -> new RecommentInform(rc))
                .collect(Collectors.toList());
        isRemoved = comment.getIsRemoved();
    }

}
