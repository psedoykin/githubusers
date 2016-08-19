package com.example.pavel.githubusers.github;

import com.example.pavel.githubusers.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GitHubService {

    private static final String BASE_URL = "https://api.github.com/";
    private static final String USER_PATH = "users";
    private static GitHubApi gitHubInterface;

    public static GitHubApi getClient() {
        if (gitHubInterface == null) {

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gitHubInterface = client.create(GitHubApi.class);
        }
        return gitHubInterface;
    }

    public interface GitHubApi {

        @GET(USER_PATH)
        Call<List<User>> getUsers();

        @GET(USER_PATH)
        Call<List<User>> getUsersSinceID(@Query("since") int id);
    }
}
