package study.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.entity.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {

}
