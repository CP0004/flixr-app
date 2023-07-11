package com.flixr.jo.listener;

import com.flixr.jo.database.DatabaseCategorises;

import java.util.List;

public interface CallbackReaderCategorises {
    void onCallbackReaderCategorises(int state, String text, List<DatabaseCategorises> databaseCategorisesList);
}
