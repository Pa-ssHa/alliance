package ru.kozelsk.alliance.repositories.excursion.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.excursion.booking.UnavailablePeriod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UnavailablePeriodRepository extends JpaRepository<UnavailablePeriod, Integer> {

    List<UnavailablePeriod> findByTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(un) > 0 FROM UnavailablePeriod un WHERE un.time = :bookingTime")
    boolean existsByTime(@Param("bookingTime") LocalDateTime time);

    @Query("SELECT un FROM UnavailablePeriod un WHERE un.time = :bookingTime")
    Optional<UnavailablePeriod> findByTime(@Param("bookingTime") LocalDateTime time);

    @Query("SELECT un FROM UnavailablePeriod un WHERE un.time >= :startTime AND un.time <= :endTime")
    List<UnavailablePeriod> findByDate(@Param("startTime") LocalDateTime start, @Param("endTime") LocalDateTime end);
}
