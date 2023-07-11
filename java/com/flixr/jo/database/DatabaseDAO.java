package com.flixr.jo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DatabaseDAO {

    @Insert
    void insertDatabase(List<DatabaseCategorises> databaseCategorisesList);

    @Query("delete from DatabaseCategorises")
    void deleteAllDatabase();

    @Query("select * from DatabaseCategorises")
    LiveData<List<DatabaseCategorises>> getAllDatabase();

    @Query("select * from DatabaseCategorises where typeCategorises like '%'||:categories ||'%'")
    LiveData<List<DatabaseCategorises>> getDataByCategories(String categories);

    @Query("select * from DatabaseCategorises where name like '%'||:name ||'%'")
    LiveData<List<DatabaseCategorises>> getDataByCategoriesSearch(String name);

    @Query("select * from databasecategorises where typeCategorises like '%'||:categories ||'%'AND name like '%'||:name ||'%'")
    LiveData<List<DatabaseCategorises>> getDataByEpisode(String categories, String name);

    @Query("select * from DatabaseCategorises where typeCategorises like '%'||:categories ||'%' AND name like '%'||:name ||'%' AND name like '%'||:seasonsS0 ||'%' OR typeCategorises like '%'||:categories ||'%' AND name like '%'||:name ||'%' AND name like '%'||:seasonsS00 ||'%'")
    LiveData<List<DatabaseCategorises>> getDataFromTheNameBySeasons(String categories, String name, String seasonsS0, String seasonsS00);
}