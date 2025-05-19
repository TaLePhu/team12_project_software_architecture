package iuh.se.team.webbookstore_backend.dto;

import java.util.List;

public interface BookSumaryDTO {
    int getBookId();
    String getBookName();
    String getAuthorName();
    String getIsbn();
    String getDescription();
    double getListedPrice();
    double getSalePrice();
    int getQuantity();
    double getAverageRating();

    List<CategoryDTO> getCategories();

    interface CategoryDTO {
        int getCategoryId();
        String getCategoryName();
    }
}
