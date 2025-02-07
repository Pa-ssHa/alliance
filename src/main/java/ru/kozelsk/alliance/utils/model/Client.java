package ru.kozelsk.alliance.utils.model;

public interface Client {

    int getId();
    String getName();
    String getDescription();
    String getPhone();
    void setPhone(String phone);
    void setId(int id);
    void setName(String name);
    void setDescription(String description);
}
