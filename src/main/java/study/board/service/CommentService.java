package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.comment.Comment;
import study.board.entity.Post;
import study.board.entity.User;
import study.board.entity.comment.Recomment;
import study.board.repository.CommentRepository;
import study.board.repository.PostRepository;
import study.board.repository.RecommentRepository;
import study.board.service.dto.CommentCreateForm;
import study.board.service.dto.RecommentCreateForm;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("no such comment"));
    }

    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public Comment saveComment(CommentCreateForm form, Long userId, Long postId) {
        User user = userService.findById(userId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("no such post"));

        return commentRepository.save(new Comment(form.getContent(), user, post));
    }

    public Recomment saveRecomment(RecommentCreateForm form, Long userId, Long commentId) {
        User user = userService.findById(userId);
        Comment comment = findById(commentId);

        return recommentRepository.save(new Recomment(form.getContent(), user, comment));
    }

    public int getCount(List<Comment> comments) {
        return comments.size() + comments.stream()
                .mapToInt(comment -> comment.getRecomments().size())
                .sum();
    }
}
