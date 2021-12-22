package com.maestros.FlyingBartender.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.activities.FavoriteActivity;
import com.maestros.FlyingBartender.activities.ProductDetailActivity;
import com.maestros.FlyingBartender.adapter.CategorytAdapter;
import com.maestros.FlyingBartender.adapter.FavoriteAdapter;
import com.maestros.FlyingBartender.adapter.ProductDetailSimilarAdapter;
import com.maestros.FlyingBartender.databinding.FragmentFavouriteBinding;
import com.maestros.FlyingBartender.databinding.FragmentHomeBinding;
import com.maestros.FlyingBartender.model.FavoriteModel;
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

import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_FAVOURITE;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_SIMILAR_PRODUCT;

public class FavouriteFragment extends Fragment {
    private FragmentFavouriteBinding binding;
    private View view;

    private FavoriteAdapter adapter;
    private List<FavoriteModel> favoriteList = new ArrayList<>();

    String User_Id="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentFavouriteBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();


        Show_Favourite();
        return view;
    }

    private void Show_Favourite() {
        String User_Id = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
        Log.e("rfdsgfdgfdg", "stCategoryId: " + User_Id);
    /*    CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, getContext());*/
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SHOW_FAVOURITE)
                .addBodyParameter("userID", User_Id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                     //   dialog.hideDialog();
                        favoriteList = new ArrayList<>();
                        Log.e("rgdfgfdgf", "onResponse: " + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response.getString("product"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                FavoriteModel favoriteModel = new FavoriteModel();
                                favoriteModel.setProductID(jsonObject.getString("productID"));
                                favoriteModel.setName(jsonObject.getString("name"));
                                favoriteModel.setDescription(jsonObject.getString("description"));
                                favoriteModel.setPrice(jsonObject.getString("price"));
                                favoriteModel.setVolume(jsonObject.getString("volume"));

                                JSONObject jsonObject1 = jsonObject.getJSONObject("images");
                                favoriteModel.setImage(jsonObject1.getString("image"));

                                favoriteModel.setLike(jsonObject.getString("like"));
                                favoriteModel.setPath(jsonObject.getString("path"));

                                favoriteList.add(favoriteModel);

                            }

                            binding.rvFavorite.setHasFixedSize(true);
                            binding.rvFavorite.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                            binding.rvFavorite.setAdapter(new FavoriteAdapter(getContext(), favoriteList));

                        } catch (JSONException e) {
                            Log.e("dfdsfds", "onResponse: "+e.getMessage());
                         //   dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfdsfds", "onResponse: "+anError.getMessage());
                      //  dialog.hideDialog();
                    }
                });
    }
}