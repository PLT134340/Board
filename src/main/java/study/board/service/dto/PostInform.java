package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.Post;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class PostInform {

    private Long id;
    private String title;
    private String createdDateTime;
    private String username;
    private String content;
    private int commentCount;
    private List<CommentInform> comments;
    private int like;

    public PostInform(Post post, int count) {
        id = post.getId();
        title = post.getTitle();
        createdDateTime = post.getCreatedDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
        username = post.getUser().getUsername();
        content = post.getContent();
        like = post.getLike().getCount();
        commentCount = count;
        comments = post.getComments()
                .stream()
                .filter(comment -> comment.getUpperComment() == null)
                .map(comment -> new CommentInform(comment))
                .collect(Collectors.toList());
    }

}
