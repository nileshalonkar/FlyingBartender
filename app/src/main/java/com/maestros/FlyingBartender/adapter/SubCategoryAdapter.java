package com.maestros.FlyingBartender.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.activities.ShowCatWiseProductActivity;
import com.maestros.FlyingBartender.databinding.RowCigratteLayoutBinding;
import com.maestros.FlyingBartender.model.HomeSubCategoryModel;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {
    private Context mContext;
    private List<HomeSubCategoryModel> catList;

    public SubCategoryAdapter(Context mContext, List<HomeSubCategoryModel> catList) {
        this.mContext = mContext;
        this.catList = catList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowCigratteLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent , false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HomeSubCategoryModel modelObject = catList.get(position);
        holder.rowCigratteLayoutBinding.txtName.setText(modelObject.getName());

        if (!modelObject.equals("")){
            Glide.with(mContext).load(modelObject.getPath()+modelObject.getImage())
                 //   .placeholder(R.drawable.bottle).override(250, 250)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.rowCigratteLayoutBinding.imgcategory);
        }

        holder.rowCigratteLayoutBinding.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedHelper.putKey(mContext, AppConstats.SUB_CATEGORYID, modelObject.getSubcategoryID());
                SharedHelper.putKey(mContext, AppConstats.SUB_CATEGORYNAME, modelObject.getName());
                mContext.startActivity(new Intent(mContext, ShowCatWiseProductActivity.class));
            }
        });


        Log.e("dafrsdfd", modelObject.getName());

    }

    @Override
    public int getItemCount() {
        return catList == null ? 0 : catList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowCigratteLayoutBinding rowCigratteLayoutBinding;

        public MyViewHolder(RowCigratteLayoutBinding rowCigratteLayoutBinding) {
            super(rowCigratteLayoutBinding.getRoot());
            this.rowCigratteLayoutBinding = rowCigratteLayoutBinding;
        }

    }
}
