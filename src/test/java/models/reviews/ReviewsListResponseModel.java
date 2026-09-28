package models.reviews;

import java.util.List;

public record ReviewsListResponseModel(
        Integer count,
        String next,
        String previous,
        List<ReviewResponseModel> results
) {
}
