package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.Board;

import java.util.List;
import java.util.stream.Collectors;

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
