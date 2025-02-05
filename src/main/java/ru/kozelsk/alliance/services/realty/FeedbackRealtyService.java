package ru.kozelsk.alliance.services.realty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kozelsk.alliance.models.realty.FeedbackRealty;
import ru.kozelsk.alliance.repositories.realty.FeedbackRealtyRepository;
import ru.kozelsk.alliance.utils.model.Feedback;
import ru.kozelsk.alliance.utils.services.FeedbackServiceImpl;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FeedbackRealtyService implements FeedbackServiceImpl<FeedbackRealty> {

    private final FeedbackRealtyRepository feedbackRealtyRepository;

    @Autowired
    public FeedbackRealtyService(FeedbackRealtyRepository feedbackRealtyRepository) {
        this.feedbackRealtyRepository = feedbackRealtyRepository;
    }

    @Override
    public List<FeedbackRealty> findAll(){
        return feedbackRealtyRepository.findAll();
    }

    public FeedbackRealty findOne(int id){
        Optional<FeedbackRealty> oneFeedback = feedbackRealtyRepository.findById(id);
        return oneFeedback.orElse(null);
    }

    @Override
    @Transactional
    public void save(FeedbackRealty feedbackRealty) {
        feedbackRealtyRepository.save(feedbackRealty);
    }

    @Override
    @Transactional
    public void delete(int id){
        feedbackRealtyRepository.deleteById(id);
    }

}
