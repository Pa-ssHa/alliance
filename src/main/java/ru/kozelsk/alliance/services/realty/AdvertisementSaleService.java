package ru.kozelsk.alliance.services.realty;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.realty.AdvertisementSale;
import ru.kozelsk.alliance.models.realty.AdvertisementSaleImage;
import ru.kozelsk.alliance.repositories.realty.AdvertisementSaleImageRepository;
import ru.kozelsk.alliance.repositories.realty.AdvertisementSaleRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AdvertisementSaleService {

    private final AdvertisementSaleRepository advertisementRepository;

    private static final String IMAGE_DIR = "M:/uploads/advertisementSale";

    @Autowired
    public AdvertisementSaleService(AdvertisementSaleRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    public List<AdvertisementSale> findAll(){
        return advertisementRepository.findAll();
    }

    public AdvertisementSale findOne(int id){
        Optional<AdvertisementSale> selectedAdvertisement = advertisementRepository.findById(id);
        return selectedAdvertisement.orElse(null);
    }

    @Transactional
    public void save(AdvertisementSale advertisement){
        advertisementRepository.save(advertisement);
    }

    @Transactional
    public void delete(int id){
        advertisementRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, AdvertisementSale upAdvertisement){
        upAdvertisement.setId(id);
        advertisementRepository.save(upAdvertisement);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////IMAGE

    /*@PostConstruct
    public void init(){
        File directory = new File(IMAGE_DIR);
        if(!directory.exists()){
            directory.mkdir();
        }
    }*/

    /*@Transactional
    public void saveAdvertisementWithImage(AdvertisementSale advertisementSale, MultipartFile[] images) throws IOException {

        AdvertisementSale savedAdvertisement = advertisementRepository.save(advertisementSale);

        List<AdvertisementSaleImage> savedImages = new ArrayList<>();
        for (MultipartFile file : images) {
            if(!file.isEmpty()){
                String fileName = file.getOriginalFilename();
                File targetFile = new File(IMAGE_DIR + fileName);
                file.transferTo(targetFile);

                AdvertisementSaleImage savedImage = new AdvertisementSaleImage();
                savedImage.setImagePath(targetFile.getAbsolutePath());
                savedImage.setFilename(fileName);
                savedImage.setAdvertisement(savedAdvertisement);

                savedImages.add(savedImage);
                advertisementImageRepository.save(savedImage);
            }
        }
        savedAdvertisement.setImages(savedImages);
        advertisementRepository.save(savedAdvertisement);
    }*/

    /*public void deleteImage(int id){
        advertisementImageRepository.deleteById(id);
    }*/

}
