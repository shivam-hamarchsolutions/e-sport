package app.puretech.e_sport.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.SchoolYearlyPlannerMonthSubActivity;
import app.puretech.e_sport.activity.TrainerYearlyPlannerMonthSubActivity;
import app.puretech.e_sport.model.TrainerYearlyPlannerMonthDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;

public class SchoolYearlyPlannerMonthAdapter extends RecyclerView.Adapter<SchoolYearlyPlannerMonthAdapter.ViewHolder> {

    private List<TrainerYearlyPlannerMonthDTO> array_list;
    private Activity activity;

    public SchoolYearlyPlannerMonthAdapter(List<TrainerYearlyPlannerMonthDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public SchoolYearlyPlannerMonthAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trainer_yearly_planner_month_item, viewGroup, false);

        return new SchoolYearlyPlannerMonthAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolYearlyPlannerMonthAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_title.setText(CommonUtil.convertDate(array_list.get(position).getTitle(),"M","MMM"));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("month",array_list.get(position).getTitle());
                Intent i_next = new Intent(activity, SchoolYearlyPlannerMonthSubActivity.class);
                activity.startActivity(i_next);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_trainer_yearly_planner_month_title);

        }
    }
}
