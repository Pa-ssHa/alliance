package ru.kozelsk.alliance.services.excursion.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.excursion.booking.UnavailablePeriod;
import ru.kozelsk.alliance.repositories.excursion.booking.BookingRepository;
import ru.kozelsk.alliance.repositories.excursion.booking.UnavailablePeriodRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UnavailablePeriodService {

    private final UnavailablePeriodRepository unavailablePeriodRepository;

    @Autowired
    public UnavailablePeriodService(UnavailablePeriodRepository unavailablePeriodRepository) {
        this.unavailablePeriodRepository = unavailablePeriodRepository;
    }

    public List<UnavailablePeriod> findAll() {
        return unavailablePeriodRepository.findAll();
    }

    public Optional<UnavailablePeriod> findOne(int id) {
        return unavailablePeriodRepository.findById(id);
    }

    public Optional<UnavailablePeriod> findByTime(LocalDateTime time) {
        return unavailablePeriodRepository.findByTime(time);
    }

    public void delete(int id) {
        unavailablePeriodRepository.deleteById(id);
    }

    public Map<LocalDate, List<UnavailablePeriod>> findUnavailablePeriodsByDay() {
        List<UnavailablePeriod> unavailablePeriods = unavailablePeriodRepository.findAll();
        Collections.sort(unavailablePeriods);
        return unavailablePeriods.stream().collect(Collectors.groupingBy(unavailablePeriod -> unavailablePeriod.getTime().toLocalDate(),
                TreeMap::new,
                Collectors.toList()));
    }


    public List<UnavailablePeriod> getByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return unavailablePeriodRepository.findByDate(start, end);
    }

    public boolean isDateAvailable(LocalDate localDate) {
        return getByDate(localDate).isEmpty();
    }

    public boolean isTimeSlotAvailable(LocalDateTime localDateTime) {
        return !unavailablePeriodRepository.existsByTime(localDateTime);
    }

    public void addUnavailablePeriod(List<LocalDateTime> localDateTimeList) {
        if (!localDateTimeList.isEmpty()) {
            for (LocalDateTime localDateTime : localDateTimeList) {
                UnavailablePeriod unavailablePeriod = new UnavailablePeriod();
                unavailablePeriod.setTime(localDateTime);
                unavailablePeriodRepository.save(unavailablePeriod);
            }
        }
    }


}
