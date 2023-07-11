package com.flixr.jo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DatabaseCategorises.class}, version = 1, exportSchema = false)
public abstract class DatabaseRoom extends RoomDatabase {

    public abstract DatabaseDAO databaseDAO();

    private static volatile DatabaseRoom INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static DatabaseRoom GetDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseRoom.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseRoom.class, "database_room")
                            .addCallback(DatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback DatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {

            });
        }
    };
}
