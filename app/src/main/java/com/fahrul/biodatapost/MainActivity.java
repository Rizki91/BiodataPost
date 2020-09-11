package com.fahrul.biodatapost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fahrul.biodatapost.model.ModelReselutAdd;
import com.fahrul.biodatapost.service.APIClient;
import com.fahrul.biodatapost.service.APIInterfacesRest;
import com.location.aravind.getlocation.GeoLocator;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import in.balakrishnan.easycam.CameraBundleBuilder;
import in.balakrishnan.easycam.CameraControllerActivity;
import in.balakrishnan.easycam.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText txtNama,txtAlamat,txtTelepon,txtGps;
    ImageView Img;
    Button btnCamera,btnSimpan,btnBatal;
    private  String [] list;
    File file;

    GeoLocator geoLocator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geoLocator = new GeoLocator(getApplicationContext(),MainActivity.this);


        txtNama = findViewById(R.id.txtNama);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtTelepon = findViewById(R.id.txtTelepon);
        txtGps = findViewById(R.id.txtGps);

        btnCamera = findViewById(R.id.btn_Camera);
        btnSimpan = findViewById(R.id.btn_Simpan);
        btnBatal = findViewById(R.id.btn_Batal);
        Img = findViewById(R.id.imgPhoto);




        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ambilCamera();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//


                sendData(txtNama.getText().toString(),txtAlamat.getText().toString(),txtTelepon.getText().toString(),String.valueOf(geoLocator.getLattitude()),String.valueOf(geoLocator.getLongitude()));
            }
        });

    }




    APIInterfacesRest apiInterface;

    ProgressDialog progressDialog;

    private void sendData(String nama, String alamat, String telepon, String lat, String lon) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");

        progressDialog.show();


        RequestBody requestFile1 = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part bodyImg1 =
                MultipartBody.Part.createFormData("photo", file.getName(), requestFile1);


        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);


        Call<ModelReselutAdd> absentAdd = apiInterface.addBiodata(
                toRequestBody(nama),
                toRequestBody(alamat),
                toRequestBody(telepon),
                toRequestBody(lat),
                toRequestBody(lon),
                bodyImg1);

        absentAdd.enqueue(new Callback<ModelReselutAdd>() {
            @Override
            public void onResponse(Call<ModelReselutAdd> call, Response<ModelReselutAdd> response) {

                progressDialog.dismiss();

                ModelReselutAdd status = response.body();

                if (status != null) {

                    if (status.getStatus()) {


                    } else {
                        progressDialog.dismiss();
                        try {
                            JSONObject jObjError = new JSONObject(response.body().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                } else {

                    progressDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //     Toast.makeText(ShoppingProductGrid.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        String error = jObjError.get("status_detail").toString();
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Send Failed, " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        //    Toast.makeText(ShoppingProductGrid.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ModelReselutAdd> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    public RequestBody toRequestBody(String value) {
        if (value == null) {
            value = "";
        }
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    public void ambilCamera(){
        Intent intent = new Intent(this, CameraControllerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("inputData", new CameraBundleBuilder()
                .setFullscreenMode(false)
                .setDoneButtonString("Add")
                .setSinglePhotoMode(false)
                .setMax_photo(1)
                .setManualFocus(true)
                .setBucketName(getClass().getName())
                .setPreviewEnableCount(true)
                .setPreviewIconVisiblity(true)
                .setPreviewPageRedirection(true)
                .setEnableDone(false)
                .setClearBucket(true)
                .createCameraBundle());
        startActivityForResult(intent, 214);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 214) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                list = data.getStringArrayExtra("resultData");
                file = new File(list[0]);
                Picasso.get().load(file).into(Img);

                txtGps.setText(String.valueOf(geoLocator.getLattitude()) +";"+ String.valueOf(geoLocator.getLongitude()));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.clearAllFiles(this, getClass().getName());
    }
}