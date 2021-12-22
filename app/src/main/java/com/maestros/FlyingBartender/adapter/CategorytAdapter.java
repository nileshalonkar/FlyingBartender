package com.maestros.FlyingBartender.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.maestros.FlyingBartender.activities.ShowAllCategoryActivity;
import com.maestros.FlyingBartender.activities.ShowCatWiseProductActivity;
import com.maestros.FlyingBartender.databinding.RowCatproductLayoutBinding;
import com.maestros.FlyingBartender.model.HomeCategoryModel;
import com.maestros.FlyingBartender.model.HomeSubCategoryModel;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_SUB_CATEGORY;

public class CategorytAdapter extends RecyclerView.Adapter<CategorytAdapter.MyViewHolder> {
    private Context mContext;
    private List<HomeCategoryModel> homeCategoryModelList;
    ArrayList<HomeSubCategoryModel> homeSubCategoryList;

    public CategorytAdapter(Context mContext, List<HomeCategoryModel> homeCategoryModelList) {
        this.mContext = mContext;
        this.homeCategoryModelList = homeCategoryModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowCatproductLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HomeCategoryModel homeCategoryModel = homeCategoryModelList.get(position);
        holder.rowCatproductLayoutBinding.txtCategoryName.setText(homeCategoryModel.getName());

        Log.e("fgfdgfdgfd", homeCategoryModel.getName());
        Log.e("fhfhfdhfh", homeCategoryModel.getCategoryID());

        AllSubCategory(homeCategoryModel.getCategoryID(), holder);

        holder.rowCatproductLayoutBinding.txtViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedHelper.putKey(mContext, AppConstats.CATEGORYID, homeCategoryModel.getCategoryID());
                mContext.startActivity(new Intent(mContext, ShowAllCategoryActivity.class));
            }
        });

        Log.e("fgfdgf", homeCategoryModel.getCategoryID());

   /*     holder.rowCatproductLayoutBinding.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedHelper.putKey(mContext, AppConstats.CATEGORYID, homeCategoryModel.getCategoryID());
                SharedHelper.putKey(mContext, AppConstats.CATEGORYNAME, homeCategoryModel.getName());
                mContext.startActivity(new Intent(mContext, ShowCatWiseProductActivity.class));
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return homeCategoryModelList == null ? 0 : homeCategoryModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowCatproductLayoutBinding rowCatproductLayoutBinding;

        public MyViewHolder(RowCatproductLayoutBinding rowCatproductLayoutBinding) {
            super(rowCatproductLayoutBinding.getRoot());
            this.rowCatproductLayoutBinding = rowCatproductLayoutBinding;
        }
    }

    public void AllSubCategory(String categoryID, MyViewHolder holder) {
        AndroidNetworking.initialize(mContext);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        AndroidNetworking.initialize(mContext, okHttpClient);
        Log.e("ewrwe", categoryID);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SHOW_SUB_CATEGORY)
                .addBodyParameter("categoryID", categoryID)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ghjfgkjgfdg", response.toString());
                        homeSubCategoryList = new ArrayList<>();
                        try {

                            JSONArray jsonArray = new JSONArray(response.getString("data"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HomeSubCategoryModel homeSubCategoryModel = new HomeSubCategoryModel();
                                homeSubCategoryModel.setSubcategoryID(jsonObject.getString("subcategoryID"));
                                homeSubCategoryModel.setCategoryID(jsonObject.getString("categoryID"));
                                homeSubCategoryModel.setName(jsonObject.getString("name"));
                                homeSubCategoryModel.setImage(jsonObject.getString("image"));
                                homeSubCategoryModel.setPath(jsonObject.getString("path"));
                                homeSubCategoryList.add(homeSubCategoryModel);
                            }

                            //   RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                            holder.rowCatproductLayoutBinding.rvSubCategory.setHasFixedSize(true);
                            holder.rowCatproductLayoutBinding.rvSubCategory.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
                            holder.rowCatproductLayoutBinding.rvSubCategory.setAdapter(new SubCategoryAdapter(mContext, homeSubCategoryList));

                        } catch (Exception exception) {
                            Log.e("abcdhd", exception.getMessage());


                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("abcdhd", "onError: " + anError);

                    }
                });

    }
}
