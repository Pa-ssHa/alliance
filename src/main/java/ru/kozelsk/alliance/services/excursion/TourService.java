package ru.kozelsk.alliance.services.excursion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kozelsk.alliance.models.excursion.Tour;
import ru.kozelsk.alliance.repositories.excursion.TourRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TourService {

    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> findAll() {
        return tourRepository.findAll();
    }
    public Tour findOne(int id) {
        Optional<Tour> tour = tourRepository.findById(id);
        return tour.orElse(null);
    }

    @Transactional
    public void save(Tour tour) {
        tourRepository.save(tour);
    }

    @Transactional
    public void delete(int id) {
        tourRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Tour tour) {
        tour.setId(id);
        tourRepository.save(tour);
    }

}
