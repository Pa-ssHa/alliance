package ru.kozelsk.alliance.repositories.excursion.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.excursion.admin.ClientExcursion;

@Repository
public interface ClientExcursionRepository extends JpaRepository<ClientExcursion, Integer> {
}
