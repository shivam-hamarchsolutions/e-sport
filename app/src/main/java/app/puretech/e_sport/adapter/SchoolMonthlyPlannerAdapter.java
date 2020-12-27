package app.puretech.e_sport.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.SchoolMonthlyPlannerSubActivity;
import app.puretech.e_sport.model.SchoolMonthlyPlannerDTO;
import app.puretech.e_sport.utill.AppPreferences;

public class SchoolMonthlyPlannerAdapter extends BaseAdapter {

    private List<SchoolMonthlyPlannerDTO> array_list;
    private Activity activity;

    public SchoolMonthlyPlannerAdapter(List<SchoolMonthlyPlannerDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return array_list.size();
    }

    @Override
    public Object getItem(int position) {
        return array_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.school_monthly_planner_item, parent, false);
        }
        //do init
        TextView tv_title = convertView.findViewById(R.id.tv_trainer_monthly_planner_title);

        //set init
        tv_title.setText(array_list.get(position).getTitle());

        //do action
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("class",array_list.get(position).getTitle());
                Intent i_next = new Intent(activity, SchoolMonthlyPlannerSubActivity.class);
                activity.startActivity(i_next);
            }
        });
        return convertView;
    }
}
