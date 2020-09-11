package com.fahrul.biodatapost.service;

import com.fahrul.biodatapost.model.ModelReselutAdd;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterfacesRest {

    @Multipart
    @POST("biodata/add")
    Call<ModelReselutAdd>
    addBiodata(
            @Part("nama") RequestBody nama,
            @Part("alamat") RequestBody alamat,
            @Part("telepon") RequestBody telepon,
            @Part("lat") RequestBody lat,
            @Part("lon") RequestBody lon,
            @Part MultipartBody.Part img1
    );
}
