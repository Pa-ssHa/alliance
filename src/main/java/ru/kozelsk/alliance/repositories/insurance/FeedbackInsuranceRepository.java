package ru.kozelsk.alliance.repositories.insurance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.insurance.FeedbackInsurance;

@Repository
public interface FeedbackInsuranceRepository extends JpaRepository<FeedbackInsurance, Integer> {
}
