package models.clubs;

import java.util.List;

public record ClubsListResponseModel(
        Integer count,
        String next,
        String previous,
        List<ClubModel> results
) {}