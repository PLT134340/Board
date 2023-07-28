package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.Board;
import study.board.entity.Comment;
import study.board.entity.Post;
import study.board.entity.User;
import study.board.repository.CommentRepository;
import study.board.repository.PostRepository;
import study.board.service.dto.*;
import study.board.service.enumeration.SearchType;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final UserService userService;
    private final BoardService boardService;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Long createPost(PostCreateForm form) {
        Board board = boardService.findById(form.getBoardId());
        User user = userService.findById(form.getUserId());

        return postRepository.save(new Post(form.getTitle(), form.getContent(), board, user)).getId();
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no such post"));
    }

    public PageInform searchByKeyword(SearchType type, String keyword, Pageable pageable) {
        if(type == SearchType.TITLE) {
            return new PageInform(postRepository.findByTitleContaining(keyword, pageable), type, keyword);
        } else if (type == SearchType.CONTENT) {
            return new PageInform(postRepository.findByContentContaining(keyword, pageable), type, keyword);
        } else if (type == SearchType.USERNAME) {
            return new PageInform(postRepository.findByUser_UsernameContaining(keyword, pageable), type, keyword);
        } else {
            throw new IllegalStateException("no such type");
        }
    }

    public PostInform toPostInform(Long id) {
        Post post = findById(id);

        int count = commentRepository.countByPost_Id(id);
        List<Comment> comments = commentRepository.findAllByPostId(id);

        post.mergeComments(comments);

        return new PostInform(post, count);
    }

    public void updatePost(PostUpdateForm postUpdateForm) {
        Post post = findById(postUpdateForm.getPostId());

        post.updatePost(postUpdateForm.getTitle(), postUpdateForm.getContent());
    }

    public PostUpdateForm toPostUpdateForm(Long id) {
        return new PostUpdateForm(findById(id));
    }

    public void addLike(Long id) {
        findById(id).addLike();
    }

}
