package com.maestros.FlyingBartender.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.databinding.RowFeatureRecommendLayoutBinding;
import com.maestros.FlyingBartender.databinding.RowRecommendLayoutBinding;
import com.maestros.FlyingBartender.model.RecommentModel;

import java.util.List;

public class RecommentAdapter extends RecyclerView.Adapter<RecommentAdapter.MyViewHolder> {


    private Context mContext;
    private List<RecommentModel> recommentList;

    public RecommentAdapter(Context mContext, List<RecommentModel> recommentList) {
        this.mContext = mContext;
        this.recommentList = recommentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowRecommendLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecommentModel modelObject = recommentList.get(position);
         holder.rowRecommendLayoutBinding.txtPName.setText(modelObject.getProductName());
        holder.rowRecommendLayoutBinding.txtdish.setText(modelObject.getShop_description());
        holder.rowRecommendLayoutBinding.txtrate.setText(modelObject.getRating());

     try {
            Glide.with(mContext).load(modelObject.getPath()+modelObject.getImage())
                    .placeholder(R.drawable.imageb).override(250, 250)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.rowRecommendLayoutBinding.imgFavorite);
        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {
        return recommentList == null ? 0 : recommentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowRecommendLayoutBinding rowRecommendLayoutBinding;

        public MyViewHolder(RowRecommendLayoutBinding rowRecommendLayoutBinding) {
            super(rowRecommendLayoutBinding.getRoot());
            this.rowRecommendLayoutBinding = rowRecommendLayoutBinding;
        }

    }
}
