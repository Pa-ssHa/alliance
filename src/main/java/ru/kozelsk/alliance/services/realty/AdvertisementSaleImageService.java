package ru.kozelsk.alliance.services.realty;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.realty.AdvertisementSaleImage;
import ru.kozelsk.alliance.repositories.realty.AdvertisementSaleImageRepository;
import ru.kozelsk.alliance.repositories.realty.AdvertisementSaleRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdvertisementSaleImageService {

    private final AdvertisementSaleImageRepository advertisementSaleImageRepository;

    private static final String uploadDir = "M:/uploads/advertisementSale/";

    @Autowired
    public AdvertisementSaleImageService(AdvertisementSaleImageRepository advertisementSaleImageRepository) {
        this.advertisementSaleImageRepository = advertisementSaleImageRepository;
    }

//    удаление изображения из бд и из папки
    public void deleteImage(int id){
        AdvertisementSaleImage image = findOne(id);

        Path path = Paths.get(image.getImagePath());
        try{
            Files.deleteIfExists(path);
        }catch (IOException e){
            e.printStackTrace();
        }
        advertisementSaleImageRepository.deleteById(id);

//        String uploadDir = "M:/uploads/advertisementSale/";
        String imagePath = uploadDir + image.getImagePath();
        try {
            Files.deleteIfExists(Path.of(imagePath));
        } catch (IOException e) {
            throw new RuntimeException("ошибка при удалении из папки",e);
        }

    }

    public AdvertisementSaleImage findOne(int id){
        return advertisementSaleImageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Изображение не найдено"));
    }

//    сохранение только конечного имени с кодом
    public static String saveImageWithName(MultipartFile imageFile) {
//        String uploadDir = "M:/uploads/advertisementSale/";
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

//    сохранение полного пути
    public static String saveImageWithPath(MultipartFile imageFile) throws IOException {
        Path uploadPath = Paths.get("M:/uploads/advertisementSale/");
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(fileName);
        imageFile.transferTo(filePath.toFile());
        return filePath.toString();

    }

    public void save(AdvertisementSaleImage advertisementSaleImage) {
        advertisementSaleImageRepository.save(advertisementSaleImage);
    }


    // установка главного изображения
    public void setMainImage(int imageId, int advertisementId){
        List<AdvertisementSaleImage> images = advertisementSaleImageRepository.findByAdvertisementId(advertisementId);
        for (AdvertisementSaleImage image : images){
            image.setMain(false);
            advertisementSaleImageRepository.save(image);

            AdvertisementSaleImage mainImage = advertisementSaleImageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Изображение не найдено"));
            mainImage.setMain(true);
            advertisementSaleImageRepository.save(mainImage);
        }
    }
}
