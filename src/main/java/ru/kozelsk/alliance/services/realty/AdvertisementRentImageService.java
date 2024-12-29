package ru.kozelsk.alliance.services.realty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.realty.AdvertisementRentImage;
import ru.kozelsk.alliance.models.realty.AdvertisementSaleImage;
import ru.kozelsk.alliance.repositories.realty.AdvertisementRentImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AdvertisementRentImageService {

    private final AdvertisementRentImageRepository advertisementRentImageRepository;

    private static final String uploadDir = "M:/uploads/advertisementRent/";

    @Autowired
    public AdvertisementRentImageService(AdvertisementRentImageRepository advertisementRentImageRepository) {
        this.advertisementRentImageRepository = advertisementRentImageRepository;
    }

    //    удаление изображения из бд и из папки
    public void deleteImage(int id){
        AdvertisementRentImage image = findOne(id);

        Path path = Paths.get(image.getImagePath());
        try{
            Files.deleteIfExists(path);
        }catch (IOException e){
            e.printStackTrace();
        }
        advertisementRentImageRepository.deleteById(id);

//        String uploadDir = "M:/uploads/advertisementSale/";
        String imagePath = uploadDir + image.getImagePath();
        try {
            Files.deleteIfExists(Path.of(imagePath));
        } catch (IOException e) {
            throw new RuntimeException("ошибка при удалении из папки",e);
        }
    }

    public AdvertisementRentImage findOne(int id){
        return advertisementRentImageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Изображение не найдено"));
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
        Path uploadPath = Paths.get("M:/uploads/advertisementRent/");
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(fileName);
        imageFile.transferTo(filePath.toFile());
        return filePath.toString();

    }

    public void save(AdvertisementRentImage advertisementRentImage) {
        advertisementRentImageRepository.save(advertisementRentImage);
    }


    // установка главного изображения
    public void setMainImage(int imageId, int advertisementId){
        List<AdvertisementRentImage> images = advertisementRentImageRepository.findByAdvertisementId(advertisementId);
        for (AdvertisementRentImage image : images){
            image.setMain(false);
            advertisementRentImageRepository.save(image);

            AdvertisementRentImage mainImage = advertisementRentImageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Изображение не найдено"));
            mainImage.setMain(true);
            advertisementRentImageRepository.save(mainImage);
        }
    }
}
