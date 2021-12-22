package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.adapter.CartProductAdapter;
import com.maestros.FlyingBartender.adapter.CartTittleAdapter;
import com.maestros.FlyingBartender.adapter.ShowProductAdapter;
import com.maestros.FlyingBartender.databinding.ActivityCartBinding;
import com.maestros.FlyingBartender.model.CartProductModel;
import com.maestros.FlyingBartender.model.SubCatWiseProductModel;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.ProgressBarCustom.CustomDialog;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_CART;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    View view;
    RecyclerView.LayoutManager layoutManagerTittle;
    public static ArrayList<String> myLocalCartList = new ArrayList<>();
    ArrayList<Boolean> booleans = new ArrayList<>();
    public static int CountAll = 0;

    List<CartProductModel> cartProductModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        layoutManagerTittle = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvCart.setLayoutManager(layoutManagerTittle);
        binding.rvCart.setHasFixedSize(true);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.txtCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, AddCardActivity.class));
            }
        });
        show_cart();


    }

    private void show_cart() {
        String stUserId = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        Log.e("CartActivity", "onCreate: " + stUserId);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SHOW_CART)
                .addBodyParameter("userID", stUserId)
                .setPriority(Priority.HIGH)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                cartProductModelList = new ArrayList<>();
                Log.e("CartActivity", "onResponse: " + response);
                dialog.hideDialog();
                try {
                    binding.txtGrandTotal.setText(response.getString("grand_total"));
                    JSONArray jsonArray = new JSONArray(response.getString("data"));
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CartProductModel cartProductModel = new CartProductModel();
                        cartProductModel.setCartID(jsonObject.getString("cartID"));
                        cartProductModel.setProductID(jsonObject.getString("productID"));
                        cartProductModel.setQuantity(jsonObject.getString("quantity"));
                        cartProductModel.setGrand_total(jsonObject.getString("grand_total"));
                        cartProductModel.setTotal_price(jsonObject.getString("total_price"));

                        JSONObject jsonObject1 = jsonObject.getJSONObject("product_info");
                        cartProductModel.setName(jsonObject1.getString("name"));
                        cartProductModel.setDescription(jsonObject1.getString("description"));
                        cartProductModel.setPrice(jsonObject1.getString("price"));
                        cartProductModel.setVolume(jsonObject1.getString("volume"));
                        cartProductModel.setPath(jsonObject1.getString("path"));

                        JSONObject jsonObject2 = jsonObject1.getJSONObject("images");
                        cartProductModel.setImage(jsonObject2.getString("image"));

                        cartProductModelList.add(cartProductModel);

                    }

                    binding.rvCart.setHasFixedSize(true);
                    binding.rvCart.setLayoutManager(new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false));
                    binding.rvCart.setAdapter(new CartProductAdapter(CartActivity.this, cartProductModelList));


                } catch (Exception e) {
                    dialog.hideDialog();
                    Log.e("CartActivity", "onResponse: " + e.getMessage());
                }


            }


            @Override
            public void onError(ANError anError) {
                dialog.hideDialog();
                Log.e("CartActivity", "onError: " + anError.getMessage());
            }
        });
    }

}


