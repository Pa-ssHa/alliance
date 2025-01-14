package ru.kozelsk.alliance.services.excursion;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.excursion.Tour;
import ru.kozelsk.alliance.models.excursion.TourImage;
import ru.kozelsk.alliance.repositories.excursion.TourImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class TourImageService {

    private final TourImageRepository tourImageRepository;

    private static final String uploadDir = "M:/uploads/tour/";

    @Autowired
    public TourImageService(TourImageRepository tourImageRepository) {
        this.tourImageRepository = tourImageRepository;
    }

    public TourImage findOne(int id) {
        return tourImageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("image is not found"));
    }

    public void deleteImage(int id) {

        TourImage tourImage = findOne(id);

//        Path path = Paths.get(tourImage.getImagePath());
//        try{
//            Files.deleteIfExists(path);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
        tourImageRepository.deleteById(id);

        String imagePath = uploadDir + tourImage.getImagePath();
        try{
            Files.deleteIfExists(Path.of(imagePath));
        }catch (IOException e){
            throw new RuntimeException("ошибка при удалении из папки",e);
        }
    }

    public static String saveImageWithName(MultipartFile imageFile){
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);

        try{
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());
        }catch (IOException e){
            throw new RuntimeException("ошибка при сохранении", e);
        }
        return fileName;
    }

    public void save(TourImage tourImage){
        tourImageRepository.save(tourImage);
    }

    public void setMainImage(int imageId, int tourId){
        List<TourImage> images = tourImageRepository.findByTourId(tourId);
        for (TourImage image : images) {
            image.setMain(false);
            tourImageRepository.save(image);
        }
            TourImage mainImage = tourImageRepository.findById(imageId)
                    .orElseThrow(() -> new IllegalArgumentException("image is not found"));
            mainImage.setMain(true);
            tourImageRepository.save(mainImage);

    }

}
