package api;

public class ApiClient {

    public final AuthApiClient auth = new AuthApiClient();
    public final UsersApiClient users = new UsersApiClient();
    public final ClubsApiClient clubs = new ClubsApiClient();
    public final ReviewsApiClient reviews = new ReviewsApiClient();  // ← НОВОЕ
}