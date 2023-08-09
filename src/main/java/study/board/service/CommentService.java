package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.comment.BaseComment;
import study.board.entity.comment.Comment;
import study.board.entity.Post;
import study.board.entity.User;
import study.board.entity.comment.Recomment;
import study.board.repository.BaseCommentRepository;
import study.board.repository.CommentRepository;
import study.board.repository.RecommentRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final BaseCommentRepository baseCommentRepository;
    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;

    public BaseComment findById(Long id) {
        return baseCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("no such base comment"));
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("no such comment"));
    }

    public Recomment findRecommentById(Long recommentId) {
        return recommentRepository.findById(recommentId)
                .orElseThrow(() -> new IllegalStateException("no such recomment"));
    }

    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public Comment saveComment(String content, User user, Post post) {
        return commentRepository.save(new Comment(content, user, post));
    }

    public Recomment saveRecomment(String content, User user, Comment comment) {
        Recomment recomment = new Recomment(content, user, comment);
        comment.getRecomments().add(recomment);
        return recomment;
    }

    public void removeComment(Long userId,  Long commentId) {
        BaseComment baseComment = findById(commentId);
        validateCorrectUser(userId, baseComment);

        baseCommentRepository.delete(baseComment);
    }

    private void validateCorrectUser(Long userId, BaseComment baseComment) {
        if (!baseComment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("improper comment remove request");
        }
    }

}
