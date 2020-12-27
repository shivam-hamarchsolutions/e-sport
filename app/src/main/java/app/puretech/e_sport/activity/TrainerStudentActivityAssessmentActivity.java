package app.puretech.e_sport.activity;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
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
import app.puretech.e_sport.model.AssessmentActivityDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TrainerStudentActivityAssessmentActivity extends AppCompatActivity {

    private App app;
    private Activity activity;
    private Dialog dialog;
    private ArrayList NoofActivity, NameOfActivity;
    private ArrayList<AssessmentActivityDTO> arrayList;
    private PieChart pieChart;
    private Toolbar toolbar;
    private Intent i_back;
    private TextView tv_assessment, tv_term_one, tv_term_two, tv_term_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_trainer_student_activity_assessment);
        doInit();
        getAssessmentActivity(AppPreferences.init(activity).getString("school_id", ""), AppPreferences.init(activity).getString("student_id", ""), AppPreferences.init(activity).getString("term", ""));
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        i_back = new Intent(activity, TrainerStudentAssessmentListActivity.class);
        NoofActivity = new ArrayList();
        NameOfActivity = new ArrayList();
        arrayList = new ArrayList<AssessmentActivityDTO>();
        pieChart = findViewById(R.id.piechart);
        toolbar = findViewById(R.id.toolbar_home);
        tv_assessment = findViewById(R.id.tv_assessment);
        tv_term_one = findViewById(R.id.tv_term_one);
        tv_term_two = findViewById(R.id.tv_term_two);
        tv_term_total = findViewById(R.id.tv_term_total);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(i_back);
                activity.finish();
            }
        });
    }

    private void getAssessmentActivity(String str_school_id, String str_student_id, String str_term) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            NoofActivity.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("school_id", str_school_id)
                    .addFormDataPart("student_id", str_student_id)
                    .addFormDataPart("term", str_term)
                    .build();
            app.getApiService().doAssessmentActivity(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                    AssessmentActivityDTO assessmentActivityDTO = new AssessmentActivityDTO();
                                    assessmentActivityDTO.setStr_name(c.getString("sport_name"));
                                    assessmentActivityDTO.setStr_id(c.getString("activity_id"));
                                    arrayList.add(assessmentActivityDTO);
                                    NoofActivity.add(new Entry(1f, i));
                                    NameOfActivity.add(c.getString("sport_name"));
                                }
                                tv_term_one.setText(jobj.getString("term_1_total"));
                                tv_term_two.setText(jobj.getString("term_2_total"));
                                tv_term_total.setText(jobj.getString("total_term"));
                                tv_assessment.setText("Assessment \n" + jobj.getString("term_1_total"));
                                setPieData();
                            } else if (status.equals("1")) {
                                setPieData();
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

    private void setPieData() {
        PieDataSet dataSet = new PieDataSet(NoofActivity, "");
        PieData data = new PieData(NameOfActivity, dataSet);
        pieChart.setData(data);
        pieChart.setDescription("");
        pieChart.getLegend().setEnabled(false);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(11f);
        data.setDrawValues(false);
        pieChart.animateXY(1000, 1000);
        //do summary details
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @SuppressLint("NewApi")
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                for (int i = 0; i < arrayList.size(); i++) {
                    String str_name = arrayList.get(i).getStr_name();
                    if (str_name.equals(NameOfActivity.get(h.getXIndex()))) {
                        AppPreferences.init(activity).setString("activity_id", arrayList.get(i).getStr_id());
                        Intent i_next = new Intent(getApplicationContext(), TrainerStudentActivityAssessmentBarchartActivity.class);
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
        activity.startActivity(i_back);
        activity.finish();
    }
}
