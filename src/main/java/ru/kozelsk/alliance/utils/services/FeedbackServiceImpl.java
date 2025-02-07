package ru.kozelsk.alliance.utils.services;

import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.utils.model.Feedback;

import java.util.List;

public interface FeedbackServiceImpl<T extends Feedback> {

    void delete(int id);
    void save(T feedback);
    List<T> findAll();
}
