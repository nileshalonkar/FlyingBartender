package com.maestros.FlyingBartender.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.databinding.RowCatproductLayoutBinding;
import com.maestros.FlyingBartender.databinding.RowShowCartLayoutBinding;
import com.maestros.FlyingBartender.model.ShowCreditCartModel;

import java.util.List;

public class ShowCreateCartAdapter extends RecyclerView.Adapter<ShowCreateCartAdapter.MyViewHolder> {


    private Context mContext;
    private List<ShowCreditCartModel> creditList;

    public ShowCreateCartAdapter(Context mContext, List<ShowCreditCartModel> creditList) {
        this.mContext = mContext;
        this.creditList = creditList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowShowCartLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ShowCreditCartModel modelObject = creditList.get(position);
        holder.rowShowCartLayoutBinding.textViewCardNumber.setText(modelObject.getCardNumber());
        holder.rowShowCartLayoutBinding.textViewName.setText(modelObject.getHolderName());
        holder.rowShowCartLayoutBinding.textViewValid.setText(modelObject.getExpiryDate());
        holder.rowShowCartLayoutBinding.constraintLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_Sucess();
            }
        });

    }

    @Override
    public int getItemCount() {
        return creditList == null ? 0 : creditList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowShowCartLayoutBinding rowShowCartLayoutBinding;

        public MyViewHolder(RowShowCartLayoutBinding rowShowCartLayoutBinding) {
            super(rowShowCartLayoutBinding.getRoot());
            this.rowShowCartLayoutBinding = rowShowCartLayoutBinding;
        }

    }

    private void dialog_Sucess() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_successful_layout);
        Button btnOk = dialog.findViewById(R.id.btnOk);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
}
