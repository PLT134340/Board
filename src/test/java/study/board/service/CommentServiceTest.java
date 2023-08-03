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
import study.board.repository.RecommentRepository;
import study.board.service.dto.CommentForm;
import study.board.service.dto.RecommentForm;

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
        Recomment comment = new Recomment("hi", getUser(), getPost(), getComment());
        ReflectionTestUtils.setField(comment, "id", 1L);
        return comment;
    }

    private CommentForm getCommentForm() {
        CommentForm commentForm = new CommentForm();
        commentForm.setUserId(1L);
        commentForm.setPostId(1L);
        commentForm.setContent("hi");
        return commentForm;
    }

    private RecommentForm getRecommentForm() {
        RecommentForm recommentForm = new RecommentForm();
        recommentForm.setUserId(1L);
        recommentForm.setPostId(1L);
        recommentForm.setCommentId(1L);
        recommentForm.setContent("hi");
        return recommentForm;
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
        CommentForm commentForm = getCommentForm();

        User user = getUser();
        Post post = getPost();
        Comment comment = getComment();

        given(userService.findById(1L)).willReturn(user);
        given(postService.findById(1L)).willReturn(post);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        Comment saveComment = commentService.saveComment(commentForm);

        // then
        assertThat(saveComment.getId()).isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("대댓글 저장")
    void saveRecomment() {
        // given
        RecommentForm recommentForm = getRecommentForm();

        User user = getUser();
        Post post = getPost();
        Comment comment = getComment();
        Recomment recomment = getRecomment();

        given(userService.findById(1L)).willReturn(user);
        given(postService.findById(1L)).willReturn(post);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
        given(recommentRepository.save(any(Recomment.class))).willReturn(recomment);

        // when
        Recomment saveRecomment = commentService.saveRecomment(recommentForm);

        // then
        assertThat(saveRecomment.getId()).isEqualTo(recomment.getId());
        assertThat(saveRecomment.getUpperComment().getId()).isEqualTo(comment.getId());
    }

}