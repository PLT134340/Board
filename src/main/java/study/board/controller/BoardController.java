package study.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.board.common.argumentresolver.Login;
import study.board.service.BoardService;
import study.board.service.PostService;
import study.board.service.UserService;
import study.board.service.dto.*;

import java.util.List;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final PostService postService;

    @GetMapping("/create")
    public String createForm(@ModelAttribute("form") BoardCreateForm boardCreateForm) {
        return "boards/createBoardForm";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form") BoardCreateForm form, BindingResult result,
                         @Login UserInform userInform, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "boards/createBoardForm";
        }

        form.setUserId(userInform.getId());
        Long boardId = boardService.createBoard(form);

        redirectAttributes.addAttribute("boardId", boardId);

        return "redirect:/boards/{boardId}";
    }

    @GetMapping
    public String search(@RequestParam(value = "boardName", required = false, defaultValue = "") String boardName,
                         Model model) {
        List<BoardListInform> boardInforms = boardService.searchByName(boardName);
        model.addAttribute("boardInforms", boardInforms);
        return "boards/boardLists";
    }

    @GetMapping("/{boardId}")
    public String boardInform(@PathVariable("boardId") Long boardId,
                              @RequestParam(value = "title", defaultValue = "") String title,
                              @PageableDefault(size = 10) Pageable pageable, Model model) {
        BoardInform boardInform = new BoardInform(boardService.findById(boardId));

        Page<PostSummaryInform> page = postService.searchByTitle(title, pageable);
        List<PostSummaryInform> posts = page.getContent();

        model.addAttribute("boardInform", boardInform);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("total", page.getTotalPages());
        model.addAttribute("posts", posts);

        return "boards/boardInform";
    }

    @GetMapping("/{boardId}/write")
    public String createPostForm(@PathVariable("boardId") Long boardId, @ModelAttribute("form") PostCreateForm form,
                                 Model model) {
        model.addAttribute("boardId", boardId);
        return "posts/createPostForm";
    }

    @PostMapping("{boardId}/write")
    public String createPost(@PathVariable("boardId") Long boardId,
                             @Valid @ModelAttribute("form") PostCreateForm form, BindingResult result,
                             @Login UserInform userInform, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "posts/createPostForm";
        }

        form.setUserId(userInform.getId());
        form.setBoardId(boardId);
        Long postId = postService.createPost(form);

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/boards/{boardId}/{postId}";
    }


    @GetMapping("/{boardId}/{postId}")
    public String postInform(@PathVariable("boardId") Long boardId,
                             @PathVariable("postId") Long postId,
                             @ModelAttribute("commentForm") CommentForm form,
                             Model model) {
        PostInform postInform = postService.toPostInform(postId);

        model.addAttribute("postInform", postInform);

        return "posts/postInform";
    }

    @GetMapping("/{boardId}/{postId}/like")
    public String like(@PathVariable("boardId") Long boardId,
                       @PathVariable("postId") Long postId,
                       RedirectAttributes redirectAttributes) {
        postService.addLike(postId);

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/boards/{boardId}/{postId}";
    }

    @PostMapping("/{boardId}/{postId}/comment")
    public String comment(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId,
                          @ModelAttribute("comment") CommentForm form, @Login UserInform userInform,
                          RedirectAttributes redirectAttributes) {
        form.setUserId(userInform.getId());
        postService.saveComment(form);

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/boards/{boardId}/{postId}";
    }



}
