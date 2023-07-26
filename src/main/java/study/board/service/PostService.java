package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import study.board.entity.Board;
import study.board.entity.Comment;
import study.board.entity.Post;
import study.board.entity.User;
import study.board.repository.CommentRepository;
import study.board.repository.PostRepository;
import study.board.service.dto.*;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class PostService {

    private final UserService userService;
    private final BoardService boardService;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Long createPost(PostCreateForm form) {
        Board board = boardService.findById(form.getBoardId());
        User user = userService.findById(form.getUserId());

        return postRepository.save(new Post(form.getTitle(), form.getContent(), board, user)).getId();
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no such post"));
    }

    public PageInform searchByKeyword(String type, String keyword, Pageable pageable) {
        if(type.equals("title")) {
            return new PageInform(postRepository.findByTitleContaining(keyword, pageable), type, keyword);
        } else {
            return new PageInform(postRepository.findByContentContaining(keyword, pageable), type, keyword);
        }
    }


    public PostSummaryInform toPostSummaryInform(Long id) {
        return new PostSummaryInform(findById(id));
    }

    public PostInform toPostInform(Long id) {
        return new PostInform(findById(id));
    }

    public void updatePost(PostUpdateForm postUpdateForm) {
        Post post = findById(postUpdateForm.getPostId());
        Board board = boardService.findById(postUpdateForm.getBoardId());

        post.updatePost(postUpdateForm.getTitle(), postUpdateForm.getContent());
    }

    public void addLike(Long id) {
        findById(id).addLike();
    }

    public void saveComment(CommentForm form) {
        User user = userService.findById(form.getUserId());
        Post post = findById(form.getPostId());
        commentRepository.save(new Comment(form.getContent(), user, post));
    }

}
