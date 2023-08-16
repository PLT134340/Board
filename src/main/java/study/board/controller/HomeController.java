package study.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.board.common.annotation.AuthUser;
import study.board.service.UserService;
import study.board.service.dto.user.UserCreateForm;
import study.board.service.dto.user.UserInform;
import study.board.service.dto.user.UserLoginForm;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping("/")
    public String home(@AuthUser UserInform userInform, Model model) {
        if (userInform == null) {
            return "home";
        }

        model.addAttribute("userInform", userInform);
        return "loginHome";
    }

    @GetMapping("/sign-up")
    public String joinForm(@ModelAttribute("form") UserCreateForm form) {
        return "users/createUserForm";
    }

    @PostMapping("/sign-up")
    public String join(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult result,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "users/createUserForm";
        }

        userService.join(form);

        redirectAttributes.addAttribute("status", true);

        return "redirect:/sign-in";
    }

    @GetMapping("/sign-in")
    public String loginForm(HttpServletRequest request, @ModelAttribute("form") UserLoginForm form) {
        String prevPage = (String) request.getSession().getAttribute("prevPage");
        String uri = request.getHeader("Referer");

        if (prevPage == null && uri != null && !uri.contains("users/sign-in")) {
            request.getSession().setAttribute("prevPage", uri);
        }

        return "users/loginForm";
    }

}
