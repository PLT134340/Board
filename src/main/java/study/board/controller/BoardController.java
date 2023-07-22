package study.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/create")
    public String createForm(@ModelAttribute("form") BoardCreateForm boardCreateForm, Model model) {
        List<UserInform> userInforms = userService.findAllToUserInform();
        model.addAttribute("userInforms", userInforms);
        return "boards/createBoardForm";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form") BoardCreateForm form, BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "boards/createBoardForm";
        }

        Long boardId = boardService.createBoard(form);

        redirectAttributes.addAttribute("boardId", boardId);

        return "redirect:/boards/{boardId}";
    }

    @GetMapping
    public String search(@RequestParam(value = "boardName", required = false, defaultValue = "") String boardName, Model model) {
        List<BoardListInform> boardInforms = boardService.searchByName(boardName);
        model.addAttribute("boardInforms", boardInforms);
        return "boards/boardLists";
    }

    @GetMapping("/{boardId}")
    public String boardInform(@PathVariable("boardId") Long boardId, Model model) {
        model.addAttribute("boardInform", new BoardInform(boardService.findById(boardId)));
        return "boards/boardInform";
    }

    @GetMapping("/{boardId}/write")
    public String createPostForm(@PathVariable("boardId") Long boardId,
                             @ModelAttribute("form") PostCreateForm form,
                             Model model) {
        form.setBoardId(boardId);
        List<UserInform> userInforms = userService.findAllToUserInform();
        model.addAttribute("userInforms", userInforms);
        return "posts/createPostForm";
    }

    @PostMapping("/write")
    public String createPost(@Valid @ModelAttribute("form") PostCreateForm form, BindingResult result,
                             RedirectAttributes redirectAttributes) {
        Long postId = postService.createPost(form);

        redirectAttributes.addAttribute("boardId", form.getBoardId());
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/boards/{boardId}/{postId}";
    }


    @GetMapping("/{boardId}/{postId}")
    public String postInform(@PathVariable("boardId") Long boardId,
                             @PathVariable("postId") Long postId,
                             @ModelAttribute("commentForm") CommentForm commentForm,
                             Model model) {
        PostInform postInform = postService.toPostInform(postId);
        List<UserInform> userInforms = userService.findAllToUserInform();

        model.addAttribute("postInform", postInform);
        model.addAttribute("userInforms", userInforms);

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
    public String comment(@PathVariable("boardId") Long boardId,
                          @PathVariable("postId") Long postId,
                          @ModelAttribute("comment") CommentForm comment,
                          RedirectAttributes redirectAttributes) {
        postService.saveComment(comment);
        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/boards/{boardId}/{postId}";
    }



}
