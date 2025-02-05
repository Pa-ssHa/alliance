package ru.kozelsk.alliance.utils.model;

import ru.kozelsk.alliance.models.users.User;

import java.util.Date;

public interface Feedback {

    int getId();
    void setId(int id);
    String getText();
    void setText(String text);
    Date getDateOfPlacement();
    void setDateOfPlacement(Date dateOfPlacement);
    User getUser();
    void setUser(User user);
    int rating();
    void setRating(int rating);
}
