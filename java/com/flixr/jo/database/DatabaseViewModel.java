package com.flixr.jo.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DatabaseViewModel extends AndroidViewModel {

    DatabaseRepository databaseRepository;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
    }

    public void insertDatabase(List<DatabaseCategorises> databaseCategorisesList) {
        DatabaseRoom.databaseWriteExecutor.execute(() -> databaseRepository.insertDatabase(databaseCategorisesList));
    }

    public void deleteAllContent() {
        DatabaseRoom.databaseWriteExecutor.execute(() -> databaseRepository.deleteAllDatabase());

    }

    public LiveData<List<DatabaseCategorises>> getAllDatabase() {
        return databaseRepository.getAllDatabase();
    }

    public LiveData<List<DatabaseCategorises>> getDataByCategories(String categories) {
        return databaseRepository.getDataByCategories(categories);

    }

    public LiveData<List<DatabaseCategorises>> getDataByCategoriesSearch(String name) {
        return databaseRepository.getDataByCategoriesSearch(name);

    }


    public LiveData<List<DatabaseCategorises>> getDataByEpisode(String categories, String name) {
        return databaseRepository.getDataByEpisode(categories, name);

    }

    public LiveData<List<DatabaseCategorises>> getDataFromTheNameBySeasons(String categories, String name, String seasonsS0, String seasonsS00) {
        return databaseRepository.getDataFromTheNameBySeasons(categories, name, seasonsS0, seasonsS00);

    }
}
