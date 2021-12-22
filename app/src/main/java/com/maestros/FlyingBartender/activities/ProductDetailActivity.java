package com.maestros.FlyingBartender.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.BuildConfig;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.adapter.ProductDetailSimilarAdapter;
import com.maestros.FlyingBartender.databinding.ActivityProductDetailBinding;
import com.maestros.FlyingBartender.model.ProductSimilarModel;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.ProgressBarCustom.CustomDialog;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.ADD_TO_CART;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_PRODUCT_DETAILS;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_SIMILAR_PRODUCT;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.UPDATE_QUANTITY;

public class ProductDetailActivity extends AppCompatActivity {
    ActivityProductDetailBinding binding;
    private ProductDetailSimilarAdapter adapter;
    private List<ProductSimilarModel> similarList = new ArrayList<>();
    String stSellerId = "", stUSER_Id = "", strProdQ = "";
    String new_str = "", cartID = "";
    int stock_count = 0;
    String Product_Id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        stSellerId = SharedHelper.getKey(getApplicationContext(), AppConstats.SELLERID);
        stUSER_Id = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        Log.e("ProductDetailActivity", "USER_ID: " + stUSER_Id);

        Product_Id = SharedHelper.getKey(ProductDetailActivity.this, AppConstats.PRODUCTID);
        Log.e("dgfsgfg", Product_Id);


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this,CartActivity.class));
            }
        });


        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strQuantity = binding.txtCount.getText().toString().trim();
                addToCart(strQuantity);
            }
        });

        binding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "share demo");
                    String shareMessage = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(intent, "share by"));

                } catch (Exception e) {
                    Toast.makeText(ProductDetailActivity.this, "Error occured", Toast.LENGTH_SHORT).show();

                }


            }
        });


      /*  binding.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(ProductDetailActivity.this, RatingAndReviewActivity.class));
            }
        });
*/
        binding.rvSimilar.setLayoutManager(new GridLayoutManager(ProductDetailActivity.this, 4));




        binding.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strProdQ = binding.txtCount.getText().toString();
                new_str = String.valueOf(Integer.parseInt(strProdQ) - 1);
                int xyz = Integer.parseInt(strProdQ) - 1;
                if (xyz < 1) {

                    binding.txtCount.setText("1");

                } else {

                    binding.txtCount.setText(new_str);



                }
            }
        });

        binding.ivplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                strProdQ = binding.txtCount.getText().toString();

                new_str = String.valueOf(Integer.parseInt(strProdQ) + 1);

                binding.txtCount.setText(new_str);

                int qty = Integer.parseInt(new_str);

             /*   if (qty > stock_count) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProductDetailActivity.this);
                    alertDialogBuilder.setMessage("Can't purchase more than stock Please select under the stock ");
                    alertDialogBuilder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                    Intent plusActivity = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
                                    startActivity(plusActivity);
                                    finish();


                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {



                }
*/

            }
        });
        getSimilarData();
        productDetails();
    }

    private void productDetails() {
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SHOW_PRODUCT_DETAILS)
                .addBodyParameter("productID", Product_Id)
                .addBodyParameter("userID", stUSER_Id)
                .setTag("SHOW PRODUCT DETAILS ")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ProductDetailActivity", "onResponse: " + response);

                        try {
                            JSONObject jsonObject = response.getJSONObject("data");
                            binding.txtName.setText(jsonObject.getString("name"));
                            binding.txtDescription.setText(jsonObject.getString("description"));
                            binding.txtPrice.setText("KES" + jsonObject.getString("price"));
                            binding.txtFav.setText(jsonObject.getString("like_count"));
                            JSONObject jsonObject1 = jsonObject.getJSONObject("images");
                            Glide.with(ProductDetailActivity.this).load(jsonObject.getString("path") + jsonObject1.getString("image"))
                                    //  .placeholder(R.drawable.bottle).override(250, 250)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivProduct);

                            Log.e("dsgffdgfdg", jsonObject1.getString("image"));


                                  /*
                                    String stock = object.getString("stock");
                                    if (!stock.equals("")) {
                                        stock_count = Integer.parseInt(stock);
                                    }
                                    binding.ivplus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {


                                            strProdQ = binding.txtCount.getText().toString();

                                            new_str = String.valueOf(Integer.parseInt(strProdQ) + 1);

                                            binding.txtCount.setText(new_str);

                                            int qty = Integer.parseInt(new_str);

                                            if (qty > stock_count) {

                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProductDetailActivity.this);
                                                alertDialogBuilder.setMessage("Can't purchase more than stock Please select under the stock ");
                                                alertDialogBuilder.setPositiveButton("ok",
                                                        new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface arg0, int arg1) {

                                                                Intent plusActivity = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
                                                                startActivity(plusActivity);
                                                                finish();


                                                            }
                                                        });

                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                alertDialog.show();

                                            } else {



                                            }


                                        }
                                    });

                                    binding.ivMinus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            strProdQ = binding.txtCount.getText().toString();
                                            new_str = String.valueOf(Integer.parseInt(strProdQ) - 1);
                                            int xyz = Integer.parseInt(strProdQ) - 1;
                                            if (xyz < 1) {

                                                binding.txtCount.setText("1");

                                            } else {

                                                binding.txtCount.setText(new_str);



                                            }
                                        }
                                    });

                                    if (cart_status.equals("false")){
                                        binding.btnAdd.setVisibility(View.VISIBLE);
                                        binding.btnAlready.setVisibility(View.GONE);


                                    }
                                    else {

                                        JSONObject object1 =new JSONObject(cart_details);
                                        cartID=object1.getString("cartID");
                                        String  quantity=object1.getString("quantity");
                                        binding.txtCount.setText(quantity);

                                        binding.btnAdd.setVisibility(View.GONE);
                                        binding.btnAlready.setVisibility(View.VISIBLE);

                                        binding.ivplus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(final View v) {


                                                strProdQ = binding.txtCount.getText().toString();

                                                new_str = String.valueOf(Integer.parseInt(strProdQ) + 1);

                                                binding.txtCount.setText(new_str);

                                                int qty = Integer.parseInt(new_str);

                                                if (qty > stock_count) {

                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProductDetailActivity.this);
                                                    alertDialogBuilder.setMessage("Can't purchase more than stock Please select under the stock ");
                                                    alertDialogBuilder.setPositiveButton("ok",
                                                            new DialogInterface.OnClickListener() {

                                                                @Override
                                                                public void onClick(DialogInterface arg0, int arg1) {

                                                                    Intent plusActivity = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
                                                                    startActivity(plusActivity);
                                                                    finish();


                                                                }
                                                            });

                                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                                    alertDialog.show();

                                                } else {

                                                    update_quantity(cartID,productID,new_str);

                                                }


                                            }
                                        });

                                        binding.ivMinus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                strProdQ = binding.txtCount.getText().toString();

                                                new_str = String.valueOf(Integer.parseInt(strProdQ) - 1);

                                                int xyz = Integer.parseInt(strProdQ) - 1;
                                                if (xyz < 1) {

                                                  //  binding.txtCount.setText("1");

                                                } else {

                                                    binding.txtCount.setText(new_str);

                                                    update_quantity(cartID,productID,new_str);

                                                }
                                            }
                                        });



                                    }

                                    JSONArray jsonArray = new JSONArray(images);
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object2 = jsonArray.getJSONObject(i);

                                            try {
                                                Glide.with(ProductDetailActivity.this).load(object2.getString("path") + object2.getString("image"))
                                                        .placeholder(R.drawable.dummy).override(250, 250)
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .into(binding.ivProduct);
                                            } catch (Exception e) {

                                            }
                                        }

                                        binding.txtName.setText(object.getString("name"));
                                        binding.txtDescription.setText(object.getString("description"));
                                     //
                                        //
                                        //   binding.txtPrice.setText( object.getString("price")+"KES");





                                    }*/


                        } catch (JSONException e) {
                            Log.e("gjvjjjj", "e: " + e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("gjvjjjj", "anError: " + anError.getMessage());

                    }
                });

    }

    private void getSimilarData() {
        String SubCategoryId = SharedHelper.getKey(getApplicationContext(), AppConstats.SUB_CATEGORYID);
        Log.e("ProductDetailActivity", "stCategoryId: " + SubCategoryId);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SHOW_SIMILAR_PRODUCT)
                .addBodyParameter("subcategoryID", SubCategoryId)
                .setTag("SHOW SIMILAR PRODUCT IMAGE")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hideDialog();
                        similarList = new ArrayList<>();
                        Log.e("ProductDetailActivity", "onResponse: " + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response.getString("data"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ProductSimilarModel productSimilarModel = new ProductSimilarModel();
                                productSimilarModel.setProductID(jsonObject.getString("productID"));
                                productSimilarModel.setName(jsonObject.getString("name"));
                                productSimilarModel.setDescription(jsonObject.getString("description"));
                                productSimilarModel.setPrice(jsonObject.getString("price"));

                                JSONObject jsonObject1 = jsonObject.getJSONObject("images");
                                productSimilarModel.setImage(jsonObject1.getString("image"));

                                productSimilarModel.setPath(jsonObject.getString("path"));

                                similarList.add(productSimilarModel);

                            }

                        adapter = new ProductDetailSimilarAdapter(ProductDetailActivity.this, similarList);
                        binding.rvSimilar.setAdapter(adapter);

                    } catch(
                    JSONException e) {
                        dialog.hideDialog();
                    }
                }

        @Override
        public void onError (ANError anError){
            dialog.hideDialog();
        }
    });

}

    private void addToCart(String strQuantity) {
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", ADD_TO_CART)
                .addBodyParameter("productID", Product_Id)
                .addBodyParameter("userID", stUSER_Id)
                .addBodyParameter("sellerID", stSellerId)
                .addBodyParameter("quantity", strQuantity)
                .setTag("ADD TO CART")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("result") == true) {
                                binding.btnAdd.setVisibility(View.GONE);
                                binding.btnAlready.setVisibility(View.VISIBLE);
                                Toasty.success(ProductDetailActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
                            } else {

                                Toasty.error(ProductDetailActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
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


    public void update_quantity(String cartId, String showProductId, String new_str) {
        String strUserId = SharedHelper.getKey(this, AppConstats.USER_ID);
        Log.e("fkdkg", new_str);
        Log.e("fkdkg", strUserId);
        Log.e("fkdkg", showProductId);
        Log.e("fkdkg", cartId);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", UPDATE_QUANTITY)
                .addBodyParameter("cartID", cartId)
                .addBodyParameter("productID", showProductId)
                .addBodyParameter("quantity", new_str)
                .addBodyParameter("userID", strUserId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("regtdfrh", response.toString());
                        try {
                            if (response.getString("result").equals("true")) {

                            }
                        } catch (JSONException e) {
                            Log.e("tyhth", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ukuihj", anError.getMessage());
                    }
                });


    }
}