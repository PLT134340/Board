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
import study.board.service.PostService;
import study.board.service.dto.board.BoardCreateForm;
import study.board.service.dto.board.BoardInform;
import study.board.service.dto.board.PageInform;
import study.board.service.dto.post.PostCreateForm;
import study.board.service.dto.user.UserInform;
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
    public String search(@RequestParam(value = "boardName", defaultValue = "") String boardName,
                         Model model) {
        List<BoardInform> boardInforms = boardService.searchByName(boardName);
        model.addAttribute("boardInforms", boardInforms);

        return "boards/boardLists";
    }

    @GetMapping("/{boardId}")
    public String boardInform(@PathVariable("boardId") Long boardId,
                              @RequestParam(value = "type", defaultValue = "TITLE") SearchType type,
                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
                              @PageableDefault(size = 10) Pageable pageable, Model model) {
        BoardInform boardInform = boardService.toBoardInform(boardId);
        PageInform pageInform = postService.getPageInformByKeyword(type, keyword, pageable, boardId);

        model.addAttribute("boardInform", boardInform);
        model.addAttribute("pageInform", pageInform);

        return "boards/boardInform";
    }

    @GetMapping("/{boardId}/write")
    public String createPostForm(@PathVariable("boardId") Long boardId, @ModelAttribute("form") PostCreateForm form) {
        return "posts/createPostForm";
    }

    @PostMapping("{boardId}/write")
    public String createPost(@PathVariable("boardId") Long boardId,
                             @Valid @ModelAttribute("form") PostCreateForm form, BindingResult result,
                             @AuthUser UserInform userInform, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "posts/createPostForm";
        }

        Long postId = postService.createPost(form, boardId, userInform.getId()).getId();

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/posts/{postId}";
    }

    @GetMapping("/hotboard")
    public String hotBoard(@PageableDefault(size = 10) Pageable pageable, Model model) {
        PageInform pageInform = postService.getHotPostPageInform(pageable);

        model.addAttribute("pageInform", pageInform);

        return "boards/hotBoardInform";
    }

}
