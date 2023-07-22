package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.Board;

@Getter @Setter
public class BoardListInform {

    private Long id;
    private String name;
    private String subtitle;

    public BoardListInform(Board board) {
        id = board.getId();
        name = board.getName();
        subtitle = board.getSubtitle();
    }

}
