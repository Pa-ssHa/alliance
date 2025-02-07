package ru.kozelsk.alliance.repositories.realty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.realty.AdvertisementSale;

@Repository
public interface AdvertisementSaleRepository extends JpaRepository<AdvertisementSale, Integer> {


}
