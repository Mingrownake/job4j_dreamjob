package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.model.User;
import ru.job4j.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String getRegistrationPage() {
        return "users/register";
    }

    @PostMapping("/registration")
    public String register(Model model, @ModelAttribute User user) {
        var newUser = userService.save(user);
        if (newUser.isEmpty()) {
            model.addAttribute("message", "Пользователь с данным email: "
                    + user.getEmail() + " уже существует");
            return "error/404";
        }
        return "redirect:/vacancies";
    }
}
