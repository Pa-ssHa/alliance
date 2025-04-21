package ru.kozelsk.alliance.services.insurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.insurance.FeedbackInsurance;
import ru.kozelsk.alliance.repositories.insurance.FeedbackInsuranceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackInsuranceService {

    private final FeedbackInsuranceRepository feedbackInsuranceRepository;

    @Autowired
    public FeedbackInsuranceService(FeedbackInsuranceRepository feedbackInsuranceRepository) {
        this.feedbackInsuranceRepository = feedbackInsuranceRepository;
    }

    public Optional<FeedbackInsurance> findOne(int id) {
        return feedbackInsuranceRepository.findById(id);
    }

    public List<FeedbackInsurance> findAll() {
        return feedbackInsuranceRepository.findAll();
    }

    public void save(FeedbackInsurance feedbackInsurance) {
        feedbackInsuranceRepository.save(feedbackInsurance);
    }

    public void delete(int id) {
        feedbackInsuranceRepository.deleteById(id);
    }

}
