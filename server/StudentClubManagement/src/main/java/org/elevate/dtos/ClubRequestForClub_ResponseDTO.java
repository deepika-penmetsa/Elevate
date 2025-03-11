package org.elevate.dtos;

import org.elevate.models.RequestStatus;
import org.elevate.models.Role;

import java.time.LocalDateTime;

public class ClubRequestForClub_ResponseDTO {
    private Long clubRequestId;

    /**
     * USER DETAILS
     */
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPhone;
    private String userStreet;
    private String userApartment;
    private String userCity;
    private String userState;
    private String userZipcode;
    private String userCountry;
    private String userProfilePhoto; // Now a Base64 string
    private String userBio;
    private int userJoinedClubs;
    private Role userRole;

    /**
     *
     */
    private RequestStatus requestStatus;
    private String userComment;
    private String approverComment;
    private LocalDateTime requestCreatedAt;
    private LocalDateTime requestUpdatedAt;


    /**
     * GETTERS, SETTERS
     */
    public Long getClubRequestId() {
        return clubRequestId;
    }

    public void setClubRequestId(Long clubRequestId) {
        this.clubRequestId = clubRequestId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserStreet() {
        return userStreet;
    }

    public void setUserStreet(String userStreet) {
        this.userStreet = userStreet;
    }

    public String getUserApartment() {
        return userApartment;
    }

    public void setUserApartment(String userApartment) {
        this.userApartment = userApartment;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getUserZipcode() {
        return userZipcode;
    }

    public void setUserZipcode(String userZipcode) {
        this.userZipcode = userZipcode;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public int getUserJoinedClubs() {
        return userJoinedClubs;
    }

    public void setUserJoinedClubs(int userJoinedClubs) {
        this.userJoinedClubs = userJoinedClubs;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getApproverComment() {
        return approverComment;
    }

    public void setApproverComment(String approverComment) {
        this.approverComment = approverComment;
    }

    public LocalDateTime getRequestCreatedAt() {
        return requestCreatedAt;
    }
    public void setRequestCreatedAt(LocalDateTime requestCreatedAt) {
        this.requestCreatedAt = requestCreatedAt;
    }
    public LocalDateTime getRequestUpdatedAt() {
        return requestUpdatedAt;
    }
    public void setRequestUpdatedAt(LocalDateTime requestUpdatedAt) {
        this.requestUpdatedAt = requestUpdatedAt;
    }
}
