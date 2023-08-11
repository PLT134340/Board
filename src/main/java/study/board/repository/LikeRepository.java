package study.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.entity.Like;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByPost_Id(Long postId);
    Long countByPost_Id(Long postId);

}
