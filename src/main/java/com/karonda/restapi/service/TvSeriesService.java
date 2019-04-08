package com.karonda.restapi.service;

import com.karonda.restapi.dao.TvCharacterDao;
import com.karonda.restapi.dao.TvSeriesDao;
import com.karonda.restapi.pojo.TvCharacter;
import com.karonda.restapi.pojo.TvSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TvSeriesService {

    @Autowired
    private TvSeriesDao seriesDao;
    @Autowired
    private TvCharacterDao characterDao;

    public List<TvSeries> getAllTvSeries(){
        List<TvSeries> list = seriesDao.getAll();

        return list;
    }

    public TvSeries getTvSeriesById(int tvSeriesId) {

        TvSeries series = seriesDao.getOneById(tvSeriesId);
        if(series != null) {
            series.setTvCharacters(characterDao.getAllByTvSeriesId(tvSeriesId));
        }
        return series;
    }

    public TvSeries updateTvSeries(TvSeries tvSeries) {
        seriesDao.update(tvSeries);
        return tvSeries;
    }

    public TvSeries addTvSeries(TvSeries tvSeries) {
        seriesDao.insert(tvSeries);
        if(tvSeries.getId() == null) {
            throw new RuntimeException("cannot got primary key id");
        }
        if(tvSeries.getTvCharacters() != null) {
            for(TvCharacter tc : tvSeries.getTvCharacters()) {
                tc.setTvSeriesId(tvSeries.getId().intValue());
                characterDao.insert(tc);
            }
        }
        return tvSeries;
    }

    public TvCharacter updateTvCharacter(TvCharacter tvCharacter) {
        characterDao.update(tvCharacter);
        return tvCharacter;
    }

    public TvCharacter addTvCharacter(TvCharacter tvCharacter) {
        characterDao.insert(tvCharacter);
        return tvCharacter;
    }

    public boolean deleteTvSeries(int id, String reason) {
        seriesDao.logicDelete(id, reason);
        return true;
    }
}
