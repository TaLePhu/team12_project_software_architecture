package iuh.se.team.webbookstore_backend.dto;


import java.time.LocalDateTime;
import java.util.List;

public interface ReviewDTO {
    int getReviewId();
    double getRatingScore();
    String getComment();
    LocalDateTime getCreatedAt();
    BookSummaryDTO getBook();



    interface BookSummaryDTO {
        int getBookId();
        String getBookName();
        String getAuthorName();
        String getIsbn();
        String getDescription();
        double getListedPrice();
        double getSalePrice();
        int getQuantity();
        double getAverageRating();
        int getSold();

        List<CategoryDTO> getCategories();

        interface CategoryDTO {
            int getCategoryId();
            String getCategoryName();
        }
    }

    UserDTO getUser();

    interface UserDTO {
        int getUserId();
        String getLastName();
        String getFirstName();
    }
}

