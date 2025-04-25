package ru.kozelsk.alliance.controllers.admin.feedback;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.services.coursework.FeedbackCourseworkService;
import ru.kozelsk.alliance.services.excursion.FeedbackExcursionService;
import ru.kozelsk.alliance.services.insurance.FeedbackInsuranceService;
import ru.kozelsk.alliance.services.realty.FeedbackRealtyService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

@IsAdmin
@Controller
@RequestMapping("/admin/feedback")
public class FeedbackAdminController {

    private final FeedbackRealtyService feedbackRealtyService;
    private final FeedbackExcursionService feedbackExcursionService;
    private final FeedbackInsuranceService feedbackInsuranceService;
    private final FeedbackCourseworkService feedbackCourseworkService;

    @Autowired
    public FeedbackAdminController(FeedbackRealtyService feedbackRealtyService, FeedbackExcursionService feedbackExcursionService, FeedbackInsuranceService feedbackInsuranceService, FeedbackCourseworkService feedbackCourseworkService) {
        this.feedbackRealtyService = feedbackRealtyService;
        this.feedbackExcursionService = feedbackExcursionService;
        this.feedbackInsuranceService = feedbackInsuranceService;
        this.feedbackCourseworkService = feedbackCourseworkService;
    }

    @GetMapping
    public String allFeedbacks(Model model) {

        model.addAttribute("feedbacksRealty", feedbackRealtyService.findAll());
        model.addAttribute("feedbacksExcursion", feedbackExcursionService.findAll());
        model.addAttribute("feedbacksInsurance", feedbackInsuranceService.findAll());
        model.addAttribute("feedbacksCoursework", feedbackCourseworkService.findAll());
        return "admin/feedback/main";
    }
}
