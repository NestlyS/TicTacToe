package com.example.mygame.TicTacToe;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;

public class InterfaceManager {

    private Activity coreActivity;
    private static final InterfaceManager ourInstance = new InterfaceManager();

    public static InterfaceManager getInstance() {
        return ourInstance;
    }

    public View getView(@IdRes int resId ){
        return coreActivity.findViewById(resId);
    }

    public void setActivity(Activity activity){
        coreActivity = activity;
    }

    public Activity getActivity(){
        return coreActivity;
    }

    public TicTacToeInterface getActivityAsTicTac() throws Exception{
        if(coreActivity instanceof  TicTacToeInterface) {
            return  (TicTacToeInterface) coreActivity;
        }
        Log.d("InterfaceManager", "WrongActivity");
        throw new Exception("WrongActivity");
    }

    public void resetActivity(){
        coreActivity = null;
    }

    private InterfaceManager() {
    }
}
