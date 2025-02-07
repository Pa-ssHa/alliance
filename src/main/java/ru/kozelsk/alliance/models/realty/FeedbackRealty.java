package ru.kozelsk.alliance.models.realty;

import jakarta.persistence.*;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.utils.model.Feedback;

import java.util.Date;


@Entity
@Table(name = "feedback_realty")
public class FeedbackRealty implements Feedback {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "text")
    private String text;

    @Column(name = "date_of_placement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfPlacement;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int rating;

    public FeedbackRealty(){}

    public FeedbackRealty(String text, Date dateOfPlacement, User user, int rating) {
        this.text = text;
        this.dateOfPlacement = dateOfPlacement;
        this.rating = rating;
        this.user = user;
    }


    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", dateOfPlacement=" + dateOfPlacement +
                '}';
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Date getDateOfPlacement() {
        return dateOfPlacement;
    }

    @Override
    public void setDateOfPlacement(Date dateOfPlacement) {
        this.dateOfPlacement = dateOfPlacement;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int rating() {
        return rating;
    }

    @Override
    public void setRating(int rating) {
        this.rating = rating;
    }
}
