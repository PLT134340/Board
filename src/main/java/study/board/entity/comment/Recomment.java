package study.board.entity.comment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import study.board.entity.Post;
import study.board.entity.User;

@Entity
@SQLDelete(sql = "update base_comment set is_removed = true where comment_id = ?")
@Where(clause = "is_removed = false")
@DiscriminatorValue("recomment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recomment extends BaseComment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_comment_id")
    private Comment upperComment;

    public Recomment(String content, User user, Post post, Comment upperComment) {
        super(content, user, post);
        this.upperComment = upperComment;
    }

}
