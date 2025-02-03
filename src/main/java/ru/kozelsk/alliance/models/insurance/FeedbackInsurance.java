package ru.kozelsk.alliance.models.insurance;

import jakarta.persistence.*;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.utils.model.Feedback;

import javax.xml.crypto.Data;
import java.util.Date;

@Entity
@Table(name = "feedback_insurance")
public class FeedbackInsurance implements Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String text;

    @Column(name = "dataOfPlacement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfPlacement;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public FeedbackInsurance() {}

    public FeedbackInsurance(String text, Date dateOfPlacement, User user) {
        this.text = text;
        this.dateOfPlacement = dateOfPlacement;
        this.user = user;
    }


    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public void setText(String text) {

    }

    @Override
    public Date getDateOfPlacement() {
        return null;
    }

    @Override
    public void setDateOfPlacement(Date dateOfPlacement) {

    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public void setUser(User user) {

    }
}
