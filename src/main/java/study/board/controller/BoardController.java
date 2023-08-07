package study.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.board.common.annotation.AuthUser;
import study.board.service.BoardService;
import study.board.service.CommentService;
import study.board.service.PostService;
import study.board.service.dto.*;
import study.board.service.enumeration.SearchType;

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
                         @AuthUser UserInform userInform, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "boards/createBoardForm";
        }

        Long boardId = boardService.createBoard(form, userInform.getId());

        redirectAttributes.addAttribute("boardId", boardId);

        return "redirect:/boards/{boardId}";
    }

    @GetMapping
    public String search(@RequestParam(value = "boardName", required = false, defaultValue = "") String boardName,
                         Model model) {
        List<BoardInform> boardInforms = boardService.searchByName(boardName);
        model.addAttribute("boardName", boardName);
        model.addAttribute("boardInforms", boardInforms);
        return "boards/boardLists";
    }

    @GetMapping("/{boardId}")
    public String boardInform(@PathVariable("boardId") Long boardId,
                              @RequestParam(value = "type", defaultValue = "TITLE") SearchType type,
                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
                              @PageableDefault(size = 10) Pageable pageable, Model model) {
        BoardInform boardInform = boardService.toBoardInform(boardId);
        PageInform pageInform = postService.searchByKeyword(type, keyword, pageable, boardId);

        model.addAttribute("boardInform", boardInform);
        model.addAttribute("pageInform", pageInform);

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
                             @AuthUser UserInform userInform, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "posts/createPostForm";
        }

        Long postId = postService.createPost(form, boardId, userInform.getId());

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/boards/{boardId}/{postId}";
    }


    @GetMapping("/{boardId}/{postId}")
    public String postInform(@PathVariable("boardId") Long boardId,
                             @PathVariable("postId") Long postId,
                             @AuthUser UserInform userInform,
                             @ModelAttribute("commentCreateForm") CommentCreateForm form, Model model) {
        PostInform postInform = postService.toPostInform(postId);

        model.addAttribute("postInform", postInform);
        model.addAttribute("userInform", userInform);

        return "posts/postInform";
    }

    @GetMapping("/{boardId}/{postId}/edit")
    public String modifyPostForm(@PathVariable("boardId") Long boardId,
                                 @PathVariable("postId") Long postId, Model model) {
        PostUpdateForm form = postService.toPostUpdateForm(postId);

        model.addAttribute("form", form);

        return "posts/modifyPostForm";
    }

    @PostMapping("/{boardId}/{postId}/edit")
    public String modifyPost(@PathVariable("boardId") Long boardId,
                             @PathVariable("postId") Long postId,
                             @Valid @ModelAttribute("form") PostUpdateForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "posts/modifyPostForm";
        }

        form.setPostId(postId);
        postService.updatePost(form);

        return  "redirect:/boards/{boardId}/{postId}";
    }

    @GetMapping("/{boardId}/{postId}/like")
    public String like(@PathVariable("boardId") Long boardId,
                       @PathVariable("postId") Long postId, @AuthUser UserInform userInform,
                       RedirectAttributes redirectAttributes) {
        postService.addLike(postId, userInform.getId());

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/boards/{boardId}/{postId}";
    }

    @PostMapping("/{boardId}/{postId}/comment")
    public String comment(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId,
                          @ModelAttribute("comment") CommentCreateForm form, @AuthUser UserInform userInform,
                          RedirectAttributes redirectAttributes) {
        postService.saveComment(form, userInform.getId(), postId);

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/boards/{boardId}/{postId}";
    }

    @PostMapping("/{boardId}/{postId}/{commentId}/recomment")
    public String recomment(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId,
                            @PathVariable("commentId") Long commentId, @AuthUser UserInform userInform,
                            @ModelAttribute("recomment") RecommentCreateForm form, RedirectAttributes redirectAttributes) {
        postService.saveRecomment(form, userInform.getId(), postId, commentId);

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/boards/{boardId}/{postId}";
    }

}
