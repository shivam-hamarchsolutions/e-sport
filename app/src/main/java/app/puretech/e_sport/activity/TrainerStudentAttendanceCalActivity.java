package app.puretech.e_sport.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.util.Map;
import java.util.StringTokenizer;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.SchoolStudentAbsentAdapter;
import app.puretech.e_sport.adapter.SchoolStudentAttendanceListAdapter;
import app.puretech.e_sport.model.SchoolStudentAbsentDTO;
import app.puretech.e_sport.model.SchoolStudentAttendanceListDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TrainerStudentAttendanceCalActivity extends AppCompatActivity implements View.OnClickListener {
    private App app;
    private Activity activity;
    private Toolbar toolbar;
    private CalendarView cv_cal;
    private ArrayList<SchoolStudentAttendanceListDTO> itemsArrayList;
    private ArrayList<SchoolStudentAbsentDTO> arrayList;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_student_attendance_cal);
        doInit();
        getCalData(AppPreferences.init(activity).getString("trainer_id", ""), AppPreferences.init(activity).getString("month", ""));

    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        itemsArrayList = new ArrayList<>();
        arrayList = new ArrayList<>();// calls function to get items list
        cv_cal = findViewById(R.id.cv_cal);
        try {
            cv_cal.setDate(new SimpleDateFormat("yyyy-MM").parse(AppPreferences.init(activity).getString("month", "")).getTime(), true, true);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setOnClickListener(this);
    }

    private void getCalData(String str_trainer_id, String str_month) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            itemsArrayList.clear();
            arrayList.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("trainer_id", str_trainer_id)
                    .addFormDataPart("month", str_month)
                    .build();
            app.getApiService().getTrainerAttendanceCal(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("attendance_data");
                                JSONArray jsonArray1 = jobj.getJSONArray("leave_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    StringTokenizer st = new StringTokenizer(c.getString("attendance_date"), "-");
                                    String year = st.nextToken();
                                    String month = st.nextToken();
                                    String day = st.nextToken();
                                    SchoolStudentAttendanceListDTO schoolStudentAttendanceListDTO = new SchoolStudentAttendanceListDTO();
                                    schoolStudentAttendanceListDTO.setTitle(c.getString("attendance_date"));
                                    schoolStudentAttendanceListDTO.setDay(day);
                                    itemsArrayList.add(schoolStudentAttendanceListDTO);
                                }
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject c = jsonArray1.getJSONObject(i);
                                    StringTokenizer st = new StringTokenizer(c.getString("leave_date"), "-");
                                    String year = st.nextToken();
                                    String month = st.nextToken();
                                    String day = st.nextToken();
                                    SchoolStudentAbsentDTO absentDTO = new SchoolStudentAbsentDTO();
                                    absentDTO.setTitle(c.getString("leave_date"));
                                    absentDTO.setDescription(c.getString("description"));
                                    absentDTO.setSubject(c.getString("subject"));
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
        GridView itemsListView = findViewById(R.id.gv_list);
        GridView gridView = findViewById(R.id.gv_list_absent);
        gridView.setAdapter(absentAdapter);
        itemsListView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.attendance_dialog);
                final TextView tv_subject,tv_date,tv_note;
                tv_subject = dialog.findViewById(R.id.tv_subject);
                tv_date =dialog.findViewById(R.id.tv_date);
                tv_note = dialog.findViewById(R.id.tv_note);
                tv_subject.setText(arrayList.get(position).getSubject());
                tv_date.setText(arrayList.get(position).getTitle());
                tv_note.setText(arrayList.get(position).getDescription());
                dialog.show();
            }
        });
        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.remark_dialog);
                final TextView tv_subject,tv_date,tv_note;
                tv_subject = dialog.findViewById(R.id.tv_subject);
                tv_date =dialog.findViewById(R.id.tv_date);
                tv_note = dialog.findViewById(R.id.tv_note);

                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(activity, TrainerStudentAttendanceBarActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_home:
                Intent i = new Intent(activity, TrainerStudentAttendanceBarActivity.class);
                activity.startActivity(i);
                activity.finish();
                break;
        }
    }
}
