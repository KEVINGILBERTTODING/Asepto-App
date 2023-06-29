package com.example.asepto.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TaskModel implements Serializable {
    @SerializedName("task_id")
    private String taskId;
    @SerializedName("project_id")
    private String projectId;
    @SerializedName("task_name")
    private String taskName;
    @SerializedName("status")
    private Integer status;
    @SerializedName("karyawan_id")
    private String karyawanId;
    @SerializedName("keterangan")
    private String keterangan;
    @SerializedName("date_post")
    private String datePost;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("nama_karyawan")
    private String namaKaryawan;


    public TaskModel(String taskId, String namaKaryawan, String projectId, String taskName, Integer status, String karyawanId, String keterangan, String datePost, String createdAt, String updatedAt) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.taskName = taskName;
        this.status = status;
        this.karyawanId = karyawanId;
        this.keterangan = keterangan;
        this.datePost = datePost;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.namaKaryawan = namaKaryawan;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getKaryawanId() {
        return karyawanId;
    }

    public void setKaryawanId(String karyawanId) {
        this.karyawanId = karyawanId;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }
}
