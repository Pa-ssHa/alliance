package ru.kozelsk.alliance.services.realty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kozelsk.alliance.models.realty.FeedbackRealty;
import ru.kozelsk.alliance.repositories.realty.FeedbackRealtyRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FeedbackRealtyService {

    private final FeedbackRealtyRepository feedbackRealtyRepository;

    @Autowired
    public FeedbackRealtyService(FeedbackRealtyRepository feedbackRealtyRepository) {
        this.feedbackRealtyRepository = feedbackRealtyRepository;
    }

    public List<FeedbackRealty> findAll(){
        return feedbackRealtyRepository.findAll();
    }

    public FeedbackRealty findOne(int id){
        Optional<FeedbackRealty> oneFeedback = feedbackRealtyRepository.findById(id);
        return oneFeedback.orElse(null);
    }

    @Transactional
    public FeedbackRealty save(FeedbackRealty feedbackRealty){
        return feedbackRealtyRepository.save(feedbackRealty);
    }

}
