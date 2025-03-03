package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchResultsRecyclerView;
    private MoviesAdapter moviesAdapter;
    private TMDBApiService apiService;
    private static final String API_KEY = "INSERT_API_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText searchInput = findViewById(R.id.search_input);
        ImageView searchIcon = findViewById(R.id.search_icon);

        searchResultsRecyclerView = findViewById(R.id.search_results_recycler_view);
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        moviesAdapter = new MoviesAdapter();
        searchResultsRecyclerView.setAdapter(moviesAdapter);

        apiService = RetrofitClient.getClient();

        searchIcon.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                searchMovies(query);
            } else {
                Toast.makeText(SearchActivity.this, "Please enter a search query", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_explore) {
                startActivity(new Intent(SearchActivity.this, ExploreActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_liked) {
                startActivity(new Intent(SearchActivity.this, LikedMoviesActivity.class));
                return true;
            }
            return false;
        });
    }

    private void searchMovies(String query) {
        apiService.searchMovies(API_KEY, query, "en-US", 1).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    moviesAdapter.setMovies(movies);
                } else {
                    Log.e("SearchActivity", "Failed to fetch search results");
                    Toast.makeText(SearchActivity.this, "Failed to fetch search results", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("SearchActivity", "API call failed", t);
                Toast.makeText(SearchActivity.this, "API call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
