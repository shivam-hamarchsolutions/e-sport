package app.puretech.e_sport.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.model.TrainerYearlyPlannerMonthSubDTO;

public class SchoolYearlyPlannerMonthSubAdapter extends RecyclerView.Adapter<SchoolYearlyPlannerMonthSubAdapter.ViewHolder> {

    private List<TrainerYearlyPlannerMonthSubDTO> array_list;
    private Activity activity;

    public SchoolYearlyPlannerMonthSubAdapter(List<TrainerYearlyPlannerMonthSubDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public SchoolYearlyPlannerMonthSubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trainer_yearly_planner_month_sub_item, viewGroup, false);

        return new SchoolYearlyPlannerMonthSubAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolYearlyPlannerMonthSubAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_title.setText(array_list.get(position).getTitle());
        viewHolder.tv_sport_name.setText(array_list.get(position).getStr_sprot_name());
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i_next = new Intent(activity, TrainerYearlyPlannerMonthSubActivity.class);
//                activity.startActivity(i_next);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_sport_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_trainer_yearly_planner_month_sub_title);
            tv_sport_name = itemView.findViewById(R.id.tv_sport_name);

        }
    }
}


