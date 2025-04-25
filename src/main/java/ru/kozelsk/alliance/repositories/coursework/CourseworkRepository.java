package ru.kozelsk.alliance.repositories.coursework;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.coursework.Coursework;

@Repository
public interface CourseworkRepository extends JpaRepository<Coursework, Integer> {
}
