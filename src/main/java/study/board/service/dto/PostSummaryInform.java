package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.Post;

@Getter @Setter
public class PostSummaryInform {

    private Long id;
    private String title;
    private String username;
    private int commentCount;
    private int like;

    public PostSummaryInform(Post post) {
        id = post.getId();
        title = post.getTitle();
        username = post.getUser().getUsername();
        commentCount = post.getComments().size();
        like = post.getLike();
    }

}
