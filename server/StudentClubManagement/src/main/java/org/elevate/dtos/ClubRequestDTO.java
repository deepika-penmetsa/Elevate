package org.elevate.dtos;

import org.elevate.models.RequestStatus;

import java.time.LocalDateTime;

public class ClubRequestDTO {
    private Long userId;
    private String clubName;
    private Long clubId;
    private RequestStatus requestStatus;
    private String userComment;
    private String approverComment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ClubRequestDTO() {}

    public ClubRequestDTO(Long userId, String clubName, Long clubId, String userComment, String approverComment, RequestStatus requestStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.clubName = clubName;
        this.clubId = clubId;
        this.userComment = userComment;
        this.approverComment = approverComment;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ClubRequestDTO(Long userId, Long clubId, String userComment, RequestStatus status) {
        this.userId = userId;
        this.clubId = clubId;
        this.userComment = userComment;
        this.requestStatus = status;
    }

    // Getters & Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
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

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
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
}
