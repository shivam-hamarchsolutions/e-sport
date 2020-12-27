package app.puretech.e_sport.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.ParentStudentAttendanceCalActivity;
import app.puretech.e_sport.model.AttendanceBarDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentStudentAttendanceFragment extends Fragment {

    View view;
    private App app;
    private Activity activity;
    private ArrayList<AttendanceBarDTO> arrayList;
    private BarChart chart;
    private ArrayList NoOfActivity, NameOfYear;
    private Dialog dialog;
    private TextView tv_avg;

    public ParentStudentAttendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_student_attendance, container, false);
        app = (App) getActivity().getApplication();
        activity = getActivity();
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        arrayList = new ArrayList<>();
        chart = view.findViewById(R.id.barchart);
        NoOfActivity = new ArrayList();
        NameOfYear = new ArrayList();
        tv_avg = view.findViewById(R.id.tv_avg);
        getSchoolBarData();
        return view;
    }
    private void getSchoolBarData() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            NoOfActivity.clear();
            NameOfYear.clear();
            dialog.show();
//            RequestBody requestBody = new MultipartBuilder()
//                    .type(MultipartBuilder.FORM)
//                    .addFormDataPart("student_id", AppPreferences.init(activity).getString("student_id", ""))
//                    .addFormDataPart("school_id", AppPreferences.init(activity).getString("school_id", ""))
//                    .addFormDataPart("class", AppPreferences.init(activity).getString("class", ""))
//                    .build();
            app.getApiService().getParentStudentAttendanceBar().enqueue(new Callback<Map<String, Object>>() {
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
                    dialog.dismiss();
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
        bardataset.setColors(new int[]{R.color.appDarkOrange}, getActivity());
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
                        Intent i_next = new Intent(getActivity(), ParentStudentAttendanceCalActivity.class);
                        activity.startActivity(i_next);
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }


}
