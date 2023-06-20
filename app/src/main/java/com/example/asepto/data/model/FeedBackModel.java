package com.example.asepto.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeedBackModel implements Serializable {

    @SerializedName("feedback_id")
    private String feedbackId;

    @SerializedName("project_id")
    private String projectId;

    @SerializedName("karyawan_id")
    private String karyawanId;

    @SerializedName("nama_project")
    private String namaProject;

    @SerializedName("feedback")
    private String feedback;

    @SerializedName("admin_id")
    private String adminId;

    @SerializedName("nama")
    private String nama;

    public FeedBackModel(String feedbackId, String projectId, String karyawanId, String namaProject, String feedback, String adminId, String nama) {
        this.feedbackId = feedbackId;
        this.projectId = projectId;
        this.karyawanId = karyawanId;
        this.namaProject = namaProject;
        this.feedback = feedback;
        this.adminId = adminId;
        this.nama = nama;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getKaryawanId() {
        return karyawanId;
    }

    public void setKaryawanId(String karyawanId) {
        this.karyawanId = karyawanId;
    }

    public String getNamaProject() {
        return namaProject;
    }

    public void setNamaProject(String namaProject) {
        this.namaProject = namaProject;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
