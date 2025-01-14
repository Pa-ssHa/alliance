package ru.kozelsk.alliance.repositories.excursion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.excursion.TourImage;

import java.util.List;

@Repository
public interface TourImageRepository extends JpaRepository<TourImage, Integer> {

    List<TourImage> findByTourId(int tourId);
}
