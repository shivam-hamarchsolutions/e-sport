package app.puretech.e_sport.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.SchoolMonthlyPlannerAdapter;
import app.puretech.e_sport.model.SchoolMonthlyPlannerDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ParentMonthlyPlannerActivity extends AppCompatActivity {

    private App app;
    private Activity activity;
    private SwipeRefreshLayout s_Refresh;
    private Toolbar toolbar;
    private Intent i_back;

    private Spinner spinner_class, spinner_division;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_monthly_planner);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        s_Refresh = findViewById(R.id.s_refresh);
        toolbar = findViewById(R.id.toolbar_home);
        i_back = new Intent(activity, ParentHomeActivity.class);
        spinner_class = findViewById(R.id.spinner_class);
        spinner_division = findViewById(R.id.spinner_division);
        btn_next = findViewById(R.id.btn_next);
        swipRefresh(s_Refresh);
        doBack(toolbar);
        //getMonthlyPlannerClass();
        btn_next.setOnClickListener(v -> {
            AppPreferences.init(activity).setString("class",spinner_class.getSelectedItem().toString()+" "+spinner_division.getSelectedItem().toString());
            Intent i_next = new Intent(activity, ParentMonthlyPlannerDataActivity.class);
            activity.startActivity(i_next);
        });
    }

    private void swipRefresh(SwipeRefreshLayout view) {
        view.setOnRefreshListener(() -> s_Refresh.setRefreshing(false));
    }

    private void doBack(Toolbar toolbar) {
        toolbar.setOnClickListener(v -> {
            activity.startActivity(i_back);
            activity.finish();
        });
    }

    @Override
    public void onBackPressed() {
        activity.startActivity(i_back);
        activity.finish();
    }
}
