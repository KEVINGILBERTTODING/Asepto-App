package com.example.asepto.data.api;

import com.example.asepto.data.model.ProjectModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KaryawanService {
    @GET("karyawan/getMyProject")
    Call<List<ProjectModel>> getMyProject(
            @Query("id")  String id,
            @Query("status") String status
    );
}
