package com.demo.controller.browser;

import com.demo.dto.RegisterDTO;
import com.demo.dto.UserDTO;
import com.demo.exception.user.UserAlreadyExistsException;
import com.demo.service.layer.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String username,
                           @RequestParam String password) throws UserAlreadyExistsException {

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setUsername(username);
        userDTO.setPassword(password);

        if (userService.saveUser(userDTO))
            return "redirect:/login?register";
        return "redirect:/register?error";
    }
}
