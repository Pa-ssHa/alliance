package ru.kozelsk.alliance.controllers.admin.feedback;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.services.realty.FeedbackRealtyService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

@IsAdmin
@Controller
@RequestMapping("/admin/feedback")
public class FeedbackAdminController {

    private final FeedbackRealtyService feedbackRealtyService;

    @Autowired
    public FeedbackAdminController(FeedbackRealtyService feedbackRealtyService) {
        this.feedbackRealtyService = feedbackRealtyService;
    }

    @GetMapping
    public String allFeedbacks(Model model) {

        model.addAttribute("feedbacksRealty", feedbackRealtyService.findAll());
        return "admin/feedback/main";
    }
}
