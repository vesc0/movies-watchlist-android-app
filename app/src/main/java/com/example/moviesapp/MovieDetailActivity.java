package com.example.moviesapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView moviePoster, likeButton;
    private TextView movieTitle, movieRating, movieSummary, movieReleaseYear;
    private boolean isLiked = false;
    private Movie currentMovie;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        db = FirebaseFirestore.getInstance();

        moviePoster = findViewById(R.id.movie_poster);
        movieTitle = findViewById(R.id.movie_title);
        movieRating = findViewById(R.id.movie_rating);
        movieSummary = findViewById(R.id.movie_summary);
        likeButton = findViewById(R.id.like_button);
        movieReleaseYear = findViewById(R.id.movie_release_year);

        currentMovie = (Movie) getIntent().getSerializableExtra("movie");
        if (currentMovie != null) {
            displayMovieDetails(currentMovie);
            checkIfLiked();
        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLikeStatus();
            }
        });
    }

    private void checkIfLiked() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String movieId = String.valueOf(currentMovie.getId());

        db.collection("liked_movies")
                .document(userId)
                .collection("movies")
                .document(movieId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        isLiked = true;
                        likeButton.setImageResource(R.drawable.ic_heart_filled);
                    } else {
                        isLiked = false;
                        likeButton.setImageResource(R.drawable.ic_heart_outline);
                    }
                });
    }

    private void displayMovieDetails(Movie movie) {
        movieTitle.setText(movie.getTitle());
        movieRating.setText("Rating: " + movie.getVoteAverage() + "/10");
        movieSummary.setText(movie.getOverview());
        movieReleaseYear.setText("Release Year: " + movie.getReleaseYear());

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500/" + movie.getPosterPath())
                .into(moviePoster);
    }

    private void toggleLikeStatus() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String movieId = String.valueOf(currentMovie.getId());
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String movieJson = new Gson().toJson(currentMovie);

        if (isLiked) {
            likeButton.setImageResource(R.drawable.ic_heart_outline);
            isLiked = false;

            // Remove movie from Firestore
            db.collection("liked_movies")
                    .document(userId)
                    .collection("movies")
                    .document(movieId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MovieDetailActivity.this, "Removed from Liked Movies", Toast.LENGTH_SHORT).show();
                    });

        } else {
            likeButton.setImageResource(R.drawable.ic_heart_filled);
            isLiked = true;

            // Add movie to Firestore
            db.collection("liked_movies")
                    .document(userId)
                    .collection("movies")
                    .document(movieId)
                    .set(currentMovie)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MovieDetailActivity.this, "Added to Liked Movies", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
