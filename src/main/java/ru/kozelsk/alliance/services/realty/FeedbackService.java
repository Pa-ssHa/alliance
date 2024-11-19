package ru.kozelsk.alliance.services.realty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.realty.Feedback;
import ru.kozelsk.alliance.repositories.realty.FeedbackRepository;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> findAll(){
        return feedbackRepository.findAll();
    }

}
