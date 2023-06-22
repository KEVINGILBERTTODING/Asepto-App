package com.example.asepto.data.model;

import com.example.asepto.util.Constans;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProjectModel implements Serializable {


        @SerializedName("id")
        private String id;

        @SerializedName("project_id")
        private String projectId;

        @SerializedName("nama_project")
        private String namaProject;

        @SerializedName("deskripsi_project")
        private String deskripsiProject;

        @SerializedName("kategori")
        private String kategori;

        @SerializedName("gambar_project")
        private String gambarProject;

        @SerializedName("tgl_mulai")
        private String tglMulai;

        @SerializedName("tgl_selesai")
        private String tglSelesai;

        @SerializedName("nama_perusahaan")
        private String namaPerusahaan;

        @SerializedName("email_perusahaan")
        private String emailPerusahaan;

        @SerializedName("tipe_perusahaan")
        private String tipePerusahaan;

        @SerializedName("budget")
        private String budget;

        @SerializedName("karyawan_id")
        private String karyawanId;

        @SerializedName("nama")
        private String nama;

        @SerializedName("status")
        private String status;

    public ProjectModel(String id, String projectId, String namaProject, String deskripsiProject, String kategori, String gambarProject, String tglMulai, String tglSelesai, String namaPerusahaan, String emailPerusahaan, String tipePerusahaan, String budget, String karyawanId, String nama, String status) {
        this.id = id;
        this.projectId = projectId;
        this.namaProject = namaProject;
        this.deskripsiProject = deskripsiProject;
        this.kategori = kategori;
        this.gambarProject = gambarProject;
        this.tglMulai = tglMulai;
        this.tglSelesai = tglSelesai;
        this.namaPerusahaan = namaPerusahaan;
        this.emailPerusahaan = emailPerusahaan;
        this.tipePerusahaan = tipePerusahaan;
        this.budget = budget;
        this.karyawanId = karyawanId;
        this.nama = nama;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getNamaProject() {
        return namaProject;
    }

    public void setNamaProject(String namaProject) {
        this.namaProject = namaProject;
    }

    public String getDeskripsiProject() {
        return deskripsiProject;
    }

    public void setDeskripsiProject(String deskripsiProject) {
        this.deskripsiProject = deskripsiProject;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getGambarProject() {
        return Constans.URL_EVIDENCE + gambarProject;
    }

    public void setGambarProject(String gambarProject) {
        this.gambarProject = gambarProject;
    }

    public String getTglMulai() {
        return tglMulai;
    }

    public void setTglMulai(String tglMulai) {
        this.tglMulai = tglMulai;
    }

    public String getTglSelesai() {
        return tglSelesai;
    }

    public void setTglSelesai(String tglSelesai) {
        this.tglSelesai = tglSelesai;
    }

    public String getNamaPerusahaan() {
        return namaPerusahaan;
    }

    public void setNamaPerusahaan(String namaPerusahaan) {
        this.namaPerusahaan = namaPerusahaan;
    }

    public String getEmailPerusahaan() {
        return emailPerusahaan;
    }

    public void setEmailPerusahaan(String emailPerusahaan) {
        this.emailPerusahaan = emailPerusahaan;
    }

    public String getTipePerusahaan() {
        return tipePerusahaan;
    }

    public void setTipePerusahaan(String tipePerusahaan) {
        this.tipePerusahaan = tipePerusahaan;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getKaryawanId() {
        return karyawanId;
    }

    public void setKaryawanId(String karyawanId) {
        this.karyawanId = karyawanId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

