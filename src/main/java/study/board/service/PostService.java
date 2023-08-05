package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.*;
import study.board.entity.comment.Comment;
import study.board.repository.PostRepository;
import study.board.service.dto.*;
import study.board.service.enumeration.SearchType;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final UserService userService;
    private final BoardService boardService;
    private final CommentService commentService;

    private final PostRepository postRepository;


    public Long createPost(PostCreateForm form, Long boardId, Long userId) {
        Board board = boardService.findById(boardId);
        User user = userService.findById(userId);

        return postRepository.save(new Post(form.getTitle(), form.getContent(), board, user)).getId();
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no such post"));
    }

    public PageInform searchByKeyword(SearchType type, String keyword, Pageable pageable, Long boardId) {
        Page<Post> page;
        if(type == SearchType.TITLE) {
            page = postRepository.findByBoard_IdAndTitleContaining(boardId, keyword, pageable);
        } else if (type == SearchType.CONTENT) {
            page = postRepository.findByBoard_IdAndContentContaining(boardId, keyword, pageable);
        } else if (type == SearchType.USERNAME) {
            page = postRepository.findByBoard_IdAndUser_UsernameContaining(boardId, keyword, pageable);
        } else {
            throw new IllegalStateException("no such type");
        }
        return new PageInform(page, type, keyword);
    }

    public PostInform toPostInform(Long id) {
        Post post = findById(id);

        List<Comment> comments = commentService.findAllByPostId(id);

        return new PostInform(post, comments, commentService.getCount(comments));
    }

    public PostInform toPostSummaryInform(Long id) {
        Post post = findById(id);

        List<Comment> comments = commentService.findAllByPostId(id);

        return new PostInform(post, comments, commentService.getCount(comments));
    }

    public void updatePost(PostUpdateForm postUpdateForm) {
        Post post = findById(postUpdateForm.getPostId());

        post.updatePost(postUpdateForm.getTitle(), postUpdateForm.getContent());
    }

    public PostUpdateForm toPostUpdateForm(Long id) {
        return new PostUpdateForm(findById(id));
    }

    public void addLike(Long postId, Long userId) {
        User user = userService.findById(userId);
        Post post = findById(postId);

        if(post.getLike().getUsers().contains(user)) {
            throw new IllegalArgumentException("already like");
        }

        findById(postId).getLike().addLike(user);
    }

}
