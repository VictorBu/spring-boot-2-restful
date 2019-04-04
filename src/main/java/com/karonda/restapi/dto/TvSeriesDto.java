package com.karonda.restapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TvSeriesDto {
    private Integer id;
    private String name;
    private int seasonCount;
    // 如果想用long型的timestamp表示日期，可用： @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    // @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date originRelease;
    
    public TvSeriesDto() { 
    }
    
    public TvSeriesDto(int id, String name, int seasonCount, Date originRelease) {
        this.id = id;
        this.name = name;
        this.seasonCount = seasonCount;
        this.originRelease = originRelease;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public Date getOriginRelease() {
        return originRelease;
    }

    public void setOriginRelease(Date originRelease) {
        this.originRelease = originRelease;
    }
}
