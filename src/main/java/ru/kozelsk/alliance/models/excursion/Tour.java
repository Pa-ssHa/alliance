package ru.kozelsk.alliance.models.excursion;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String title;
    private String description;
    private int price;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TourImage> images;

    public Tour() {}

    public Tour(String title, String description, int price, List<TourImage> images) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.images = images;
    }

    @Override
    public String toString() {
        return "Tour{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", images=" + images +
                '}';
    }
}
