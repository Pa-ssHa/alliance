package ru.kozelsk.alliance.models.coursework;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "coursework")
public class Coursework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private int price;

    private String image;

    public Coursework() {}

    public Coursework(String title, String description, int price, String image) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
