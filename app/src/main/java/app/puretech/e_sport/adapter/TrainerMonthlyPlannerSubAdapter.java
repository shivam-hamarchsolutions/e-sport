package app.puretech.e_sport.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.model.TrainerMonthlyPlannerSubDTO;
import app.puretech.e_sport.utill.ImageUtil;

public class TrainerMonthlyPlannerSubAdapter extends RecyclerView.Adapter<TrainerMonthlyPlannerSubAdapter.ViewHolder> {

    private List<TrainerMonthlyPlannerSubDTO> array_list;
    private Activity activity;

    public TrainerMonthlyPlannerSubAdapter(List<TrainerMonthlyPlannerSubDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public TrainerMonthlyPlannerSubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trainer_monthly_planer_sub_item, viewGroup, false);

        return new TrainerMonthlyPlannerSubAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerMonthlyPlannerSubAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_title.setText(array_list.get(position).getTitle());
        viewHolder.tv_sport_name.setText(array_list.get(position).getStr_sport_name());
        ImageUtil.displayImage(viewHolder.cv_image,array_list.get(position).getStr_image(),null);

    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_sport_name;
        private ImageView cv_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_trainer_monthly_planner_sub_title);
            cv_image = itemView.findViewById(R.id.iv_image);
            tv_sport_name = itemView.findViewById(R.id.tv_sport_name);

        }
    }
}
