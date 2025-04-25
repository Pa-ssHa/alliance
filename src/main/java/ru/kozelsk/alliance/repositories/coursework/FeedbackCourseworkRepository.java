package ru.kozelsk.alliance.repositories.coursework;

import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.coursework.FeedbackCoursework;

@Repository
public interface FeedbackCourseworkRepository extends JpaRepository<FeedbackCoursework, Integer> {
}
