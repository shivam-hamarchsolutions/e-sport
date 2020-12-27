package app.puretech.e_sport.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.TrainerAchievementsActivity;
import app.puretech.e_sport.activity.TrainerDailyTimeTableActivity;
import app.puretech.e_sport.activity.TrainerMonthlyPlannerActivity;
import app.puretech.e_sport.activity.TrainerNotificationActivity;
import app.puretech.e_sport.activity.TrainerYearlyEventsActivity;
import app.puretech.e_sport.activity.TrainerYearlyPlannerActivity;
import app.puretech.e_sport.utill.CommonUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainerHomeFragment extends Fragment {


    App app;
    View view;
    Activity activity;
    private TextView tv_user_name;
    private LinearLayout fab_daily_time_table, fab_monthly_planner, fab_yearly_planner, fab_yearly_events, fab_notifications, fab_achievements;

    public TrainerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_trainer_home, container, false);
        app = (App) getActivity().getApplication();
        activity = getActivity();
        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_user_name.setVisibility(View.GONE);
        //tv_user_name.setText("Welcome "+AppPreferences.init(activity).getString("user_name",""));

        fab_daily_time_table = view.findViewById(R.id.fab_trainer_daily_time_table);
        fab_yearly_planner = view.findViewById(R.id.fab_trainer_yearly_planner);
        fab_monthly_planner = view.findViewById(R.id.fab_trainer_monthly_planner);
        fab_yearly_events = view.findViewById(R.id.fab_trainer_yearly_events);
        fab_notifications = view.findViewById(R.id.fab_trainer_notifications);
        fab_achievements = view.findViewById(R.id.fab_trainer_Achievements);
        //Load view action
        doDailyTimeTable(fab_daily_time_table);
        doYearlyEvents(fab_yearly_events);
        doNotifications(fab_notifications);
        doAchievements(fab_achievements);
        doMonthlyPlanner(fab_monthly_planner);
        doYearlyPlanner(fab_yearly_planner);
        return view;
    }

    private void doDailyTimeTable(LinearLayout fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isNetworkAvailable(activity)) {
                    Intent i_daily_time_table = new Intent(activity, TrainerDailyTimeTableActivity.class);
                    activity.startActivity(i_daily_time_table);
                    activity.finish();
                } else {
                    app.showToast("No internet connection !");
                }
            }
        });
    }

    private void doMonthlyPlanner(LinearLayout fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isNetworkAvailable(activity)) {
                    Intent i_daily_time_table = new Intent(activity, TrainerMonthlyPlannerActivity.class);
                    activity.startActivity(i_daily_time_table);
                    activity.finish();
                } else {
                    app.showToast("No internet connection !");
                }
            }
        });
    }

    private void doYearlyPlanner(LinearLayout fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isNetworkAvailable(activity)) {
                    Intent i_daily_time_table = new Intent(activity, TrainerYearlyPlannerActivity.class);
                    activity.startActivity(i_daily_time_table);
                    activity.finish();
                } else {
                    app.showToast("No internet connection !");
                }
            }
        });
    }

    private void doYearlyEvents(LinearLayout fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isNetworkAvailable(activity)) {
                    Intent i_yearly_event = new Intent(activity, TrainerYearlyEventsActivity.class);
                    activity.startActivity(i_yearly_event);
                    activity.finish();
                } else {
                    app.showToast("No internet connection !");
                }
            }
        });
    }

    private void doNotifications(LinearLayout fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isNetworkAvailable(activity)) {
                    Intent i_notifications = new Intent(activity, TrainerNotificationActivity.class);
                    activity.startActivity(i_notifications);
                    activity.finish();
                } else {
                    app.showToast("No internet connection !");
                }
            }
        });
    }

    private void doAchievements(LinearLayout fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isNetworkAvailable(activity)) {
                    Intent i_achievements = new Intent(activity, TrainerAchievementsActivity.class);
                    activity.startActivity(i_achievements);
                    activity.finish();
                } else {
                    app.showToast("No internet connection !");
                }
            }
        });
    }
}
