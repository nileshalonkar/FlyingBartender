package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.adapter.AllListingAdapter;
import com.maestros.FlyingBartender.databinding.ActivityAllListingProductBinding;
import com.maestros.FlyingBartender.databinding.ActivityRecommendedBinding;
import com.maestros.FlyingBartender.model.AllListingModel;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.ProgressBarCustom.CustomDialog;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_FILTER_PRODUCT;

public class AllListingProductActivity extends AppCompatActivity {
    ActivityAllListingProductBinding binding;

    String brandName="";
    private List<AllListingModel> allList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllListingProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        brandName = SharedHelper.getKey(getApplicationContext(), AppConstats.BRANDNAME);

        binding.txt.setText(brandName);
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(AllListingProductActivity.this,2);
        binding.rvAllList.setLayoutManager(layoutManager);

        getAllListData();
    }

    private void getAllListData() {

        String brandId = SharedHelper.getKey(getApplicationContext(), AppConstats.BRANDID);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);

        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SHOW_FILTER_PRODUCT)
                .addBodyParameter("brandID", brandId)
                .setTag("Show Filter Product")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("AllListingProductActivity", "onResponse: " + response.toString());
                        dialog.hideDialog();
                        allList=new ArrayList<>();

                        try {
                            if (response.getBoolean("result") == true) {

                                String data = response.getString("data");
                                JSONArray jsonArray = new JSONArray(data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String images = jsonObject.getString("images");

                                    JSONArray array = new JSONArray(images);
                                    for (int j = 0; j < array.length(); j++) {
                                        JSONObject object = array.getJSONObject(j);

                                        AllListingModel model = new AllListingModel();
                                            model.setProductId(jsonObject.getString("productID"));
                                            model.setCategoryId(jsonObject.getString("categoryID"));
                                            model.setSellerId(jsonObject.getString("sellerID"));
                                            model.setProductName(jsonObject.getString("name"));
                                            model.setPrice(jsonObject.getString("price"));
                                            model.setDescription(jsonObject.getString("description"));
                                            if (!images.equals("")) {
                                                model.setImage(object.getString("image"));
                                                model.setPath(object.getString("path"));
                                                allList.add(model);


                                        }



                                    }


                                }
                                AllListingAdapter adapter = new AllListingAdapter(AllListingProductActivity.this, allList);
                                binding.rvAllList.setAdapter(adapter);
                            }

                            else {
                                binding.lotiAnimation.setVisibility(View.VISIBLE);
                                binding.txtNo.setVisibility(View.VISIBLE);
                             //   Toast.makeText(AllListingProductActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                dialog.hideDialog();
                            }
                        } catch (JSONException e) {
                            Log.e("AllListingProductActivity", "e: " + e.getMessage());
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