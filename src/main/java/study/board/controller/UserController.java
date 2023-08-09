package study.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.board.service.UserService;
import study.board.service.dto.user.UserUpdateForm;
import study.board.service.dto.user.UserInform;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String userList(Model model) {
        List<UserInform> users = userService.findAllToUserInform();
        model.addAttribute("users", users);
        return "users/userList";
    }

    @GetMapping("/{userId}")
    public String userInform(@PathVariable("userId") Long userId, Model model) {
        UserInform user = userService.toUserInform(userId);
        model.addAttribute("user", user);
        return "users/userInform";
    }


    @GetMapping("/{userId}/edit")
    public String modifyForm(@PathVariable("userId") Long userId, Model model) {

        UserUpdateForm form = userService.toUserUpdateForm(userId);
        model.addAttribute("form", form);

        return "users/modifyUserForm";
    }

    @PostMapping("/{userId}/edit")
    public String modify(@PathVariable("userId") Long userId,
                         @Valid @ModelAttribute("form") UserUpdateForm form, BindingResult result,
                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "users/modifyUserForm";
        }

        userService.modify(form, userId);
        redirectAttributes.addAttribute("status", true);

        return "redirect:/users/{userId}";
    }

    @PostMapping("/{userId}/delete")
    public String withdraw(@PathVariable("userId") Long userId) {
        userService.withdraw(userId);
        return "redirect:/";
    }

}
