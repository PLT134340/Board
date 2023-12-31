package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.common.exception.CustomIllegalArgumentException;
import study.board.entity.comment.BaseComment;
import study.board.entity.comment.Comment;
import study.board.entity.Post;
import study.board.entity.User;
import study.board.entity.comment.Recomment;
import study.board.repository.BaseCommentRepository;
import study.board.repository.CommentRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final BaseCommentRepository baseCommentRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public BaseComment findById(Long id) {
        return baseCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("no such base comment"));
    }

    @Transactional(readOnly = true)
    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("no such comment"));
    }

    @Transactional(readOnly = true)
    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public Comment saveComment(String content, User user, Post post) {
        return commentRepository.save(new Comment(content, user, post));
    }

    public Recomment saveRecomment(String content, User user, Post post, Comment comment) {
        Recomment recomment = new Recomment(content, user, post, comment);
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
            throw new CustomIllegalArgumentException("improper comment remove request");
        }
    }

    @Transactional(readOnly = true)
    public Long countByPostId(Long postId) {
        return baseCommentRepository.countByPost_Id(postId);
    }

}
