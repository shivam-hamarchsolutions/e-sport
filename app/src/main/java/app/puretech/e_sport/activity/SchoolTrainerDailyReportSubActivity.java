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
import java.util.Date;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.SchoolTrainerDailyReportSubAdapter;
import app.puretech.e_sport.model.SchoolTrainerDailyReportSubDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SchoolTrainerDailyReportSubActivity extends AppCompatActivity implements View.OnClickListener {

    private App app;
    private Activity activity;
    private RecyclerView rv_show;
    private List<SchoolTrainerDailyReportSubDTO> array_list = new ArrayList<>();
    private SchoolTrainerDailyReportSubAdapter schoolTrainerDailyReportSubAdapter;
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
    private HorizontalCalendar horizontalCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_school_trainer_daily_report_sub);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_show = findViewById(R.id.rv_school_trainer_daily_report_sub);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        toolbar = findViewById(R.id.toolbar_home);
        rl_month_back = findViewById(R.id.rl_month_back);
        rl_month_next = findViewById(R.id.rl_month_next);
        tv_trainer_monthly_planner_month = findViewById(R.id.tv_trainer_monthly_planner_month);
        i_back = new Intent(activity, SchoolTrainerDailyTimeTableActivity.class);
        rl_month_next.setOnClickListener(this);
        rl_month_back.setOnClickListener(this);
        //Get current month
        c = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        horizontalCalendar = new HorizontalCalendar.Builder(activity, R.id.cv_daily_time_table)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(3)
                .dayNameFormat("EEE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .textSize(12f, 14f, 12f)
                .showDayName(true)
                .showMonthName(true)
                .build();
        AppPreferences.init(activity).setString("one", "one");
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {

                if (AppPreferences.init(activity).getString("one", "").equals("one")) {
                    AppPreferences.init(activity).setString("pos", "" + position);
                    AppPreferences.init(activity).setString("one", "one+two");
                }
                int str_pos = Integer.parseInt(AppPreferences.init(activity).getString("pos", ""));
                //Toast.makeText(activity, DateFormat.getDateInstance().format(date) + " is selected!", Toast.LENGTH_SHORT).show();
                if (str_pos == position) {
                    //  getDailyTimeTable(getDateFormat(date));
                    getDailyTimeTable(String.valueOf(date), AppPreferences.init(activity).getString("trainer_id", ""));
                } else if (str_pos < position) {
                    // getDailyTimeTable(getDateFormat(date));
                    getDailyTimeTable(String.valueOf(date), AppPreferences.init(activity).getString("trainer_id", ""));
                } else {
                    getDailyTimeTable(String.valueOf(date), AppPreferences.init(activity).getString("trainer_id", ""));
                    //  getDailyTimeTable(getDateFormat(date));
                }
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }
        });
        df_daily = new SimpleDateFormat("yyyy-MM-dd");
        fd_daily = new String[]{df_daily.format(c.getTime())};
        tv_trainer_monthly_planner_month.setText(fd_daily[0]);
        swipRefresh(s_Refresh);
        doBack(toolbar);
        // getDailyTimeTable(tv_trainer_monthly_planner_month.getText().toString(), AppPreferences.init(activity).getString("trainer_id", ""));

    }

    private void getDailyTimeTable(String str_date, String str_trainer_id) {
        Log.d("ghjghgh", "getDailyTimeTable: " + str_date);
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("current_date", str_date)
                    .addFormDataPart("trainer_id", str_trainer_id)
                    .build();
            app.getApiService().getTrainerDailyReport(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("trainer_daily_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    SchoolTrainerDailyReportSubDTO schoolTrainerDailyReportSubDTO = new SchoolTrainerDailyReportSubDTO();
                                    schoolTrainerDailyReportSubDTO.setStr_day(c.getString("day"));
                                    schoolTrainerDailyReportSubDTO.setStr_time(c.getString("time"));
                                    schoolTrainerDailyReportSubDTO.setStr_class(c.getString("class"));
                                    schoolTrainerDailyReportSubDTO.setStr_session_name(c.getString("session_name"));
                                    schoolTrainerDailyReportSubDTO.setStr_school_name(c.getString("school_name"));
                                    schoolTrainerDailyReportSubDTO.setStr_session_pic(c.getString("session_pic"));
                                    schoolTrainerDailyReportSubDTO.setStr_present_student(c.getString("present_student"));
                                    schoolTrainerDailyReportSubDTO.setStr_total_student(c.getString("total_student"));
                                    schoolTrainerDailyReportSubDTO.setStr_attendance_status(c.getString("attendance_status"));
                                    schoolTrainerDailyReportSubDTO.setToatlStu(c.getInt("total_student"));
                                    schoolTrainerDailyReportSubDTO.setPresentStu(c.getInt("present_student"));

                                    array_list.add(schoolTrainerDailyReportSubDTO);
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
        schoolTrainerDailyReportSubAdapter = new SchoolTrainerDailyReportSubAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_show.setLayoutManager(mLayoutManager);
        rv_show.setItemAnimator(new DefaultItemAnimator());
        rv_show.setAdapter(schoolTrainerDailyReportSubAdapter);
        if (schoolTrainerDailyReportSubAdapter.getItemCount() == 0) {
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
                getDailyTimeTable(tv_trainer_monthly_planner_month.getText().toString(), AppPreferences.init(activity).getString("trainer_id", ""));

                break;
            case R.id.rl_month_next:
                c.add(Calendar.DAY_OF_MONTH, 1);
                fd_daily[0] = df_daily.format(c.getTime());
                tv_trainer_monthly_planner_month.setText(fd_daily[0]);
                getDailyTimeTable(tv_trainer_monthly_planner_month.getText().toString(), AppPreferences.init(activity).getString("trainer_id", ""));

                break;
        }
    }
}