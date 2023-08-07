package study.board;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import study.board.entity.Board;
import study.board.entity.comment.Comment;
import study.board.entity.Post;
import study.board.entity.User;
import study.board.entity.comment.Recomment;
import study.board.repository.BoardRepository;
import study.board.repository.CommentRepository;
import study.board.repository.UserRepository;

//@Component
@RequiredArgsConstructor
public class BoardInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        // 더미 데이터 작성
        User kim = new User("kim", passwordEncoder.encode("1234"));
        User lee = new User("lee", passwordEncoder.encode("2345"));

        Board jpa = new Board("jpa", "orm", kim);
        Board toby = new Board("toby", "spring 3.1", lee);

        Post hello = new Post("hello", "world", jpa, kim);


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