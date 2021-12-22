package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.adapter.AdapterTablayout;
import com.maestros.FlyingBartender.adapter.CategorytAdapter;
import com.maestros.FlyingBartender.adapter.ShowProductAdapter;
import com.maestros.FlyingBartender.databinding.ActivityFavoriteBinding;
import com.maestros.FlyingBartender.databinding.ActivityShowCatWiseProductBinding;

import com.maestros.FlyingBartender.model.HomeCategoryModel;
import com.maestros.FlyingBartender.model.ShowCatWiseProductModel;
import com.maestros.FlyingBartender.model.SubCatWiseProductModel;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.Connectivity;
import com.maestros.FlyingBartender.utils.ProgressBarCustom.CustomDialog;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_FILTER_PRODUCT;

public class ShowCatWiseProductActivity extends AppCompatActivity {
   ActivityShowCatWiseProductBinding binding;
    private List<ShowCatWiseProductModel> productArrayList=new ArrayList<>();
      List<SubCatWiseProductModel> subCatWiseProductModelList;
    String SubCategoryId="",SubCategoryName="",User_Id="";
    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowCatWiseProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SubCategoryId = SharedHelper.getKey(getApplicationContext(), AppConstats.SUB_CATEGORYID);
        SubCategoryName = SharedHelper.getKey(getApplicationContext(), AppConstats.SUB_CATEGORYNAME);
        User_Id = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);

        Log.e("gvfrgfdg",SubCategoryId);
        Log.e("ghjnhgjh",User_Id);
        binding.txtCatName.setText(SubCategoryName);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       /* for (int i = 0; i < no_of_weeks; i++) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("TAB " + String.valueOf(i + 1)));
        }*/
     //   binding.tabLayout.addTab( binding.tabLayout.newTab().setText( "Sign Up" ) );
     //   binding.tabLayout.addTab( binding.tabLayout.newTab().setText( "Sign In" ) );

/*
        final AdapterTablayout adapterTablayout = new AdapterTablayout( getSupportFragmentManager(), binding.tabLayout.getTabCount() );
        binding.viewPager.setAdapter( adapterTablayout );
        binding.viewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener( binding.tabLayout ) );

        binding.tabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem( tab.getPosition() );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        } );
*/



        binding.rvProducts.setLayoutManager(new GridLayoutManager(this,2));

        Connectivity connectivity = new Connectivity(this);
        if (connectivity.isOnline()){
            showProduct();

        }
        else {
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }

    }

    private void showProduct() {
        CustomDialog dialog=new CustomDialog();
        dialog.showDialog(R.layout.progress_layout,this);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SHOW_FILTER_PRODUCT)
                .addBodyParameter("subcategoryID", SubCategoryId)
                .addBodyParameter("userID", User_Id)
                .setTag("Show Category wise  Product")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        subCatWiseProductModelList = new ArrayList<>();

                        Log.e("ShowCatWiseProductActivity", response.toString());
                        dialog.hideDialog();
                        try {
                            if (response.getString("message").equals("showing data Successfully")){
                                JSONArray jsonArray = new JSONArray(response.getString("data"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    SubCatWiseProductModel subCatWiseProductModel = new SubCatWiseProductModel();
                                    subCatWiseProductModel.setProductID(jsonObject.getString("productID"));
                                    subCatWiseProductModel.setName(jsonObject.getString("name"));
                                    subCatWiseProductModel.setDescription(jsonObject.getString("description"));
                                    subCatWiseProductModel.setPrice(jsonObject.getString("price"));
                                    subCatWiseProductModel.setVolume(jsonObject.getString("volume"));
                                    subCatWiseProductModel.setLike(jsonObject.getString("like"));
                                    subCatWiseProductModel.setLike_count(jsonObject.getString("like_count"));

                                    JSONObject jsonObject1 = jsonObject.getJSONObject("images");
                                    subCatWiseProductModel.setImage(jsonObject1.getString("image"));

                                    subCatWiseProductModel.setPath(jsonObject.getString("path"));
                                    subCatWiseProductModelList.add(subCatWiseProductModel);

                                    Log.e("dffrgrfgr",jsonObject.getString("path"));
                            }



                            }else {

                                binding.lotiAnimation.setVisibility(View.VISIBLE);
                                binding.txtNo.setVisibility(View.VISIBLE);
                                dialog.hideDialog();
                            }

                            binding.rvProducts.setHasFixedSize(true);
                            binding.rvProducts.setLayoutManager(new GridLayoutManager(ShowCatWiseProductActivity.this, 2, RecyclerView.VERTICAL, false));
                            binding.rvProducts.setAdapter(new ShowProductAdapter(ShowCatWiseProductActivity.this, subCatWiseProductModelList));

                        } catch (JSONException e) {
                            Log.e("ShowCatWiseProductActivity", "e: " + e.getMessage());
                            dialog.hideDialog();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.hideDialog();
                    }
                });
    }

}
