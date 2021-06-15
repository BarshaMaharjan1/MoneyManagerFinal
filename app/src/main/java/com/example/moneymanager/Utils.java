package com.example.moneymanager;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    private String FIRST_LOGIN = "first_login";
    private String ICONS_INSERTED = "icons_inserted";
    private String NAME = "name";
    private String EMAIL = "email";
    private String ID = "id";
    private final SharedPreferences prefs;

    public Utils(Context context) {
        prefs = context.getSharedPreferences("manager_prefs", Context.MODE_PRIVATE);
    }


    public void iconsInserted(boolean b) {
        prefs.edit().putBoolean(ICONS_INSERTED, b).apply();
    }

    public boolean isIconInserted() {
        return prefs.getBoolean(ICONS_INSERTED, false);
    }

    public void saveUserData(String personName, String personEmail, String personId) {
        prefs.edit().putString(NAME, personName)
                .putString(EMAIL, personEmail)
                .putString(ID, personId).apply();
    }

    public void deleteUserDatas(){
        prefs.edit().putString(NAME,"").putString(EMAIL,"").putString(ID,"").apply();
    }

    public String getUserId() {
        return prefs.getString(ID, "");
    }

    public boolean isLoggedInFirstTime() {
        return prefs.getBoolean(FIRST_LOGIN, true);
    }

    public void saveFirstLogin(boolean b){
        prefs.edit().putBoolean(FIRST_LOGIN,b).apply();
    }
}
