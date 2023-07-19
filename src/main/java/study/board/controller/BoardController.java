package study.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import study.board.service.BoardService;
import study.board.service.PostService;
import study.board.service.dto.BoardForm;

import java.util.List;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final PostService postService;

    @GetMapping("/create")
    public String createForm(BoardForm boardForm) {
        return "boards/createBoardForm";
    }

    @PostMapping("/create")
    public String create(@Valid BoardForm boardForm, BindingResult result) {
        if (result.hasErrors()) {
            return "boards/createBoardForm";
        }

        boardService.createBoard(boardForm);
        return "redirect:/boards";
    }

    @GetMapping
    public String search(@RequestParam(value = "name", required = false, defaultValue = "") String name, Model model) {
        List<String> boards = boardService.nameSearch(name);
        model.addAttribute("boards", boards);
        return "boards/boardLists";
    }

    @GetMapping("/{boardName}")
    public String boardInform(@PathVariable("boardName") String boardName, Model model) {
        model.addAttribute(boardService.boardInform(boardName));
        return "boards/boardPage";
    }



//    @GetMapping("/{boardName}/{postId}")




}
