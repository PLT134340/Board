package study.board.service.dto.board;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.Board;

@Getter @Setter
public class BoardInform {

    private Long id;
    private String name;
    private String subtitle;

    public BoardInform(Board board) {
        id = board.getId();
        name = board.getName();
        subtitle = board.getSubtitle();
    }

}
