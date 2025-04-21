package ru.kozelsk.alliance.services.excursion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.excursion.FeedbackExcursion;
import ru.kozelsk.alliance.repositories.excursion.FeedbackExcursionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackExcursionService {

    private final FeedbackExcursionRepository feedbackExcursionRepository;

    @Autowired
    public FeedbackExcursionService(FeedbackExcursionRepository feedbackExcursionRepository) {
        this.feedbackExcursionRepository = feedbackExcursionRepository;
    }

    public Optional<FeedbackExcursion> findOne(int id) {
        return feedbackExcursionRepository.findById(id);
    }

    public List<FeedbackExcursion> findAll() {
        return feedbackExcursionRepository.findAll();
    }

    public void save(FeedbackExcursion feedbackExcursion) {
        feedbackExcursionRepository.save(feedbackExcursion);
    }

    public void delete(int id) {
        feedbackExcursionRepository.deleteById(id);
    }
}

