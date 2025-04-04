package ru.kozelsk.alliance.controllers.realty.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.models.realty.FeedbackRealty;
import ru.kozelsk.alliance.services.realty.FeedbackRealtyService;
import ru.kozelsk.alliance.services.realty.admin.ClientRealtyService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/realty/admin/feedback")
public class FeedbackRealtyAdminController {

    private final ClientRealtyService clientRealtyService;
    private final FeedbackRealtyService feedbackRealtyService;

    @Autowired
    public FeedbackRealtyAdminController(ClientRealtyService clientRealtyService, FeedbackRealtyService feedbackRealtyService) {
        this.clientRealtyService = clientRealtyService;
        this.feedbackRealtyService = feedbackRealtyService;
    }

    @GetMapping
    public String allFeedback(Model model) {
        model.addAttribute("feedbacksRealty", feedbackRealtyService.findAll());
        List<String> listPhone = new ArrayList<>();
        for(FeedbackRealty feedbackRealty : feedbackRealtyService.findAll()) {
            if(clientRealtyService.findByPhone(feedbackRealty.getUser().getPhone()).isPresent()){
                String phone = feedbackRealty.getUser().getPhone().startsWith("+7")?
                        feedbackRealty.getUser().getPhone().substring(2):
                        feedbackRealty.getUser().getPhone().substring(1);
                listPhone.add(phone);
            }
        }
        model.addAttribute("listPhone", listPhone);

        return "realty/admin/feedback/main";
    }
}
