package com.karonda.restapi.dao;

import com.karonda.restapi.pojo.TvSeries;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TvSeriesDao {

    @Select("select * from tv_series where id=#{id}")
    TvSeries getOneById(int id);

    @Select("select * from tv_series")
    List<TvSeries> getAll();

    int update(TvSeries tvSeries);
    int insert(TvSeries tvSeries);

    @Delete("delete from tv_series where id=#{id}")
    int delete(int id);

    @Update("update tv_series set status=-1, reason=#{reason} where id=#{id}")
    int logicDelete(int id, String reason);
}
