package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.Post;
import study.board.entity.comment.Comment;

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

    public PostInform(Post post, List<Comment> commentList, int count) {
        id = post.getId();
        title = post.getTitle();
        createdDateTime = post.getCreatedDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
        username = post.getUser().getUsername();
        content = post.getContent();
        commentCount = count;
        comments = commentList
                .stream()
                .map(comment -> new CommentInform(comment))
                .collect(Collectors.toList());
        like = post.getLike().getCount();
    }

}
