package com.flixr.jo.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DatabaseCategorises {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String thumbnail;
    @NonNull
    private String url;
    @NonNull
    private String typeCategorises;
    @NonNull
    private String section;
    private boolean isMudbalaj;


    public DatabaseCategorises(@NonNull String name, @NonNull String thumbnail, @NonNull String url, @NonNull String typeCategorises, @NonNull String section, boolean isMudbalaj) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.url = url;
        this.typeCategorises = typeCategorises;
        this.section = section;
        this.isMudbalaj = isMudbalaj;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(@NonNull String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    @NonNull
    public String getTypeCategorises() {
        return typeCategorises;
    }

    public void setTypeCategorises(@NonNull String typeCategorises) {
        this.typeCategorises = typeCategorises;
    }

    @NonNull
    public String getSection() {
        return section;
    }

    public void setSection(@NonNull String section) {
        this.section = section;
    }

    public boolean isMudbalaj() {
        return isMudbalaj;
    }

    public void setMudbalaj(boolean mudbalaj) {
        isMudbalaj = mudbalaj;
    }

    @NonNull
    @Override
    public String toString() {
        return "DatabaseCategorises{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", url='" + url + '\'' +
                ", typeCategorises='" + typeCategorises + '\'' +
                ", section='" + section + '\'' +
                ", isMudbalaj=" + isMudbalaj +
                '}';
    }
}
