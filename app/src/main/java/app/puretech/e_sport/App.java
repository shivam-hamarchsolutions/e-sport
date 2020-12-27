package app.puretech.e_sport;

import android.app.Activity;
import android.app.Application;
import com.google.android.material.snackbar.Snackbar;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.util.LinkedList;
import java.util.Queue;
import app.puretech.e_sport.api.APIHelper;
import app.puretech.e_sport.api.APIService;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.SessionManager;
import app.puretech.e_sport.utill.Logger;

public class App extends Application {

    public static final String TAG = App.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static App mInstance;
    private SessionManager sessionManager;
    private ImageLoader imageLoader;
    private Logger logger;
    private AppPreferences appPreferences;
    private APIHelper apiHelper;
    private Queue<String> msgQueue;
    private boolean isSnackBarShowing;
    App app;
    //UserDTO userDTO2;

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        app = (App) getApplicationContext();
        mInstance = this;
        MultiDex.install(this);
        doInit();
    }

    private void doInit() {
        sessionManager = new SessionManager(this);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        logger = Logger.init();
        appPreferences = AppPreferences.init(this);
        apiHelper = APIHelper.init(this);
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public synchronized SessionManager getSessionManager() {
        return sessionManager;
    }

    public synchronized ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void showToast(String message) {
        if (null != message) {
            Toast toast = Toast.makeText(
                    getApplicationContext(), message,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public synchronized AppPreferences getPreferences() {
        return appPreferences;
    }


    public synchronized Logger getLogger() {
        return logger;
    }

    public synchronized APIService getApiService() {
        return apiHelper.getApiService();
    }

    public synchronized void showSnackBar(View view, String msg) {
        if (null != msg && null != view) {
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        }
    }

    public synchronized void showSnackBar(Activity activity, int stringResId) {
        String msg = getString(stringResId);
        if (null != activity) {
            Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
        }
    }

    public synchronized void showSnackBar(final Activity activity, final String msg) {
        if (null != activity) {
            if (msgQueue == null) {
                msgQueue = new LinkedList<>();
            }
            msgQueue.add(msg);
            showSnackBar(activity);
        }
    }

    private void showSnackBar(final Activity activity) {
        if (!isSnackBarShowing) {
            Snackbar sb = Snackbar.make(activity.findViewById(android.R.id.content), msgQueue.poll(), Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    super.onDismissed(snackbar, event);
                    isSnackBarShowing = false;
                    if (msgQueue.size() > 0) {
                        showSnackBar(activity);
                    }
                }
            });
            View sbView = sb.getView();
            //set background color
            // sbView.setBackgroundColor(getResources().getColor(R.color.custom_bg));
            //Get the textview of the snackbar text
            TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            //set text color
            textView.setTextColor(getResources().getColor(R.color.red));
            //increase max lines of text in snackbar. default is 2.
            textView.setMaxLines(10);
            sb.show();
            isSnackBarShowing = true;
        }

    }
}