package com.flixr.jo.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;

import com.flixr.jo.database.DatabaseCategorises;
import com.flixr.jo.enums.DataServer;
import com.flixr.jo.enums.StateServer;
import com.flixr.jo.listener.CallbackReaderCategorises;
import com.flixr.jo.tool.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReaderCategorises extends AsyncTask<String, String, String> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final CallbackReaderCategorises callbackReaderCategorises;

    private List<DatabaseCategorises> databaseCategorisesList;
    private PowerManager.WakeLock wakeLock;
    private boolean isLink = true;

    public ReaderCategorises(Context context, CallbackReaderCategorises callbackReaderCategorises) {
        this.context = context;
        this.callbackReaderCategorises = callbackReaderCategorises;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        wakeLock.acquire(999999 * 60 * 1000L);
        databaseCategorisesList = new ArrayList<>();
    }

    @Override
    protected String doInBackground(String... strings) {
        getData(strings[DataServer.TARGETED_MOVIE.ordinal()],
                strings[DataServer.TARGETED_SERIES.ordinal()],
                strings[DataServer.TARGETED_TV.ordinal()],
                strings[DataServer.CATEGORISES_URL.ordinal()]);
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        String text = "...";
        callbackReaderCategorises.onCallbackReaderCategorises(StateServer.UPDATE.ordinal(), text, databaseCategorisesList);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        wakeLock.acquire(60 * 1000L);
        if (isLink)
            callbackReaderCategorises.onCallbackReaderCategorises(StateServer.SUCCESSFUL.ordinal(), null, databaseCategorisesList);
        else
            callbackReaderCategorises.onCallbackReaderCategorises(StateServer.INVALID.ordinal(), null, databaseCategorisesList);
    }

    private void getData(String... string) throws RuntimeException {
        String targetMovie = string[DataServer.TARGETED_MOVIE.ordinal()];
        String targetSeries = string[DataServer.TARGETED_SERIES.ordinal()];
        String targetTv = string[DataServer.TARGETED_TV.ordinal()];
        String textUrl = string[DataServer.CATEGORISES_URL.ordinal()];

        HttpURLConnection connection;
        String name = null, videoUrl = null, thumbnail = null, categories = null, section = null, targetSection = null;
        boolean mudbalaj = false, categoriesFinding;

        try {
            URL url = new URL(textUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                isLink = false;
                return;
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String textLinesContent;
            while ((textLinesContent = bufferedReader.readLine()) != null) {
                categoriesFinding = false;

                if (textLinesContent.startsWith(Value.FILTER_EXTINF)) {
                    name = textLinesContent.split(Value.FILTER_EXTINF)[1].split(Value.FILTER_TVG_NAME)[1].split("\"")[0];
                    mudbalaj = textLinesContent.split(Value.FILTER_EXTINF)[1].split(Value.FILTER_TVG_NAME)[1].split("\"")[0].contains("مدبلج");
                    section = textLinesContent.split(Value.FILTER_EXTINF)[1].split(Value.FILTER_TVG_TITLE)[1].split("\"")[0].trim();
                    targetSection = textLinesContent.split(Value.FILTER_EXTINF)[1].split(Value.FILTER_TVG_TITLE)[1].split("\"")[0].split("-")[0].trim();
                    thumbnail = textLinesContent.split(Value.FILTER_EXTINF)[1].split(Value.FILTER_TVG_LOGO)[1].split("\"")[0];
                }

                if (textLinesContent.startsWith(Value.FILTER_HTTP) || textLinesContent.startsWith(Value.FILTER_HTTPS)) {
                    videoUrl = textLinesContent.trim();
                    String[] array = textLinesContent.split("/");
                    for (String s : array) {
                        if (!categoriesFinding) {
                            switch (s) {
                                case Value.FILTER_CATEGORIES_MOVIE:
                                    categories = Value.FILTER_CATEGORIES_MOVIE;
                                    categoriesFinding = true;
                                    break;
                                case Value.FILTER_CATEGORIES_SERIES:
                                    categories = Value.FILTER_CATEGORIES_SERIES;
                                    categoriesFinding = true;
                                    break;
                                default:
                                    categories = Value.FILTER_CATEGORIES_TV;
                                    break;
                            }
                        }
                    }
                }

                if (name != null && thumbnail != null && videoUrl != null && categories != null && section != null) {
                    publishProgress(name);
                    if (categories.equals(Value.FILTER_CATEGORIES_MOVIE) && targetMovie.contains(targetSection) ||
                            categories.equals(Value.FILTER_CATEGORIES_SERIES) && targetSeries.contains(targetSection) ||
                            categories.equals(Value.FILTER_CATEGORIES_TV) && targetTv.contains(targetSection) ||
                            categories.equals(Value.FILTER_CATEGORIES_MOVIE) && targetMovie.contains(Value.APP_TEXT_ALL) ||
                            categories.equals(Value.FILTER_CATEGORIES_SERIES) && targetSeries.contains(Value.APP_TEXT_ALL) ||
                            categories.equals(Value.FILTER_CATEGORIES_TV) && targetTv.contains(Value.APP_TEXT_ALL)) {
                        databaseCategorisesList.add(new DatabaseCategorises(name, thumbnail, videoUrl, categories, section, mudbalaj));
                        name = null;
                        thumbnail = null;
                        videoUrl = null;
                        categories = null;
                        section = null;
                        mudbalaj = false;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
