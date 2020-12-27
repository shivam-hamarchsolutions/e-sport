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

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.TrainerDailyTimeTableAdapter;
import app.puretech.e_sport.model.TrainerDailyTimeTableDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import app.puretech.e_sport.webservices.DailyTimeTableAPI;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ParentDailyTimeTableActivity extends AppCompatActivity {

    private App app;
    private HorizontalCalendar horizontalCalendar;
    private Activity activity;
    private RecyclerView rv_Daily_Time_Table;
    private ArrayList<TrainerDailyTimeTableDTO> array_list = new ArrayList<>();
    private TrainerDailyTimeTableAdapter trainerDailyTimeTableAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Toolbar toolbar;
    private Intent i_back;
    private DailyTimeTableAPI dailyTimeTableAPI;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_daily_time_table);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dailyTimeTableAPI = new DailyTimeTableAPI();
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);

        rv_Daily_Time_Table = findViewById(R.id.rv_Daily_Time_Table);
        s_Refresh = findViewById(R.id.s_refresh);
        swipRefresh(s_Refresh);
        ll_hide = findViewById(R.id.ll_Hide);
        toolbar = findViewById(R.id.toolbar_home);
        doBack(toolbar);
        i_back = new Intent(activity, ParentHomeActivity.class);
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
                    getDailyTimeTable(getDateFormat(date));
                } else if (str_pos < position) {
                    getDailyTimeTable(getDateFormat(date));
                } else {
                    getDailyTimeTable(getDateFormat(date));
                }
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }
        });
    }

    private void setAdapter() {
        trainerDailyTimeTableAdapter = new TrainerDailyTimeTableAdapter(array_list, activity, app, dialog,"parent");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_Daily_Time_Table.setLayoutManager(mLayoutManager);
        rv_Daily_Time_Table.setItemAnimator(new DefaultItemAnimator());
        rv_Daily_Time_Table.setAdapter(trainerDailyTimeTableAdapter);
        if (trainerDailyTimeTableAdapter.getItemCount() == 0) {
            ll_hide.setVisibility(View.VISIBLE);
        } else {
            ll_hide.setVisibility(View.GONE);
        }
    }

    private void getDailyTimeTable(String str_date) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("current_date", str_date)
                    .addFormDataPart("school_id", AppPreferences.init(activity).getString("school_id", ""))
                    .addFormDataPart("class", AppPreferences.init(activity).getString("class", ""))
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
                                    TrainerDailyTimeTableDTO trainerDailyTimeTableDTO = new TrainerDailyTimeTableDTO();
                                    trainerDailyTimeTableDTO.setStr_school_id(c.getString("school_id"));
                                    trainerDailyTimeTableDTO.setStr_school_name(c.getString("school_name"));
                                    trainerDailyTimeTableDTO.setStr_time(c.getString("time"));
                                    trainerDailyTimeTableDTO.setStr_class(c.getString("class"));
                                    trainerDailyTimeTableDTO.setStr_session(c.getString("sport_name"));
                                    trainerDailyTimeTableDTO.setStr_days(c.getString("day"));
                                    trainerDailyTimeTableDTO.setStr_class_session_id(c.getString("clsSession_id"));
                                    trainerDailyTimeTableDTO.setStr_image(c.getString("session_pic"));
                                    trainerDailyTimeTableDTO.setStr_session_pdf(c.getString("session_pdf"));
                                    trainerDailyTimeTableDTO.setStr_present_student(c.getString("present_student") + " / " + c.getString("total_student"));
                                    //trainerDailyTimeTableDTO.setStr_status(c.getString(""));
                                    trainerDailyTimeTableDTO.setPresetStu(c.getInt("present_student"));
                                    trainerDailyTimeTableDTO.setTotalStu(c.getInt("total_student"));

                                    array_list.add(trainerDailyTimeTableDTO);
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
            CommonUtil.showBar(activity, ".activity.TrainerDailyTimeTableActivity");
        }

    }

    private String getDateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        AppPreferences.init(activity).setString("date", formatter.format(date));
        return formatter.format(date);
    }

    private void swipRefresh(SwipeRefreshLayout view) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                s_Refresh.setRefreshing(false);
                getDailyTimeTable(AppPreferences.init(activity).getString("date", ""));
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
}
