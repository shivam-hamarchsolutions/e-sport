package app.puretech.e_sport.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.fragment.ParentAchievementsFragment;
import app.puretech.e_sport.fragment.ParentEventFragment;
import app.puretech.e_sport.fragment.ParentFeedbackFragment;
import app.puretech.e_sport.fragment.ParentGalleryFragment;
import app.puretech.e_sport.fragment.ParentHomeFragment;
import app.puretech.e_sport.fragment.ParentNotifactionFragment;
import app.puretech.e_sport.fragment.ParentStudentAttendanceFragment;
import app.puretech.e_sport.model.SelectChildDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import app.puretech.e_sport.utill.ImageUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ParentHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    App app;
    private Activity activity;
    private DrawerLayout drawerLayout;
    public static Toolbar toolbar;
    private NavigationView navigationView;
    public static int iDashboard = 0;

    private Dialog dialog;
    public static ArrayList<SelectChildDTO> child_array = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_parent_home);
        doInit();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ParentHomeFragment()).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
            navigationView.setCheckedItem(R.id.nev_TrainerHome);
            iDashboard = 2;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(AppPreferences.init(activity).getString("name", ""));
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_Layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
//        tv_AgentName = hView.findViewById(R.id.tv_AgentName);
//        userProfile = hView.findViewById(R.id.profile_image);
        CircleImageView user = hView.findViewById(R.id.cv_UserProfile);
        TextView tvName = hView.findViewById(R.id.tv_Name);
        ImageUtil.displayImageParent(user, AppPreferences.init(activity).getString("parent_image", ""), null);
        tvName.setText(AppPreferences.init(activity).getString("name", ""));
        //go to user
        user.setOnClickListener(v -> {
            Intent i_user = new Intent(activity, ParentProfileActivity.class);
            activity.startActivity(i_user);
            activity.finish();
        });
        //    getSelectChild();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nev_Parent_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentHomeFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Home");
                iDashboard = 1;
                break;

            case R.id.nev_Parent_StudentAssessment:
                Intent i_parent_ass = new Intent(activity, ParentStudentAssessmentActivity.class);
                activity.startActivity(i_parent_ass);
                break;

            case R.id.nev_Parent_notifaction:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentNotifactionFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Notification");
                iDashboard = 1;
                break;
            case R.id.nev_Parent_feedback:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentFeedbackFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Feedback");
                iDashboard = 1;
                break;
            case R.id.nev_ParentGallery:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentGalleryFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Gallery");
                iDashboard = 1;
                break;
            case R.id.nev_Parent_yearly_event:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentEventFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Yearly Event");
                iDashboard = 1;
                break;
            case R.id.nev_TrainerPostSchoolActivity:
                Intent i_school = new Intent(activity, TrainerPostSchoolActivity.class);
                activity.startActivity(i_school);
                break;
            case R.id.nev_Parent_achievements:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentAchievementsFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Achievements");
                iDashboard = 1;
                break;
            case R.id.nev_Parent_student_attendance:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentStudentAttendanceFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Student Attendance");
                iDashboard = 1;
                break;
            case R.id.nev_Parent_about_us:
                Intent i_about_us = new Intent(activity, AboutUsActivity.class);
                activity.startActivity(i_about_us);
                break;
            case R.id.nev_ParentRateUs:
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName()));
                startActivity(rateIntent);
                break;
            case R.id.nev_ParentShare:
                ShareCompat.IntentBuilder.from(activity)
                        .setType("text/plain")
                        .setChooserTitle("E-Sport")
                        .setText("http://play.google.com/store/apps/details?id=" + activity.getPackageName())
                        .startChooser();

                break;
//
            case R.id.nev_ParentLogout:
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
                    new ParentHomeFragment()).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
            navigationView.setCheckedItem(R.id.nev_TrainerHome);
            toolbar.setTitle("" + AppPreferences.init(activity).getString("name", ""));
            iDashboard = 2;
        } else if (iDashboard == 2) {
            app.showToast("Press again to close E-Sport !");
            iDashboard = 3;

        } else if (iDashboard == 3) {
            Intent intent = new Intent(
                    Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void getSelectChild() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            child_array.clear();
            dialog.show();
            app.getApiService().getChildList().enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("child_list");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    SelectChildDTO selectChildDTO = new SelectChildDTO();
                                    selectChildDTO.setSchool_id(c.getString("school_id"));
                                    selectChildDTO.setStudent_name(c.getString("student_name"));
                                    selectChildDTO.setStr_class(c.getString("class"));
                                    selectChildDTO.setStudent_id(c.getString("student_id"));
                                    selectChildDTO.setSchool_name(c.getString("school_name"));
                                    child_array.add(selectChildDTO);
                                }
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
            CommonUtil.showBar(activity, ".activity.TrainerNotificationActivity");
        }
    }
}