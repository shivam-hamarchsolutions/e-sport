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
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.TrainerYearlyEventsAdapter;
import app.puretech.e_sport.model.TrainerYearlyEventsDTO;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TrainerYearlyEventsActivity extends AppCompatActivity {

    App app;
    Activity activity;
    private RecyclerView rv_trainer_yearly_event;
    private List<TrainerYearlyEventsDTO> array_list = new ArrayList<>();
    private TrainerYearlyEventsAdapter trainerYearlyEventsAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Toolbar toolbar;
    Intent i_back;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_trainer_yearly_events);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_trainer_yearly_event = findViewById(R.id.rv_Trainer_yearly_events);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        toolbar = findViewById(R.id.toolbar_home);
        i_back = new Intent(activity, TrainerHomeActivity.class);
        swipRefresh(s_Refresh);
        doBack(toolbar);
        getYearlyEvents();
    }

    private void getYearlyEvents() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            app.getApiService().getYearlyEvents().enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("event_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    TrainerYearlyEventsDTO trainerYearlyEventsDTO = new TrainerYearlyEventsDTO();
                                    trainerYearlyEventsDTO.setStr_sub_event_name(c.getString("sub_event_name"));
                                    trainerYearlyEventsDTO.setStr_day(c.getString("day"));
                                    trainerYearlyEventsDTO.setStr_date(c.getString("date"));
                                    trainerYearlyEventsDTO.setStr_timing(c.getString("timing"));
                                    trainerYearlyEventsDTO.setStr_session(c.getString("session"));
                                    trainerYearlyEventsDTO.setStr_venue(c.getString("venue"));
                                    trainerYearlyEventsDTO.setStr_grade(c.getString("grade"));
                                    array_list.add(trainerYearlyEventsDTO);
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
            CommonUtil.showBar(activity, ".activity.TrainerYearlyEventsActivity");
        }
    }

    private void setAdapter() {
        trainerYearlyEventsAdapter = new TrainerYearlyEventsAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_trainer_yearly_event.setLayoutManager(mLayoutManager);
        rv_trainer_yearly_event.setItemAnimator(new DefaultItemAnimator());
        rv_trainer_yearly_event.setAdapter(trainerYearlyEventsAdapter);
        if (trainerYearlyEventsAdapter.getItemCount() == 0) {
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
}