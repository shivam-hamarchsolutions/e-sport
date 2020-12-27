package app.puretech.e_sport.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import app.puretech.e_sport.adapter.SchoolDailyTimeTableAdapter;
import app.puretech.e_sport.model.SchoolDailyTimeTableDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SchoolDailyTimeTableActivity extends AppCompatActivity implements View.OnClickListener {

    App app;
    Activity activity;
    private RecyclerView rv_show;
    private List<SchoolDailyTimeTableDTO> array_list = new ArrayList<>();
    private SchoolDailyTimeTableAdapter schoolDailyTimeTableAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Toolbar toolbar;
    Intent i_back;
    private RelativeLayout rl_month_next, rl_month_back;
    Calendar c;
    SimpleDateFormat df_daily;
    String[] fd_daily;
    TextView tv_trainer_monthly_planner_month;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_school_daily_time_table);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_show = findViewById(R.id.rv_school_daily_time_table);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        toolbar = findViewById(R.id.toolbar_home);
        rl_month_back = findViewById(R.id.rl_month_back);
        rl_month_next = findViewById(R.id.rl_month_next);
        tv_trainer_monthly_planner_month = findViewById(R.id.tv_trainer_monthly_planner_month);
        i_back = new Intent(activity, SchoolHomeActivity.class);
        rl_month_next.setOnClickListener(this);
        rl_month_back.setOnClickListener(this);
        //Get current month
        c = Calendar.getInstance();
        df_daily = new SimpleDateFormat("dd MMM yyyy");
        fd_daily = new String[]{df_daily.format(c.getTime())};
        tv_trainer_monthly_planner_month.setText(fd_daily[0]);
        swipRefresh(s_Refresh);
        doBack(toolbar);
        getDailyTimeTable(CommonUtil.convertDate(tv_trainer_monthly_planner_month.getText().toString(), "dd MMM yyyy", "yyyy-MM-dd"));
    }

    private void getDailyTimeTable(String str_date) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("current_date", str_date)
                    .addFormDataPart("school_id", "")
                    .addFormDataPart("trainer_id", AppPreferences.init(activity).getString("trainer_id",""))
                    .addFormDataPart("class", "")
                    .build();

            app.getApiService().getDailyTimeTable(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    SchoolDailyTimeTableDTO schoolDailyTimeTableDTO = new SchoolDailyTimeTableDTO();
                                    schoolDailyTimeTableDTO.setStr_grade(c.getString("day"));
                                    schoolDailyTimeTableDTO.setStr_time(c.getString("time"));
                                    schoolDailyTimeTableDTO.setStr_class(c.getString("class"));
                                    schoolDailyTimeTableDTO.setStr_session_name(c.getString("session_name"));
                                    schoolDailyTimeTableDTO.setStr_school_name(c.getString("school_name"));
                                    schoolDailyTimeTableDTO.setStr_image(c.getString("session_pic"));
                                    schoolDailyTimeTableDTO.setStr_subject(c.getString("day"));
                                    schoolDailyTimeTableDTO.setStr_attendance(c.getString("present_student")+" / "+c.getString("total_student"));
                                    array_list.add(schoolDailyTimeTableDTO);
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
                    Toast.makeText(app, "Something went wrong,please try again!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            CommonUtil.showBar(activity, ".activity.TrainerDailyTimeTableActivity");
        }

    }

    private void setAdapter() {
        schoolDailyTimeTableAdapter = new SchoolDailyTimeTableAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_show.setLayoutManager(mLayoutManager);
        rv_show.setItemAnimator(new DefaultItemAnimator());
        rv_show.setAdapter(schoolDailyTimeTableAdapter);
        if (schoolDailyTimeTableAdapter.getItemCount() == 0) {
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
                c.add(Calendar.DAY_OF_MONTH, -1);
                fd_daily[0] = df_daily.format(c.getTime());
                tv_trainer_monthly_planner_month.setText(fd_daily[0]);
                getDailyTimeTable(CommonUtil.convertDate(tv_trainer_monthly_planner_month.getText().toString(), "dd MMM yyyy", "yyyy-MM-dd"));

                break;
            case R.id.rl_month_next:
                c.add(Calendar.DAY_OF_MONTH, 1);
                fd_daily[0] = df_daily.format(c.getTime());
                tv_trainer_monthly_planner_month.setText(fd_daily[0]);
                getDailyTimeTable(CommonUtil.convertDate(tv_trainer_monthly_planner_month.getText().toString(), "dd MMM yyyy", "yyyy-MM-dd"));

                break;
        }
    }
}