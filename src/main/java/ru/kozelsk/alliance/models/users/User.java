package ru.kozelsk.alliance.models.users;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kozelsk.alliance.models.excursion.booking.Booking;
import ru.kozelsk.alliance.models.insurance.FeedbackInsurance;
import ru.kozelsk.alliance.models.realty.FeedbackRealty;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    private String phone;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов")
    private String name;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    private boolean isRealtyFeedback;
    private boolean isExcursionFeedback;
    private boolean isInsuranceFeedback;
    private boolean isCourseworkFeedback;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FeedbackInsurance> feedbackInsurances;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FeedbackRealty> feedbackRealties;

    public User(String email, String password, String name, String phone, boolean active,
                Set<Role> roles, boolean isRealtyFeedback, boolean isExcursionFeedback, boolean isInsuranceFeedback,
                List<Booking> bookings, List<FeedbackInsurance> feedbackInsurances, List<FeedbackRealty> feedbackRealties) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.active = active;
        this.roles = roles;
        this.isRealtyFeedback = isRealtyFeedback;
        this.isExcursionFeedback = isExcursionFeedback;
        this.isInsuranceFeedback = isInsuranceFeedback;
        this.bookings = bookings;
        this.feedbackInsurances = feedbackInsurances;
        this.feedbackRealties = feedbackRealties;
        this.phone = phone;
    }

    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public boolean isRealtyFeedback() {
        return isRealtyFeedback;
    }

    public void setRealtyFeedback(boolean realtyFeedback) {
        isRealtyFeedback = realtyFeedback;
    }

    public boolean isExcursionFeedback() {
        return isExcursionFeedback;
    }

    public void setExcursionFeedback(boolean excursionFeedback) {
        isExcursionFeedback = excursionFeedback;
    }

    public boolean isInsuranceFeedback() {
        return isInsuranceFeedback;
    }

    public void setInsuranceFeedback(boolean insuranceFeedback) {
        isInsuranceFeedback = insuranceFeedback;
    }

    public boolean isCourseworkFeedback() {
        return isCourseworkFeedback;
    }

    public void setCourseworkFeedback(boolean courseworkFeedback) {
        isCourseworkFeedback = courseworkFeedback;
    }

    public List<FeedbackInsurance> getFeedbackInsurances() {
        return feedbackInsurances;
    }

    public void setFeedbackInsurances(List<FeedbackInsurance> feedbackInsurances) {
        this.feedbackInsurances = feedbackInsurances;
    }

    public List<FeedbackRealty> getFeedbackRealties() {
        return feedbackRealties;
    }

    public void setFeedbackRealties(List<FeedbackRealty> feedbackRealties) {
        this.feedbackRealties = feedbackRealties;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true; // аккаунт не истек
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // аккаунт не заблокирован
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //учетные данные не истекли
    }

    @Override
    public boolean isEnabled() {
        return true; //аккаунт активен
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(email, user.email) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}
