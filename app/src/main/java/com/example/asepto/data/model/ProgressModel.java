package com.example.asepto.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProgressModel implements Serializable {
    @SerializedName("code")
    private Integer code;
    @SerializedName("progress")
    Integer progress;

    public ProgressModel(Integer code, Integer progress) {
        this.code = code;
        this.progress = progress;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
