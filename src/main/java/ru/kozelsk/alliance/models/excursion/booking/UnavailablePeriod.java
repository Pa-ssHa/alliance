package ru.kozelsk.alliance.models.excursion.booking;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "unavailable_period")
public class UnavailablePeriod implements Comparable<UnavailablePeriod> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime time;

    public UnavailablePeriod() {}

    public UnavailablePeriod(LocalDateTime time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime startDateTime) {
        this.time = startDateTime;
    }

    @Override
    public int compareTo(UnavailablePeriod other) {
        return this.time.compareTo(other.time);
    }
}
