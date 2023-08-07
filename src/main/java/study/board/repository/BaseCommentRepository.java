package study.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.entity.comment.BaseComment;

public interface BaseCommentRepository extends JpaRepository<BaseComment, Long> {
}
