package study.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import study.board.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByBoard_IdAndTitleContaining(Long boardId, String title, Pageable pageable);
    Page<Post> findByBoard_IdAndContentContaining(Long boardId, String content, Pageable pageable);
    Page<Post> findByBoard_IdAndUser_UsernameContaining(Long boardId, String username, Pageable pageable);

}
