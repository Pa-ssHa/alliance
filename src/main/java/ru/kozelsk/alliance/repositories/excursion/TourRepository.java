package ru.kozelsk.alliance.repositories.excursion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.excursion.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
}
