package app.puretech.e_sport.activity;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.TextView;


import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.SchoolStudentAbsentAdapter;
import app.puretech.e_sport.adapter.SchoolStudentAttendanceListAdapter;
import app.puretech.e_sport.adapter.SessionAdapter;
import app.puretech.e_sport.model.SchoolStudentAbsentDTO;
import app.puretech.e_sport.model.SchoolStudentAttendanceListDTO;
import app.puretech.e_sport.model.SessionDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SchoolStudentAttendanceCalActivity extends AppCompatActivity implements View.OnClickListener {


    private App app;
    private Activity activity;
    private Toolbar toolbar;
    private CalendarView cv_cal;
    private ArrayList<SchoolStudentAttendanceListDTO> itemsArrayList;
    private ArrayList<SchoolStudentAbsentDTO> arrayList;
    private Dialog dialog;
    private GridView gv_absent;

    private List<SessionDTO> session_array = new ArrayList<>();
    private SessionAdapter sessionAdapter;
    private GridView lv_present;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_school_student_attendance_cal);
        doInit();
        getCalData(AppPreferences.init(activity).getString("student_id", ""), CommonUtil.getCurrentMonth());
        gv_absent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getSessiondata(arrayList.get(position).getStudent_id(), arrayList.get(position).getAttendances_data(), "Absent");
            }
        });
        lv_present.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getSessiondata(itemsArrayList.get(position).getStudent_id(), itemsArrayList.get(position).getAttendance_date(), "Present");
            }
        });

    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        itemsArrayList = new ArrayList<>();
        arrayList = new ArrayList<>();// calls function to get items list
        cv_cal = findViewById(R.id.cv_cal);
        gv_absent = findViewById(R.id.gv_list_absent);
        lv_present = findViewById(R.id.gv_list);
        try {
            cv_cal.setDate(new SimpleDateFormat("yyyy-MM").parse(AppPreferences.init(activity).getString("month", "")).getTime(), true, true);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setOnClickListener(this);
    }

    private void getSessiondata(String student_id, String attendance_date, String attendance_type) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            session_array.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("student_id", student_id)
                    .addFormDataPart("attendance_date", attendance_date)
                    .addFormDataPart("attendance_type", attendance_type)
                    .build();
            app.getApiService().getSession(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("list_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    SessionDTO sessionDTO = new SessionDTO();
                                    sessionDTO.setStr_session_id(c.getString("session_id"));
                                    sessionDTO.setStr_sport_name(c.getString("sport_name"));
                                    sessionDTO.setStr_time(c.getString("time"));
                                    session_array.add(sessionDTO);
                                }
                                Dialog dialog = new Dialog(activity);
                                dialog.setContentView(R.layout.session_dialog);
                                final RecyclerView rv_show;
                                final TextView tv_session_title;
                                rv_show = dialog.findViewById(R.id.rv_session);
                                tv_session_title = dialog.findViewById(R.id.tv_session_title);
                                tv_session_title.setText(attendance_type);
                                sessionAdapter = new SessionAdapter(session_array, activity);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                rv_show.setLayoutManager(mLayoutManager);
                                rv_show.setItemAnimator(new DefaultItemAnimator());
                                rv_show.setAdapter(sessionAdapter);
                                dialog.show();
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
            CommonUtil.showBar(activity, ".activity.TrainerDailyTimeTableActivity");
        }
    }


    private void getCalData(String str_trainer_id, String str_month) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            itemsArrayList.clear();
            arrayList.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("student_id", str_trainer_id)
                    .addFormDataPart("month", str_month)
                    .build();
            app.getApiService().getStudentAttendanceCal(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("present_data");
                                JSONArray jsonArray1 = jobj.getJSONArray("absent_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    StringTokenizer st = new StringTokenizer(c.getString("attendance_date"), "-");
                                    String year = st.nextToken();
                                    String month = st.nextToken();
                                    String day = st.nextToken();
                                    SchoolStudentAttendanceListDTO schoolStudentAttendanceListDTO = new SchoolStudentAttendanceListDTO();
                                    schoolStudentAttendanceListDTO.setAttendance_date(c.getString("attendance_date"));
                                    schoolStudentAttendanceListDTO.setDay(day);
                                    schoolStudentAttendanceListDTO.setStudent_id(c.getString("student_id"));
                                    schoolStudentAttendanceListDTO.setAttendance_month(c.getString("attendance_month"));
                                    itemsArrayList.add(schoolStudentAttendanceListDTO);
                                }
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject c = jsonArray1.getJSONObject(i);
                                    StringTokenizer st = new StringTokenizer(c.getString("attendance_date"), "-");
                                    String year = st.nextToken();
                                    String month = st.nextToken();
                                    String day = st.nextToken();
                                    SchoolStudentAbsentDTO absentDTO = new SchoolStudentAbsentDTO();
                                    absentDTO.setAttendances_data(c.getString("attendance_date"));
                                    absentDTO.setDay(day);
                                    absentDTO.setStudent_id(c.getString("student_id"));
                                    absentDTO.setAttendance_month(c.getString("attendance_month"));
                                    absentDTO.setDay(day);
                                    arrayList.add(absentDTO);
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


    private void setAdapter() {
        SchoolStudentAttendanceListAdapter adapter = new SchoolStudentAttendanceListAdapter(this, itemsArrayList);
        SchoolStudentAbsentAdapter absentAdapter = new SchoolStudentAbsentAdapter(this, arrayList);
        gv_absent.setAdapter(absentAdapter);
        lv_present.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(activity, SchoolStudentAttendanceBarActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_home) {
            Intent i = new Intent(activity, SchoolStudentAttendanceBarActivity.class);
            activity.startActivity(i);
            activity.finish();
        }
    }
}
