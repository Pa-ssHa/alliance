package ru.kozelsk.alliance.services.realty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kozelsk.alliance.models.realty.AdvertisementRent;
import ru.kozelsk.alliance.models.realty.AdvertisementSale;
import ru.kozelsk.alliance.repositories.realty.AdvertisementRentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AdvertisementRentService {

    private final AdvertisementRentRepository advertisementRentRepository;

    @Autowired
    public AdvertisementRentService(AdvertisementRentRepository advertisementRentRepository) {
        this.advertisementRentRepository = advertisementRentRepository;
    }

    public List<AdvertisementRent> findAll(){
        return advertisementRentRepository.findAll();
    }
    public AdvertisementRent findOne(int id) {
        Optional<AdvertisementRent> advertisementRent = advertisementRentRepository.findById(id);
        return advertisementRent.orElse(null);
    }

    @Transactional
    public void save(AdvertisementRent advertisementRent) {
        advertisementRentRepository.save(advertisementRent);
    }

    @Transactional
    public void delete(int id) {
        advertisementRentRepository.deleteById(id);
    }
    @Transactional
    public void update(int id, AdvertisementRent upAdvertisement) {
        upAdvertisement.setId(id);
        advertisementRentRepository.save(upAdvertisement);
    }


}
