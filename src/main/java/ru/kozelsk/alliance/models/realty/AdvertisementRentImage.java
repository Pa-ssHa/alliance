package ru.kozelsk.alliance.models.realty;


import jakarta.persistence.*;

@Entity
@Table(name = "advertisement_rent_image")
public class AdvertisementRentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imagePath;

    private String filename;

    private boolean isMain;

    @ManyToOne
    @JoinColumn(name = "advertisement_rent_id", nullable = false)
    private AdvertisementRent advertisement;

    public AdvertisementRentImage(boolean isMain, String imagePath, String filename, AdvertisementRent advertisement) {
        this.isMain = isMain;
        this.imagePath = imagePath;
        this.filename = filename;
        this.advertisement = advertisement;
    }

    public AdvertisementRentImage() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public AdvertisementRent getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(AdvertisementRent advertisement) {
        this.advertisement = advertisement;
    }

    @Override
    public String toString() {
        return "AdvertisementRentImage{" +
                "id=" + id +
                ", imagePath='" + imagePath + '\'' +
                ", filename='" + filename + '\'' +
                ", isMain=" + isMain +
                ", advertisement=" + advertisement +
                '}';
    }
}
