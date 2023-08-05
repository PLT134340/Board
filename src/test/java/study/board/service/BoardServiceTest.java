package study.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.Board;
import study.board.entity.User;
import study.board.repository.BoardRepository;
import study.board.service.dto.BoardCreateForm;
import study.board.service.dto.BoardInform;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserService userService;

    private User getUser() {
        User user = new User("kim", "1234");
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Board getBoard() {
        Board board = new Board("hello", "world", getUser());
        ReflectionTestUtils.setField(board, "id", 1L);
        return board;
    }

    private BoardCreateForm getBoardCreateForm() {
        BoardCreateForm form = new BoardCreateForm();
        form.setName("hello");
        form.setSubtitle("world");
        return form;
    }

    @Test
    @DisplayName("게시판 생성 성공")
    void createBoardSuccess() {
        // given
        User user = getUser();
        BoardCreateForm form = getBoardCreateForm();
        Board board = getBoard();

        given(boardRepository.existsByName("hello")).willReturn(false);
        given(userService.findById(1L)).willReturn(user);
        given(boardRepository.save(any(Board.class))).willReturn(board);

        // when
        Long userId = 1L;
        Long boardId = boardService.createBoard(form, userId);

        // then
        assertThat(boardId).isEqualTo(board.getId());
    }

    @Test
    @DisplayName("게시판 생성 실패")
    void createBoardFail() {
        // given
        BoardCreateForm form = getBoardCreateForm();

        given(boardRepository.existsByName("hello")).willReturn(true);

        // when
        // then
        Long userId = 1L;
        assertThatThrownBy(() -> boardService.createBoard(form, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists name");
    }

    @Test
    @DisplayName("id로 조회 성공")
    void findByIdSuccess() {
        // given
        User user = getUser();
        Board board = getBoard();

        given(boardRepository.findById(1L)).willReturn(Optional.of(board));

        // when
        Board findBoard = boardService.findById(1L);

        // then
        assertThat(findBoard.getId()).isEqualTo(board.getId());
    }

    @Test
    @DisplayName("id로 조회 실패")
    void findByIdFail() {
        // given
        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> boardService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no such board");
    }

    @Test
    @DisplayName("name으로 조회 성공")
    void findByNameSuccess() {
        // given
        User user = getUser();
        Board board = getBoard();

        given(boardRepository.findByName("hello")).willReturn(Optional.of(board));

        // when
        Board findBoard = boardService.findByName("hello");

        // then
        assertThat(findBoard.getId()).isEqualTo(board.getId());
    }

    @Test
    @DisplayName("name으로 조회 실패")
    void findByNameFail() {
        // given
        given(boardRepository.findByName("hello")).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> boardService.findByName("hello"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no such board");
    }

    @Test
    @DisplayName("이름을 포함하는 게시판 리스트 반환")
    void searchByName() {
        // given
        User user = getUser();

        given(boardRepository.findByNameContaining("kim"))
                .willReturn(List.of(getBoard()));

        // when
        List<BoardInform> boardInforms = boardService.searchByName("kim");

        // then
        assertThat(boardInforms.get(0).getId()).isEqualTo(1L);
    }

}