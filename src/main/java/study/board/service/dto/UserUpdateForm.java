package study.board.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import study.board.entity.User;

@Getter @Setter
public class UserUpdateForm {

    @NotBlank
    private String username;

    public UserUpdateForm(User user) {
        username = user.getUsername();
    }


}
