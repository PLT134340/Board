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

    @Test
    @DisplayName("게시판 생성 성공")
    void createBoardSuccess() {
        // given
        User user = new User("kim");
        ReflectionTestUtils.setField(user, "id", 1L);

        BoardCreateForm form = new BoardCreateForm();
        form.setName("hello");
        form.setUserId(user.getId());
        form.setSubtitle("world");

        Board board = new Board(form.getName(), form.getSubtitle(), user);
        ReflectionTestUtils.setField(board, "id", 1L);

        given(boardRepository.existsByName(anyString())).willReturn(false);
        given(userService.findById(1L)).willReturn(user);
        given(userService.findByUsername(anyString())).willReturn(user);
        given(boardRepository.save(any(Board.class))).willReturn(board);

        // when
        Long boardId = boardService.createBoard(form);

        // then
        assertThat(boardId).isEqualTo(board.getId());
    }

    @Test
    @DisplayName("게시판 생성 실패")
    void createBoardFail() {
        // given
        User user = new User("kim");
        ReflectionTestUtils.setField(user, "id", 1L);

        BoardCreateForm form = new BoardCreateForm();
        form.setName("hello");
        form.setUserId(user.getId());
        form.setSubtitle("world");

        given(userService.findById(1L)).willReturn(user);
        given(boardRepository.existsByName(anyString())).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> boardService.createBoard(form))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already exists name");
    }

    @Test
    @DisplayName("이름을 포함하는 리스트 반환")
    void searchByName() {
        // given

        // when

        // then
    }

}