package org.elevate.dtos;

import org.elevate.models.RequestStatus;

import java.time.LocalDateTime;

public class ClubRequestForUser_ResponseDTO {

    private Long clubRequestId;
    /**
     * CLUB DETAILS
     */
    private Long clubId;
    private String clubName;
    private String clubDescription;
    private int clubNoOfMembers;
    private int clubAvailableSlots;
    private int clubTotalSlots;
    private String clubImageUrl; // Base64 encoded string or image URL
    private String clubBackgroundImageUrl; // Base64 encoded string or image URL

    /**
     *
     */
    private RequestStatus requestStatus;
    private String userComment;
    private String approverComment;
    private LocalDateTime requestCreatedAt;
    private LocalDateTime requestUpdatedAt;

    public Long getClubRequestId() {
        return clubRequestId;
    }

    public void setClubRequestId(Long clubRequestId) {
        this.clubRequestId = clubRequestId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }

    public int getClubNoOfMembers() {
        return clubNoOfMembers;
    }

    public void setClubNoOfMembers(int clubNoOfMembers) {
        this.clubNoOfMembers = clubNoOfMembers;
    }

    public int getClubAvailableSlots() {
        return clubAvailableSlots;
    }

    public void setClubAvailableSlots(int clubAvailableSlots) {
        this.clubAvailableSlots = clubAvailableSlots;
    }

    public int getClubTotalSlots() {
        return clubTotalSlots;
    }

    public void setClubTotalSlots(int clubTotalSlots) {
        this.clubTotalSlots = clubTotalSlots;
    }

    public String getClubImageUrl() {
        return clubImageUrl;
    }

    public void setClubImageUrl(String clubImageUrl) {
        this.clubImageUrl = clubImageUrl;
    }

    public String getClubBackgroundImageUrl() {
        return clubBackgroundImageUrl;
    }

    public void setClubBackgroundImageUrl(String clubBackgroundImageUrl) {
        this.clubBackgroundImageUrl = clubBackgroundImageUrl;
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
