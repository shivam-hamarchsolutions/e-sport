package app.puretech.e_sport.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.model.AttendanceBarDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SchoolStudentAttendanceBarActivity extends AppCompatActivity implements View.OnClickListener {

    private App app;
    private Activity activity;
    private Toolbar toolbar;
    private ArrayList<AttendanceBarDTO> arrayList;
    private BarChart chart;
    private ArrayList NoOfActivity, NameOfYear;
    private Dialog dialog;
    private TextView tv_avg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_school_student_attendance_bar);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setOnClickListener(this);
        arrayList = new ArrayList<>();
        chart = findViewById(R.id.barchart);
        NoOfActivity = new ArrayList();
        NameOfYear = new ArrayList();
        getSchoolBarData(AppPreferences.init(activity).getString("student_id", ""));
        tv_avg = findViewById(R.id.tv_avg);
    }

    private void getSchoolBarData(String str_trainer_id) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            NoOfActivity.clear();
            NameOfYear.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("student_id", str_trainer_id)
                    .build();
            app.getApiService().getStudentAttendanceBar(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    AttendanceBarDTO assessmentActivityDTO = new AttendanceBarDTO();
                                    assessmentActivityDTO.setStr_month(c.getString("month"));
                                    assessmentActivityDTO.setStr_avg_attendance(c.getString("avg_attendance"));
                                    arrayList.add(assessmentActivityDTO);
                                    Float avg = Float.valueOf(c.getString("avg_attendance"));
                                    NoOfActivity.add(new BarEntry(avg, i));
                                    NameOfYear.add(c.getString("month"));
                                }
                                tv_avg.setText(jobj.getString("total_avg"));
                                setBarChart();
                            } else if (status.equals("1")) {
                                setBarChart();
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
            CommonUtil.showBar(activity, ".activity.TrainerDailyTimeTableActivity");
        }
    }


    private void setBarChart() {
        BarDataSet bardataset = new BarDataSet(NoOfActivity, "");
        chart.animateY(1000);
        BarData data = new BarData(NameOfYear, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);
        bardataset.setColors(new int[]{R.color.appDarkOrange}, getApplicationContext());
        bardataset.setBarShadowColor(R.color.appTextColor);
        bardataset.setValueTextSize(14);
        bardataset.setValueTextColor(Color.BLACK);
        data.setValueFormatter(new PercentFormatter());
        chart.setData(data);
        bardataset.setBarSpacePercent(50);
        chart.setDrawBorders(true);
        chart.setClickable(false);
        chart.setDrawBorders(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(true);
        chart.setDescription("");
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawLimitLinesBehindData(false);
        chart.getXAxis().setGridLineWidth(2);
        chart.setScaleEnabled(false);
        chart.setDrawHighlightArrow(false);
        chart.animateY(1000);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getAxisRight().setDrawTopYLabelEntry(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getXAxis().removeAllLimitLines();
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(8);
        // xAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        RoundedBarChartRenderer roundedBarChartRenderer = new RoundedBarChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler());
//        roundedBarChartRenderer.setmRadius(25f);
//        chart.setRenderer(roundedBarChartRenderer);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                for (int i = 0; i < arrayList.size(); i++) {
                    String str_month = arrayList.get(i).getStr_month();
                    if (str_month.equals(NameOfYear.get(h.getXIndex()))) {
//                        AppPreferences.init(activity).setString("school_id", arrayList.get(i).getStr_trainer_id());
                        AppPreferences.init(activity).setString("month", arrayList.get(i).getStr_month());
                        Intent i_next = new Intent(getApplicationContext(), SchoolStudentAttendanceCalActivity.class);
                        activity.startActivity(i_next);
                        activity.finish();
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(activity, SchoolStudentAttendanceActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_home:
                Intent i = new Intent(activity, SchoolStudentAttendanceActivity.class);
                activity.startActivity(i);
                activity.finish();
                break;
        }
    }
}