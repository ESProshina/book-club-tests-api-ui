package models.reviews;

public record ReviewBodyModel(
        Integer club,
        String review,
        Integer assessment,
        Integer readPages
) {}
