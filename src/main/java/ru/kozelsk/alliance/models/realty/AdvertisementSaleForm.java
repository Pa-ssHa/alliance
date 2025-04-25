package ru.kozelsk.alliance.models.realty;

import org.springframework.web.multipart.MultipartFile;

public class AdvertisementSaleForm {

    private String title;
    private String description;
    private int price;
    private MultipartFile[] images;

    public AdvertisementSaleForm(String title, String description,
                                 int price, MultipartFile[] images) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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
