package app.puretech.e_sport.webservices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.SignInActivity;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class VersionControlAPI {

    public void doVersionControl(App app, Activity activity, Dialog dialog, String version_name, String version_code) {
        dialog.show();
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("version_name", version_name)
                .addFormDataPart("version_code", version_code)
                .build();
        //Get User Details
        app.getApiService().doVersionControl(requestBody).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Response<Map<String, Object>> response, Retrofit retrofit) {
                if (response.body() != null) {
                    app.getLogger().error("success");

                    JSONObject jobj;
                    String status;
                    String message;
                    try {
                        jobj = new JSONObject(response.body());
                        status = jobj.getString("success");
                        message = jobj.getString("message");
                        if (status.equals("0")) {
                            runThread(activity);
                        } else if (status.equals("1")) {
                            forceAppUpdate(activity);
                        } else {
                            app.showSnackBar(activity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    int a = response.code();
                    if (a == 401) {
                        app.showSnackBar(activity, "" + a);
                    } else {
                        app.showSnackBar(activity, "" + a);
                    }
                }
                dialog.cancel();
            }

            @Override
            public void onFailure(Throwable t) {
                app.getLogger().error("failure");
                CommonUtil.showBar(activity,".activity.SplashActivity");
                dialog.dismiss();
            }
        });
    }

    //Update dialog
    @SuppressLint("NewApi")
    private void forceAppUpdate(Activity activity) {
        final Dialog update_dialog = new Dialog(activity);
        update_dialog.setContentView(R.layout.force_update_dialog);
        update_dialog.setCancelable(false);
        Objects.requireNonNull(update_dialog.getWindow()).setGravity(Gravity.CENTER);
        final Button btn_Update, btn_Leater;
        btn_Leater = update_dialog.findViewById(R.id.btn_Leater);
        btn_Update = update_dialog.findViewById(R.id.btn_Update_Now);
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_dialog.dismiss();
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "");
                Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(goMarket);
            }
        });
        btn_Leater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_dialog.dismiss();
                runThread(activity);
            }
        });
        update_dialog.show();
    }

    private void runThread(Activity activity) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.getStackTrace();
                } finally {
                    Intent i_Run = new Intent(activity, SignInActivity.class);
                    activity.startActivity(i_Run);
                    activity.finish();
                }
            }
        };
        thread.start();
    }
}
