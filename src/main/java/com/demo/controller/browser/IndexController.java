package com.demo.controller.browser;

import com.demo.config.UserDetailsService;
import com.demo.exception.user.UserNotExistsException;
import com.demo.model.user.UserEntity;
import com.demo.model.user.UserRole;
import com.demo.service.layer.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class IndexController {

    private UserDetailsService userDetailsService;
    private UserService userService;

    @GetMapping("/index")
    public String index() throws UserNotExistsException {
        String username = userDetailsService.getUsername();
        UserEntity user = userService.getUserByUsername(username);
        if (user.getRole() == UserRole.ROLE_USER) {
            return "redirect:/products";
        } else {
            return "redirect:/admin";
        }
    }
}
