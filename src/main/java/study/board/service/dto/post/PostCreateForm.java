package study.board.service.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostCreateForm {

    @NotBlank
    private String title;
    @NotNull
    private String content;

}
