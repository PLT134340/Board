package study.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "users")
@SQLDelete(sql = "update users set is_removed = true where user_id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private Boolean isRemoved;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        isRemoved = false;
    }

    public String getUsername(){
        if (isRemoved)
            return "(알수없음)";
        return username;
    }

    public User(String username) {
        this.username = username;
    }

    public Long update(String username, String password) {
        this.username = username;
        this.password = password;
        return this.id;
    }

}
