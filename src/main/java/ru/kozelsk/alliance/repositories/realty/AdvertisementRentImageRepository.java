package ru.kozelsk.alliance.repositories.realty;

import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kozelsk.alliance.models.realty.AdvertisementRent;
import ru.kozelsk.alliance.models.realty.AdvertisementRentImage;

import java.util.List;

@Registered
public interface AdvertisementRentImageRepository extends JpaRepository<AdvertisementRentImage, Integer> {

    List<AdvertisementRentImage> findByAdvertisementId(int advertisementId);
}
