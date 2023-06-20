package com.example.asepto.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CatatanModel implements Serializable {
    @SerializedName("catatan_id")
    private String catatanId;
    @SerializedName("catatan")
    private String catatan;
    @SerializedName("tanggal_event")
    private String tanggalEvent;

    public CatatanModel(String catatanId, String catatan, String tanggalEvent) {
        this.catatanId = catatanId;
        this.catatan = catatan;
        this.tanggalEvent = tanggalEvent;
    }

    public String getCatatanId() {
        return catatanId;
    }

    public void setCatatanId(String catatanId) {
        this.catatanId = catatanId;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getTanggalEvent() {
        return tanggalEvent;
    }

    public void setTanggalEvent(String tanggalEvent) {
        this.tanggalEvent = tanggalEvent;
    }
}
