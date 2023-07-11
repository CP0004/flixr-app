package com.flixr.jo.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.flixr.jo.database.DatabaseCategorises;
import com.flixr.jo.ui.BaseViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Tools {

    //-- Display data in batches
    public static void divisionCategorises(List<DatabaseCategorises> databaseCopy, List<DatabaseCategorises> databaseView, MutableLiveData<List<DatabaseCategorises>> databaseCategorises) {
        if (databaseCopy.size() > 0) {
            randomArray(databaseCopy);
            if (databaseCopy.get(0).getTypeCategorises().equals(Value.FILTER_CATEGORIES_SERIES)) {
                int sizeArray = 0;
                for (int i = 0; i < databaseCopy.size(); i++) {
                    if (sizeArray < 30) {
                        String name = FiltersName(databaseCopy.get(i).getName());
                        if (!BaseViewModel.nameCategorises.toString().contains(name)) {
                            BaseViewModel.nameCategorises.append(name);
                            databaseView.add(databaseCopy.get(i));
                            databaseCategorises.setValue(databaseView);
                            databaseCopy.remove(databaseCopy.get(i));
                            sizeArray++;
                        }
                    } else return;
                }
            } else {
                int sizeArray = 0;
                for (int i = 0; i < databaseCopy.size(); i++) {
                    if (sizeArray < 30) {
                        databaseView.add(databaseCopy.get(i));
                        databaseCategorises.setValue(databaseView);
                        sizeArray++;
                        databaseCopy.remove(databaseCopy.get(i));
                    } else return;
                }
            }
        }
    }

    //-- Filters Name Content
    public static String FiltersName(String name) {
        return name.split("S0")[0].split("S1")[0].split("S2")[0].split("S3")[0].trim();
    }

    //--Get Photo Url
    public static void GetPhotoUrl(Context context, List<DatabaseCategorises> dataCategories, ImageView imgThumbnail, int index) {
        if (dataCategories.get(index).getThumbnail().startsWith(Value.FILTER_HTTPS) || dataCategories.get(index).getThumbnail().startsWith(Value.FILTER_HTTP))
            Glide.with(context).load(dataCategories.get(index).getThumbnail()).centerCrop().into(imgThumbnail);
        else
            Glide.with(context).load(Value.APP_DEFAULT_PHOTO_THUMBNAIL).centerCrop().into(imgThumbnail);
    }

    //-- The possibility of data changing randomly
    public static void randomArray(List<DatabaseCategorises> databaseCategorisesList) {
        Collections.addAll(databaseCategorisesList);
        Collections.shuffle(databaseCategorisesList, new Random());
    }

    //-- Convert TimeMillis To Date
    public static String MillisToDate() {
        long time = System.currentTimeMillis();
        return DateFormat.format("yyyy-MM-dd-kk", Long.parseLong(String.valueOf(time))).toString();
    }

    //-- Convert Date To Number
    public static int DateToNumber(String date) {
        String[] dates = date.split("-");
        return Integer.parseInt(dates[0] + dates[1] + dates[2] + dates[3]);
    }

    //-- Difference Between Two Dates
    public static int DifferenceTwoDates(String deviceDate, String serverDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-kk");
        try {
            return (int) TimeUnit.HOURS.convert(Objects.requireNonNull(format.parse(serverDate)).getTime() - Objects.requireNonNull(format.parse(deviceDate)).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //-- Extra Hour to date
    @SuppressLint("SimpleDateFormat")
    public static String AddHour(String deviceDate, int numberOfHour) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-kk");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(dateFormat.parse(deviceDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.HOUR_OF_DAY, numberOfHour);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd-kk");
        Date newDate = new Date(c.getTimeInMillis());
        return dateFormat.format(newDate);
    }

    // -- Convert Date To DateFormat
    public static String FormatDateToString(String date) {
        String[] dates = date.split("-");
        return dates[0] + "/" + dates[1] + "/" + dates[2];
    }

    public static String FormatDateToDey(int hour) {
        int D = hour / 24;
        return String.valueOf(D);
    }

    public static String FormatDateToHour(int hour) {
        int H = hour % 24;
        return String.valueOf(H);
    }


    //-- Open Link
    public static void GoToURL(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    //-- Copy Text
    public static void CopyText(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "" + text, Toast.LENGTH_SHORT).show();
    }

}
