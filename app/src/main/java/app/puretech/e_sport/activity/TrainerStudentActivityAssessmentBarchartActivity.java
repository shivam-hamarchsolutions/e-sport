package app.puretech.e_sport.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

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

public class TrainerStudentActivityAssessmentBarchartActivity extends AppCompatActivity {

    private App app;
    private Activity activity;
    private Toolbar toolbar;
    private ArrayList NoofActivity, NameOfActivity;
    private BarChart chart;
    private Intent i_back;
    private Dialog dialog;
    private TextView tv_assessment, tv_name, tv_grade, tv_school_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_trainer_student_assessment_barchart);
        doInit();
        getAssessmentBarchart(AppPreferences.init(activity).getString("activity_id", ""), AppPreferences.init(activity).getString("term", ""));

    }


    @SuppressLint("SetTextI18n")
    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        toolbar = findViewById(R.id.toolbar_home);
        chart = findViewById(R.id.barchart);
        tv_assessment = findViewById(R.id.tv_assessment);
        tv_name = findViewById(R.id.tv_name);
        tv_grade = findViewById(R.id.tv_grade);
        tv_school_name = findViewById(R.id.tv_school_name);
        tv_name.setText("Name : " + AppPreferences.init(activity).getString("student_name", ""));
        tv_grade.setText("Grade : " + AppPreferences.init(activity).getString("class_name", "") + " " + AppPreferences.init(activity).getString("division_name", ""));
        tv_school_name.setText("School : " + AppPreferences.init(activity).getString("school_name", ""));

        NoofActivity = new ArrayList();
        NameOfActivity = new ArrayList();
        i_back = new Intent(activity, TrainerStudentActivityAssessmentActivity.class);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(i_back);
                activity.finish();
            }
        });
    }

    private void getAssessmentBarchart(String str_activity_id, String str_term) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            NoofActivity.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("activity_id", str_activity_id)
                    .addFormDataPart("term", str_term)
                    .build();
            app.getApiService().doAssessmentBarchart(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("assessment_activity");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    Float marks = Float.valueOf(c.getString("marks"));
                                    NoofActivity.add(new BarEntry(marks, i));
                                    NameOfActivity.add(c.getString("session_name"));
                                    //  NameOfActivity.add("fitness");

                                }
                                tv_assessment.setText(jobj.getString("term") + " : " + jobj.getString("term_total"));
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
                    dialog.dismiss();
                    Toast.makeText(app, "Something went wrong,please try again!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            CommonUtil.showBar(activity, ".activity.TrainerDailyTimeTableActivity");
        }
    }

    private void setBarChart() {
        Log.d("ccc", "setBarChart: " + NoofActivity.size());
        Log.d("ccc", "setBarChart: " + NameOfActivity.size());


        BarDataSet bardataset = new BarDataSet(NoofActivity, "");
        chart.animateY(1000);
        BarData data = new BarData(NameOfActivity, bardataset);

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);
        bardataset.setColors(new int[]{R.color.appDarkOrange}, getApplicationContext());
        bardataset.setBarShadowColor(R.color.red);
        bardataset.setValueTextSize(14);
        bardataset.setValueTextColor(Color.BLACK);
        bardataset.setValueTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        chart.setData(data);
        bardataset.setBarSpacePercent(50);
        chart.setDrawBorders(true);
        chart.setClickable(false);
        chart.setDrawBorders(false);
        // chart.setTouchEnabled(false);
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
        //chart.getXAxis().removeAllLimitLines();
        chart.getXAxis().setLabelRotationAngle(-30);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setSpaceBetweenLabels(1);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);

        // xAxis.setLabelRotationAngle(20);
        xAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

//        RoundedBarChartRenderer roundedBarChartRenderer = new RoundedBarChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler());
//        roundedBarChartRenderer.setmRadius(25f);
//        chart.setRenderer(roundedBarChartRenderer);
    }

    @Override
    public void onBackPressed() {
        activity.startActivity(i_back);
        activity.finish();
    }
}
