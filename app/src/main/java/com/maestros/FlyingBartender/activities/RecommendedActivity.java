package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.adapter.FeatureRecommentAdapter;
import com.maestros.FlyingBartender.adapter.RecommentAdapter;
import com.maestros.FlyingBartender.databinding.ActivityFavoriteBinding;
import com.maestros.FlyingBartender.databinding.ActivityRecommendedBinding;
import com.maestros.FlyingBartender.model.FeatureRecommentModel;
import com.maestros.FlyingBartender.model.RecommentModel;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.ProgressBarCustom.CustomDialog;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.RECOMMEND_STORE;

public class RecommendedActivity extends AppCompatActivity {


    ActivityRecommendedBinding binding;

    private RecommentAdapter adapter;
    private List<RecommentModel> recommentList = new ArrayList<>();

    private FeatureRecommentAdapter featureadapter;
    private List<FeatureRecommentModel> featureList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecommendedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(RecommendedActivity.this, 2);
        binding.rvFeature.setLayoutManager(layoutManager);


        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(RecommendedActivity.this, RecyclerView.VERTICAL, false);
        binding.rvRecomment.setLayoutManager(layoutManager1);

        getFeatureData();
        getRecommentData();


    }

    private void getFeatureData() {


        FeatureRecommentModel favObj = new FeatureRecommentModel("Tipsy Liquors", "Chinese Food Spacialists", "4.5", R.drawable.drinksss);

        for (int i = 0; i < 4; i++) {
            featureList.add(favObj);
        }
        featureadapter = new FeatureRecommentAdapter(RecommendedActivity.this, featureList);
        binding.rvFeature.setAdapter(featureadapter);

    }




    private void getRecommentData(){

        String USER_ID = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", RECOMMEND_STORE)
                .addBodyParameter("userID", USER_ID)
                .setTag("Show Recommended Product")
                .setPriority(Priority.HIGH)
                .build()
                 .getAsJSONObject(new JSONObjectRequestListener() {
                     @Override
                     public void onResponse(JSONObject response) {
                         Log.e("RecommendedActivity", "response: " +response);
                         dialog.hideDialog();
                         try {
                             if (response.getString("result").equals("true")){
                                 String data = response.getString("data");
                                 Log.e("RecommendedActivity", "data: " +data);
                                 if (!data.isEmpty()){
                                     JSONArray jsonArray = new JSONArray(data);

                                     for (int i = 0; i <jsonArray.length() ; i++) {
                                         JSONObject jsonObject = jsonArray.getJSONObject(i);
                                         String opening_time = jsonObject.getString("opening_time");
                                         Log.e("RecommendedActivity", "opening_time: " +opening_time);

                                         if (!opening_time.isEmpty()) {
                                             JSONObject object = new JSONObject(opening_time);

                                             RecommentModel model = new RecommentModel();
                                             model.setProductName(jsonObject.getString("shop_name"));
                                             model.setRating(jsonObject.getString("avg_rating"));
                                             model.setPath(jsonObject.getString("path"));
                                             model.setImage(jsonObject.getString("profile_image"));
                                             model.setTiming(object.getString("open"));
                                             model.setShop_description(jsonObject.getString("shop_description"));
                                             recommentList.add(model);


                                         }
                                         adapter = new RecommentAdapter(RecommendedActivity.this, recommentList);
                                         binding.rvRecomment.setAdapter(adapter);
                                     }
                                 }
                                 }

                         } catch (JSONException e) {
                             Log.e("RecommendedActivity", "e: " +e.getMessage());
                             dialog.hideDialog();
                         }
                     }

                     @Override
                     public void onError(ANError anError) {
                         Log.e("RecommendedActivity", "anError: " +anError.getMessage());
                         dialog.hideDialog();

                     }
                 });

    }

}