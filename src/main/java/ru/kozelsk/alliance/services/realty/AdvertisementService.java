package ru.kozelsk.alliance.services.realty;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kozelsk.alliance.models.realty.Advertisement;
import ru.kozelsk.alliance.repositories.realty.AdvertisementRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    public List<Advertisement> findAll(){
        return advertisementRepository.findAll();
    }

    public Advertisement findOne(int id){
        Optional<Advertisement> selectedAdvertisement = advertisementRepository.findById(id);
        return selectedAdvertisement.orElse(null);
    }

    public void save(Advertisement advertisement){
        advertisementRepository.save(advertisement);
    }

    @Transactional
    public void delete(int id){
        advertisementRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Advertisement upAdvertisement){
        upAdvertisement.setId(id);
        advertisementRepository.save(upAdvertisement);
    }


}
