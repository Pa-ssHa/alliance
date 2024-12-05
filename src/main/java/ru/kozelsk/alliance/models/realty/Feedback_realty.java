package ru.kozelsk.alliance.models.realty;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "feedback_realty")
public class Feedback_realty {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "text")
    private String text;

    @Column(name = "dateOfPlacement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfPlacement;

    public Feedback_realty(){}

    public Feedback_realty(String text, Date dateOfPlacement) {
        this.text = text;
        this.dateOfPlacement = dateOfPlacement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateOfPlacement() {
        return dateOfPlacement;
    }

    public void setDateOfPlacement(Date dateOfPlacement) {
        this.dateOfPlacement = dateOfPlacement;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", dateOfPlacement=" + dateOfPlacement +
                '}';
    }
}
