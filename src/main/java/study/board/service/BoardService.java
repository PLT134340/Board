package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.Board;
import study.board.entity.User;
import study.board.repository.BoardRepository;
import study.board.service.dto.BoardCreateForm;
import study.board.service.dto.BoardInform;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final UserService userService;

    private final BoardRepository boardRepository;

    public Long createBoard(BoardCreateForm form, Long userId) {
        validateDuplicateName(form.getName());
        User user = userService.findById(userId);
        return boardRepository
                .save(new Board(form.getName(), form.getSubtitle(), user))
                .getId();
    }

    private void validateDuplicateName(String name) {
        if(boardRepository.existsByName(name))
            throw new IllegalArgumentException("already exists name");
    }


    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no such board"));
    }

    public Board findByName(String name) {
        return boardRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("no such board"));
    }

    public List<BoardInform> searchByName(String name) {
        return boardRepository.findByNameContaining(name)
                .stream()
                .map(board -> new BoardInform(board))
                .collect(Collectors.toList());
    }

    public BoardInform toBoardInform(Long id) {
        return new BoardInform(findById(id));
    }

}
