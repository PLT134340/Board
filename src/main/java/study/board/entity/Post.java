package study.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "likes")
    private int like;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String content, Board board, User user) {
        this.title = title;
        this.content = content;
        this.board = board;
        this.user = user;
        like = 0;
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addLike() {
        like++;
    }

    public void mergeComments(List<Comment> comments) {
        this.comments = comments;
    }
}
