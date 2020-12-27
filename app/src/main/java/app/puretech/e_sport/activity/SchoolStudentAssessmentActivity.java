package app.puretech.e_sport.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SchoolStudentAssessmentActivity extends AppCompatActivity {
    App app;
    Activity activity;
    private Button btn_next;
    private ArrayList arrayClass, arrayDivision;
    private Spinner spinner_class, spinner_division, spinner_term;
    private ArrayAdapter<String> classAdapter, divisionAdapter;
    private Dialog dialog;
    private Toolbar toolbar;
    private Intent i_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_student_assessment);

        doInit();
        getClassDivision();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("class_name",spinner_class.getSelectedItem().toString());
                AppPreferences.init(activity).setString("division_name",spinner_division.getSelectedItem().toString());
                AppPreferences.init(activity).setString("term",spinner_term.getSelectedItem().toString());
                Intent i_next = new Intent(activity, SchoolStudentAssessmentListActivity.class);
                activity.startActivity(i_next);
                activity.finish();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(i_back);
                activity.finish();
            }
        });
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        i_back = new Intent(activity, SchoolHomeActivity.class);
        btn_next = findViewById(R.id.btn_next);
        arrayClass = new ArrayList();
        arrayDivision = new ArrayList();
        spinner_class = findViewById(R.id.spinner_class);
        spinner_division = findViewById(R.id.spinner_division);
        spinner_term = findViewById(R.id.tv_spinner_term);
        toolbar = findViewById(R.id.toolbar_home);
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
        classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayClass);
        spinner_class.setAdapter(classAdapter);
        divisionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayDivision);
        spinner_division.setAdapter(divisionAdapter);
    }

    @Override
    public void onBackPressed() {
        activity.startActivity(i_back);
        activity.finish();
    }
}
