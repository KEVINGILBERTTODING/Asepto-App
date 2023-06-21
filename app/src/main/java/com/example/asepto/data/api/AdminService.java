package com.example.asepto.data.api;

import com.example.asepto.data.model.FeedBackModel;
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

    @FormUrlEncoded
    @POST("admin/deleteKaryawan")
    Call<ResponseModel> deleteKaryawan(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("admin/insertKaryawan")
    Call<ResponseModel> insertKaryawan(
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("jabatan") String jabatan,
            @Field("telp") String telp,
            @Field("norekening") String norekening,
            @Field("bank") String bank,
            @Field("jeniskel") String jeniskel
    );


    @GET("admin/getAllFeedBack")
    Call<List<FeedBackModel>> getAllReview();


    @FormUrlEncoded
    @POST("admin/updateFeedBack")
    Call<ResponseModel> updateFeedBack(
            @Field("id") String id,
            @Field("feedback") String feedback


    );

    @FormUrlEncoded
    @POST("admin/deleteReview")
    Call<ResponseModel> deleteReview(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("admin/addReview")
    Call<ResponseModel> insertReview(
            @Field("project_id") String project_id,
            @Field("karyawan_id") String karyawan_id,
            @Field("nama_project") String nama_project,
            @Field("feedback") String feedback
    );

    @FormUrlEncoded
    @POST("admin/updateCatatan")
    Call<ResponseModel> updateCatatan(
            @Field("id") String id,
            @Field("catatan") String catatan,
            @Field("tanggal_event") String tglEvent
    );

    @FormUrlEncoded
    @POST("admin/deleteCatatan")
    Call<ResponseModel> deleteCatatan(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("admin/insertCatatan")
    Call<ResponseModel> insertCatatan(
            @Field("catatan") String catatan,
            @Field("tanggal") String tanggal
    );






}
