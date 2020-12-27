package app.puretech.e_sport.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.fragment.TrainerHomeFragment;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.ImageUtil;
import de.hdodenhof.circleimageview.CircleImageView;

public class TrainerHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    App app;
    private Activity activity;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private int iDashboard = 0;
    private CircleImageView user;
    private TextView tv_Name;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_trainer_home);
        doInit();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TrainerHomeFragment()).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
            navigationView.setCheckedItem(R.id.nev_TrainerHome);
            iDashboard = 2;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void doInit() {
        app = (App) getApplication();
        activity = this;
        toolbar = findViewById(R.id.toolbar);
        // toolbar.setTitle("Trainer Dashboard");
        toolbar.setTitle("Welcome " + AppPreferences.init(activity).getString("user_name", ""));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_Layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        tv_Name = hView.findViewById(R.id.tv_Name);
        tv_Name.setText(AppPreferences.init(activity).getString("user_name", ""));
        user = hView.findViewById(R.id.cv_UserProfile);
        ImageUtil.displayImageTrainer(user, AppPreferences.init(activity).getString("pic", ""), null);
        //go to user
        user.setOnClickListener(v -> {
            Intent i_user = new Intent(activity, TrainerProfileActivity.class);
            activity.startActivity(i_user);
            activity.finish();
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nev_TrainerHome:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrainerHomeFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                // toolbar.setTitle("Dashboard");
                iDashboard = 1;
                break;
            case R.id.nev_TrainerEquipmentManagement:
                Intent i_Equipment_management = new Intent(activity, TrainerEquipmentManagementActivity.class);
                activity.startActivity(i_Equipment_management);
                activity.finish();
                break;

            case R.id.nev_TrainerStudentAttendence:
                Intent i_att = new Intent(activity, TrainerStudentAttendenceActivityNew.class);
                activity.startActivity(i_att);
                activity.finish();
                break;

            case R.id.nev_TrainerStudentAssessment:
                Intent i_TrainerStudentAssessmentt = new Intent(activity, TrainerStudentAssessmentActivity.class);
                activity.startActivity(i_TrainerStudentAssessmentt);
                break;
            case R.id.nev_TrainerPostSchoolActivity:
                Intent i_school = new Intent(activity, TrainerPostSchoolActivity.class);
                activity.startActivity(i_school);
                break;

            case R.id.nev_TrainerYearlyPlanner:
                Intent i_daily_time_table = new Intent(activity, TrainerYearlyPlannerActivity.class);
                activity.startActivity(i_daily_time_table);
                break;


            case R.id.nev_TrainerGallery:
                Intent i_gallery = new Intent(activity, TrainerGalleryActivity.class);
                activity.startActivity(i_gallery);
                activity.finish();
                break;
            case R.id.nev_TrainerLeaveApply:
                Intent i_leave = new Intent(activity, TrainerLeaveActivity.class);
                activity.startActivity(i_leave);
                activity.finish();
                break;
            case R.id.nev_TrainerAboutUs:
                Intent i_about_us = new Intent(activity, AboutUsActivity.class);
                activity.startActivity(i_about_us);
                break;
            case R.id.nev_TrainerRateUs:
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName()));
                startActivity(rateIntent);
                break;
            case R.id.nev_TrainerShare:
                ShareCompat.IntentBuilder.from(activity)
                        .setType("text/plain")
                        .setChooserTitle("E-Sport")
                        .setText("http://play.google.com/store/apps/details?id=" + activity.getPackageName())
                        .startChooser();
                break;
//
            case R.id.nev_TrainerLogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("E-Sport");
                builder.setIcon(R.drawable.app_logo);
                builder.setMessage("Do you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            Intent i = new Intent(activity, SignInActivity.class);
                            activity.startActivity(i);
                            activity.finish();
                            dialog.cancel();
                        })
                        .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return false;
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (iDashboard == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TrainerHomeFragment()).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
            navigationView.setCheckedItem(R.id.nev_TrainerHome);
            toolbar.setTitle("Dashboard");
            iDashboard = 2;
        } else if (iDashboard == 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("E-Sport");
            builder.setIcon(R.drawable.app_logo);
            builder.setMessage("Do you want to close this App ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        Intent intent = new Intent(
                                Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
            iDashboard = 3;

        } else if (iDashboard == 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("E-Sport");
            builder.setIcon(R.drawable.app_logo);
            builder.setMessage("Do you want to close this App ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        Intent intent = new Intent(
                                Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
            iDashboard = 3;
        }
    }
}

