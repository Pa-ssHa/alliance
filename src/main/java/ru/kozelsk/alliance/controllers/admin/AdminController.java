package ru.kozelsk.alliance.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

@Controller
public class AdminController {

    @IsAdmin
    @GetMapping("/admin-panel")
    public String adminPanel(){
        return "/users/adminPanel";
    }
}
