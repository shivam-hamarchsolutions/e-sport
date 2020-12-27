package app.puretech.e_sport.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TrainerLeaveActivity extends AppCompatActivity {

    private App app;
    private Activity activity;
    private Toolbar toolbar;
    private Intent iBack;
    private Dialog dialog;
    private EditText et_subject, et_note, et_name;
    private TextView tv_start_date, tv_end_date;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_trainer_leave);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        toolbar = findViewById(R.id.toolbar_home);
        et_subject = findViewById(R.id.et_subject);
        tv_start_date = findViewById(R.id.tv_start_date);
        tv_start_date.setText(CommonUtil.currentDate(activity));
        tv_end_date = findViewById(R.id.tv_end_date);
        tv_end_date.setText(CommonUtil.currentDate(activity));
        et_note = findViewById(R.id.et_note);
        et_name = findViewById(R.id.et_name);
        btn_submit = findViewById(R.id.btn_submit);
        iBack = new Intent(activity, TrainerHomeActivity.class);
        doBack(toolbar);

        tv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String strDate = format.format(calendar.getTime());
                        tv_start_date.setText(strDate);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });

        tv_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String strDate = format.format(calendar.getTime());
                        tv_end_date.setText(strDate);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_subject.getText().toString().isEmpty()) {
                    et_subject.setError("Subject Required!");
                } else if (et_note.getText().toString().isEmpty()) {
                    et_note.setError("Note Required!");
                } else if (et_name.getText().toString().isEmpty()) {
                    et_name.setError("Name Required!");
                } else {
                    doTrainerLeave(et_subject.getText().toString(), tv_start_date.getText().toString(), tv_end_date.getText().toString(), et_note.getText().toString(), et_name.getText().toString());
                }
            }
        });
    }

    private void doTrainerLeave(String str_subject, String str_start_date, String str_end_date, String str_note, String str_name) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("subject", str_subject)
                    .addFormDataPart("start_date", str_start_date)
                    .addFormDataPart("end_date", str_end_date)
                    .addFormDataPart("description", str_note)
                    .addFormDataPart("sincerely", str_name)
                    .build();
            app.getApiService().doTrainerLeave(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                activity.startActivity(iBack);
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
            CommonUtil.showBar(activity, ".activity.TrainerDailyTimeTableActivity");
        }

    }

    private void doBack(Toolbar toolbar) {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(iBack);
                activity.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        activity.startActivity(iBack);
        activity.finish();
    }
}
