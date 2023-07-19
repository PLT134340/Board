package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import study.board.entity.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class BoardInform {

    private Long id;
    private String name;
    private String subtitle;
    private List<PostInform> posts;

    public BoardInform(Board board) {
        id = board.getId();
        name = board.getName();
        subtitle = board.getSubtitle();
        posts = board.getPosts()
                .stream()
                .map(post -> new PostInform(post))
                .collect(Collectors.toList());
    }

}
