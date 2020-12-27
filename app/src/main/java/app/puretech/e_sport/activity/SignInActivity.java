package app.puretech.e_sport.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Objects;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import app.puretech.e_sport.webservices.SignInAPI;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    App app;
    private Activity activity;
    private Button btn_Sign, btn_sign_up;
    private SignInAPI signInAPI;
    private Dialog dialog;
    private EditText et_user, et_pass;
    private FloatingActionButton fab_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  /*      getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_sign_in);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SignInActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.d("SignIn", "onSuccess: "+newToken);
            }
        });
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        signInAPI = new SignInAPI();
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        idLoad();
        userMode();
    }

    private void idLoad() {
        btn_Sign = findViewById(R.id.btn_Sign);
        btn_sign_up = findViewById(R.id.btn_Sign_up);
        et_user = findViewById(R.id.et_UserName);
        et_pass = findViewById(R.id.et_Password);
        fab_refresh = findViewById(R.id.fab_refresh);
        doSign(btn_Sign);
        doSignUp(btn_sign_up);
        fab_refresh.setOnClickListener(this);
    }

    private void doSign(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String app_user = AppPreferences.init(activity).getString("user", "");

                if (CommonUtil.isNetworkAvailable(activity)) {

                    if (app_user.equals("Parent")) {
                      /*  AppPreferences.init(activity).setString("name", "Avinash Patil");
                        AppPreferences.init(activity).setString("email", "avinash@gmail.com");
                        AppPreferences.init(activity).setString("address", "Pune");
                        AppPreferences.init(activity).setString("mobile", "9988775544");
                        Intent i_Parent = new Intent(activity, ParentHomeActivity.class);
                        activity.startActivity(i_Parent);
                        activity.finish();*/

                        if (et_user.getText().toString().isEmpty()) {
                            et_user.setError("User Name Required !!");
                        } else if (et_pass.getText().toString().isEmpty()) {
                            et_pass.setError("Password Required !!");
                        } else {
                            signInAPI.doSignIn(app, activity, dialog, AppPreferences.init(activity).getString("user", ""),
                                    et_user.getText().toString(), et_pass.getText().toString(),AppPreferences.init(activity).getString("fcm_token", ""));
                        }

                    } else {
                        if (et_user.getText().toString().isEmpty()) {
                            et_user.setError("User Name Required !!");
                        } else if (et_pass.getText().toString().isEmpty()) {
                            et_pass.setError("Password Required !!");
                        } else {
                            signInAPI.doSignIn(app, activity, dialog, AppPreferences.init(activity).getString("user", ""),
                                    et_user.getText().toString(), et_pass.getText().toString(), AppPreferences.init(activity).getString("fcm_token", ""));
                        }
                    }
                } else {
                    CommonUtil.showBar(activity, ".activity.SignInActivity");
                }
            }
        });
    }

    private void doSignUp(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iHome = new Intent(activity, ParentSingUpActivity.class);
                activity.startActivity(iHome);
                activity.finish();
            }
        });
    }

    //Update dialog
    @SuppressLint("NewApi")
    private void userMode() {
        final Dialog user_mode = new Dialog(activity);
        user_mode.setContentView(R.layout.user_role_dialog);
        user_mode.setCancelable(false);
        Objects.requireNonNull(user_mode.getWindow()).setGravity(Gravity.CENTER);
        final LinearLayout ll_trainer, ll_School, ll_Parents;
        ll_trainer = user_mode.findViewById(R.id.ll_trainer);
        ll_School = user_mode.findViewById(R.id.ll_School);
        ll_Parents = user_mode.findViewById(R.id.ll_Parents);

        ll_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("user", "Trainer");
                btn_sign_up.setVisibility(View.GONE);
                user_mode.dismiss();
            }
        });
        ll_School.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("user", "School");
                btn_sign_up.setVisibility(View.GONE);
                user_mode.dismiss();
            }
        });
        ll_Parents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("user", "Parent");
                user_mode.dismiss();
                btn_sign_up.setVisibility(View.VISIBLE);

            }
        });
        user_mode.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_refresh:
                Intent i_refresh = new Intent(activity, SignInActivity.class);
                activity.startActivity(i_refresh);
                activity.finish();
                break;
        }
    }
}
