package ru.kozelsk.alliance.repositories.insurance.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.models.insurance.admin.ClientInsurance;
@RequestMapping
public interface ClientInsuranceRepository extends JpaRepository<ClientInsurance, Integer> {
}
