package ru.kozelsk.alliance.controllers.insurance;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/insurance")
public class InsuranceMainController {

    public InsuranceMainController() {}

    @GetMapping()
    public String allInsurance() {
        return "insurance/main";
    }
}
