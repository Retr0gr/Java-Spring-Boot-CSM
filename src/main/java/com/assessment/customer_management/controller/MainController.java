
package com.assessment.customer_management.controller;

import com.assessment.customer_management.dto.UserDto;
import com.assessment.customer_management.model.User;
import com.assessment.customer_management.repository.CustomerRepository;
import com.assessment.customer_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    //Login
    @GetMapping("/")
    public String home() {return "auth/login";}


    //Register
    @GetMapping("/register")
    public String register(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "auth/register";
    }

    @PostMapping("/register")
    public String register_save(@ModelAttribute UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
        return "auth/register";
    }

    @GetMapping("login")
    public String login() {return "auth/login";}

}
