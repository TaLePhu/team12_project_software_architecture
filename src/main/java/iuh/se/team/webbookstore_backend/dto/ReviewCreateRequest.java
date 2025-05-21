package iuh.se.team.webbookstore_backend.dto;

import lombok.Data;

@Data
public class ReviewCreateRequest {
    private float ratingScore;
    private String comment;
    private int bookId;
    private int userId;
}