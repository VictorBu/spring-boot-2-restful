package com.karonda.restapi.service;

import com.karonda.restapi.dao.TvSeriesDao;
import com.karonda.restapi.pojo.TvSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TvSeriesService {
    @Autowired
    TvSeriesDao tvSeriesDao;

    public List<TvSeries> getAllSeries(){
        return tvSeriesDao.getAll();
    }
}
