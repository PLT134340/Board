package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.Comment;
import study.board.entity.Post;
import study.board.entity.User;
import study.board.repository.CommentRepository;
import study.board.service.dto.CommentForm;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final PostService postService;

    private final CommentRepository commentRepository;

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("no such comment"));
    }

    public Comment saveComment(CommentForm form) {
        User user = userService.findById(form.getUserId());
        Post post = postService.findById(form.getPostId());
        Comment comment = commentRepository.findById(form.getCommentId()).orElse(null);
        return commentRepository.save(new Comment(form.getContent(), user, post, comment));
    }


}
