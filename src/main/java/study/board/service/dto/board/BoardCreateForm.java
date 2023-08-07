package study.board.service.dto.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardCreateForm {

    @NotBlank
    private String name;
    @NotBlank
    private String subtitle;

}
