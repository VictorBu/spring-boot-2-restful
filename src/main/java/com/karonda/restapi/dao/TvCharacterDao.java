package com.karonda.restapi.dao;

import com.karonda.restapi.pojo.TvCharacter;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvCharacterDao {
    
    @Select("select * from tv_character where id=#{id}")
    TvCharacter getOneById(int id);
    
    @Select("select * from tv_character where tv_series_id=#{tvSeriesId}")
    List<TvCharacter> getAllByTvSeriesId(int tvSeriesId);
    
    int update(TvCharacter tvCharacter);
    int insert(TvCharacter tvCharacter);
    
    @Delete("delete from tv_character where id=#{id}")
    int delete(int id);
}
