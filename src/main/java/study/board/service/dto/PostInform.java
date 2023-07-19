package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.Post;

@Getter @Setter
public class PostInform {

    private Long id;
    private String title;
    private int like;
    private int commentCount;

    public PostInform(Post post) {
        id = post.getId();
        title = post.getTitle();
//        like = post.getLike();
        commentCount = post.getComments().size();
    }

}
