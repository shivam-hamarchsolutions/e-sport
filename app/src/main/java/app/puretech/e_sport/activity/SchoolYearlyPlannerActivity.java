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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.TrainerYearlyPlannerAdapter;
import app.puretech.e_sport.model.TrainerYearlyPlannerDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SchoolYearlyPlannerActivity extends AppCompatActivity {


    App app;
    Activity activity;
    private GridView rv_show;
    private List<TrainerYearlyPlannerDTO> array_list = new ArrayList<>();
    private TrainerYearlyPlannerAdapter trainerYearlyPlannerAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Toolbar toolbar;
    Intent i_back;
    Dialog dialog;
    private ArrayList arrayClass, arrayDivision, monthArray;
    private Spinner spinner_class, spinner_division, spinner_term;
    private ArrayAdapter<String> classAdapter, divisionAdapter, monthAdapter;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_yearly_planner);

        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_show = findViewById(R.id.rv_trainer_yearly_planner);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        toolbar = findViewById(R.id.toolbar_home);
        i_back = new Intent(activity, SchoolHomeActivity.class);
        arrayClass = new ArrayList();
        arrayDivision = new ArrayList();
        monthArray = new ArrayList();
        spinner_class = findViewById(R.id.spinner_class);
        spinner_division = findViewById(R.id.spinner_division);
        spinner_term = findViewById(R.id.tv_spinner_term);
        btn_next = findViewById(R.id.btn_next);
        swipRefresh(s_Refresh);
        doBack(toolbar);
        // getYearlyPlannerClass();
        getClassDivision();
        getYearlyPlannerMonth();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("month", spinner_term.getSelectedItem().toString());
                AppPreferences.init(activity).setString("class", spinner_class.getSelectedItem().toString() + " " + spinner_division.getSelectedItem().toString());
                Intent i_next = new Intent(activity, TrainerYearlyPlannerMonthSubActivity.class);
                activity.startActivity(i_next);
            }
        });

    }

    private void getYearlyPlannerClass() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            app.getApiService().getYearlyPlannerClass().enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("class_list");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    TrainerYearlyPlannerDTO trainerYearlyPlannerDTO = new TrainerYearlyPlannerDTO();
                                    trainerYearlyPlannerDTO.setTitle(c.getString("class"));
                                    array_list.add(trainerYearlyPlannerDTO);
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
                }
            });
        } else {
            CommonUtil.showBar(activity, ".activity.TrainerYearlyPlannerActivity");
        }

    }

    private void getClassDivision() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            arrayDivision.clear();
            arrayClass.clear();
            dialog.show();
            app.getApiService().getClassDivision().enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("class_data");
                                JSONArray jsonArray1 = jobj.getJSONArray("division_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    arrayClass.add(c.getString("class"));
                                }
                                for (int id = 0; id < jsonArray1.length(); id++) {
                                    JSONObject jsonObject = jsonArray1.getJSONObject(id);
                                    arrayDivision.add(jsonObject.getString("division"));
                                }
                                setClassAdapter();
                            } else if (status.equals("1")) {
                                setClassAdapter();
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
            CommonUtil.showBar(activity, ".activity.TrainerYearlyEventsActivity");
        }
    }

    private void getYearlyPlannerMonth() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            app.getApiService().getYearlyPlannerMonth().enqueue(new Callback<Map<String, Object>>() {
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
//                                    TrainerYearlyPlannerMonthDTO trainerYearlyPlannerMonthDTO = new TrainerYearlyPlannerMonthDTO();
//                                    trainerYearlyPlannerMonthDTO.setTitle(c.getString("month"));
                                    //ar.add(trainerYearlyPlannerMonthDTO);
                                    monthArray.add(c.getString("month"));
                                }
                                setMonthAdapter();
                            } else if (status.equals("1")) {
                                setMonthAdapter();
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
            CommonUtil.showBar(activity, ".activity.TrainerYearlyPlannerMonthActivity");
        }
    }

    private void setClassAdapter() {
        classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayClass);
        spinner_class.setAdapter(classAdapter);
        divisionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayDivision);
        spinner_division.setAdapter(divisionAdapter);
    }

    private void setAdapter() {
        trainerYearlyPlannerAdapter = new TrainerYearlyPlannerAdapter(array_list, activity);
        rv_show.setAdapter(trainerYearlyPlannerAdapter);
        if (trainerYearlyPlannerAdapter.getCount() == 0) {
            ll_hide.setVisibility(View.VISIBLE);
        } else {
            ll_hide.setVisibility(View.GONE);
        }
    }

    private void setMonthAdapter() {
        monthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, monthArray);
        spinner_term.setAdapter(monthAdapter);
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
