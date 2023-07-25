package study.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import study.board.common.argumentresolver.Login;
import study.board.service.dto.UserInform;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(@Login UserInform userInform, Model model) {
        if (userInform == null) {
            return "home";
        }

        model.addAttribute("userInform", userInform);
        return "loginHome";
    }
}
