package study.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContaining(String title);

}
