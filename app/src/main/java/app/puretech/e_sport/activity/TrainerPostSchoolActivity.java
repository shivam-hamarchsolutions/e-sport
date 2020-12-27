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
import app.puretech.e_sport.adapter.TrainerPostSchoolActivityAdapter;
import app.puretech.e_sport.model.TrainerPostSchoolActivityDTO;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TrainerPostSchoolActivity extends AppCompatActivity {

    App app;
    Activity activity;
    private RecyclerView rv_trainer_post_activity;
    private List<TrainerPostSchoolActivityDTO> array_list = new ArrayList<>();
    private TrainerPostSchoolActivityAdapter trainerPostSchoolActivityAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Toolbar toolbar;
    private Intent iBack;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_trainer_post_school);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_trainer_post_activity = findViewById(R.id.rv_Trainer_post_school_activity);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        toolbar = findViewById(R.id.toolbar_home);
        iBack = new Intent(activity, TrainerHomeActivity.class);
        swipRefresh(s_Refresh);
        doBack(toolbar);
        getSchoolPostActivity();
    }

    private void getSchoolPostActivity() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            app.getApiService().getSchoolPostActivity().enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("activity_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    TrainerPostSchoolActivityDTO trainerPostSchoolActivityDTO = new TrainerPostSchoolActivityDTO();
                                    trainerPostSchoolActivityDTO.setStr_grade("Grade : "+c.getString("grade"));
                                    trainerPostSchoolActivityDTO.setStr_sport_name(c.getString("activity_name"));
                                    trainerPostSchoolActivityDTO.setStr_sessions("Session : "+c.getString("total_session"));
                                    trainerPostSchoolActivityDTO.setStr_time(c.getString("timing"));
                                    trainerPostSchoolActivityDTO.setStr_start_date("Start Date "+c.getString("start_date"));
                                    trainerPostSchoolActivityDTO.setStr_end_date("End Date : "+c.getString("end_date"));
                                    trainerPostSchoolActivityDTO.setStr_days("Week Days : "+c.getString("weekdays"));
                                    array_list.add(trainerPostSchoolActivityDTO);
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
            CommonUtil.showBar(activity, ".activity.TrainerNotificationActivity");
        }
    }

    private void setAdapter() {
        trainerPostSchoolActivityAdapter = new TrainerPostSchoolActivityAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_trainer_post_activity.setLayoutManager(mLayoutManager);
        rv_trainer_post_activity.setItemAnimator(new DefaultItemAnimator());
        rv_trainer_post_activity.setAdapter(trainerPostSchoolActivityAdapter);
        if (trainerPostSchoolActivityAdapter.getItemCount() == 0) {
            ll_hide.setVisibility(View.VISIBLE);
        } else {
            ll_hide.setVisibility(View.GONE);
        }
    }

    private void swipRefresh(SwipeRefreshLayout view) {
        view.setOnRefreshListener(() -> s_Refresh.setRefreshing(false));
    }

    private void doBack(Toolbar toolbar) {
        toolbar.setOnClickListener(v -> {
           // activity.startActivity(iBack);
            activity.finish();
        });
    }

    @Override
    public void onBackPressed() {
      //  activity.startActivity(iBack);
        activity.finish();
    }
}
