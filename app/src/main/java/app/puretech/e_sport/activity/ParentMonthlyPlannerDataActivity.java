package app.puretech.e_sport.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.SchoolMonthlyPlannerSubAdapter;
import app.puretech.e_sport.model.SchoolMonthlyPlannerSubDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ParentMonthlyPlannerDataActivity extends AppCompatActivity implements View.OnClickListener {

    private App app;
    private Activity activity;
    private RecyclerView rv_show;
    private List<SchoolMonthlyPlannerSubDTO> array_list = new ArrayList<>();
    private SchoolMonthlyPlannerSubAdapter schoolMonthlyPlannerSubAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Toolbar toolbar;
    private Intent i_back;
    private RelativeLayout rl_month_next, rl_month_back;
    private Calendar c;
    private SimpleDateFormat df_daily;
    private String[] fd_daily;
    private TextView tv_trainer_monthly_planner_month;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_monthly_planner_data);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_show = findViewById(R.id.rv_school_monthly_planner_sub);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        toolbar = findViewById(R.id.toolbar_home);
        rl_month_back = findViewById(R.id.rl_month_back);
        rl_month_next = findViewById(R.id.rl_month_next);
        tv_trainer_monthly_planner_month = findViewById(R.id.tv_trainer_monthly_planner_month);
        i_back = new Intent(activity, ParentMonthlyPlannerActivity.class);
        rl_month_next.setOnClickListener(this);
        rl_month_back.setOnClickListener(this);
        //Get current month
        c = Calendar.getInstance();
        df_daily = new SimpleDateFormat("yyyy-MM");
        fd_daily = new String[]{df_daily.format(c.getTime())};
        tv_trainer_monthly_planner_month.setText(fd_daily[0]);
        swipRefresh(s_Refresh);
        doBack(toolbar);
        getMonthlyPlanner(tv_trainer_monthly_planner_month.getText().toString(), AppPreferences.init(activity).getString("class", ""));
    }

    private void getMonthlyPlanner(String str_month, String str_class) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("month", str_month)
                    .addFormDataPart("class", str_class)
                    .build();
            app.getApiService().getMonthlyPlanner(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("monthly_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    SchoolMonthlyPlannerSubDTO schoolMonthlyPlannerSubDTO = new SchoolMonthlyPlannerSubDTO();
                                    schoolMonthlyPlannerSubDTO.setTitle(c.getString("day"));
                                    schoolMonthlyPlannerSubDTO.setStr_sport_name(c.getString("sport_name"));
                                    schoolMonthlyPlannerSubDTO.setStr_image(c.getString("session_pic"));
                                    array_list.add(schoolMonthlyPlannerSubDTO);
                                }
                                setAdapter();
                            } else if (status.equals("1")) {
                                setAdapter();
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
            CommonUtil.showBar(activity, ".activity.TrainerMonthlyPlannerSubActivity");
        }

    }

    private void setAdapter() {
        schoolMonthlyPlannerSubAdapter = new SchoolMonthlyPlannerSubAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_show.setLayoutManager(mLayoutManager);
        rv_show.setItemAnimator(new DefaultItemAnimator());
        rv_show.setAdapter(schoolMonthlyPlannerSubAdapter);
        if (schoolMonthlyPlannerSubAdapter.getItemCount() == 0) {
            ll_hide.setVisibility(View.VISIBLE);
        } else {
            ll_hide.setVisibility(View.GONE);
        }
    }

    private void swipRefresh(SwipeRefreshLayout view) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                s_Refresh.setRefreshing(false);
            }
        });
    }

    private void doBack(Toolbar toolbar) {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(i_back);
                activity.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        activity.startActivity(i_back);
        activity.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_month_back:
                c.add(Calendar.MONTH, -1);
                fd_daily[0] = df_daily.format(c.getTime());
                tv_trainer_monthly_planner_month.setText(fd_daily[0]);
                getMonthlyPlanner(tv_trainer_monthly_planner_month.getText().toString(), AppPreferences.init(activity).getString("class", ""));
                break;
            case R.id.rl_month_next:
                c.add(Calendar.MONTH, 1);
                fd_daily[0] = df_daily.format(c.getTime());
                tv_trainer_monthly_planner_month.setText(fd_daily[0]);
                getMonthlyPlanner(tv_trainer_monthly_planner_month.getText().toString(), AppPreferences.init(activity).getString("class", ""));
                break;
        }
    }
}
