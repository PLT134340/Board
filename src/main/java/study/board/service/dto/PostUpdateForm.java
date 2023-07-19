package study.board.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostUpdateForm {

    private Long postId;
    @NotBlank
    private String title;
    @NotNull
    private String content;
    private Long boardId;

}
