package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.maestros.FlyingBartender.adapter.ReviewAdapter;
import com.maestros.FlyingBartender.databinding.ActivityChatBinding;
import com.maestros.FlyingBartender.databinding.ActivityRatingAndReviewBinding;
import com.maestros.FlyingBartender.model.ReviewModel;

import java.util.ArrayList;
import java.util.List;

public class RatingAndReviewActivity extends AppCompatActivity {
ActivityRatingAndReviewBinding binding;
    private ReviewAdapter adapter;
    private List<ReviewModel> reviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRatingAndReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new ReviewAdapter(RatingAndReviewActivity.this, reviewList);

        binding.rvReview.setLayoutManager(new LinearLayoutManager(RatingAndReviewActivity.this, RecyclerView.VERTICAL,false));
        binding.rvReview.setAdapter(adapter);
        getReviewData();

    }

    private void getReviewData() {
        ReviewModel reviewObj = new ReviewModel("Julie Wills", "Don't wait visit us and enjoy the lowest rates before these raises again");

        for (int i = 0; i < 4; i++) {
            reviewList.add(reviewObj);
        }

    }
}