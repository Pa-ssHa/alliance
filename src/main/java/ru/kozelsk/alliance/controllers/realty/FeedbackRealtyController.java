package ru.kozelsk.alliance.controllers.realty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.realty.FeedbackRealty;
import ru.kozelsk.alliance.services.realty.FeedbackRealtyService;

@Controller
@RequestMapping("/realty/feedback")
public class FeedbackRealtyController {

    private final FeedbackRealtyService feedbackRealtyService;

    @Autowired
    public FeedbackRealtyController(FeedbackRealtyService feedbackRealtyService) {
        this.feedbackRealtyService = feedbackRealtyService;
    }

    @GetMapping("/{id}")
    public String showFeedBackRealty(@PathVariable int id, Model model) {
        model.addAttribute("oneFeedback", feedbackRealtyService.findOne(id));
        return "realty/feedback/show";
    }

    @GetMapping("/new")
    public String newFeedbackRealty(@ModelAttribute("newFeedbackRealty") FeedbackRealty feedbackRealty) {
        return "realty/feedback/new";
    }

    @PostMapping()
    public String createFeedbackRealty(@ModelAttribute("newFeedbackRealty") FeedbackRealty feedbackRealty,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "realty/feedback/new";
        }
        feedbackRealtyService.save(feedbackRealty);
        return "redirect:/realty";
    }
}
