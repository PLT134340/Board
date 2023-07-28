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
import study.board.repository.PostRepository;
import study.board.service.dto.PostCreateForm;
import study.board.service.dto.PostInform;
import study.board.service.dto.PostUpdateForm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private UserService userService;
    @Mock
    private BoardService boardService;

    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;

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

    private PostCreateForm getPostCreateForm() {
        PostCreateForm postCreateForm = new PostCreateForm();
        postCreateForm.setTitle("JPA");
        postCreateForm.setContent("JAVA Persistence API");
        postCreateForm.setBoardId(1L);
        postCreateForm.setUserId(1L);
        return postCreateForm;
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

    private PostUpdateForm getPostUpdateForm() {
        PostUpdateForm postUpdateForm = new PostUpdateForm();
        postUpdateForm.setTitle("Toby");
        postUpdateForm.setContent("Spring");
        ReflectionTestUtils.setField(postUpdateForm, "postId", 1L);
        return postUpdateForm;
    }


    @Test
    @DisplayName("게시글 생성")
    void createPost() {
        // given
        PostCreateForm postCreateForm = getPostCreateForm();

        User user = getUser();
        Board board = getBoard();
        Post post = getPost();

        given(boardService.findById(1L)).willReturn(board);
        given(userService.findById(1L)).willReturn(user);
        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        Long postId = postService.createPost(postCreateForm);

        // then
        assertThat(postId).isEqualTo(post.getId());
    }

    @Test
    @DisplayName("id로 조회 성공")
    void findByIdSuccess() {
        // given
        Post post = getPost();

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        
        // when
        Post findPost = postService.findById(1L);

        // then
        assertThat(findPost.getId()).isEqualTo(post.getId());
    }
    
    @Test
    @DisplayName("id로 조회 실패")
    void findByIdFail() {
        // given
        given(postRepository.findById(1L)).willReturn(Optional.empty());
        
        // when
        // then
        assertThatThrownBy(() -> postService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no such post");
    }

    @Test
    @DisplayName("조회 dto 변환")
    void toPostInform() {
        // given
        given(postRepository.findById(1L)).willReturn(Optional.of(getPost()));
        given(commentRepository.countByPost_Id(1L)).willReturn(1);
        given(commentRepository.findAllByPostId(1L)).willReturn(List.of(getComment()));
        
        // when
        PostInform postInform = postService.toPostInform(1L);

        // then
        assertThat(postInform.getId()).isEqualTo(1L);
        assertThat(postInform.getCommentCount()).isEqualTo(1);
        assertThat(postInform.getComments().get(0).getId()).isEqualTo(1L);
    }
    
    @Test
    @DisplayName("게시글 수정")
    void updatePost() {
        // given
        PostUpdateForm postUpdateForm = getPostUpdateForm();

        Post post = getPost();

        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        // when
        postService.updatePost(postUpdateForm);
        
        // then
        assertThat(post.getId()).isEqualTo(1L);
        assertThat(post.getTitle()).isEqualTo("Toby");
        assertThat(post.getContent()).isEqualTo("Spring");
    }

    @Test
    @DisplayName("수정 dto 변환")
    void toPostUpdateForm() {
        // given
        given(postRepository.findById(1L)).willReturn(Optional.of(getPost()));
        
        // when
        PostUpdateForm postUpdateForm = postService.toPostUpdateForm(1L);

        // then
        assertThat(postUpdateForm.getTitle()).isEqualTo("JPA");
        assertThat(postUpdateForm.getContent()).isEqualTo("JAVA Persistence API");
    }

//    @Test
//    @DisplayName("")
//    void addLikeSuccess() {
//        // given
//        given(userService.findById(1L)).willReturn(getUser());
//        given(postRepository.findById(1L)).willReturn(Optional.of(getPost()));
//
//        // when
//
//        // then
//    }
}