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
import study.board.entity.Post;
import study.board.entity.User;
import study.board.entity.comment.Comment;
import study.board.entity.comment.Recomment;
import study.board.repository.CommentRepository;
import study.board.repository.PostRepository;
import study.board.repository.RecommentRepository;
import study.board.service.dto.CommentCreateForm;
import study.board.service.dto.RecommentCreateForm;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private UserService userService;
    @Mock
    private PostService postService;

    @Mock
    private PostRepository postRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    RecommentRepository recommentRepository;

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

    private Post getPost() {
        Post post = new Post("JPA", "JAVA Persistence API", getBoard(), getUser());
        ReflectionTestUtils.setField(post, "id", 1L);
        ReflectionTestUtils.setField(post, "createdDate", LocalDateTime.now());
        ReflectionTestUtils.setField(post, "lastModifiedDate", LocalDateTime.now());
        return post;
    }

    private Comment getComment() {
        Comment comment = new Comment("hi", getUser(), getPost());
        ReflectionTestUtils.setField(comment, "id", 1L);
        return comment;
    }

    private Recomment getRecomment() {
        Recomment comment = new Recomment("hi", getUser(), getComment());
        ReflectionTestUtils.setField(comment, "id", 1L);
        return comment;
    }

    private CommentCreateForm getCommentForm() {
        CommentCreateForm commentCreateForm = new CommentCreateForm();
        commentCreateForm.setContent("hi");
        return commentCreateForm;
    }

    private RecommentCreateForm getRecommentForm() {
        RecommentCreateForm recommentCreateForm = new RecommentCreateForm();
        recommentCreateForm.setContent("hi");
        return recommentCreateForm;
    }

    @Test
    @DisplayName("id로 조회 성공")
    void findByIdSuccess() {
        // given
        Comment comment = getComment();

        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        // when
        Comment findComment = commentService.findById(1L);

        // then
        assertThat(findComment.getId()).isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("id로 조회 실패")
    void findByIdFail() {
        // given
        given(commentRepository.findById(1L)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> commentService.findById(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("no such comment");
    }

    @Test
    @DisplayName("댓글 저장")
    void saveComment() {
        // given
        CommentCreateForm commentCreateForm = getCommentForm();

        User user = getUser();
        Post post = getPost();
        Comment comment = getComment();

        given(userService.findById(1L)).willReturn(user);
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        Long userId = 1L;
        Long postId = 1L;
        Comment saveComment = commentService.saveComment(commentCreateForm, userId, postId);

        // then
        assertThat(saveComment.getId()).isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("대댓글 저장")
    void saveRecomment() {
        // given
        RecommentCreateForm recommentCreateForm = getRecommentForm();

        User user = getUser();
        Post post = getPost();
        Comment comment = getComment();
        Recomment recomment = getRecomment();

        given(userService.findById(1L)).willReturn(user);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
        given(recommentRepository.save(any(Recomment.class))).willReturn(recomment);

        // when
        Long userId = 1L;
        Long commentId = 1L;
        Recomment saveRecomment = commentService.saveRecomment(recommentCreateForm, userId, commentId);

        // then
        assertThat(saveRecomment.getId()).isEqualTo(recomment.getId());
        assertThat(saveRecomment.getUpperComment().getId()).isEqualTo(comment.getId());
    }

}