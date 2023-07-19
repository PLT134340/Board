package study.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String username;

    public User(String username) {
        this.username = username;
    }

    public Long updateUsername(String username) {
        this.username = username;
        return this.id;
    }
}
