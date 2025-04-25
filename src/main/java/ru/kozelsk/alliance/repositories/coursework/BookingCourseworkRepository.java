package ru.kozelsk.alliance.repositories.coursework;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.coursework.BookingCoursework;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingCourseworkRepository extends JpaRepository<BookingCoursework, Integer> {

    Optional<BookingCoursework> findByUserId(Integer userId);
}
