package com.karonda.restapi.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;

public class TvCharacter {
    private Integer id;
    private int tvSeriesId;
    @NotNull
    private String name;
    @JsonIgnore
    private String photo;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public int getTvSeriesId() {
        return tvSeriesId;
    }
    public void setTvSeriesId(int tvSeriesId) {
        this.tvSeriesId = tvSeriesId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoUrl() {
        if(this.photo == null) {
            return null;
        }else {
            return "http://127.0.0.1/photos/" + this.photo;
        }
    }
}
