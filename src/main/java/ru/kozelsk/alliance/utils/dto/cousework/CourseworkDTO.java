package ru.kozelsk.alliance.utils.dto.cousework;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CourseworkDTO {

    private String title;
    private String description;
    private int price;

    private String images;

    public CourseworkDTO() {}

    public CourseworkDTO(String title, String description, int price, String image) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.images = images;
    }
}
