package study.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.entity.LikeInform;
import study.board.entity.User;

import java.util.List;

public interface LikeInformRepository extends JpaRepository<LikeInform, Long> {

    List<LikeInform> findByPost_Id(Long postId);
    int countByPost_Id(Long postId);

}
