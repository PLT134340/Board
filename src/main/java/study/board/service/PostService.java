package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.common.exception.LikeDuplicateException;
import study.board.common.exception.CustomIllegalArgumentException;
import study.board.entity.*;
import study.board.entity.comment.Comment;
import study.board.entity.comment.Recomment;
import study.board.repository.LikeRepository;
import study.board.repository.PostRepository;
import study.board.service.dto.board.PageInform;
import study.board.service.dto.comment.CommentCreateForm;
import study.board.service.dto.comment.RecommentCreateForm;
import study.board.service.dto.post.PostCreateForm;
import study.board.service.dto.post.PostInform;
import study.board.service.dto.post.PostSummaryInform;
import study.board.service.dto.post.PostUpdateForm;
import study.board.service.enumeration.SearchType;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final UserService userService;
    private final BoardService boardService;
    private final CommentService commentService;

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;


    public Post createPost(PostCreateForm form, Long boardId, Long userId) {
        Board board = boardService.findById(boardId);
        User user = userService.findById(userId);

        return postRepository.save(new Post(form.getTitle(), form.getContent(), board, user));
    }

    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no such post"));
    }

    @Transactional(readOnly = true)
    public PageInform getPageInformByKeyword(SearchType type, String keyword, Pageable pageable, Long boardId) {
        Page<Post> page;
        if(type == SearchType.TITLE) {
            page = postRepository.findByBoard_IdAndTitleContaining(boardId, keyword, pageable);
        } else if (type == SearchType.CONTENT) {
            page = postRepository.findByBoard_IdAndContentContaining(boardId, keyword, pageable);
        } else if (type == SearchType.USERNAME) {
            page = postRepository.findByBoard_IdAndUser_UsernameContaining(boardId, keyword, pageable);
        } else {
            throw new CustomIllegalArgumentException("no such search type");
        }
        return toPageInform(page);
    }

    @Transactional(readOnly = true)
    public PageInform toPageInform(Page<Post> page) {
        List<PostSummaryInform> posts = page.getContent()
                .stream()
                .map(post -> new PostSummaryInform(post, commentService.countByPostId(post.getId()), likeRepository.countByPost_Id(post.getId())))
                .collect(Collectors.toList());
        return new PageInform(page, posts);
    }

    @Transactional(readOnly = true)
    public PostInform toPostInform(Long id) {
        Post post = findById(id);

        List<Comment> comments = commentService.findAllByPostId(id);
        Long commentCount = commentService.countByPostId(id);
        Long likeCount = likeRepository.countByPost_Id(id);

        return new PostInform(post, comments, commentCount, likeCount);
    }

    public void updatePost(PostUpdateForm postUpdateForm, Long postId) {
        findById(postId).updatePost(postUpdateForm.getTitle(), postUpdateForm.getContent());
    }

    @Transactional(readOnly = true)
    public PostUpdateForm toPostUpdateForm(Long id) {
        return new PostUpdateForm(findById(id));
    }

    public void addLike(Long postId, Long userId) {
        User user = userService.findById(userId);
        List<Like> likes = likeRepository.findByPost_Id(postId);

        if (likes.stream()
                .anyMatch(li -> li.getUser().equals(user))) {
            throw new LikeDuplicateException("postId=" + postId.toString() + " " + "userId=" + userId);
        }

        Post post = findById(postId);
        likeRepository.save(new Like(post, user));
    }

    public Comment saveComment(CommentCreateForm form, Long userId, Long postId) {
        User user = userService.findById(userId);
        Post post = findById(postId);
        return commentService.saveComment(form.getContent(), user, post);
    }

    public Recomment saveRecomment(RecommentCreateForm form, Long userId, Long postId, Long commentId) {
        User user = userService.findById(userId);
        Post post = findById(postId);
        Comment comment = commentService.findCommentById(commentId);
        return commentService.saveRecomment(form.getContent(), user, post, comment);
    }

    public void removeComment(Long userId, Long commentId) {
        commentService.removeComment(userId, commentId);
    }

    @Transactional(readOnly = true)
    public PageInform getPostPageInformByUserId(Long userId, Pageable pageable) {
        return toPageInform(postRepository.findByUser_Id(userId, pageable));
    }

    @Transactional(readOnly = true)
    public PageInform getPostPageInformByCommentUserId(Long userId, Pageable pageable) {
        return toPageInform(postRepository.findByCommentUserId(userId, pageable));
    }

    @Transactional(readOnly = true)
    public PageInform getHotPostPageInform(Pageable pageable) {
        return toPageInform(postRepository.findByLikeGreaterThanEqual10(pageable));
    }

}
