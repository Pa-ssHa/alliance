package ru.kozelsk.alliance.controllers.admin.feedback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kozelsk.alliance.services.realty.FeedbackRealtyService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/admin/feedback-realty")
public class FeedbackRealtyAdminRestController {

    private final FeedbackRealtyService feedbackRealtyService;

    @Autowired
    public FeedbackRealtyAdminRestController(FeedbackRealtyService feedbackRealtyService) {
        this.feedbackRealtyService = feedbackRealtyService;
    }

    @IsAdmin
    @PostMapping("/{id}/changeActive")
    public ResponseEntity<?> changeActive(@PathVariable int id) {

        log.info("The changing of active is starting");
        return Optional.of(feedbackRealtyService.findOne(id))
                .map( feedbackRealty -> {
                    feedbackRealty.setActive(!feedbackRealty.isActive());
                    feedbackRealtyService.save(feedbackRealty);
                    log.info("The changed active is finished");
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());

    }
}
