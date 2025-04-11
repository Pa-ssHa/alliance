package ru.kozelsk.alliance.models.excursion.booking;

import jakarta.persistence.*;
import ru.kozelsk.alliance.models.excursion.Tour;
import ru.kozelsk.alliance.models.users.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking implements Comparable<Booking> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private boolean isActive;

    private String description;

    private LocalDateTime bookingTime;

    public Booking(Tour tour, User user, LocalDateTime bookingTime) {
        this.tour = tour;
        this.user = user;
        this.bookingTime = bookingTime;
    }

    public Booking() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int compareTo(Booking other) {
        return this.bookingTime.compareTo(other.bookingTime);
    }
}
