package ru.kozelsk.alliance.services.coursework;

import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.coursework.FeedbackCoursework;
import ru.kozelsk.alliance.repositories.coursework.FeedbackCourseworkRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackCourseworkService {

    private final FeedbackCourseworkRepository feedbackCourseworkRepository;

    public FeedbackCourseworkService(FeedbackCourseworkRepository feedbackCourseworkRepository) {
        this.feedbackCourseworkRepository = feedbackCourseworkRepository;
    }

    public Optional<FeedbackCoursework> findOne(int id) {
        return feedbackCourseworkRepository.findById(id);
    }
    public List<FeedbackCoursework> findAll() {
        return feedbackCourseworkRepository.findAll();
    }
    public void save(FeedbackCoursework feedbackCoursework) {
        feedbackCourseworkRepository.save(feedbackCoursework);
    }
    public void delete(int id) {
        feedbackCourseworkRepository.deleteById(id);
    }
}
