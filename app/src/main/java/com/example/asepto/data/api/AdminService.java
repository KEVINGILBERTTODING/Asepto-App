package com.example.asepto.data.api;

import com.example.asepto.data.model.KaryawanModel;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.data.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AdminService {
    @GET("admin/getproject")
    Call<List<ProjectModel>> getAllProject(
            @Query("status") String status
    );

    @GET("admin/getAllKaryawan")
    Call<List<KaryawanModel>> getAllKaryawan();

    @FormUrlEncoded
    @POST("admin/updateKaryawan")
    Call<ResponseModel> updateKarywan(
            @Field("id") String id,
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("jabatan") String jabatan,
            @Field("telp") String telp,
            @Field("norekening") String norekening,
            @Field("bank") String bank,
            @Field("jeniskel") String jeniskel
    );
}
