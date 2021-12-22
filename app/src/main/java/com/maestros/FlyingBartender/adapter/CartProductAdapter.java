package com.maestros.FlyingBartender.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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

import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.activities.CartActivity;
import com.maestros.FlyingBartender.activities.CartData;
import com.maestros.FlyingBartender.activities.CheckInterface;
import com.maestros.FlyingBartender.databinding.RowCartProductLayoutBinding;
import com.maestros.FlyingBartender.model.CartProductModel;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.DELETE_CART;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.UPDATE_QUANTITY;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.MyViewHolder> {

    Context mContext;
    List<CartProductModel> cartProductAdapterList;
    String strProdQ="",new_str = "";

    public CartProductAdapter(Context mContext, List<CartProductModel> cartProductAdapterList) {
        this.mContext = mContext;
        this.cartProductAdapterList = cartProductAdapterList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowCartProductLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartProductModel modelObject = cartProductAdapterList.get(position);
        holder.rowCartProductLayoutBinding.txtPName.setText(modelObject.getName());
        holder.rowCartProductLayoutBinding.txtMedium.setText(modelObject.getDescription());
        holder.rowCartProductLayoutBinding.txtPrice.setText(modelObject.getTotal_price());
        holder.rowCartProductLayoutBinding.txtItemCount.setText(modelObject.getQuantity());

        if (!modelObject.equals("")){

            try {
                Glide.with(mContext).load(modelObject.getPath() + modelObject.getImage())
                        .placeholder(R.drawable.imageb).override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.rowCartProductLayoutBinding.imgPremium);
            } catch (Exception e){
            Log.e("gfddfgdgfh", "onBindViewHolder: " +e.getMessage());
            }

        }


        holder.rowCartProductLayoutBinding.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                strProdQ = holder.rowCartProductLayoutBinding.txtItemCount.getText().toString();

                new_str = String.valueOf(Integer.parseInt(strProdQ) + 1);

                holder.rowCartProductLayoutBinding.txtItemCount.setText(new_str);

                int qty = Integer.parseInt(new_str);

/*
                if (qty > stock_count) {
                    Log.e("CartProductAdapter", "qty: " + qty);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setMessage("Can't purchase more than stock Please select under the stock ");
                    alertDialogBuilder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                    Intent plusActivity = new Intent(mContext, CartActivity.class);
                                    mContext.startActivity(plusActivity);
                                    ((Activity) mContext).finish();


                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {

                    update_quantity(modelObject.getCartID(), modelObject.getProductID(), new_str, holder);

                }*/

                update_quantity(modelObject.getCartID(), modelObject.getProductID(), new_str, holder);

            }
        });

        holder.rowCartProductLayoutBinding.imgMinus.setOnClickListener(v -> {

            strProdQ = holder.rowCartProductLayoutBinding.txtItemCount.getText().toString();
            strProdQ = holder.rowCartProductLayoutBinding.txtItemCount.getText().toString();

            new_str = String.valueOf(Integer.parseInt(strProdQ) - 1);

            int xyz = Integer.parseInt(strProdQ) - 1;
            if (xyz < 1) {

                // holder.rowCartProductLayoutBinding.txtItemCount.setText("1");

            } else {

                holder.rowCartProductLayoutBinding.txtItemCount.setText(new_str);

                update_quantity(modelObject.getCartID(), modelObject.getProductID(), new_str, holder);

            }
        });

        holder.rowCartProductLayoutBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_cart(modelObject.getProductID(), modelObject.getCartID());
            }
        });


    }

    @Override
    public int getItemCount() {

        return cartProductAdapterList == null ? 0 : cartProductAdapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowCartProductLayoutBinding rowCartProductLayoutBinding;

        public MyViewHolder(RowCartProductLayoutBinding rowCartProductLayoutBinding) {
            super(rowCartProductLayoutBinding.getRoot());
            this.rowCartProductLayoutBinding = rowCartProductLayoutBinding;
        }

    }


    private void delete_cart(String productID, String showCartId) {
        String strUserId = SharedHelper.getKey(mContext, AppConstats.USER_ID);

        Log.e("fdskftgfdr", showCartId);
        Log.e("fdskftgfdr", strUserId);
        Log.e("fdskftgfdr", productID);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", DELETE_CART)
                .addBodyParameter("productID", productID)
                .addBodyParameter("userID", strUserId)
                .addBodyParameter("cartID", showCartId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("edgregr", response.toString());

                        try {
                            if (response.getString("result").equals("true")) {

                                Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();

                                mContext.startActivity(new Intent(mContext, CartActivity.class));
                                ((Activity) mContext).finish();
                            }
                        } catch (JSONException e) {
                            Log.e("kllgb", e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tyuyfb", anError.getMessage());

                    }
                });

    }


    public void update_quantity(String cartId, String showProductId, String new_str, MyViewHolder holder) {
        String strUserId = SharedHelper.getKey(mContext, AppConstats.USER_ID);
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
                            if (response.getString("message").equals("product updated  Successfully")) {
                                Toast.makeText(mContext, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                               /* AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.TotalAmount,total_pay );
                                editor.commit();
*/
                                mContext.startActivity(new Intent(mContext, CartActivity.class));
                                ((Activity) mContext).finish();

                            }else {
                                Toast.makeText(mContext, ""+response.getString("message"), Toast.LENGTH_SHORT).show();

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
