package com.maestros.FlyingBartender.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.databinding.RowAllcatLayoutBinding;
import com.maestros.FlyingBartender.databinding.RowResultCategoryLayoutBinding;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import java.util.List;

public class SearchResultCategorytAdapter extends RecyclerView.Adapter<SearchResultCategorytAdapter.MyViewHolder> {


    private Context mContext;
    private List<CategoryHome.Data> catList;

    public SearchResultCategorytAdapter(Context mContext, List<CategoryHome.Data> catList) {
        this.mContext = mContext;
        this.catList = catList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowResultCategoryLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoryHome.Data modelObject = catList.get(position);
        holder.rowResultCategoryLayoutBinding.txtName.setText(modelObject.getName());

        holder.rowResultCategoryLayoutBinding.rlCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rowResultCategoryLayoutBinding.rlCat.setBackgroundColor(mContext.getResources().getColor(R.color.md_teal_400));
                SharedHelper.putKey(mContext, AppConstats.CATEGORYID, modelObject.getCategoryID());
            }
        });




    }

    @Override
    public int getItemCount() {
        return catList == null ? 0 : catList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowResultCategoryLayoutBinding rowResultCategoryLayoutBinding;

        public MyViewHolder(RowResultCategoryLayoutBinding rowResultCategoryLayoutBinding) {
            super(rowResultCategoryLayoutBinding.getRoot());
            this.rowResultCategoryLayoutBinding = rowResultCategoryLayoutBinding;
        }

    }
}
