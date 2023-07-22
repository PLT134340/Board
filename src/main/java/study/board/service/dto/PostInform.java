package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.Post;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class PostInform {

    private Long id;
    private String title;
    private String username;
    private String content;
    private List<CommentInform> comments;
    private int like;

    public PostInform(Post post) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        username = post.getUser().getUsername();
        like = post.getLike();
        comments = post.getComments()
                .stream()
                .map(comment -> new CommentInform(comment))
                .collect(Collectors.toList());
    }

}
