package ru.kozelsk.alliance.repositories.realty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.realty.AdvertisementSaleImage;

import java.util.List;

@Repository
public interface AdvertisementSaleImageRepository extends JpaRepository<AdvertisementSaleImage, Integer> {


    List<AdvertisementSaleImage> findByAdvertisementId(int advertisementId);
}
