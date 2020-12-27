package app.puretech.e_sport.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
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
import app.puretech.e_sport.adapter.SchoolMonthlyPlannerAdapter;
import app.puretech.e_sport.model.SchoolMonthlyPlannerDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SchoolMonthlyPlannerActivity extends AppCompatActivity {

    private App app;
    private Activity activity;
    private GridView rv_show;
    private List<SchoolMonthlyPlannerDTO> array_list = new ArrayList<>();
    private SchoolMonthlyPlannerAdapter schoolMonthlyPlannerAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Toolbar toolbar;
    private Intent i_back;
    private Dialog dialog;
    private ArrayList arrayClass, arrayDivision;
    private Spinner spinner_class, spinner_division, spinner_term;
    private ArrayAdapter<String> classAdapter, divisionAdapter;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_school_monthly_planner);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_show = findViewById(R.id.rv_school_monthly_planner);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        toolbar = findViewById(R.id.toolbar_home);
        i_back = new Intent(activity, SchoolHomeActivity.class);
        arrayClass = new ArrayList();
        arrayDivision = new ArrayList();
        spinner_class = findViewById(R.id.spinner_class);
        spinner_division = findViewById(R.id.spinner_division);
        spinner_term = findViewById(R.id.tv_spinner_term);
        btn_next = findViewById(R.id.btn_next);
        swipRefresh(s_Refresh);
        doBack(toolbar);
        //getMonthlyPlannerClass();
        getClassDivision();
        btn_next.setOnClickListener(v -> {
            AppPreferences.init(activity).setString("class",spinner_class.getSelectedItem().toString()+" "+spinner_division.getSelectedItem().toString());
            Intent i_next = new Intent(activity, SchoolMonthlyPlannerSubActivity.class);
            activity.startActivity(i_next);
        });
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
                    dialog.dismiss();
                    Toast.makeText(app, "Something went wrong,please try again!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            CommonUtil.showBar(activity, ".activity.TrainerYearlyEventsActivity");
        }
    }

    private void setClassAdapter() {
        classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayClass);
        spinner_class.setAdapter(classAdapter);
        divisionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayDivision);
        spinner_division.setAdapter(divisionAdapter);
    }

    private void getMonthlyPlannerClass() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            app.getApiService().getMonthlyPlannerClass().enqueue(new Callback<Map<String, Object>>() {
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
                                    SchoolMonthlyPlannerDTO schoolMonthlyPlannerDTO = new SchoolMonthlyPlannerDTO();
                                    schoolMonthlyPlannerDTO.setTitle(c.getString("class"));
                                    array_list.add(schoolMonthlyPlannerDTO);
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
            CommonUtil.showBar(activity, ".activity.TrainerMonthlyPlannerActivity");
        }
    }

    private void setAdapter() {
        schoolMonthlyPlannerAdapter = new SchoolMonthlyPlannerAdapter(array_list, activity);
        rv_show.setAdapter(schoolMonthlyPlannerAdapter);
        if (schoolMonthlyPlannerAdapter.getCount() == 0) {
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