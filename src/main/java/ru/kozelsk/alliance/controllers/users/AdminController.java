package ru.kozelsk.alliance.controllers.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public AdminController(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String allUsers(Model model) {
        model.addAttribute("users", myUserDetailsService.findAll());
        return "/users/mainUsersPanel";
    }


}
