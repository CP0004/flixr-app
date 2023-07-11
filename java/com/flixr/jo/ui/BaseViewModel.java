package com.flixr.jo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.flixr.jo.database.DatabaseCategorises;
import com.flixr.jo.database.DatabaseViewModel;
import com.flixr.jo.enums.DataServer;
import com.flixr.jo.enums.StateServer;
import com.flixr.jo.model.ReaderCategorises;
import com.flixr.jo.model.Server;
import com.flixr.jo.tool.Preference;
import com.flixr.jo.tool.Tools;
import com.flixr.jo.tool.Value;

import java.util.ArrayList;
import java.util.List;

public class BaseViewModel extends ViewModel {

    public static StringBuilder nameCategorises = new StringBuilder();

    public static String instagram = "";
    public static String telegram = "";
    public static String tiktok = "";

    //--== SPLASH VALUE
    boolean isGetDefaultData;
    public MutableLiveData<String> showCategorisesName = new MutableLiveData<>();
    public MutableLiveData<Boolean> loadingCategorisesDate = new MutableLiveData<>();

    //--== MAIN VALUE
    private final List<DatabaseCategorises> databaseMovies = new ArrayList<>();
    private final List<DatabaseCategorises> databaseSeries = new ArrayList<>();
    private final List<DatabaseCategorises> databaseTv = new ArrayList<>();
    private final List<DatabaseCategorises> databaseCopy = new ArrayList<>();
    private final List<DatabaseCategorises> databaseView = new ArrayList<>();
    boolean isGetDefaultCategorises;
    public MutableLiveData<List<DatabaseCategorises>> setCategorisesToAdapter = new MutableLiveData<>();
    public MutableLiveData<Boolean> loadingSetCategorisesToAdapter = new MutableLiveData<>();

    //--== SEARCH VALUE
    private final List<DatabaseCategorises> databaseSearch = new ArrayList<>();
    private final List<DatabaseCategorises> databaseViewSearch = new ArrayList<>();
    public MutableLiveData<List<DatabaseCategorises>> setCategorisesSearchToAdapter = new MutableLiveData<>();
    public MutableLiveData<Boolean> loadingSetCategorisesSearchToAdapter = new MutableLiveData<>();

    //--== EPISODE VALUE
    boolean isGetDefaultCategorisesEpisode;
    private final List<DatabaseCategorises> databaseEpisode = new ArrayList<>();
    private final List<String> databaseSeason = new ArrayList<>();
    private final List<DatabaseCategorises> databaseViewEpisode = new ArrayList<>();
    public MutableLiveData<List<DatabaseCategorises>> setCategorisesEpisodeToAdapter = new MutableLiveData<>();
    public MutableLiveData<List<String>> setCategorisesEpisodeForSeasonToAdapter = new MutableLiveData<>();
    public MutableLiveData<Boolean> loadingSetCategorisesEpisodeToAdapter = new MutableLiveData<>();

    //--== LOG VALUE
    public MutableLiveData<Integer> stateLog = new MutableLiveData<>();

    //--== SPLASH FUNCTION =========================================================================

    public void defaultGetData(Activity context) {
        DatabaseViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(DatabaseViewModel.class);
        if (!isGetDefaultData) {
            isGetDefaultData = true;
            new Server(context, (stateServer, value) -> {
                stateLog.setValue(stateServer);
                if (stateServer == StateServer.SUCCESSFUL.ordinal()) {
                    instagram = value[DataServer.INSTAGRAM.ordinal()];
                    telegram = value[DataServer.TELEGRAM.ordinal()];
                    tiktok = value[DataServer.TIKTOK.ordinal()];
                    if (!value[DataServer.VERSION_CATEGORIES.ordinal()].equals(Preference.getValueString(context, Value.SAVE_VERSION_CATEGORISES))) {
                        viewModel.deleteAllContent();
                        new ReaderCategorises(context, (stateReader, text, databaseCategorisesList) -> {
                            stateLog.setValue(stateReader);
                            if (stateReader == StateServer.SUCCESSFUL.ordinal()) {
                                viewModel.insertDatabase(databaseCategorisesList);
                                showCategorisesName.setValue(text);
                                viewModel.getAllDatabase().observe((LifecycleOwner) context, databaseCategorises -> {
                                    Preference.setValue(context, Value.SAVE_VERSION_CATEGORISES, value[DataServer.VERSION_CATEGORIES.ordinal()]);
                                    context.startActivity(new Intent(context, MainActivity.class));
                                    context.finish();
                                    loadingCategorisesDate.setValue(true);
                                });
                            } else if (stateReader == StateServer.INVALID.ordinal()) {
                                context.startActivity(new Intent(context, LogActivity.class));
                                context.finish();
                            } else if (stateReader == StateServer.UPDATE.ordinal()) {
                                showCategorisesName.setValue(text);
                            }
                        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, value[DataServer.TARGETED_MOVIE.ordinal()], value[DataServer.TARGETED_SERIES.ordinal()], value[DataServer.TARGETED_TV.ordinal()], value[DataServer.CATEGORISES_URL.ordinal()]);
                    } else {
                        loadingCategorisesDate.setValue(true);
                        context.startActivity(new Intent(context, MainActivity.class));
                        context.finish();
                    }
                } else if (stateServer == StateServer.ERROR.ordinal()) {
                    context.startActivity(new Intent(context, LogActivity.class));
                    context.finish();
                } else if (stateServer == StateServer.EXCEPTION.ordinal()) {
                    context.startActivity(new Intent(context, LogActivity.class));
                    context.finish();
                } else if (stateServer == StateServer.MAINTENANCE.ordinal()) {
                    context.startActivity(new Intent(context, LogActivity.class));
                    context.finish();
                } else if (stateServer == StateServer.UPDATE.ordinal()) {
                    context.startActivity(new Intent(context, LogActivity.class));
                    context.finish();
                    ;
                } else if (stateServer == StateServer.DOWN.ordinal()) {
                    context.startActivity(new Intent(context, LogActivity.class));
                    context.finish();
                }
            }).login(Value.SECURITY_EMAIL, Value.SECURITY_PASS);
        }
    }

    //--== MAIN FUNCTION============================================================================

    public void getDefaultDataCategorises(Context context) {
        DatabaseViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(DatabaseViewModel.class);
        if (!isGetDefaultCategorises) {
            isGetDefaultCategorises = true;
            loadingSetCategorisesToAdapter.setValue(true);

            viewModel.getDataByCategories(Value.FILTER_CATEGORIES_MOVIE).observe((LifecycleOwner) context, databaseCategorisesMovie -> {
                databaseMovies.addAll(databaseCategorisesMovie);
                getDataCategorises(context, Value.FILTER_CATEGORIES_MOVIE);
            });

            viewModel.getDataByCategories(Value.FILTER_CATEGORIES_SERIES).observe((LifecycleOwner) context, databaseCategorisesSeries -> {
                databaseSeries.addAll(databaseCategorisesSeries);
                loadingSetCategorisesToAdapter.setValue(false);
            });

            viewModel.getDataByCategories(Value.FILTER_CATEGORIES_TV).observe((LifecycleOwner) context, databaseTv::addAll);
        }
    }

    public void getDataCategorises(Context context, String type) {
        databaseCopy.clear();
        databaseView.clear();
        switch (type) {
            case Value.FILTER_CATEGORIES_MOVIE:
                databaseCopy.addAll(databaseMovies);
                break;
            case Value.FILTER_CATEGORIES_SERIES:
                databaseCopy.addAll(databaseSeries);
                break;
            case Value.FILTER_CATEGORIES_TV:
                databaseCopy.addAll(databaseTv);
                break;
        }
        Tools.divisionCategorises(databaseCopy, databaseView, setCategorisesToAdapter);
    }

    public void addDataCategorises() {
        Tools.divisionCategorises(databaseCopy, databaseView, setCategorisesToAdapter);
    }

    //--== SEARCH FUNCTION==========================================================================

    public void getDataCategorisesSearch(Activity context, String name) {
        databaseSearch.clear();
        databaseViewSearch.clear();
        loadingSetCategorisesSearchToAdapter.setValue(true);
        DatabaseViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(DatabaseViewModel.class);
        viewModel.getDataByCategoriesSearch(name).observe((LifecycleOwner) context, databaseCategorisesSearch -> {
            BaseViewModel.nameCategorises = new StringBuilder();
            databaseSearch.addAll(databaseCategorisesSearch);
            getDataCategorisesSearch(context);
            loadingSetCategorisesSearchToAdapter.setValue(false);
        });
    }

    public void addDataCategorisesSearch() {
        Tools.divisionCategorises(databaseSearch, databaseViewSearch, setCategorisesSearchToAdapter);
    }

    public void getDataCategorisesSearch(Context context) {
        Tools.divisionCategorises(databaseSearch, databaseViewSearch, setCategorisesSearchToAdapter);
    }

    //--== EPISODE FUNCTION=========================================================================

    public void getDataCategorisesEpisode(Activity context, String name) {
        if (!isGetDefaultCategorisesEpisode) {
            isGetDefaultCategorisesEpisode = true;
            databaseEpisode.clear();
            databaseSeason.clear();
            databaseViewEpisode.clear();
            loadingSetCategorisesEpisodeToAdapter.setValue(true);
            DatabaseViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(DatabaseViewModel.class);
            viewModel.getDataByEpisode(Value.FILTER_CATEGORIES_SERIES, name).observe((LifecycleOwner) context, databaseCategorisesEpisode -> {
                int numSeason = 0;
                String seasonS0 = "null";
                String seasonS00 = "null";
                StringBuilder toSection = new StringBuilder("");
                synchronized (context) {
                    for (int i = 0; i < databaseCategorisesEpisode.size(); i++) {
                        String[] numEpisode = databaseCategorisesEpisode.get(i).getName().split(" ");
                        if (databaseCategorisesEpisode.get(i).getName().split(" ")[numEpisode.length - 1].equals("E01") || databaseCategorisesEpisode.get(i).getName().split(" ")[numEpisode.length - 1].equals("E1")) {
                            numSeason++;
                            seasonS0 = " S" + numSeason;
                            seasonS00 = " S0" + numSeason;
                        }
                        if (databaseCategorisesEpisode.get(i).getName().contains(seasonS0)) {
                            if (!toSection.toString().contains(seasonS0)) {
                                toSection.append(seasonS0);
                                databaseSeason.add(seasonS0);
                            }
                        }
                        if (databaseCategorisesEpisode.get(i).getName().contains(seasonS00)) {
                            if (!toSection.toString().contains(seasonS00)) {
                                toSection.append(seasonS00);
                                databaseSeason.add(seasonS00);
                            }
                        }
                    }
                }
                if (databaseSeason.size() > 0) {
                    viewModel.getDataFromTheNameBySeasons(Value.FILTER_CATEGORIES_SERIES, name, databaseSeason.get(0), databaseSeason.get(0)).observe((LifecycleOwner) context, databaseCategorisesEpisodeForSeason -> {
                        databaseEpisode.addAll(databaseCategorisesEpisode);
                        setCategorisesEpisodeToAdapter.setValue(databaseCategorisesEpisodeForSeason);

                        setCategorisesEpisodeForSeasonToAdapter.setValue(databaseSeason);
                        loadingSetCategorisesEpisodeToAdapter.setValue(false);
                    });
                } else {
                    databaseEpisode.addAll(databaseCategorisesEpisode);
                    setCategorisesEpisodeToAdapter.setValue(databaseCategorisesEpisode);
                    setCategorisesEpisodeForSeasonToAdapter.setValue(databaseSeason);
                    loadingSetCategorisesEpisodeToAdapter.setValue(false);
                }
            });
        }
    }

    public void getDataCategorisesEpisodeForSeason(Activity context, String name, String season) {
        databaseEpisode.clear();
        databaseViewEpisode.clear();
        loadingSetCategorisesEpisodeToAdapter.setValue(true);
        DatabaseViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(DatabaseViewModel.class);
        viewModel.getDataFromTheNameBySeasons(Value.FILTER_CATEGORIES_SERIES, name, season, season).observe((LifecycleOwner) context, databaseCategorisesEpisodeForSeason -> {
            databaseEpisode.addAll(databaseCategorisesEpisodeForSeason);
            setCategorisesEpisodeToAdapter.setValue(databaseCategorisesEpisodeForSeason);
            loadingSetCategorisesEpisodeToAdapter.setValue(false);
        });
    }

}
