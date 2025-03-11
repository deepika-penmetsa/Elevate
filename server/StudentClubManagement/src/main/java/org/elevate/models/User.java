package org.elevate.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "First name is mandatory")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = 50)
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phone;

    // Address fields
    @Size(max = 100)
    private String street;

    @Size(max = 50)
    private String apartment;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String state;

    @Size(max = 10)
    private String zipcode;

    @Size(max = 50)
    private String country;

    // New Fields
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profilePhoto;

    @Size(max = 500)
    private String bio;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;


    @Min(value = 0)
    @Max(value = 3)
    private int joinedClubs = 0;

    @JsonIgnore
    @OneToOne(mappedBy = "clubAdmin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Club club;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserClub> userClubs;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClubRequest> clubRequests;

    @JsonIgnore
    @OneToMany(mappedBy = "postedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Announcement> announcements;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Question> questions;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers;

    @JsonIgnore
    @OneToMany(mappedBy = "clubAdmin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClubAdminRequest> clubAdminRequests;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserAnnouncement> userAnnouncements;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }




    /**
     * Getters and Setters for all fields
     *
     **/

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getBirthday() {
        return dateOfBirth;
    }

    public void setBirthday(Date birthday) {
        this.dateOfBirth = birthday;
    }

    public int getJoinedClubs() {
        return joinedClubs;
    }

    public void setJoinedClubs(int joinedClubs) {
        this.joinedClubs = joinedClubs;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<UserClub> getUserClubs() {
        return userClubs;
    }

    public void setUserClubs(Set<UserClub> userClubs) {
        this.userClubs = userClubs;
    }

    public Set<ClubRequest> getClubRequests() {
        return clubRequests;
    }

    public void setClubRequests(Set<ClubRequest> clubRequests) {
        this.clubRequests = clubRequests;
    }

    public Set<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(Set<Announcement> announcements) {
        this.announcements = announcements;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Set<ClubAdminRequest> getClubAdminRequests() {
        return clubAdminRequests;
    }

    public void setClubAdminRequests(Set<ClubAdminRequest> clubAdminRequests) {
        this.clubAdminRequests = clubAdminRequests;
    }

    public Set<UserAnnouncement> getUserAnnouncements() {
        return userAnnouncements;
    }

    public void setUserAnnouncements(Set<UserAnnouncement> userAnnouncements) {
        this.userAnnouncements = userAnnouncements;
    }
}
