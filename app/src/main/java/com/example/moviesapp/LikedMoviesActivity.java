package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class LikedMoviesActivity extends AppCompatActivity {

    private RecyclerView likedMoviesRecyclerView;
    private MoviesAdapter moviesAdapter;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_movies);

        db = FirebaseFirestore.getInstance();

        likedMoviesRecyclerView = findViewById(R.id.liked_movies_recycler_view);
        likedMoviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        moviesAdapter = new MoviesAdapter();
        likedMoviesRecyclerView.setAdapter(moviesAdapter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        loadLikedMovies();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_explore) {
                startActivity(new Intent(LikedMoviesActivity.this, ExploreActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_search) {
                startActivity(new Intent(LikedMoviesActivity.this, SearchActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_liked) {
                return true;
            }
            return false;
        });
    }

    private void loadLikedMovies() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("liked_movies")
                .document(userId)
                .collection("movies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Movie> movies = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Movie movie = document.toObject(Movie.class);
                            if (movie != null) {
                                movies.add(movie);
                            }
                        }
                        moviesAdapter.setMovies(movies);
                    }
                });
    }
}
