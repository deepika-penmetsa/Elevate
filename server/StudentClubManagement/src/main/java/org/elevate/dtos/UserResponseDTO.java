package org.elevate.dtos;

import org.elevate.models.Role;

import java.time.LocalDateTime;

public class UserResponseDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String street;
    private String apartment;
    private String city;
    private String state;
    private String zipcode;
    private String country;
    private String profilePhoto; // Now a Base64 string
    private String bio;
    private int joinedClubs;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getApartment() { return apartment; }
    public void setApartment(String apartment) { this.apartment = apartment; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public int getJoinedClubs() { return joinedClubs; }
    public void setJoinedClubs(int joinedClubs) { this.joinedClubs = joinedClubs; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
