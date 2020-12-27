package app.puretech.e_sport.utill;

import android.util.Log;

/**
 * Created by dinesh on 14-02-2018.
 */

public class Logger {

    public static String TAG = "Sakal Money";
    public static Logger instance;

    private static final boolean debugEnabled = true;
//    private static final boolean debugEnabled = false;

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    public static synchronized Logger init() {
        if (null == instance) {
            instance = new Logger();
        }
        return instance;
    }

    public void debug(String msg) {
        if (debugEnabled && msg != null) {
            Log.d(TAG, msg);
        }
    }

    public void debug(String msg, Throwable t) {
        if (debugEnabled && msg != null) {
            Log.d(TAG, msg, t);
        }
    }

    public void debug(Throwable t) {
        if (debugEnabled) {
            Log.d(TAG, "Exception: ", t);
        }
    }

    public void debug(String tag, String msg) {
        if (debugEnabled && msg != null) {
            Log.d(tag, msg);
        }
    }

    public void warn(String msg) {
        if (debugEnabled && msg != null) {
            Log.w(TAG, msg);
        }
    }

    public void info(String msg) {
        if (debugEnabled && msg != null) {
            Log.i(TAG, msg);
        }
    }

    public void error(String msg) {
        if (debugEnabled && msg != null) {
            Log.e(TAG, msg);
        }
    }
}
