package study.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.board.common.annotation.AuthUser;
import study.board.service.PostService;
import study.board.service.dto.comment.CommentCreateForm;
import study.board.service.dto.comment.RecommentCreateForm;
import study.board.service.dto.post.PostInform;
import study.board.service.dto.post.PostUpdateForm;
import study.board.service.dto.user.UserInform;

@Controller
@RequestMapping("/boards/{boardId}/{postId}")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public String postInform(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId,
                             @AuthUser UserInform userInform,
                             @ModelAttribute("commentCreateForm") CommentCreateForm form, Model model) {
        PostInform postInform = postService.toPostInform(postId);

        model.addAttribute("postInform", postInform);
        model.addAttribute("userInform", userInform);

        return "posts/postInform";
    }

    @GetMapping("/edit")
    public String modifyPostForm(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId,
                                 Model model) {
        PostUpdateForm form = postService.toPostUpdateForm(postId);

        model.addAttribute("form", form);

        return "posts/modifyPostForm";
    }

    @PostMapping("/edit")
    public String modifyPost(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId,
                             @Valid @ModelAttribute("form") PostUpdateForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "posts/modifyPostForm";
        }

        form.setPostId(postId);
        postService.updatePost(form);

        return  "redirect:/boards/{boardId}/{postId}";
    }

    @GetMapping("/like")
    public String like(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId,
                       @AuthUser UserInform userInform,
                       RedirectAttributes redirectAttributes) {
        postService.addLike(postId, userInform.getId());

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/boards/{boardId}/{postId}";
    }

    @PostMapping("/comment")
    public String comment(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId,
                          @ModelAttribute("comment") CommentCreateForm form, @AuthUser UserInform userInform,
                          RedirectAttributes redirectAttributes) {
        postService.saveComment(form, userInform.getId(), postId);

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/boards/{boardId}/{postId}";
    }

    @PostMapping("/{commentId}/recomment")
    public String recomment(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId,
                            @PathVariable("commentId") Long commentId, @AuthUser UserInform userInform,
                            @ModelAttribute("recomment") RecommentCreateForm form, RedirectAttributes redirectAttributes) {
        postService.saveRecomment(form, userInform.getId(), postId, commentId);

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/boards/{boardId}/{postId}";
    }

    @PostMapping("/{commentId}/remove")
    public String remove(@PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId,
                         @PathVariable("commentId") Long commentId, @AuthUser UserInform userInform,
                         RedirectAttributes redirectAttributes) {
        postService.removeComment(userInform.getId(), postId, commentId);

        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addAttribute("postId", postId);

        return "redirect:/boards/{boardId}/{postId}";
    }
}
