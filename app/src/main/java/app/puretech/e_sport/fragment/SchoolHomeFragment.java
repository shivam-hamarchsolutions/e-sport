package app.puretech.e_sport.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.SchoolAchievementsActivity;
import app.puretech.e_sport.activity.SchoolDailyTimeTableActivity;
import app.puretech.e_sport.activity.SchoolMonthlyPlannerActivity;
import app.puretech.e_sport.activity.SchoolMonthlyPlannerTrainerListActivity;
import app.puretech.e_sport.activity.SchoolNotificationActivity;
import app.puretech.e_sport.activity.SchoolStudentAttendanceActivity;
import app.puretech.e_sport.activity.SchoolTrainerDailyReportActivity;
import app.puretech.e_sport.activity.SchoolTrainerDailyTimeTableActivity;
import app.puretech.e_sport.activity.SchoolYearlyPlannerActivity;
import app.puretech.e_sport.activity.SchoolYearlyPlannerTrainerListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolHomeFragment extends Fragment implements View.OnClickListener {


    private View view;
    private App app;
    private Activity activity;
    private LinearLayout fab_daily_time_table, fab_monthly_planner, fab_trainer_yearly_planner, fab_student_attendance, fab_notifications, fab_achievements;

    public SchoolHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_school_home, container, false);
        viewLoad(view);
        userActionLoad();
        return view;
    }

    private void viewLoad(View view) {
        app = (App) getActivity().getApplication();
        activity = getActivity();
        fab_daily_time_table = view.findViewById(R.id.fab_school_daily_time_table);
        fab_monthly_planner = view.findViewById(R.id.fab_school_monthly_planner);
        fab_trainer_yearly_planner = view.findViewById(R.id.fab_trainer_yearly_planner);
        fab_student_attendance = view.findViewById(R.id.fab_school_student_attendance);
        fab_notifications = view.findViewById(R.id.fab_school_notifications);
        fab_achievements = view.findViewById(R.id.fab_school_Achievements);
    }

    private void userActionLoad() {
        fab_daily_time_table.setOnClickListener(this);
        fab_monthly_planner.setOnClickListener(this);
        fab_trainer_yearly_planner.setOnClickListener(this);
        fab_student_attendance.setOnClickListener(this);
        fab_notifications.setOnClickListener(this);
        fab_achievements.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_school_daily_time_table:
                Intent i_school_daily_time_table = new Intent(activity, SchoolTrainerDailyTimeTableActivity.class);
                activity.startActivity(i_school_daily_time_table);
                activity.finish();
                break;
            case R.id.fab_school_monthly_planner:
                Intent i_monthly_planner = new Intent(activity, SchoolMonthlyPlannerTrainerListActivity.class);
                activity.startActivity(i_monthly_planner);
                activity.finish();
                break;
            case R.id.fab_trainer_yearly_planner:
                Intent i_daily_report = new Intent(activity, SchoolYearlyPlannerTrainerListActivity.class);
                activity.startActivity(i_daily_report);
                activity.finish();
                break;
            case R.id.fab_school_student_attendance:
                Intent i_student_attendance = new Intent(activity, SchoolStudentAttendanceActivity.class);
                activity.startActivity(i_student_attendance);
                activity.finish();
                break;
            case R.id.fab_school_notifications:
                Intent i_notification = new Intent(activity, SchoolNotificationActivity.class);
                activity.startActivity(i_notification);
                activity.finish();
                break;
            case R.id.fab_school_Achievements:
                Intent intent = new Intent(activity, SchoolAchievementsActivity.class);
                activity.startActivity(intent);
                activity.finish();
                break;
        }
    }
}
