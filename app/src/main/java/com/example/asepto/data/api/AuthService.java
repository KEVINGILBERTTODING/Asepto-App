package com.example.asepto.data.api;

import com.example.asepto.data.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {

    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseModel> loginKaryawan(
            @Field("username") String username,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseModel> loginAdmin(
            @Field("username") String username,
            @Field("password") String password
    );
}
