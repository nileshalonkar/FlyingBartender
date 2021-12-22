package com.maestros.FlyingBartender.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.databinding.ActivityHomeBinding;
import com.maestros.FlyingBartender.fragment.FeedFragment;
import com.maestros.FlyingBartender.fragment.HomeFragment;
import com.maestros.FlyingBartender.fragment.NotificationFragment;
import com.maestros.FlyingBartender.fragment.ProfileFragment;
import com.maestros.FlyingBartender.fragment.RewardFragment;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Context context;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        context = this;

        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
        }
        loadFragment(new HomeFragment());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.menu_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
            /*    case R.id.menu_feed:
                    fragment = new FeedFragment();
                    loadFragment(fragment);
                    return true;*/

                case R.id.menu_messages:
                    fragment = new NotificationFragment();
//                    fragment = new MessagesFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.menu_favorite:
                    fragment = new RewardFragment();
//                    fragment = new FavoriteFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.menu_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return true;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}