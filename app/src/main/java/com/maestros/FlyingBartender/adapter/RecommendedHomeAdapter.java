package com.maestros.FlyingBartender.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.activities.ProductDetailActivity;
import com.maestros.FlyingBartender.databinding.RowRecommendLayoutBinding;
import com.maestros.FlyingBartender.databinding.RowRecommendedHomeLayoutBinding;
import com.maestros.FlyingBartender.model.RecommendedHomeModel;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class RecommendedHomeAdapter extends RecyclerView.Adapter<RecommendedHomeAdapter.MyViewHolder> {


    private Context mContext;
    private List<RecommendedHomeModel> recommentList;

    public RecommendedHomeAdapter(Context mContext, List<RecommendedHomeModel> recommentList) {
        this.mContext = mContext;
        this.recommentList = recommentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowRecommendedHomeLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecommendedHomeModel modelObject = recommentList.get(position);
        holder.rowRecommendedHomeLayoutBinding.txtName.setText(modelObject.getProductName());
        holder.rowRecommendedHomeLayoutBinding.txtCan.setText(modelObject.getDescription());
        holder.rowRecommendedHomeLayoutBinding.txtCategory.setText(modelObject.getCatName());
        holder.rowRecommendedHomeLayoutBinding.txPriceRecommend.setText(modelObject.getPrice() +"KES");

        if (position==0){

            try {
                Glide.with(mContext).load(R.drawable.recommende_img1)
                        .placeholder(R.drawable.dummy).override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.rowRecommendedHomeLayoutBinding.imgRecommended);
            }catch (Exception e){

            }

        }else if (position==1){
            try {
                Glide.with(mContext).load(R.drawable.recommed_img2)
                        .placeholder(R.drawable.dummy).override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.rowRecommendedHomeLayoutBinding.imgRecommended);
            }catch (Exception e){

            }
        }else if (position==2){
            try {
                Glide.with(mContext).load(R.drawable.smiranoff)
                        .placeholder(R.drawable.dummy).override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.rowRecommendedHomeLayoutBinding.imgRecommended);
            }catch (Exception e){

            }
        }else if (position==3) {
            try {
                Glide.with(mContext).load(R.drawable.heinken)
                        .placeholder(R.drawable.dummy).override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.rowRecommendedHomeLayoutBinding.imgRecommended);
            } catch (Exception e) {

            }
        }
    /* try {
            Glide.with(mContext).load(modelObject.getPath()+modelObject.getImage())
                    .placeholder(R.drawable.imageb).override(250, 250)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.rowRecommendedHomeLayoutBinding.imgRecommended);
        } catch (Exception e) {

        }*/


        holder.rowRecommendedHomeLayoutBinding.rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedHelper.putKey(mContext, AppConstats.SELLERID, modelObject.getSellerId());
                SharedHelper.putKey(mContext, AppConstats.CATEGORYID, modelObject.getCategoryId());
                SharedHelper.putKey(mContext, AppConstats.PRODUCTID, modelObject.getProductId());
                mContext.startActivity(new Intent(mContext, ProductDetailActivity.class));
            }
        });

        holder.rowRecommendedHomeLayoutBinding.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart (modelObject.getProductId(),modelObject.getSellerId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return recommentList == null ? 0 : recommentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowRecommendedHomeLayoutBinding rowRecommendedHomeLayoutBinding;

        public MyViewHolder(RowRecommendedHomeLayoutBinding rowRecommendedHomeLayoutBinding) {
            super(rowRecommendedHomeLayoutBinding.getRoot());
            this.rowRecommendedHomeLayoutBinding = rowRecommendedHomeLayoutBinding;
        }

    }
    private void addToCart(String productID, String sellerID) {
        String  stUSER_Id = SharedHelper.getKey(mContext, AppConstats.USER_ID);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", "add_to_cart")
                .addBodyParameter("productID", productID)
                .addBodyParameter("userID", stUSER_Id)
                .addBodyParameter("sellerID", sellerID)
                .addBodyParameter("quantity", "1")
                .setTag("ADD TO CART")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            if (response.getBoolean("result") == true) {


                                Toasty.success(mContext, response.getString("message"), Toasty.LENGTH_SHORT).show();
                            } else {

                                Toasty.error(mContext, response.getString("message"), Toasty.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }
}
