package com.example.asepto.data.api;

import com.example.asepto.data.model.ProgressModel;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.data.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface KaryawanService {
    @GET("karyawan/getMyProject")
    Call<List<ProjectModel>> getMyProject(
            @Query("id")  String id,
            @Query("status") String status
    );

    @GET("karyawan/getProgressById")
    Call<List<ProgressModel>> getProgressById(
            @Query("user_id") String userId,
            @Query("project_id") String projectId
    );

    @FormUrlEncoded
    @POST("karyawan/updateProgress")
    Call<ResponseModel> updateProgress(
            @Field("id") String id,
            @Field("keterangan") String keterangan
    );
}
