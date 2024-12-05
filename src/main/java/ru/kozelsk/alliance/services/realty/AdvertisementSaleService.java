package ru.kozelsk.alliance.services.realty;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kozelsk.alliance.models.realty.AdvertisementSale;
import ru.kozelsk.alliance.repositories.realty.AdvertisementSaleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AdvertisementSaleService {

    private final AdvertisementSaleRepository advertisementRepository;

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


}
