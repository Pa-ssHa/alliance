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

    public static String saveImage(MultipartFile imageFile) {
        String uploadDir = "M:/uploads/advertisementSale/";
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);

        try{
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());
        }catch (IOException e){
            throw new RuntimeException("ошибка при сохранении", e);
        }
        return path.toString();
    }
}
