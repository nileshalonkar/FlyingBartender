package com.maestros.FlyingBartender.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maestros.FlyingBartender.adapter.FeedAdapter;
import com.maestros.FlyingBartender.databinding.FragmentFeedBinding;
import com.maestros.FlyingBartender.model.Feed;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {

    private FeedAdapter adapter;
    private List<Feed> feedList;
    private FragmentFeedBinding binding;
    private View view;
    private Context context;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentFeedBinding.inflate(getLayoutInflater(), container, false);
        view=binding.getRoot();
        context=getActivity();

        feedList = new ArrayList<>();
        adapter = new FeedAdapter(context, feedList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        binding.rvFeed.setLayoutManager(mLayoutManager);
        binding.rvFeed.setAdapter(adapter);

        getData();
        return view;
    }

    private void getData() {

        Feed a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a);
        a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a); a = new Feed("Lorem Ipsum is simply dummy", "Lorem Ipsum is simply dummy");
        feedList.add(a);
        adapter.notifyDataSetChanged();
    }
}