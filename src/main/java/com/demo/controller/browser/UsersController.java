package com.demo.controller.browser;

import com.demo.model.user.UserEntity;
import com.demo.service.layer.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping("/admin/allUsers")
    public String allUsers(Model model) {
        List<UserEntity> usersList = userService.getAllUsers();
        model.addAttribute("usersList", usersList);
        return "users";
    }
}
