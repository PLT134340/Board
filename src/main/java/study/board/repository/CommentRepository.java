package study.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.board.entity.comment.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c left join fetch c.recomments where c.post.id = :id")
    List<Comment> findAllByPostId(@Param("id") Long postId);
    int countByPost_Id(Long postId);
}
