package app.puretech.e_sport.activity;

import android.app.Activity;
import android.app.Dialog;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.TrainerStudentAttendanceAdapter;
import app.puretech.e_sport.model.TrainerStudentAttendanceDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TrainerStudentAttendanceActivity extends AppCompatActivity {

    App app;
    Activity activity;
    private CheckBox cb_Trainer_attendance;
    private RecyclerView rv_student_attendance;
    private List<TrainerStudentAttendanceDTO> array_list = new ArrayList<>();
    private TrainerStudentAttendanceAdapter trainerStudentAttendanceAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Toolbar toolbar;
    private Dialog dialog;
    private TextView tv_present, tv_absent, tv_class, tv_total_student;
    private Button btn_submit;
    JSONArray jsonArray = new JSONArray();
    String valid_until,todayDateNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_traner_student_attendance);

        valid_until = AppPreferences.init(activity).getString("str_date", "");

        /* get current date */
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         todayDateNew = formatter.format(todayDate);

        Log.d("fffffffffff", "date: " + valid_until);
        Log.d("fffffffffff", "today date: " + todayDateNew);

        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        toolbar = findViewById(R.id.toolbar_home);
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        tv_class = findViewById(R.id.tv_class);
        tv_total_student = findViewById(R.id.tv_total_student);
        tv_absent = findViewById(R.id.tv_absent);
        tv_present = findViewById(R.id.tv_present);
        btn_submit = findViewById(R.id.btn_submit);
        doBack(toolbar);
        cb_Trainer_attendance = findViewById(R.id.cb_Trainer_attendance);
        setCheckBox(cb_Trainer_attendance);
        rv_student_attendance = findViewById(R.id.rv_Trainer_student_attendance);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        tv_class.setText(AppPreferences.init(activity).getString("class_name", ""));
        swipRefresh(s_Refresh);
        getStudentList(tv_class.getText().toString(), AppPreferences.init(activity).getString("school_id", ""));
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(valid_until.equals(todayDateNew)){
                    if (AppPreferences.init(activity).getString("attendence_status", "").equals("0")) {
                        for (int i = 0; i < trainerStudentAttendanceAdapter.getItemCount(); i++) {
                            String status = array_list.get(i).getStr_status();
                            String id = array_list.get(i).getStr_id();
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("student_id", id);
                                jsonObject.put("attendance_id", status);
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        doStudentAttendance(AppPreferences.init(activity).getString("cls_session_id", ""), AppPreferences.init(activity).getString("school_id", ""), jsonArray);
                    } else {
                        Toast.makeText(app, "Attendance already submitted!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(app, "Sorry, you can not update attendence!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getStudentList(String str_class, String school_id) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("class_name", str_class)
                    .addFormDataPart("school_id", school_id)
                    .build();
            app.getApiService().getStudentList(requestBody).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Response<Map<String, Object>> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        app.getLogger().error("success");
                        String status;
                        String message;
                        String student_count;
                        JSONObject jobj;
                        try {
                            jobj = new JSONObject(response.body());
                            status = jobj.getString("success");
                            message = jobj.getString("message");
                            student_count = jobj.getString("student_count");

                            if (status.equals("0")) {
                                JSONArray jsonArray = jobj.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    TrainerStudentAttendanceDTO trainerStudentAttendanceDTO = new TrainerStudentAttendanceDTO();
                                    trainerStudentAttendanceDTO.setStr_id(c.getString("id"));
                                    trainerStudentAttendanceDTO.setStr_serial_number(c.getString("roll_no"));
                                    trainerStudentAttendanceDTO.setStr_student_name(c.getString("student_name"));
                                    trainerStudentAttendanceDTO.setStr_status("1");
                                    array_list.add(trainerStudentAttendanceDTO);
                                }
                                tv_total_student.setText("Total Student : " + student_count);
                                tv_absent.setText("0");
                                tv_present.setText(student_count);
                                setAdapter();
                            } else if (status.equals("1")) {
                                tv_total_student.setText("Total Student : 0");
                                tv_absent.setText("0");
                                tv_present.setText("0");
                                setAdapter();
                                app.showSnackBar(activity, message);
                            } else {
                                tv_total_student.setText("Total Student : 0");
                                tv_absent.setText("0");
                                tv_present.setText("0");
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
            CommonUtil.showBar(activity, ".activity.TrainerStudentAttendanceActivity");
        }

    }

    private void setAdapter() {
        trainerStudentAttendanceAdapter = new TrainerStudentAttendanceAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        rv_student_attendance.setLayoutManager(mLayoutManager);

        rv_student_attendance.setItemAnimator(new DefaultItemAnimator());
        rv_student_attendance.setAdapter(trainerStudentAttendanceAdapter);
        if (trainerStudentAttendanceAdapter.getItemCount() == 0) {
            ll_hide.setVisibility(View.VISIBLE);
        } else {
            ll_hide.setVisibility(View.GONE);
        }

    }

    private void doStudentAttendance(String str_cls_session_id, String str_school_id, JSONArray str_attendance_data) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("cls_session_id", str_cls_session_id)
                    .addFormDataPart("school_id", str_school_id)
                    .addFormDataPart("attendance_data", String.valueOf(str_attendance_data))
                    .build();
            app.getApiService().doStudentAttendance(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                app.showToast(message);
                                activity.finish();
                            } else if (status.equals("1")) {
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
            CommonUtil.showBar(activity, ".activity.TrainerStudentAttendanceActivity");
        }

    }

    public void dopresent(String status) {
        int present = Integer.parseInt(tv_present.getText().toString());
        int absent = Integer.parseInt(tv_absent.getText().toString());
        if (status.equals("0")) {
            int i_present = present - 1;
            int i_absent = absent + 1;
            tv_present.setText("" + i_present);
            tv_absent.setText("" + i_absent);

        } else {
            int i_present = present + 1;
            int i_absent = absent - 1;
            tv_present.setText("" + i_present);
            tv_absent.setText("" + i_absent);
        }
    }

    private void setCheckBox(final CheckBox view) {
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (view.isChecked()) {
                    view.setText("Present");
                } else {
                    view.setText("Absent");
                }
            }
        });
    }

    private void swipRefresh(SwipeRefreshLayout view) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                s_Refresh.setRefreshing(false);
                getStudentList(tv_class.getText().toString(), AppPreferences.init(activity).getString("school_id", ""));
            }
        });
    }

    private void doBack(Toolbar toolbar) {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
