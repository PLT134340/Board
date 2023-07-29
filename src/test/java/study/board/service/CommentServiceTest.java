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
import study.board.entity.Comment;
import study.board.entity.Post;
import study.board.entity.User;
import study.board.repository.CommentRepository;
import study.board.service.dto.CommentForm;

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
        Comment comment = new Comment("hi", getUser(), getPost(), null);
        ReflectionTestUtils.setField(comment, "id", 1L);
        return comment;
    }

    private Comment getRecomment() {
        Comment comment = new Comment("hi", getUser(), getPost(), getComment());
        ReflectionTestUtils.setField(comment, "id", 1L);
        return comment;
    }

    private CommentForm getCommentForm() {
        CommentForm commentForm = new CommentForm();
        commentForm.setUserId(1L);
        commentForm.setPostId(1L);
        commentForm.setCommentId(1L);
        commentForm.setContent("hi");
        return commentForm;
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
        commentForm.setCommentId(0L);

        User user = getUser();
        Post post = getPost();
        Comment comment = getComment();

        given(userService.findById(1L)).willReturn(user);
        given(postService.findById(1L)).willReturn(post);
        given(commentRepository.findById(0L)).willReturn(Optional.empty());
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
        CommentForm commentForm = getCommentForm();

        User user = getUser();
        Post post = getPost();
        Comment comment = getComment();
        Comment recomment = getRecomment();

        given(userService.findById(1L)).willReturn(user);
        given(postService.findById(1L)).willReturn(post);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
        given(commentRepository.save(any(Comment.class))).willReturn(recomment);

        // when
        Comment saveComment = commentService.saveComment(commentForm);

        // then
        assertThat(saveComment.getId()).isEqualTo(recomment.getId());
        assertThat(saveComment.getUpperComment().getId()).isEqualTo(comment.getId());
    }

}