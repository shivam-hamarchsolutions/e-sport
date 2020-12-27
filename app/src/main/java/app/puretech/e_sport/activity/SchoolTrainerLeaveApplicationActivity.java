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
import app.puretech.e_sport.adapter.SchoolTrainerLeaveApplicationAdapter;
import app.puretech.e_sport.model.SchoolTrainerLeaveApplicationDTO;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SchoolTrainerLeaveApplicationActivity extends AppCompatActivity {

    App app;
    Activity activity;
    private RecyclerView rv_show;
    private List<SchoolTrainerLeaveApplicationDTO> array_list = new ArrayList<>();
    private SchoolTrainerLeaveApplicationAdapter schoolTrainerLeaveApplicationAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Toolbar toolbar;
    private Intent i_back;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_school_trainer_leave_application);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_show = findViewById(R.id.rv_School_trainer_leave_application);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        toolbar = findViewById(R.id.toolbar_home);
        i_back = new Intent(activity, SchoolHomeActivity.class);
        swipRefresh(s_Refresh);
        doBack(toolbar);
        getTrainerLeave();

    }
    private void getTrainerLeave() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            app.getApiService().getTrainerLeaveApplication().enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("leave_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    SchoolTrainerLeaveApplicationDTO schoolTrainerLeaveApplicationDTO = new SchoolTrainerLeaveApplicationDTO();
                                    schoolTrainerLeaveApplicationDTO.setStr_name(c.getString("name"));
                                    schoolTrainerLeaveApplicationDTO.setStr_sport_name(c.getString("primary_game"));
                                    schoolTrainerLeaveApplicationDTO.setStr_subject(c.getString("subject"));
                                    schoolTrainerLeaveApplicationDTO.setStr_start_date(c.getString("start_date"));
                                    schoolTrainerLeaveApplicationDTO.setStr_end_date(c.getString("end_date"));
                                    schoolTrainerLeaveApplicationDTO.setStr_note(c.getString("description"));
                                    schoolTrainerLeaveApplicationDTO.setStr_image(c.getString("profile_photo"));
                                    array_list.add(schoolTrainerLeaveApplicationDTO);
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
    private void setAdapter(){
        schoolTrainerLeaveApplicationAdapter = new SchoolTrainerLeaveApplicationAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_show.setLayoutManager(mLayoutManager);
        rv_show.setItemAnimator(new DefaultItemAnimator());
        rv_show.setAdapter(schoolTrainerLeaveApplicationAdapter);
        if (schoolTrainerLeaveApplicationAdapter.getItemCount() == 0) {
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