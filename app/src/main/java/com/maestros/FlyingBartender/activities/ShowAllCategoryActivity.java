package com.maestros.FlyingBartender.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.adapter.CategoryHome;
import com.maestros.FlyingBartender.adapter.CategorytAdapter;
import com.maestros.FlyingBartender.adapter.PremiumAdapter;
import com.maestros.FlyingBartender.adapter.ShowAllCategorytAdapter;
import com.maestros.FlyingBartender.adapter.SubCategoryAdapter;
import com.maestros.FlyingBartender.databinding.ActivityShowAllCatBinding;
import com.maestros.FlyingBartender.model.HomeSubCategoryModel;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.ProgressBarCustom.CustomDialog;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_BRAND;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_CATEGORY;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_SUB_CATEGORY;

public class ShowAllCategoryActivity extends AppCompatActivity {

    ActivityShowAllCatBinding binding;
    ArrayList<HomeSubCategoryModel> homeSubCategoryList;
    String Category_Id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowAllCatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Category_Id = SharedHelper.getKey(ShowAllCategoryActivity.this, AppConstats.CATEGORYID);
        Log.e("fgfdgfdgfd", Category_Id);

     /*   binding.rvCategory.setLayoutManager(new GridLayoutManager(ShowAllCategoryActivity.this, 2));
        binding.rvBrands.setLayoutManager(new GridLayoutManager(ShowAllCategoryActivity.this, 2));*/
     //   getCategoryData();

        AllSubCategory();

    }


    private void getCategoryData() {
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SHOW_CATEGORY)
                .setTag("Show Category")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ShowAllCategoryActivity", response.toString());
                        dialog.hideDialog();
                        try {
                            if (response.getBoolean("result") == true) {

                                Gson gson = new Gson();
                                CategoryHome dataCategory = gson.fromJson(response.toString(), CategoryHome.class);

                                ArrayList arrayList = new ArrayList<CategoryHome.Data>();
                                if (!dataCategory.getData().isEmpty()) {
                                    arrayList.addAll(dataCategory.getData());
                                } else {
                                    Toast.makeText(ShowAllCategoryActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                }


                                ShowAllCategorytAdapter adapterCat = new ShowAllCategorytAdapter(ShowAllCategoryActivity.this, arrayList);
                            //    binding.rvCategory.setAdapter(adapterCat);
                            }
                        } catch (JSONException e) {
                            Log.e("ShowAllCategoryActivity", "e: " + e.getMessage());
                            dialog.hideDialog();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.hideDialog();

                    }
                });

    }

    public void AllSubCategory() {
     /*   AndroidNetworking.initialize(mContext);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        AndroidNetworking.initialize(mContext, okHttpClient);*/
        Log.e("ewrwe", Category_Id);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SHOW_SUB_CATEGORY)
                .addBodyParameter("categoryID", Category_Id)
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

                         binding.rvSubCategory.setHasFixedSize(true);
                         binding.rvSubCategory.setLayoutManager(new GridLayoutManager(ShowAllCategoryActivity.this, 2, RecyclerView.VERTICAL, false));
                         binding.rvSubCategory.setAdapter(new SubCategoryAdapter(ShowAllCategoryActivity.this, homeSubCategoryList));

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


