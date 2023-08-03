package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.comment.Recomment;

@Getter @Setter
public class RecommentInform {

    private Long id;
    private String content;
    private String username;

    public RecommentInform(Recomment recomment) {
        id = recomment.getId();
        content = recomment.getContent();
        username = recomment.getUser().getUsername();
    }

}
