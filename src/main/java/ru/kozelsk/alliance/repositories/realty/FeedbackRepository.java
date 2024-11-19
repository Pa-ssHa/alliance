package ru.kozelsk.alliance.repositories.realty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.realty.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {



}
