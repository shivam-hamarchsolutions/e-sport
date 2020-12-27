package app.puretech.e_sport.webservices;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.activity.SignInActivity;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignUpAPI {
    public void doSignUp(App app, Activity activity, Dialog dialog, String str_name, String str_email, String str_mobile, String str_address, String str_number_of_child, String str_password) {
        dialog.show();
        RequestBody requestLogin = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("name", str_name)
                .addFormDataPart("email", str_email)
                .addFormDataPart("mobile", str_mobile)
                .addFormDataPart("address", str_address)
                .addFormDataPart("number_of_child", str_number_of_child)
                .addFormDataPart("password", str_password)
                .build();
        app.getApiService().doSignIn(requestLogin).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Response<Map<String, Object>> response, Retrofit retrofit) {
                if (response.body() != null) {
                    app.getLogger().error("success");
                    String status;
                    String message;
                    JSONObject jobj;
                    try {
                        jobj = new JSONObject(response.body());
                        status = jobj.getString("success");
                        message = jobj.getString("message");
                        if (status.equals("0")) {
                            app.showSnackBar(activity,message);
                            Intent i = new Intent(activity, SignInActivity.class);
                            activity.startActivity(i);
                            activity.finish();
                        } else if (status.equals("1")) {
                            app.showSnackBar(activity, message);
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
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                app.getLogger().error("failure");
            }
        });
    }

}
