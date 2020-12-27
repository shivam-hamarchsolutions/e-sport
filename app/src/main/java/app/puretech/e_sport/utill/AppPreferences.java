package app.puretech.e_sport.utill;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

import app.puretech.e_sport.model.UserDTO;


/**
 * Created by dinesh on 14-02-2018.
 */

public class AppPreferences {

    private static AppPreferences instance;
    public static final String NOTIFICATION_BUZZ = "NOTIFICATION_BUZZ";


    public static AppPreferences init(Context context) {
        if (null == instance) {
            instance = new AppPreferences(context);

        }
        return instance;
    }

    private Context context;
    protected SharedPreferences sharedPreferences;

    public AppPreferences(Context context) {
        super();
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * *      Private Methods To Get & Set values in Preferences
     **/

    private int getInteger(String key, int def) {
        int i = sharedPreferences.getInt(key, def);
        return i;
    }


    public String getString(String key, String def) {
        String s = sharedPreferences.getString(key, def);
        return s;
    }

    private boolean getBoolean(String key, boolean def) {
        boolean b = sharedPreferences.getBoolean(key, def);
        return b;
    }

    private double getDouble(String key, double def) {
        double d = Double.parseDouble(sharedPreferences.getString(key, String.valueOf(def)));
        return d;
    }

    private long getLong(String key, long def) {
        return sharedPreferences.getLong(key, def);
    }

    private void setInteger(String key, int val) {
        Editor e = sharedPreferences.edit();
        e.putInt(key, val);

        e.apply();

        e.commit();

    }

    public void setString(String key, String val) {
        Editor e = sharedPreferences.edit();
        e.putString(key, val);

        e.apply();

        e.commit();

    }

    private void setBoolean(String key, boolean val) {
        Editor e = sharedPreferences.edit();
        e.putBoolean(key, val);

        e.apply();

        e.commit();

    }

    private void setDouble(String key, double val) {
        Editor e = sharedPreferences.edit();
        e.putString(key, String.valueOf(val));

        e.apply();

        e.commit();

    }

    private void setLong(String key, long val) {
        Editor e = sharedPreferences.edit();
        e.putLong(key, val);

        e.apply();

        e.commit();

    }
    private static String BitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte[] arr = baos.toByteArray();
        return Base64.encodeToString(arr, Base64.DEFAULT);
    }

    /**
     * *      Public Methods To Get & Set Preferences
     **/

    private static final String IS_OTP_SET = "IS_OTP_SET";
    private static final String IS_FIRST_TIME = "IS_FIRST_TIME";

    public boolean isOtpSet() {
        return getBoolean(IS_OTP_SET, false);
    }

    public void setOtp(boolean isOtp) {
        setBoolean(IS_OTP_SET, isOtp);
    }

    public boolean isFirstTime() {
        return getBoolean(IS_FIRST_TIME, true);
    }

    public void setFirstTime(boolean isFirstTime) {
        setBoolean(IS_FIRST_TIME, isFirstTime);
    }

    public void setPageId(String PageId) {
        setString(AppConstants.PageId, PageId);
    }

    public String getPageId() {
        return getString(AppConstants.PageId, null);
    }

    public UserDTO getUser() {
        UserDTO user = null;
        Gson gson = new Gson();
        String value = getString(AppConstants.USER, null);
        if (null != value) {
            try {
                user = gson.fromJson(value, UserDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void setUser(UserDTO user) {
        Gson gson = new Gson();
        String value = null;
        try {
            value = gson.toJson(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setString(AppConstants.USER, value);
    }

//    public ItemDefect getDefect() {
//        ItemDefect defect = null;
//        Gson gson = new Gson();
//        String value = getString(AppConstants.DEFECT, null);
//        if (null != value) {
//            try {
//                defect = gson.fromJson(value, ItemDefect.class);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return defect;
//    }

//    public void SetDefect(ItemDefect defect) {
//        Gson gson = new Gson();
//        String value = null;
//        try {
//            value = gson.toJson(defect);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        setString(AppConstants.DEFECT, value);
//    }

    public void setNotificationBuzz(Boolean Notificationbuzz) {
        setBoolean(NOTIFICATION_BUZZ, Notificationbuzz);
    }

    public Boolean getNotificationBuzz() {
        return getBoolean(NOTIFICATION_BUZZ, true);
    }

    public void setIsNotificationRead(Boolean isNotificationRead) {
        setBoolean(AppConstants.NOTIFICATIONREAD, isNotificationRead);
    }

    public Boolean getIsNotificationRead() {
        return getBoolean(AppConstants.NOTIFICATIONREAD, false);
    }

    //Storing Temporiry values from login for OTP page

    public void setActive(String active) {
        setString(AppConstants.ACTIVE, active);
    }

    public String getActive() {
        return getString(AppConstants.ACTIVE, null);
    }


    //Default values after app data clean up
    public void logoutUser(Activity activity) {
        setUser(null);
        setOtp(false);
        setPageId(null);
        setFirstTime(false);
        setActive(null);
//       // Intent intent = new Intent(activity, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        activity.startActivity(intent);
    }

}
