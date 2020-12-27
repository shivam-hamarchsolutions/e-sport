package app.puretech.e_sport.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.utill.CommonUtil;
import app.puretech.e_sport.webservices.VersionControlAPI;

public class SplashActivity extends AppCompatActivity {

    App app;
    private Activity activity;
    private VersionControlAPI versionControlAPI;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        doInit();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.d("xxxx", "onSuccess: "+newToken);
            }
        });

    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        versionControlAPI = new VersionControlAPI();
        doVersionControl();
    }

    private void doVersionControl() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            try {
                PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                String str_version_name = pInfo.versionName;
                int i_version_code = pInfo.versionCode;
                String str_version_code = String.valueOf(i_version_code);
                versionControlAPI.doVersionControl(app, activity, dialog, str_version_name, str_version_code);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {

            CommonUtil.showBar(activity, ".activity.SplashActivity");
        }
    }

}
