package study.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.board.entity.comment.BaseComment;

public interface BaseCommentRepository extends JpaRepository<BaseComment, Long> {

    @Query("select count(*) from BaseComment bc where bc.post.id = :postId and bc.isRemoved = false")
    int countByPost_Id(@Param("postId") Long postId);

}
