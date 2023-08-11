package study.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.board.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByBoard_IdAndTitleContaining(Long boardId, String title, Pageable pageable);
    Page<Post> findByBoard_IdAndContentContaining(Long boardId, String content, Pageable pageable);
    Page<Post> findByBoard_IdAndUser_UsernameContaining(Long boardId, String username, Pageable pageable);
    Page<Post> findByUser_Id(Long userId, Pageable pageable);
    @Query("select p from Post p where p.id in" +
            " (select distinct bc.post.id from BaseComment bc where bc.isRemoved = false and bc.user.id = :userId)")
    Page<Post> findByCommentUserId(@Param("userId") Long userId, Pageable pageable);
    @Query("select p from Post p where (select count(*) from Like li where p.id = li.post.id) >= 10")
    Page<Post> findByLikeGreaterThanEqual10(Pageable pageable);
}
