package ru.kozelsk.alliance.models.realty;

import org.springframework.web.multipart.MultipartFile;

public class AdvertisementSaleForm {

    private String title;
    private String description;
    private Double price;
    private MultipartFile[] images;

    public AdvertisementSaleForm(String title, String description,
                                 double price, MultipartFile[] images) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.images = images;
    }

    public AdvertisementSaleForm() {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public MultipartFile[] getImages() {
        return images;
    }

    public void setImages(MultipartFile[] images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "AdvertisementSaleForm{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
