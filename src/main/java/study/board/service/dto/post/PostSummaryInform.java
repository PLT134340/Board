package study.board.service.dto.post;

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

    public PostSummaryInform(Post post, int commentCount, int likeCount) {
        id = post.getId();
        title = post.getTitle();
        username = post.getUser().getUsername();
        this.commentCount = commentCount;
        this.like = likeCount;
    }

}
