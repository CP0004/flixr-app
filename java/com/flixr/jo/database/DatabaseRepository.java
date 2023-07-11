package com.flixr.jo.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DatabaseRepository {

    DatabaseDAO databaseDAO;

    public DatabaseRepository(Application application) {
        DatabaseRoom databaseContentRoom = DatabaseRoom.GetDatabase(application);
        databaseDAO = databaseContentRoom.databaseDAO();
    }

    public void insertDatabase(List<DatabaseCategorises> databaseCategorisesList) {
        DatabaseRoom.databaseWriteExecutor.execute(() -> databaseDAO.insertDatabase(databaseCategorisesList));
    }

    public void deleteAllDatabase() {
        DatabaseRoom.databaseWriteExecutor.execute(() -> databaseDAO.deleteAllDatabase());

    }

    public LiveData<List<DatabaseCategorises>> getAllDatabase() {
        return databaseDAO.getAllDatabase();
    }

    public LiveData<List<DatabaseCategorises>> getDataByCategories(String categories) {
        return databaseDAO.getDataByCategories(categories);

    }

    public LiveData<List<DatabaseCategorises>> getDataByCategoriesSearch(String name) {
        return databaseDAO.getDataByCategoriesSearch(name);

    }

    public LiveData<List<DatabaseCategorises>> getDataByEpisode(String categories, String name) {
        return databaseDAO.getDataByEpisode(categories, name);

    }

    public LiveData<List<DatabaseCategorises>> getDataFromTheNameBySeasons(String categories, String name, String seasonsS0, String seasonsS00) {
        return databaseDAO.getDataFromTheNameBySeasons(categories, name, seasonsS0, seasonsS00);

    }
}
