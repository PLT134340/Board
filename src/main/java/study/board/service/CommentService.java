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

    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("no such comment"));
    }

    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public Comment saveComment(String content, User user, Post post) {
        return commentRepository.save(new Comment(content, user, post));
    }

    public Recomment saveRecomment(String content, User user, Comment comment) {
        return recommentRepository.save(new Recomment(content, user, comment));
    }

}
