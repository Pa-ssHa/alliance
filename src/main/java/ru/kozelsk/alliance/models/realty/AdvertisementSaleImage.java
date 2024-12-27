package ru.kozelsk.alliance.models.realty;

import jakarta.persistence.*;

@Entity
@Table(name = "advertisement_sale_image")
public class AdvertisementSaleImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String filename;

    @ManyToOne
    @JoinColumn(name = "advertisement_sale_id", nullable = false)
    private AdvertisementSale advertisement;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_main")
    private boolean isMain;

    public AdvertisementSaleImage() {}

    public AdvertisementSaleImage(AdvertisementSale advertisement, String imagePath, String filename, boolean isMain) {
        this.filename = filename;
        this.advertisement = advertisement;
        this.imagePath = imagePath;
        this.isMain = false;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AdvertisementSale getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(AdvertisementSale advertisement) {
        this.advertisement = advertisement;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    @Override
    public String toString() {
        return "AdvertisementImage{" +
                "id=" + id +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
