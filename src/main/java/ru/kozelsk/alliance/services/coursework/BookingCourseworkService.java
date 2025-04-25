package ru.kozelsk.alliance.services.coursework;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.coursework.BookingCoursework;
import ru.kozelsk.alliance.repositories.coursework.BookingCourseworkRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookingCourseworkService {

    private final BookingCourseworkRepository bookingCourseworkRepository;

    @Autowired
    public BookingCourseworkService(BookingCourseworkRepository bookingCourseworkRepository) {
        this.bookingCourseworkRepository = bookingCourseworkRepository;
    }

    public Optional<BookingCoursework> findOne(int id) {
        return bookingCourseworkRepository.findById(id);
    }
    public List<BookingCoursework> findAll() {
        return bookingCourseworkRepository.findAll();
    }
    public void save(BookingCoursework bookingCoursework) {
        bookingCourseworkRepository.save(bookingCoursework);
    }
    public void delete(int id) {
        bookingCourseworkRepository.deleteById(id);
    }

    public Optional<BookingCoursework> getBookingForUser(int userId) {
        return bookingCourseworkRepository.findByUserId(userId);
    }

    public boolean isHasBookingForUser(int userId) {
        return getBookingForUser(userId).isPresent();
    }

}
