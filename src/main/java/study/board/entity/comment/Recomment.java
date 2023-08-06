package study.board.entity.comment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.board.entity.User;

@Entity
@DiscriminatorValue("recomment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recomment extends BaseComment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_comment_id")
    private Comment upperComment;

    public Recomment(String content, User user, Comment upperComment) {
        super(content, user);
        this.upperComment = upperComment;
    }

}
