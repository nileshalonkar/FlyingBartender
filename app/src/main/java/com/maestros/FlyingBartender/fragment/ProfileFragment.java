package com.maestros.FlyingBartender.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.Api;
import com.maestros.FlyingBartender.R;
import com.maestros.FlyingBartender.activities.AddAddressActivity;
import com.maestros.FlyingBartender.activities.AddressActivity;
import com.maestros.FlyingBartender.activities.ChangePasswordActivity;
import com.maestros.FlyingBartender.activities.HomeActivity;
import com.maestros.FlyingBartender.retrofit.BaseUrl;
import com.maestros.FlyingBartender.utils.AppConstats;
import com.maestros.FlyingBartender.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

import static android.app.Activity.RESULT_CANCELED;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.SHOW_PROFILE;
import static com.maestros.FlyingBartender.retrofit.BaseUrl.UPDATE_PROFILE;

public class ProfileFragment extends Fragment {
    CircleImageView ProfileCircle;
    TextView txtAddress, txtMobile, tv_change_pwd;
    EditText etFullName, etEmail, etAge, etHouseNo, etAreaColony, etPinCode, etCity;
    Button btnUpdate;
    String User_Id = "";

    private static final String IMAGE_DIRECTORY = "/directory";
    File f;
    String strimage = "";
    private int GALLERY = 1, CAMERA = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ProfileCircle = view.findViewById(R.id.ProfileCircle);
        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        txtMobile = view.findViewById(R.id.txtMobile);
        etAge = view.findViewById(R.id.etAge);
        //  etHouseNo = view.findViewById(R.id.etHouseNo);
        //   etAreaColony = view.findViewById(R.id.etAreaColony);
        //    etPinCode = view.findViewById(R.id.etPinCode);
        //     etCity = view.findViewById(R.id.etCity);
        tv_change_pwd = view.findViewById(R.id.tv_change_pwd);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        txtAddress = view.findViewById(R.id.txtAddress);


        User_Id = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
        Log.e("frfdfrrdsf", User_Id);

        ProfileCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();              //in fragment
            }
        });

        tv_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangePasswordActivity.class));
            }
        });

        txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddressActivity.class));
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_Profile();
            }
        });

        Show_My_Profile();

        return view;
    }


    public void showPictureDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture image from camera"};

        builder.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0:
                        choosePhotoFromGallery();
                        break;

                    case 1:
                        captureFromCamera();
                        break;
                }

            }
        });

        builder.show();
    }

    public void choosePhotoFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, GALLERY);
    }


    public void captureFromCamera() {

        Intent intent_2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent_2, CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    strimage = String.valueOf(f);
                    ProfileCircle.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ProfileCircle.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            strimage = String.valueOf(f);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("fvbcbv", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void Update_Profile() {
        //      progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.initialize(getActivity());
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        AndroidNetworking.initialize(getActivity(), okHttpClient);
        AndroidNetworking.upload(BaseUrl.BASEURL)
                .addMultipartParameter("control", UPDATE_PROFILE)
                .addMultipartParameter("userID", User_Id)
                .addMultipartParameter("full_name", etFullName.getText().toString())
                .addMultipartParameter("email", etEmail.getText().toString())
                //  .addMultipartParameter("mobile", etMobile.getText().toString())
                .addMultipartParameter("age", etAge.getText().toString())
                //     .addMultipartParameter("house_number", etHouseNo.getText().toString())
                //      .addMultipartParameter("area", etAreaColony.getText().toString())
                //      .addMultipartParameter("pincode", etPinCode.getText().toString())
                //        .addMultipartParameter("city", etCity.getText().toString())
                .addMultipartFile("image", f)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //     progressBar.setVisibility(View.GONE);
                        Log.e("eferwfr", "" + response.toString());

                        try {
                            if (response.getString("result").equals("update successfully")) {
                                Toast.makeText(getActivity(), "" + response.getString("result"), Toast.LENGTH_SHORT).show();
                             /*   Glide.with(getContext()).load(response.getString("image"))
                                        .placeholder(R.drawable.user_icon).override(250, 250)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(ProfileCircle);*/
                           /*     etFullName.setText(response.getString("full_name"));
                                etEmail.setText(response.getString("email"));
                                txtMobile.setText(response.getString("mobile"));
                                etAge.setText(response.getString("age"));
                                etHouseNo.setText(response.getString("house_number"));
                                etAreaColony.setText(response.getString("area"));
                                etPinCode.setText(response.getString("pincode"));
                                etCity.setText(response.getString("city"));*/
                                // startActivity(new Intent(getActivity(), HomeActivity.class));

                            } else {
                                Toast.makeText(getActivity(), "" + response.getString("result"), Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("dgfffdf", e.getMessage());
                            //  progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fytfytf", "onError: " + anError);
                        // progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void Show_My_Profile() {
        //    progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.post(BaseUrl.BASEURL)
                .addBodyParameter("control", SHOW_PROFILE)
                .addBodyParameter("userID", User_Id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //   progressBar.setVisibility(View.GONE);
                        Log.e("eferwfr", "" + response.toString());

                        try {
                            if (response.getString("result").equals("show profile successfully")) {
                                //     Toast.makeText(getActivity(), "" + response.getString("result"), Toast.LENGTH_SHORT).show();
                                Glide.with(getContext()).load(response.getString("path") + response.getString("image"))
                                        .placeholder(R.drawable.user_icon).override(100, 100)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(ProfileCircle);
                                etFullName.setText(response.getString("name"));
                                etEmail.setText(response.getString("email"));
                                txtMobile.setText(response.getString("mobile"));
                                etAge.setText(response.getString("age"));
                                //     etHouseNo.setText(response.getString("house_number"));
                                //      etAreaColony.setText(response.getString("area"));
                                ///      etPinCode.setText(response.getString("pincode"));
                                //        etCity.setText(response.getString("city"));
                                // startActivity(new Intent(getActivity(), HomeActivity.class));

                            } else {
                                Toast.makeText(getActivity(), "" + response.getString("result"), Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("dgfffdf", e.getMessage());
                            //     progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("sdafetrr", "onError: " + anError);
                        //   progressBar.setVisibility(View.GONE);
                    }
                });
    }

}