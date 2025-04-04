package ru.kozelsk.alliance.repositories.realty.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.realty.admin.ClientRealty;

import java.util.Optional;

@Repository
public interface ClientRealtyRepository extends JpaRepository<ClientRealty, Integer> {

    Optional<ClientRealty> findByPhone(String phone);
}
