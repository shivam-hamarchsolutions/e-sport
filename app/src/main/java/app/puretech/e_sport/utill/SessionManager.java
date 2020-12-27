package app.puretech.e_sport.utill;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * Created by dinesh on 14-02-2018.
 */

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "chat";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String LOGINTYPE = "logintype";
    private static final String DEVICEID = "deviceid";
    private static final String PROFILEPIC = "profilepic";
    private static final String EMAIL = "email";
    private static final String MOBILE = "mobile";
    private static final String GENDER = "gender";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String LATTITUDE = "lattitude";
    private static final String LONGITUDE = "longitude";
    private static final String GCMID = "gcmid";
    private static final String USERID = "userid";

    // Constructor

    @SuppressLint("CommitPrefEdits")


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void createLoginSession(String firstname, String lastname, String deviceid, String gcmId, String longitude, String lattitude, String country, String city, String gender, String mobile, String email) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        editor.putString(FIRSTNAME, firstname);
        editor.putString(LASTNAME, lastname);
        editor.putString(DEVICEID, deviceid);
        editor.putString(GCMID, gcmId);
        editor.putString(LONGITUDE, longitude);
        editor.putString(LATTITUDE, lattitude);
        editor.putString(COUNTRY, country);
        editor.putString(CITY, city);
        editor.putString(MOBILE, mobile);
        editor.putString(EMAIL, email);
        editor.putString(GENDER, gender);
        // commit changes
        editor.commit();
    }
    public void setlogintype(String logintype) {
        // Storing login value as TRUE
        // Storing name in pref
        editor.putString(LOGINTYPE, logintype);

        // commit changes
        editor.commit();
    }

    public String getLogintype() {

        String logintype = "";

        logintype = pref.getString(LOGINTYPE, "");

        return logintype;
    }

    public void setProfilepic(String profilepic) {
        // Storing login value as TRUE


        // Storing name in pref
        editor.putString(PROFILEPIC, profilepic);

        // commit changes
        editor.commit();
    }

    public String getProfilePic() {

        String logintype = "";

        logintype = pref.getString(PROFILEPIC, "");

        return logintype;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity

         //   Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
          //  _context.startActivity(i);

        } else {

//				Intent i = new Intent(_context, MyMessageActivity.class);
//				// Closing all the Activities
//				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//				// Add new Flag to start new Activity
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//				// Staring Login Activity
//				_context.startActivity(i);

        }

    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        DataManager.status = "";
        DataManager.message = "";

        // After logout redirect user to Loing Activity

       // Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        System.out.println("testing here....");
        // Add new Flag to start new Activity
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        System.out.println("testing here....2");
        // Staring Login Activity
//        _context.startActivity(i);

    }

    public String getuserid() {
        return pref.getString(USERID, "");
    }

    public String getdeviceid() {
        return pref.getString(DEVICEID, "");
    }

    public String firstname() {
        return pref.getString(FIRSTNAME, "");
    }

    public String lastname() {
        return pref.getString(LASTNAME, "");
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
