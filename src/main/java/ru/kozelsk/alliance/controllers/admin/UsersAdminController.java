package ru.kozelsk.alliance.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

@Controller
@RequestMapping("/admin")
public class UsersAdminController {

    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public UsersAdminController(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @IsAdmin
    @GetMapping("/users")
    public String allUsers(Model model) {
        model.addAttribute("users", myUserDetailsService.findAll());
        return "/users/mainUsersPanel";
    }

    @IsAdmin
    @DeleteMapping("/{id}")
    public String deleteClientRealty(@PathVariable("id") int id) {
        myUserDetailsService.delete(id);
        return "redirect:/admin/users";
    }

}
