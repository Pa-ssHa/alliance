package ru.kozelsk.alliance.repositories.excursion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.excursion.FeedbackExcursion;

@Repository
public interface FeedbackExcursionRepository extends JpaRepository<FeedbackExcursion, Integer> {
}
