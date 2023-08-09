package study.board.service.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import study.board.entity.Post;

@Getter @Setter
public class PostUpdateForm {

    @NotBlank
    private String title;
    @NotNull
    private String content;

    public PostUpdateForm() {}

    public PostUpdateForm(Post post) {
        title = post.getTitle();
        content = post.getContent();
    }

}
