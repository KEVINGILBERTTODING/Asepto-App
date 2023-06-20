package com.example.asepto.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KaryawanModel implements Serializable {
    @SerializedName("karyawan_id")
    private String karyawanId;

    @SerializedName("nama")
    private String nama;

    @SerializedName("email")
    private String email;

    @SerializedName("jabatan")
    private String jabatan;

    @SerializedName("telp")
    private String telp;

    @SerializedName("norekening")
    private String norekening;

    @SerializedName("bank")
    private String bank;

    @SerializedName("jeniskel")
    private String jeniskel;

    public KaryawanModel(String karyawanId, String nama, String email, String jabatan, String telp, String norekening, String bank, String jeniskel) {
        this.karyawanId = karyawanId;
        this.nama = nama;
        this.email = email;
        this.jabatan = jabatan;
        this.telp = telp;
        this.norekening = norekening;
        this.bank = bank;
        this.jeniskel = jeniskel;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getNorekening() {
        return norekening;
    }

    public void setNorekening(String norekening) {
        this.norekening = norekening;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getJeniskel() {
        return jeniskel;
    }

    public void setJeniskel(String jeniskel) {
        this.jeniskel = jeniskel;
    }
}
