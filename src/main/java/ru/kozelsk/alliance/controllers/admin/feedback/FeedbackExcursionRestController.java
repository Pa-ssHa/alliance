package ru.kozelsk.alliance.controllers.admin.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kozelsk.alliance.services.excursion.FeedbackExcursionService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

import java.util.Optional;

@IsAdmin
@RestController
@RequestMapping("/admin/feedback-excursion")
public class FeedbackExcursionRestController {

    private final FeedbackExcursionService feedbackExcursionService;

    @Autowired
    public FeedbackExcursionRestController(FeedbackExcursionService feedbackExcursionService) {
        this.feedbackExcursionService = feedbackExcursionService;
    }

    @PostMapping("/{id}/changeActive")
    public ResponseEntity<?> changeActive(@PathVariable int id){

        return feedbackExcursionService.findOne(id)
                .map(feedbackExcursion -> {
                    feedbackExcursion.setActive(!feedbackExcursion.isActive());
                    feedbackExcursionService.save(feedbackExcursion);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


}
