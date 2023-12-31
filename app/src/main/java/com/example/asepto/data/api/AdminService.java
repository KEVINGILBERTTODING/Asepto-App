package com.example.asepto.data.api;

import com.example.asepto.data.model.FeedBackModel;
import com.example.asepto.data.model.KaryawanModel;
import com.example.asepto.data.model.ProgressModel;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.data.model.ResponseModel;
import com.example.asepto.data.model.TaskModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @FormUrlEncoded
    @POST("admin/insertProject")
    Call<ResponseModel> insertProject(
            @Field("nama_project") String nama_project,
            @Field("deskripsi_project") String deskripsi_project,
            @Field("kategori") String kategori,
            @Field("tgl_mulai") String tgl_mulai,
            @Field("tgl_selesai") String tgl_selesai,
            @Field("nama_perusahaan") String nama_perusahaan,
            @Field("email_perusahaan") String email_perusahaan,
            @Field("budget") String budget,
            @Field("karyawan_id") String karyawan_id,
            @Field("nama") String nama
    );

    @FormUrlEncoded
    @POST("admin/updateProject")
    Call<ResponseModel> updateProject(
            @Field("id") String id,
            @Field("nama_project") String nama_project,
            @Field("deskripsi_project") String deskripsi_project,
            @Field("kategori") String kategori,
            @Field("tgl_mulai") String tgl_mulai,
            @Field("tgl_selesai") String tgl_selesai,
            @Field("nama_perusahaan") String nama_perusahaan,
            @Field("email_perusahaan") String email_perusahaan,
            @Field("budget") String budget,
            @Field("karyawan_id") String karyawan_id,
            @Field("nama") String nama
    );

    @FormUrlEncoded
    @POST("admin/deleteProject")
    Call<ResponseModel> deleteProject(
            @Field("id") String id
    );

    @Multipart
    @POST("admin/projectDone")
    Call<ResponseModel> finishProject(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part partFile
            );

    @GET("admin/getProgress")
    Call<List<ProgressModel>> getAllProgress(
            @Query("id") String id
    );
    @FormUrlEncoded
    @POST("admin/insertTask")
    Call<ResponseModel> insertTask(
            @Field("project_id") String project_id,
            @Field("task_name") String task_name,
            @Field("karyawan_id") String karyawan_id
    );

    @GET("admin/getTaskByProject")
    Call<List<TaskModel>> getTaskByProjectId(
            @Query("id") String id
    );

    @FormUrlEncoded
    @POST("admin/deleteTask")
    Call<ResponseModel> deleteTask(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("admin/updateTask")
    Call<ResponseModel> updateTask(
            @Field("id") String id,
            @Field("task_name") String taskName
    );







}
