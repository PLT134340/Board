package study.board.service.dto.user;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.User;

@Getter @Setter
public class UserInform {

    private Long id;
    private String username;

    public UserInform() {}

    public UserInform(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
