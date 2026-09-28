package models.clubs;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateClubBodyModel(
        @JsonProperty("bookTitle") String bookTitle,
        @JsonProperty("bookAuthors") String bookAuthors,
        @JsonProperty("publicationYear") Integer publicationYear,
        @JsonProperty("description") String description,
        @JsonProperty("telegramChatLink") String telegramChatLink
) {}
