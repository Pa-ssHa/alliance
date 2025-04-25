package ru.kozelsk.alliance.models.realty;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "advertisement_rent")
public class AdvertisementRent {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private int price;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdvertisementRentImage> images;

    public AdvertisementRent() {}

    public AdvertisementRent(String title, String description, int price) {
        this.title = title;
        this.description = description;
        this.price = price;
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

    public List<AdvertisementRentImage> getImages() {
        return images;
    }

    public void setImages(List<AdvertisementRentImage> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "AdvertisementRent{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
