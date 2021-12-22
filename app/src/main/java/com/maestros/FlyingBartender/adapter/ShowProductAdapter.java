package com.maestros.FlyingBartender.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.Api;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.activities.DataCatViseProduct;
import com.maestros.FlyingBartender.activities.ProductDetailActivity;
import com.maestros.FlyingBartender.databinding.RowAllListingLayoutBinding;
import com.maestros.FlyingBartender.databinding.RowShowCategoryLayoutBinding;
import com.maestros.FlyingBartender.model.SubCatWiseProductModel;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.ADD_FAVOURITE;

public class ShowProductAdapter extends RecyclerView.Adapter<ShowProductAdapter.MyViewHolder> {

    private Context mContext;
    private List<SubCatWiseProductModel> allList;
    String User_Id="";

    public ShowProductAdapter(Context mContext, List<SubCatWiseProductModel> allList) {
        this.mContext = mContext;
        this.allList = allList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowShowCategoryLayoutBinding view = (RowShowCategoryLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        User_Id = SharedHelper.getKey(mContext,AppConstats.USER_ID);
        Log.e("vdfsvfdvdf", User_Id);

        return new ShowProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubCatWiseProductModel modelObject = allList.get(position);

        holder.rowShowCategoryLayoutBinding.txtName.setText(modelObject.getName());
        holder.rowShowCategoryLayoutBinding.txtCount.setText(modelObject.getVolume());
        holder.rowShowCategoryLayoutBinding.txtPrice.setText("KES" + modelObject.getPrice());

        if (!modelObject.equals("")){
            Glide.with(mContext).load(modelObject.getPath()+modelObject.getImage())
                    //   .placeholder(R.drawable.bottle).override(250, 250)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.rowShowCategoryLayoutBinding.imgPopular);

        }

       holder.rowShowCategoryLayoutBinding.rlMain.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               SharedHelper.putKey(mContext, AppConstats.CATEGORYID, modelObject.getCategoryID());
               SharedHelper.putKey(mContext, AppConstats.PRODUCTID, modelObject.getProductID());
               SharedHelper.putKey(mContext, AppConstats.SELLERID, modelObject.getSellerID());
               mContext.startActivity(new Intent(mContext, ProductDetailActivity.class));
           }
       });

        Log.e("dvfdzdf", modelObject.getProductID() );

        if (modelObject.getLike().equals("1")){
            holder.rowShowCategoryLayoutBinding.likeRed.setVisibility(View.VISIBLE);
            holder.rowShowCategoryLayoutBinding.like.setVisibility(View.GONE);
        }else {
            holder.rowShowCategoryLayoutBinding.likeRed.setVisibility(View.GONE);
            holder.rowShowCategoryLayoutBinding.like.setVisibility(View.VISIBLE);
        }

        holder.rowShowCategoryLayoutBinding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFav( modelObject.getProductID(),holder);

            }
        });
        holder.rowShowCategoryLayoutBinding.likeRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnAddFav( modelObject.getProductID(),holder);
            }
        });


    }

    @Override
    public int getItemCount() {
        return allList == null ? 0 : allList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowShowCategoryLayoutBinding rowShowCategoryLayoutBinding;

        public MyViewHolder(RowShowCategoryLayoutBinding rowShowCategoryLayoutBinding) {
            super(rowShowCategoryLayoutBinding.getRoot());
            this.rowShowCategoryLayoutBinding = rowShowCategoryLayoutBinding;
        }
    }

    public void AddFav(String id, MyViewHolder holder) {
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control",ADD_FAVOURITE)
                .addBodyParameter("userID",User_Id)
                .addBodyParameter("likedID", id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dhdgnfn", response.toString());

                        try {
                            if (response.getString("message").equals(" Liked Successfully")) {
                                Toast.makeText(mContext, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                holder.rowShowCategoryLayoutBinding.likeRed.setVisibility(View.VISIBLE);
                                holder.rowShowCategoryLayoutBinding.like.setVisibility(View.GONE);
                            } else {
                                Toast toast = Toast.makeText(mContext, "" + response.getString("message"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    public void UnAddFav(String id, MyViewHolder holder) {
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control",ADD_FAVOURITE)
                .addBodyParameter("userID",User_Id)
                .addBodyParameter("likedID", id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dhdgnfn", response.toString());

                        try {
                            if (response.getString("message").equals(" Disliked Successfully")) {
                                Toast.makeText(mContext, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                holder.rowShowCategoryLayoutBinding.likeRed.setVisibility(View.GONE);
                                holder.rowShowCategoryLayoutBinding.like.setVisibility(View.VISIBLE);
                            } else {
                                Toast toast = Toast.makeText(mContext, "" + response.getString("message"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }
}
