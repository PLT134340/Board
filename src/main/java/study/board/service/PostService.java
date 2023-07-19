package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import study.board.entity.Board;
import study.board.entity.Post;
import study.board.entity.User;
import study.board.repository.BoardRepository;
import study.board.repository.PostRepository;
import study.board.repository.UserRepository;
import study.board.service.dto.PostForm;
import study.board.service.dto.PostUpdateForm;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class PostService {

    private final UserService userService;
    private final BoardService boardService;

    private final PostRepository postRepository;

    public void createPost(PostForm postForm) {
        Board board = boardService.findById(postForm.getBoardId());
        User user = userService.findById(postForm.getUserId());

        postRepository.save(new Post(postForm.getTitle(), postForm.getContent(), board, user));
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no such post"));
    }
/*

    public PostRequestDto requestPost(Long id) {
        PostRequestDto postRequestDto = postRepository.findPostRequestDto(id);

    }

*/
    public void updatePost(PostUpdateForm postUpdateForm) {
        Post post = findById(postUpdateForm.getPostId());
        Board board = boardService.findById(postUpdateForm.getBoardId());

        post.updatePost(postUpdateForm.getTitle(), postUpdateForm.getContent());
    }

}
