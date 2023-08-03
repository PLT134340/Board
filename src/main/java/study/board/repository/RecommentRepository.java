package study.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.entity.comment.Recomment;

public interface RecommentRepository extends JpaRepository<Recomment, Long> {

    int countByPost_Id(Long postId);

}
