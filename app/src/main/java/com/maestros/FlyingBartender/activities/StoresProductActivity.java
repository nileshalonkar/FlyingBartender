package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.maestros.FlyingBartender.adapter.StoreReviewAdapter;
import com.maestros.FlyingBartender.databinding.ActivityFavoriteBinding;
import com.maestros.FlyingBartender.databinding.ActivityStoresProductBinding;
import com.maestros.FlyingBartender.model.StoreReviewModel;

import java.util.ArrayList;
import java.util.List;

public class StoresProductActivity extends AppCompatActivity {

    ActivityStoresProductBinding binding;
    private StoreReviewAdapter adapter;
    private List<StoreReviewModel> reviewList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStoresProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        adapter = new StoreReviewAdapter(StoresProductActivity.this, reviewList);
        binding.rvShopReview.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StoresProductActivity.this, RecyclerView.VERTICAL, false);
        binding.rvShopReview.setLayoutManager(layoutManager);
        showReview();
    }

    private void showReview(){
        StoreReviewModel model=new StoreReviewModel("Julie Wills","Don't wait visit us and enjoy the lowest rates before these raises again ","3.5");
        for (int i = 0; i <3 ; i++) {
            reviewList.add(model);
        }
    }
}