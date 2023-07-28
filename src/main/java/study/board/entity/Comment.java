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
public class Comment extends DateEntity {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_comment_id")
    private Comment upperComment;
    @OneToMany(mappedBy = "upperComment", cascade = CascadeType.PERSIST)
    private List<Comment> recomments = new ArrayList<>();

    public Comment(String content, User user, Post post, Comment upperComment) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.upperComment = upperComment;
    }
}
