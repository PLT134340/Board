package study.board;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import study.board.entity.Board;
import study.board.entity.Comment;
import study.board.entity.Post;
import study.board.entity.User;
import study.board.repository.BoardRepository;
import study.board.repository.PostRepository;
import study.board.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    @Override
    public void run(ApplicationArguments args) {
        // 더미 데이터 작성
        User kim = new User("kim", passwordEncoder.encode("1234"));
        User lee = new User("lee", passwordEncoder.encode("2345"));

        Board jpa = new Board("jpa", "orm", kim);
        Board toby = new Board("toby", "spring 3.1", lee);

        Post hello = new Post("hello", "world", jpa, kim);

        Comment comment1 = new Comment("1", kim, hello);
        Comment comment2 = new Comment("2", lee, hello);
        Comment comment3 = new Comment("3", kim, hello);
        Recomment comment4 = new Recomment("4", kim, comment1);
        Recomment comment5 = new Recomment("5", kim, comment1);
        Recomment comment6 = new Recomment("6", kim, comment1);

        comment1.getRecomments().add(comment4);
        comment1.getRecomments().add(comment5);
        comment1.getRecomments().add(comment6);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        jpa.getPosts().add(hello);

        for (int i = 0; i < 200; i++) {
            jpa.getPosts().add(new Post(hello.getTitle(), hello.getContent(), hello.getBoard(), hello.getUser()));
        }

        // 더미 데이터 저장
        userRepository.save(kim);
        userRepository.save(lee);
        boardRepository.save(jpa);
        boardRepository.save(toby);
    }

}