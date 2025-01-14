package ru.kozelsk.alliance.models.excursion;


import jakarta.persistence.*;

@Entity
@Table(name = "tour_image")
public class TourImage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imagePath;
    private String filename;
    private boolean isMain;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    public TourImage(String imagePath, String filename, boolean isMain, Tour tour) {
        this.imagePath = imagePath;
        this.filename = filename;
        this.isMain = isMain;
        this.tour = tour;
    }

    public TourImage() {}

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

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    @Override
    public String toString() {
        return "TourImage{" +
                "id=" + id +
                ", imagePath='" + imagePath + '\'' +
                ", filename='" + filename + '\'' +
                ", isMain=" + isMain +
                ", tour=" + tour +
                '}';
    }
}
