package study.board.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import study.board.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByName(String name);
    List<Board> findByNameContaining(String name);

}
