package anil.sharma.bhadamandi.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ravi on 01/06/15.
 */
@SuppressLint("CommitPrefEdits")
public class PrefManager {
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String Event = "event";
    public static final String Mobile = "mobile";
    public static final String LOGIN = "login";
    // Shared pref file name
    private static final String PREF_NAME = "ScreenCast";
    private static final String PREF_NAME1 = "ScreenCast1";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences pref1;
    // Editor for Shared preferences
    Editor editor;
    Editor editor1;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Constructor
    public PrefManager(Context context) {
        this._context = context;
        pref1 = _context.getSharedPreferences(PREF_NAME1, PRIVATE_MODE);
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor = pref1.edit();
        editor1 = pref.edit();

    }

    /**
     * Create login session
     */
    public void setimage(String email) {

        // Storing email in pref
        editor1.putString("image", email);

        // commit changes
        editor1.commit();
    }

    public void setmobile(String product) {


        editor1.putString(Mobile, product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public String getEvent() {
        return pref.getString(Event, null);
    }

    public void setEvent(String product) {


        editor1.putString(Event, product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public String getImage() {
        return pref.getString("image", null);
    }

    public String getmobile() {
        return pref.getString(Mobile, null);
    }

    public String getLogin() {
        return pref.getString(LOGIN, null);
    }

    public void setLogin(String product) {


        editor1.putString(LOGIN, product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public String getName() {
        return pref.getString("name", null);
    }

    public void setName(String product) {


        editor1.putString("name", product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public ArrayList<String> getArrayName() {


        Set<String> set = new HashSet<String>();
        ArrayList<String> s1 = new ArrayList<String>();
        set = pref1.getStringSet("setname", null);
        if (set == null) ;
        else
            s1.addAll(set);
        return s1;

    }

    public void setArrayName(ArrayList<String> product) {


        Set<String> set = new HashSet<String>();
        set.addAll(product);
        editor.putStringSet("setname", set);
        editor.commit();
    }

    public ArrayList<String> getArrayNumber() {
        Set<String> set = new HashSet<String>();
        ArrayList<String> s1 = new ArrayList<String>();
        set = pref1.getStringSet("setnumber", null);
        if (set == null) ;
        else
            s1.addAll(set);
        return s1;

    }

    public void setArrayNumber(ArrayList<String> product) {


        Set<String> set = new HashSet<String>();
        set.addAll(product);
        editor.putStringSet("setnumber", set);
        editor.commit();
    }

    public String getValue() {
        return pref.getString("value", null);
    }

    public void setValue(String product) {


        editor1.putString("value", product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public String getCity() {
        return pref.getString("city", null);
    }

    public void setCity(String product) {


        editor1.putString("city", product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public String getPassword() {
        return pref.getString("password", null);
    }

    public void setPassword(String product) {


        editor1.putString("password", product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public String getUsername() {
        return pref.getString("username", null);
    }

    public void setUsername(String product) {


        editor1.putString("username", product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void logout() {
        editor1.clear();
        editor1.commit();

    }


}