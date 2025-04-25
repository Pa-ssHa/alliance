package ru.kozelsk.alliance.controllers.admin.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kozelsk.alliance.services.insurance.FeedbackInsuranceService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

@IsAdmin
@RestController
@RequestMapping("admin/feedback-insurance")
public class FeedbackInsuranceAdminRestController {

    private final FeedbackInsuranceService feedbackInsuranceService;

    @Autowired
    public FeedbackInsuranceAdminRestController(FeedbackInsuranceService feedbackInsuranceService) {
        this.feedbackInsuranceService = feedbackInsuranceService;
    }

    @PostMapping("/{id}/changeActive")
    public ResponseEntity<?> changeActive(@PathVariable int id){

        return feedbackInsuranceService.findOne(id)
                .map(feedbackInsurance -> {
                    feedbackInsurance.setActive(!feedbackInsurance.isActive());
                    feedbackInsuranceService.save(feedbackInsurance);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
