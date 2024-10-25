package ca.bc.gov.restapi.results.postgres.dto;

import java.time.LocalDateTime;

public class UserRecentOpeningDto {

    private String userId;
    private String openingId;
    private LocalDateTime lastViewed;

    public UserRecentOpeningDto(String userId, String openingId, LocalDateTime lastViewed) {
        this.userId = userId;
        this.openingId = openingId;
        this.lastViewed = lastViewed;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpeningId() {
        return openingId;
    }

    public void setOpeningId(String openingId) {
        this.openingId = openingId;
    }

    public LocalDateTime getLastViewed() {
        return lastViewed;
    }

    public void setLastViewed(LocalDateTime lastViewed) {
        this.lastViewed = lastViewed;
    }
}
