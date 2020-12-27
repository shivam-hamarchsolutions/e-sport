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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.SchoolYearlyPlannerMonthSubAdapter;
import app.puretech.e_sport.adapter.TrainerYearlyPlannerMonthSubAdapter;
import app.puretech.e_sport.model.TrainerYearlyPlannerMonthSubDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SchoolYearlyPlannerMonthSubActivity extends AppCompatActivity {
    App app;
    Activity activity;
    private RecyclerView rv_show;
    private List<TrainerYearlyPlannerMonthSubDTO> array_list = new ArrayList<>();
    private SchoolYearlyPlannerMonthSubAdapter schoolYearlyPlannerMonthSubAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Toolbar toolbar;
    Intent i_back;
    private TextView tv_month;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_yearly_planner_month_sub);
        doInit();
    }
    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_show = findViewById(R.id.rv_trainer_yearly_planner_month_sub);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle(AppPreferences.init(activity).getString("class","").toUpperCase());
        tv_month = findViewById(R.id.tv_month);
        tv_month.setText(CommonUtil.convertDate(AppPreferences.init(activity).getString("month",""),"M","MMM"));
        i_back = new Intent(activity, SchoolYearlyPlannerActivity.class);
        swipRefresh(s_Refresh);
        doBack(toolbar);
        getYearlyPlanner(AppPreferences.init(activity).getString("month",""),AppPreferences.init(activity).getString("class",""));
    }
    private void getYearlyPlanner(String str_month, String str_class) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("month", str_month)
                    .addFormDataPart("class", str_class)
                    .build();
            app.getApiService().getYearlyPlanner(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("yearly_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    TrainerYearlyPlannerMonthSubDTO trainerYearlyPlannerMonthSubDTO = new TrainerYearlyPlannerMonthSubDTO();
                                    trainerYearlyPlannerMonthSubDTO.setTitle(c.getString("day"));
                                    trainerYearlyPlannerMonthSubDTO.setStr_sprot_name(c.getString("sport_name"));
                                    array_list.add(trainerYearlyPlannerMonthSubDTO);
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
            CommonUtil.showBar(activity, ".activity.TrainerYearlyPlannerMonthSubActivity");
        }

    }
    private void setAdapter(){
        schoolYearlyPlannerMonthSubAdapter = new SchoolYearlyPlannerMonthSubAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_show.setLayoutManager(mLayoutManager);
        rv_show.setItemAnimator(new DefaultItemAnimator());
        rv_show.setAdapter(schoolYearlyPlannerMonthSubAdapter);
        if (schoolYearlyPlannerMonthSubAdapter.getItemCount() == 0) {
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
