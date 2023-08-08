package study.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends DateEntity {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;
    private String title;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private Integer likeCount;
    private Integer commentCount;

    public Post(String title, String content, Board board, User user) {
        this.title = title;
        this.content = content;
        this.board = board;
        this.user = user;
        this.likeCount = 0;
        this.commentCount = 0;
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addLike() {
        likeCount++;
    }

    public void addComment() {
        commentCount++;
    }

    public void removeComment() {
        commentCount--;
    }
}
