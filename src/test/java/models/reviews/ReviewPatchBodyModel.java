package models.reviews;

public record ReviewPatchBodyModel(
        String review,
        Integer assessment,
        Integer readPages
) {
}
