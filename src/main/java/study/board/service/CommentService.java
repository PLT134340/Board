package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.comment.Comment;
import study.board.entity.Post;
import study.board.entity.User;
import study.board.entity.comment.Recomment;
import study.board.repository.CommentRepository;
import study.board.repository.RecommentRepository;
import study.board.service.dto.CommentForm;
import study.board.service.dto.RecommentForm;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final PostService postService;

    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("no such comment"));
    }

    public Comment saveComment(CommentForm form) {
        User user = userService.findById(form.getUserId());
        Post post = postService.findById(form.getPostId());

        return commentRepository.save(new Comment(form.getContent(), user, post));
    }

    public Recomment saveRecomment(RecommentForm form) {
        User user = userService.findById(form.getUserId());
        Post post = postService.findById(form.getPostId());
        Comment comment = findById(form.getCommentId());

        return recommentRepository.save(new Recomment(form.getContent(), user, post, comment));
    }

}
