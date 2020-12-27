package app.puretech.e_sport.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import app.puretech.e_sport.webservices.SignUpAPI;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ParentSingUpActivity extends AppCompatActivity {

    private App app;
    private Activity activity;
    private Toolbar toolbar;
    private EditText et_name, et_email, et_mobile, et_address, et_number_of_child, et_password, et_number, et_last_name;
    private Button btn_submit, btn_verify;
    private SignUpAPI signUpAPI;
    private Dialog dialog;
    private LinearLayout ll_verify, ll_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_parent_sing_up);
        doInit();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().isEmpty()) {
                    et_name.setError("Name Required!");
                } else if (et_last_name.getText().toString().isEmpty()) {
                    et_last_name.setError("Last Name Required!");
                } else if (et_address.getText().toString().isEmpty()) {
                    et_address.setError("Address Required!");
                } else if (et_password.getText().toString().isEmpty()) {
                    et_password.setError("Password Required!");
                } else {
                    do_Reg(et_name.getText().toString(), et_last_name.getText().toString(), et_email.getText().toString(), et_mobile.getText().toString(), et_address.getText().toString(), et_password.getText().toString());
                }
            }
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_mobile.getText().toString().isEmpty()) {
                    et_mobile.setError("unique id required!!");
                } else {
                    do_Verify(et_mobile.getText().toString());
                }
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, SignInActivity.class);
                activity.startActivity(i);
                activity.finish();
            }
        });
    }

    public void doInit() {
        app = (App) getApplication();
        activity = this;
        signUpAPI = new SignUpAPI();
        toolbar = findViewById(R.id.toolbar_home);
        btn_submit = findViewById(R.id.btn_Submit);
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        et_name = findViewById(R.id.et_name);
        et_address = findViewById(R.id.et_address);
        et_email = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        et_number = findViewById(R.id.et_number);
        et_number_of_child = findViewById(R.id.et_number_of_child);
        et_password = findViewById(R.id.et_Password);
        et_last_name = findViewById(R.id.et_last_name);
        ll_reg = findViewById(R.id.ll_sing_up);
        ll_verify = findViewById(R.id.ll_verify);
        btn_verify = findViewById(R.id.btn_verify);
    }

    private void do_Verify(String str_mobile_number) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("unique_id", str_mobile_number)
                    .build();
            app.getApiService().doVerify(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                app.showSnackBar(activity, message);
                                AppPreferences.init(activity).setString("student_id", jobj.getString("student_id"));
                                ll_reg.setVisibility(View.VISIBLE);
                                ll_verify.setVisibility(View.GONE);
                                et_number.setText("" + et_mobile.getText().toString());
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
                    dialog.dismiss();
                }
            });
        } else {
            CommonUtil.showBar(activity, ".activity.TrainerDailyTimeTableActivity");
        }

    }

    private void do_Reg(String str_first_name, String str_last_name, String str_email, String str_mobile, String str_address, String str_password) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("student_id", AppPreferences.init(activity).getString("student_id", ""))
                    .addFormDataPart("first_name", str_first_name)
                    .addFormDataPart("last_name", str_last_name)
                    .addFormDataPart("contact", str_mobile)
                    .addFormDataPart("address", str_address)
                    .addFormDataPart("password", str_password)
                    .build();
            app.getApiService().doReg(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                app.showToast(message);
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
        } else {
            CommonUtil.showBar(activity, ".activity.TrainerDailyTimeTableActivity");
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(activity, SignInActivity.class);
        activity.startActivity(i);
        activity.finish();
    }
}
