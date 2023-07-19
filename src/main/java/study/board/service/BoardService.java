package study.board.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import study.board.entity.Board;
import study.board.entity.User;
import study.board.repository.BoardRepository;
import study.board.service.dto.BoardForm;
import study.board.service.dto.BoardInform;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class BoardService {

    private final UserService userService;

    private final BoardRepository boardRepository;

    public void createBoard(BoardForm form) {
        User user = userService.findByUsername(form.getUsername());
        boardRepository.save(new Board(form.getName(), form.getSubtitle(), user));
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no such board"));
    }

    public Board findByName(String name) {
        return boardRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("no such board"));
    }

/*
    public List<Board> findAll() {
        return boardRepository.findAll();
    }
*/

    public List<String> nameSearch(String name) {
        return boardRepository.findByNameContaining(name)
                .stream()
                .map(b -> b.getName())
                .collect(Collectors.toList());
    }

    public BoardInform boardInform(String name) {
        return new BoardInform(findByName(name));
    }
}
