package com.flixr.jo.tool;

import androidx.recyclerview.widget.DiffUtil;

import com.flixr.jo.database.DatabaseCategorises;

import java.util.List;

public class DiffUtilCallbackData extends DiffUtil.Callback {

    List<DatabaseCategorises> oldList;
    List<DatabaseCategorises> newList;

    public DiffUtilCallbackData(List<DatabaseCategorises> oldList, List<DatabaseCategorises> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }
}
