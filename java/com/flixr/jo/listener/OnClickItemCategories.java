package com.flixr.jo.listener;

import com.flixr.jo.database.DatabaseCategorises;

import java.util.List;

public interface OnClickItemCategories {
    void onClickItemCategories(List<DatabaseCategorises> databaseCategorisesList, int position);
}
