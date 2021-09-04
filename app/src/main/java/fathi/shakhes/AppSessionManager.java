package fathi.shakhes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.text.DecimalFormat;
import java.util.HashMap;

public class AppSessionManager {
    // Shared Preferences
    SharedPreferences pref;
    public static boolean IsAccountNegative = false;

    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "ShakhesPrefs";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String FOOD_USERNAME = "fusername";
    public static final String FOOD_ACC_T = "facc";
    public static final String FOOD_STUNUM = "fstunum";
    public static final String FOOD_FNAME = "ffname";
    public static final String FOOD_LNAME = "flname";
    public static final String FOOD_ACC = "faccount";


    // Constructor
    public AppSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createFoodLoginSession(String username, String acc) {
        editor.putString(FOOD_USERNAME, username);
        editor.putString(FOOD_STUNUM, username);
        editor.putString(FOOD_ACC_T, acc);

        // commit changes
        editor.apply();
    }

    public void saveuserfood(String t) {
        editor.putString("dininguser", t);

        // commit changes
        editor.apply();
    }

    public String getuserfood() {
        return pref.getString("dininguser", "");
    }

    public void savepassfood(String t) {
        editor.putString("diningpass", t);

        // commit changes
        editor.apply();
    }

    public String getpassfood() {
        return pref.getString("diningpass", "");
    }


    public void setcheckboxstate_food(Boolean b) {
        editor.putBoolean("ischecked_food", b);
        // commit changes
        editor.apply();
    }

    public boolean getcheckboxstate_food() {
        return pref.getBoolean("ischecked_food", false);
    }


    public void insertFoodProfileData(String fname, String lname, String stunum, String account) {
        // Storing login value as TRUE
        if (fname == "null") {
            fname = "-----------";
        }
        if (lname == "null") {
            lname = "-----------";
        }
        if (stunum == "null") {
            stunum = "-----------";
        }
        if (account == "null") {
            account = "-----------";
        }
        DecimalFormat formatter = new DecimalFormat("#,###");
        double Account1 = Double.parseDouble(account);
        if (Account1 < 0) {
            IsAccountNegative = true;
        } else {
            IsAccountNegative = false;
        }

        String AccountNumber = formatter.format(Account1);

        editor.putString(FOOD_ACC, AccountNumber);
        editor.putString(FOOD_FNAME, fname);
        editor.putString(FOOD_LNAME, lname);
        editor.putString(FOOD_STUNUM, stunum);

        // commit changes
        editor.apply();
    }


    public String[] getFoodProfileData() {
        String[] ret = new String[4];
        ret[0] = pref.getString(FOOD_FNAME, "");
        ret[1] = pref.getString(FOOD_LNAME, "");
        ret[2] = pref.getString(FOOD_STUNUM, "");
        ret[3] = pref.getString(FOOD_ACC, "");
        return ret;
    }


    public HashMap<String, String> getDiningData() {
        HashMap<String, String> user = new HashMap();

        user.put(KEY_USERNAME, pref.getString(FOOD_USERNAME, null));

        user.put(KEY_ACCESS_TOKEN, pref.getString(FOOD_ACC_T, null));

        return user;
    }

    public void logout_dining() {
        editor.remove("data_food_json").apply();
        editor.remove("data_food_temp").apply();
    }
}
