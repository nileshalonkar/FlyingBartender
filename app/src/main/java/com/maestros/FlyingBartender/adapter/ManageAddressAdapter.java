package com.maestros.FlyingBartender.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.maestros.FlyingBartender.activities.BottomNavActivity;
import com.maestros.FlyingBartender.activities.ManageAddressActivity;
import com.maestros.FlyingBartender.databinding.RowCatproductLayoutBinding;
import com.maestros.FlyingBartender.databinding.RowManageAddressLayoutBinding;
import com.maestros.FlyingBartender.model.ManageAddressModel;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.maestros.FlyingBartender.retrofit.BaseUrl.DELETE_ADDRESS;

public class ManageAddressAdapter extends RecyclerView.Adapter<ManageAddressAdapter.MyViewHolder> {


    private Context mContext;
    private List<ManageAddressModel> addressList;
    String stUserId="";
    public ManageAddressAdapter(Context mContext, List<ManageAddressModel> addressList) {
        this.mContext = mContext;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowManageAddressLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ManageAddressModel modelObject = addressList.get(position);
        holder.rowManageAddressLayoutBinding.txtName.setText(modelObject.getName());
        holder.rowManageAddressLayoutBinding.txtAddress.setText(modelObject.getAddress());
        holder.rowManageAddressLayoutBinding.txtType.setText(modelObject.getType());


        holder.rowManageAddressLayoutBinding.rlMAin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SharedHelper.putKey(mContext,AppConstats.SELECTADDRESS,modelObject.getAddress());
               mContext.startActivity(new Intent(mContext, BottomNavActivity.class));
            }
        });

      holder.rowManageAddressLayoutBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
           deleteAddress(modelObject.getAddressId());
         }
     });
    }

    @Override
    public int getItemCount() {
        return addressList == null ? 0 : addressList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowManageAddressLayoutBinding rowManageAddressLayoutBinding;

        public MyViewHolder(RowManageAddressLayoutBinding rowManageAddressLayoutBinding) {
            super(rowManageAddressLayoutBinding.getRoot());
            this.rowManageAddressLayoutBinding = rowManageAddressLayoutBinding;
        }

    }
    private void deleteAddress(String addressId){
        stUserId=SharedHelper.getKey(mContext,AppConstats.USER_ID);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", DELETE_ADDRESS)
                .addBodyParameter("userID", stUserId)
                .addBodyParameter("addressID", addressId)
                .setTag("address deleted Successfully")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ManageAddressAdapter", "response: " +response);
                        try {
                            if (response.getString("result").equals("true")){
                                Toasty.success(mContext,response.getString("message"),Toasty.LENGTH_SHORT).show();
                               mContext.startActivity(new Intent(mContext, ManageAddressActivity.class));
                            }
                            else {
                                Toasty.error(mContext,response.getString("message"),Toasty.LENGTH_SHORT).show();
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
