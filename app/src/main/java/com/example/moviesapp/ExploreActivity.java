package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExploreActivity extends AppCompatActivity {

    private RecyclerView moviesRecyclerView;
    private BottomNavigationView bottomNavigationView;
    private MoviesAdapter moviesAdapter;
    private TMDBApiService apiService;
    private static final String API_KEY = "INSERT_API_KEY";
    private int currentPage = 1;
    private boolean isLoading = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        mAuth = FirebaseAuth.getInstance();

        moviesRecyclerView = findViewById(R.id.movies_recycler_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        moviesAdapter = new MoviesAdapter();
        moviesRecyclerView.setAdapter(moviesAdapter);

        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        apiService = RetrofitClient.getClient();

        fetchMovies();

        TextView welcomeText = findViewById(R.id.welcome_text);
        if (mAuth.getCurrentUser() != null) {
            String userName = mAuth.getCurrentUser().getDisplayName();
            if (userName != null && !userName.isEmpty()) {
                welcomeText.setText(String.format("Welcome, %s", userName));
            } else {
                welcomeText.setText("Welcome, User");
            }
        }

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ExploreActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_explore) {
                return true;
            } else if (item.getItemId() == R.id.nav_search) {
                startActivity(new Intent(ExploreActivity.this, SearchActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_liked) {
                startActivity(new Intent(ExploreActivity.this, LikedMoviesActivity.class));
                return true;
            }
            return false;
        });

        moviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == moviesAdapter.getItemCount() - 1) {
                    loadMoreMovies();
                }
            }
        });
    }

    private void fetchMovies() {
        isLoading = true;
        apiService.getPopularMovies(API_KEY, "en-US", currentPage).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    if (currentPage == 1) {
                        moviesAdapter.setMovies(movies);
                    } else {
                        moviesAdapter.addMovies(movies);
                    }
                } else {
                    Log.e("ExploreActivity", "Failed to fetch movies");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                isLoading = false;
                Log.e("ExploreActivity", "API call failed", t);
            }
        });
    }

    private void loadMoreMovies() {
        currentPage++;
        fetchMovies();
    }
}
