package study.board.common.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import study.board.entity.User;
import study.board.service.dto.user.UserInform;

import java.util.List;

@Getter
public class UserAdapter extends org.springframework.security.core.userdetails.User {

    private final UserInform userInform;

    public UserAdapter(User user,List<GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        userInform = new UserInform(user);
    }
}
