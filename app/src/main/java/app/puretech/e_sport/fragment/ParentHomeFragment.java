package app.puretech.e_sport.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.ParentDailyTimeTableActivity;
import app.puretech.e_sport.activity.ParentHomeActivity;
import app.puretech.e_sport.activity.ParentMonthlyPlannerActivity;
import app.puretech.e_sport.activity.ParentYearlyPlannerActivity;
import app.puretech.e_sport.activity.SchoolMonthlyPlannerActivity;
import app.puretech.e_sport.activity.SchoolYearlyPlannerActivity;
import app.puretech.e_sport.adapter.SelectChildAdapter;
import app.puretech.e_sport.model.ParentDailyTimeTableDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentHomeFragment extends Fragment implements View.OnClickListener {


    private Activity activity;
    private List<ParentDailyTimeTableDTO> array_list = new ArrayList<>();
    private SwipeRefreshLayout s_Refresh;
    private Calendar c;
    private SimpleDateFormat df_daily;
    private String[] fd_daily;
    private TextView tv_trainer_monthly_planner_month;

    public ParentHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_home, container, false);
        doInit(view);
        return view;
    }

    @SuppressLint("SimpleDateFormat")
    private void doInit(View view) {
        activity = getActivity();
        RecyclerView rv_show = view.findViewById(R.id.rv_parent_daily_time_table);
        LinearLayout ll_hide = view.findViewById(R.id.ll_Hide);
        s_Refresh = view.findViewById(R.id.s_refresh);
        RelativeLayout rl_month_back = view.findViewById(R.id.rl_month_back);
        RelativeLayout rl_month_next = view.findViewById(R.id.rl_month_next);
        tv_trainer_monthly_planner_month = view.findViewById(R.id.tv_trainer_monthly_planner_month);
        LinearLayout cv_daily_time_table = view.findViewById(R.id.cv_daily_time_table);
        LinearLayout cv_student_attendance = view.findViewById(R.id.cv_student_attendance);
        LinearLayout cv_gallery = view.findViewById(R.id.cv_parent_gallary);
        LinearLayout cv_yearly_event = view.findViewById(R.id.cv_parent_yearly_event);
        LinearLayout cv_notification = view.findViewById(R.id.cv_notification);

       /* LinearLayout cv_monthly_planner = view.findViewById(R.id.fab_parent_monthly_planner);
        LinearLayout cv_yearly_planner = view.findViewById(R.id.fab_parent_yearly_planner);*/

        LinearLayout cv_achievements = view.findViewById(R.id.cv_achievement);
        rl_month_next.setOnClickListener(this);
        rl_month_back.setOnClickListener(this);
        cv_daily_time_table.setOnClickListener(this);
        cv_student_attendance.setOnClickListener(this);
        cv_gallery.setOnClickListener(this);
      /*  cv_monthly_planner.setOnClickListener(this);
        cv_yearly_planner.setOnClickListener(this);*/

        cv_yearly_event.setOnClickListener(this);
        cv_notification.setOnClickListener(this);
        cv_achievements.setOnClickListener(this);
        //Get current month
        c = Calendar.getInstance();
        df_daily = new SimpleDateFormat("dd MMM yyyy");
        fd_daily = new String[]{df_daily.format(c.getTime())};
        tv_trainer_monthly_planner_month.setText(fd_daily[0]);
        swipRefresh(s_Refresh);

    }

    private void swipRefresh(SwipeRefreshLayout view) {
        view.setOnRefreshListener(() -> s_Refresh.setRefreshing(false));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_month_back:
                c.add(Calendar.DAY_OF_MONTH, -1);
                fd_daily[0] = df_daily.format(c.getTime());
                tv_trainer_monthly_planner_month.setText(fd_daily[0]);
                break;
            case R.id.rl_month_next:
                c.add(Calendar.DAY_OF_MONTH, 1);
                fd_daily[0] = df_daily.format(c.getTime());
                tv_trainer_monthly_planner_month.setText(fd_daily[0]);
                break;
            case R.id.cv_daily_time_table:
                ParentHomeActivity.iDashboard = 1;
                ParentHomeActivity.toolbar.setTitle("Daily Time Table");
                //selectChildDialog(activity, "0");

                Intent i_dailt_time = new Intent(activity, ParentDailyTimeTableActivity.class);
                activity.startActivity(i_dailt_time);
                activity.finish();
                break;
            case R.id.cv_student_attendance:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentStudentAttendanceFragment()).commit();
                ParentHomeActivity.iDashboard = 1;
                ParentHomeActivity.toolbar.setTitle("Student Attendance");
                //  selectChildDialog(activity, "1");
                break;
            case R.id.cv_parent_gallary:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentGalleryFragment()).commit();
                ParentHomeActivity.iDashboard = 1;
                ParentHomeActivity.toolbar.setTitle("Gallery");
                // selectChildDialog(activity, "3");
                break;
          /*  case R.id.fab_parent_monthly_planner:
                Intent i_monthly_planner = new Intent(activity, ParentMonthlyPlannerActivity.class);
                activity.startActivity(i_monthly_planner);
                activity.finish();
                //  Toast.makeText(activity, "monthly planner", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_parent_yearly_planner:
                Intent i_daily_report = new Intent(activity, ParentYearlyPlannerActivity.class);
                activity.startActivity(i_daily_report);
                activity.finish();
                //   Toast.makeText(activity, "yearly planner", Toast.LENGTH_SHORT).show();
                break;*/

            case R.id.cv_parent_yearly_event:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentEventFragment()).commit();
                ParentHomeActivity.iDashboard = 1;
                ParentHomeActivity.toolbar.setTitle("Yearly Event");
                //selectChildDialog(activity, "4");
                break;
            case R.id.cv_notification:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentNotifactionFragment()).commit();
                ParentHomeActivity.iDashboard = 1;
                ParentHomeActivity.toolbar.setTitle("Notification");
                //  selectChildDialog(activity, "5");
                break;
            case R.id.cv_achievement:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentAchievementsFragment()).commit();
                ParentHomeActivity.iDashboard = 1;
                ParentHomeActivity.toolbar.setTitle("Achievements");
                // selectChildDialog(activity, "6");
                break;
        }
    }

    public void selectChildDialog(Activity activity, String status) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.parent_child_dialog);
        final SelectChildAdapter selectChildAdapter;
        final RecyclerView rv_no_of_child = dialog.findViewById(R.id.rv_no_of_child);
        selectChildAdapter = new SelectChildAdapter(ParentHomeActivity.child_array, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_no_of_child.setLayoutManager(mLayoutManager);
        rv_no_of_child.setItemAnimator(new DefaultItemAnimator());
        rv_no_of_child.setAdapter(selectChildAdapter);
        rv_no_of_child.addOnItemTouchListener(
                new RecyclerItemClickListener(activity, rv_no_of_child, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        AppPreferences.init(activity).setString("student_id", ParentHomeActivity.child_array.get(position).getStudent_id());
                        AppPreferences.init(activity).setString("school_id", ParentHomeActivity.child_array.get(position).getSchool_id());
                        AppPreferences.init(activity).setString("class", ParentHomeActivity.child_array.get(position).getStr_class());
                        switch (status) {
                            case "0":
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentDailyTimeTableFragment()).commit();
                                ParentHomeActivity.iDashboard = 1;
                                ParentHomeActivity.toolbar.setTitle("Daily Time Table");
                                dialog.dismiss();
                                break;
                            case "1":
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentStudentAttendanceFragment()).commit();
                                ParentHomeActivity.iDashboard = 1;
                                ParentHomeActivity.toolbar.setTitle("Student Attendance");
                                dialog.dismiss();
                                break;
                            case "3":
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentGalleryFragment()).commit();
                                ParentHomeActivity.iDashboard = 1;
                                ParentHomeActivity.toolbar.setTitle("Gallery");
                                dialog.dismiss();
                                break;
                            case "4":
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentEventFragment()).commit();
                                ParentHomeActivity.iDashboard = 1;
                                ParentHomeActivity.toolbar.setTitle("Yearly Event");
                                dialog.dismiss();
                                break;
                            case "5":
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentNotifactionFragment()).commit();
                                ParentHomeActivity.iDashboard = 1;
                                ParentHomeActivity.toolbar.setTitle("Notification");
                                dialog.dismiss();
                                break;
                            case "6":
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParentAchievementsFragment()).commit();
                                ParentHomeActivity.iDashboard = 1;
                                ParentHomeActivity.toolbar.setTitle("Achievements");
                                dialog.dismiss();
                                break;
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        dialog.show();
    }

}
