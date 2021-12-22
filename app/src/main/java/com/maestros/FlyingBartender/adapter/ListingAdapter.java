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
import com.maestros.FlyingBartender.activities.ProductDetailActivity;
import com.maestros.FlyingBartender.databinding.RowListingLayoutBinding;
import com.maestros.FlyingBartender.model.ListingModel;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.MyViewHolder>{


    private Context mContext;
    private List<ListingModel> List;

    public ListingAdapter(Context mContext, List<ListingModel> List) {
        this.mContext = mContext;
        this.List = List;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowListingLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ListingModel modelObject = List.get(position);
      holder.rowListingLayoutBinding.txtListName.setText(modelObject.getName());
      holder.rowListingLayoutBinding.txtPrice.setText(modelObject.getPrice()+"KES");

      /*  if (position==0){

            try {
                Glide.with(mContext).load(R.drawable.heinken)
                        .placeholder(R.drawable.dummy).override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.rowListingLayoutBinding.imgPremium);
            }catch (Exception e){

            }

        }else if (position==1){
            try {
                Glide.with(mContext).load(R.drawable.recommend_img4)
                        .placeholder(R.drawable.dummy).override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.rowListingLayoutBinding.imgPremium);
            }catch (Exception e){

            }
        }else if (position==2){
            try {
                Glide.with(mContext).load(R.drawable.smiranoff)
                        .placeholder(R.drawable.dummy).override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.rowListingLayoutBinding.imgPremium);
            }catch (Exception e){

            }
        }else if (position==3){
            try {
                Glide.with(mContext).load(R.drawable.recommed_img2)
                        .placeholder(R.drawable.dummy).override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.rowListingLayoutBinding.imgPremium);
            }catch (Exception e){

            }
        }*/

   try {
       Glide.with(mContext).load(modelObject.getPath()+modelObject.getImage())
               .placeholder(R.drawable.imageb).override(150, 150)
               .diskCacheStrategy(DiskCacheStrategy.ALL)
               .into(holder.rowListingLayoutBinding.imgPremium);
   }catch (Exception e){

   }

        holder.rowListingLayoutBinding.rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedHelper.putKey(mContext, AppConstats.CATEGORYID, modelObject.getCategoryId());
                SharedHelper.putKey(mContext, AppConstats.PRODUCTID, modelObject.getProductId());
                SharedHelper.putKey(mContext, AppConstats.SELLERID, modelObject.getSellerId());
                mContext.startActivity(new Intent(mContext, ProductDetailActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return List == null ? 0 : List.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowListingLayoutBinding rowListingLayoutBinding;
        public MyViewHolder(RowListingLayoutBinding rowListingLayoutBinding) {
            super(rowListingLayoutBinding.getRoot());
            this.rowListingLayoutBinding = rowListingLayoutBinding;
        }

    }
}
