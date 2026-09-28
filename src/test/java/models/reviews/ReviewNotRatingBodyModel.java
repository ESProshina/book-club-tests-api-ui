package models.reviews;

public record ReviewNotRatingBodyModel(
        Integer club,
        String review,
        Integer readPages
) {
}
