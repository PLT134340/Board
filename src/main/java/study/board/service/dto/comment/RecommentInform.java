package study.board.service.dto.comment;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.comment.Recomment;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter @Setter
public class RecommentInform {

    private Long id;
    private String content;
    private String username;
    private String createdDateTime;

    public RecommentInform(Recomment recomment) {
        id = recomment.getId();
        content = recomment.getContent();
        username = recomment.getUser().getUsername();
        createdDateTime = recomment.getCreatedDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

}
