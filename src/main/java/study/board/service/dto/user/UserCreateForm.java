package study.board.service.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserCreateForm {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
