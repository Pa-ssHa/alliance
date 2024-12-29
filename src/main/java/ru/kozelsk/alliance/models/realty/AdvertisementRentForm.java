package ru.kozelsk.alliance.models.realty;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class AdvertisementRentForm {

    private String title;
    private String description;
    private double price;
    private List<MultipartFile> images;

    public AdvertisementRentForm() {}

    public AdvertisementRentForm(String title, String description, double price, List<MultipartFile> images) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.images = images;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "AdvertisementRentForm{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", images=" + images +
                '}';
    }
}
