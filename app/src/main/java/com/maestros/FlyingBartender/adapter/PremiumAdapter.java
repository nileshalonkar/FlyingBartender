package com.maestros.FlyingBartender.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.activities.AllListingProductActivity;
import com.maestros.FlyingBartender.activities.DataImagePremium;
import com.maestros.FlyingBartender.databinding.RowLayoutFeedBinding;
import com.maestros.FlyingBartender.databinding.RowPremiumLayoutBinding;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import java.util.List;

public class PremiumAdapter extends RecyclerView.Adapter<PremiumAdapter.MyViewHolder>{


    private Context mContext;
    private List<DataImagePremium.Data> premiumList;

    public PremiumAdapter(Context mContext, List<DataImagePremium.Data> premiumList) {
        this.mContext = mContext;
        this.premiumList = premiumList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowPremiumLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataImagePremium.Data modelObject = premiumList.get(position);



      /*  if (position==0){

            try {
                Glide.with(mContext).load(R.drawable.recommed_img2)
                        .placeholder(R.drawable.dummy).override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.rowPremiumLayoutBinding.imgPremium);
            }catch (Exception e){

            }

        }else if (position==1){
            try {
                Glide.with(mContext).load(R.drawable.recommende_img1)
                        .placeholder(R.drawable.dummy).override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.rowPremiumLayoutBinding.imgPremium);
            }catch (Exception e){

            }
        }*/

        try {
            Glide.with(mContext).load(modelObject.getPath()+modelObject.getImage())
                    .error(R.drawable.dummy).override(520, 520)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.rowPremiumLayoutBinding.imgPremium);
        }catch (Exception e){

        }
        holder.rowPremiumLayoutBinding.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedHelper.putKey(mContext, AppConstats.BRANDID ,modelObject.getBrandID());
                SharedHelper.putKey(mContext, AppConstats.BRANDNAME ,modelObject.getName());
                mContext.startActivity(new Intent(mContext, AllListingProductActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return premiumList == null ? 0 : premiumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowPremiumLayoutBinding rowPremiumLayoutBinding;
        public MyViewHolder(RowPremiumLayoutBinding rowPremiumLayoutBinding) {
            super(rowPremiumLayoutBinding.getRoot());
            this.rowPremiumLayoutBinding = rowPremiumLayoutBinding;
        }

    }
}
