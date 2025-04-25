package ru.kozelsk.alliance.controllers.admin.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kozelsk.alliance.services.coursework.FeedbackCourseworkService;

@RestController
@RequestMapping("/admin/feedback-coursework")
public class FeedbackCourseworkAdminRestController {

    private final FeedbackCourseworkService feedbackCourseworkService;

    @Autowired
    public FeedbackCourseworkAdminRestController(FeedbackCourseworkService feedbackCourseworkService) {
        this.feedbackCourseworkService = feedbackCourseworkService;
    }

    @PostMapping("/{id}/changeActive")
    public ResponseEntity<?> changeActive(@PathVariable int id) {
        return feedbackCourseworkService.findOne(id)
                .map(feedbackCoursework -> {
                    feedbackCoursework.setActive(!feedbackCoursework.isActive());
                    feedbackCourseworkService.save(feedbackCoursework);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
