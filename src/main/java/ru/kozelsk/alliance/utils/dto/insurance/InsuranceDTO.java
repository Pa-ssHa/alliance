package ru.kozelsk.alliance.utils.dto.insurance;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InsuranceDTO {

    private String title;
    private String description;
    private int price;

    private String images;

    public InsuranceDTO() {}

    public InsuranceDTO(String title, String description, int price, String image) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.images = images;
    }
}
