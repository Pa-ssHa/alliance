package ru.kozelsk.alliance.repositories.insurance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.insurance.Insurance;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Integer> {
}
