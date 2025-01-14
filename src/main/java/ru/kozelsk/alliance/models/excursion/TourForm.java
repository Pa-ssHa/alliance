package ru.kozelsk.alliance.models.excursion;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class TourForm {

    private String title;
    private String description;
    private int price;
    private List<MultipartFile> images;

    public TourForm() {}

    public TourForm(String title, String description, int price, List<MultipartFile> images) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.images = images;
    }

    @Override
    public String toString() {
        return "TourForm{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", images=" + images +
                '}';
    }
}
