package study.board.entity.comment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.board.entity.Post;
import study.board.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseComment {

    @OneToMany(mappedBy = "upperComment", cascade = CascadeType.PERSIST)
    private List<Recomment> recomments = new ArrayList<>();

    public Comment(String content, User user, Post post) {
        super(content, user, post);
    }
}
