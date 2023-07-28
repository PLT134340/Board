package study.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id @GeneratedValue
    @Column(name = "like_id")
    private Long id;
    private int count;
    @OneToOne(mappedBy = "like")
    private Post post;
    @ManyToMany
    @JoinTable(name = "like_member",
            joinColumns = @JoinColumn(name = "like_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    public Like(Post post) {
        this.count = 0;
        this.post = post;
    }

    public void addLike(User user) {
        users.add(user);
        count++;
    }

}
