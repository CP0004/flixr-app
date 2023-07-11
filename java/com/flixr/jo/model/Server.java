package com.flixr.jo.model;

import android.app.Activity;
import android.content.Context;

import com.flixr.jo.BuildConfig;
import com.flixr.jo.enums.StateServer;
import com.flixr.jo.listener.CallbackServer;
import com.flixr.jo.tool.Value;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class Server {

    private final Context context;
    private final CallbackServer callbackServer;

    public Server(Context context, CallbackServer callbackServer) {
        this.context = context;
        this.callbackServer = callbackServer;
    }

    //-- Automatic login in server
    public void login(String email, String pass) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener((Activity) context, task -> {
            if (task.isSuccessful()) {
                getDate();
            } else {
                if (task.getException() != null)
                    callbackServer.onCallbackServer(StateServer.EXCEPTION.ordinal());
                else callbackServer.onCallbackServer(StateServer.ERROR.ordinal());
            }
        });
    }

    //-- get data form sever on collection base
    private void getDate() {
        FirebaseFirestore.getInstance().collection(Value.COLLECTION_BASE).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot data : task.getResult()) {
                    if (Boolean.TRUE.equals(data.getBoolean(Value.BASE_SERVER))) {
                        if (Boolean.TRUE.equals(data.getBoolean(Value.BASE_MAINTENANCE))) {
                            if (Objects.equals(data.getString(Value.BASE_VERSION_APP), BuildConfig.VERSION_NAME)) {
                                callbackServer.onCallbackServer(StateServer.SUCCESSFUL.ordinal(), data.getString(Value.BASE_TARGETED_MOVIE), data.getString(Value.BASE_TARGETED_SERIES), data.getString(Value.BASE_TARGETED_TV), data.getString(Value.BASE_URL), data.getString(Value.BASE_VERSION_CATEGORIES), data.getString(Value.BASE_INSTAGRAM), data.getString(Value.BASE_TELEGRAM), data.getString(Value.BASE_TIKTOK));
                            } else {
                                //-- App Update
                                callbackServer.onCallbackServer(StateServer.UPDATE.ordinal());
                            }
                        } else {
                            //-- Server Maintenance
                            callbackServer.onCallbackServer(StateServer.MAINTENANCE.ordinal());
                        }
                    } else {
                        //-- Server Down
                        callbackServer.onCallbackServer(StateServer.DOWN.ordinal());
                    }
                }
            } else {
                //-- Exception and Error
                if (task.getException() != null)
                    callbackServer.onCallbackServer(StateServer.EXCEPTION.ordinal());
                else
                    callbackServer.onCallbackServer(StateServer.ERROR.ordinal());
            }
        });
    }
}
