package com.example.asepto.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProgressModel implements Serializable {
    @SerializedName("progress_id")
    private String progressId;

    @SerializedName("project_id")
    private String projectId;

    @SerializedName("karyawan_id")
    private String karyawanId;

    @SerializedName("jabatan")
    private String jabatan;
    @SerializedName("nama")
    private String nama;
    @SerializedName("nama_project")
    private String namaProject;

    @SerializedName("progress")
    private String progress;

    @SerializedName("keterangan")
    private String keterangan;

    @SerializedName("tanggal")
    private String tanggal;

    @SerializedName("jabatan_karyawan")
    private String jabatanKaryawan;

    public ProgressModel(String progressId, String projectId, String karyawanId, String jabatan, String nama, String namaProject, String progress, String keterangan, String tanggal, String jabatanKaryawan) {
        this.progressId = progressId;
        this.projectId = projectId;
        this.karyawanId = karyawanId;
        this.jabatan = jabatan;
        this.nama = nama;
        this.namaProject = namaProject;
        this.progress = progress;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
        this.jabatanKaryawan = jabatanKaryawan;
    }

    public String getProgressId() {
        return progressId;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
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

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNamaProject() {
        return namaProject;
    }

    public void setNamaProject(String namaProject) {
        this.namaProject = namaProject;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJabatanKaryawan() {
        return jabatanKaryawan;
    }

    public void setJabatanKaryawan(String jabatanKaryawan) {
        this.jabatanKaryawan = jabatanKaryawan;
    }
}
