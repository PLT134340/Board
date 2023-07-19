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
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;
    private String name;
    private String subtitle;
    @OneToMany(mappedBy = "board")
    private List<Post> posts = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User admin;

    public Board(String name, String subtitle, User admin) {
        this.name = name;
        this.subtitle = subtitle;
        this.admin = admin;
    }
}
