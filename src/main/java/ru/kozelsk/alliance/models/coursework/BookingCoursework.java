package ru.kozelsk.alliance.models.coursework;


import jakarta.persistence.*;
import ru.kozelsk.alliance.models.users.User;

@Entity
@Table(name = "booking_coursework")
public class BookingCoursework implements Comparable<BookingCoursework> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "coursework_id")
    private Coursework coursework;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public BookingCoursework() {}

    public BookingCoursework(String description, boolean isActive, Coursework coursework, User user) {
        this.description = description;
        this.isActive = isActive;
        this.coursework = coursework;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Coursework getCoursework() {
        return coursework;
    }

    public void setCoursework(Coursework coursework) {
        this.coursework = coursework;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int compareTo(BookingCoursework other) {
        return -(this.id - other.id);
    }

}
