package com.maestros.FlyingBartender.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.databinding.RowFavoriteDealsLayoutBinding;
import com.maestros.FlyingBartender.databinding.RowLayoutFeedBinding;
import com.maestros.FlyingBartender.model.FavoriteModel;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>{

    private Context mContext;
    private List<FavoriteModel> favoriteList;

    public FavoriteAdapter(Context mContext, List<FavoriteModel> favoriteList) {
        this.mContext = mContext;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowFavoriteDealsLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FavoriteModel modelObject = favoriteList.get(position);
        holder.rowFavoriteDealsLayoutBinding.txtName.setText(modelObject.getName());
        holder.rowFavoriteDealsLayoutBinding.txtVolume.setText(modelObject.getVolume());
        holder.rowFavoriteDealsLayoutBinding.txtPrice.setText(modelObject.getPrice());
        try {
            Glide.with(mContext).load(modelObject.getPath()+modelObject.getImage())
                    .placeholder(R.drawable.dummy).override(250, 250)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.rowFavoriteDealsLayoutBinding.imgFavorite);
        } catch (Exception e) {

        }

        Log.e("ythghgfhv", modelObject.getName());

    }

    @Override
    public int getItemCount() {
        return favoriteList == null ? 0 : favoriteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowFavoriteDealsLayoutBinding rowFavoriteDealsLayoutBinding;
        public MyViewHolder(RowFavoriteDealsLayoutBinding rowFavoriteDealsLayoutBinding) {
            super(rowFavoriteDealsLayoutBinding.getRoot());
            this.rowFavoriteDealsLayoutBinding = rowFavoriteDealsLayoutBinding;
        }

    }
}
