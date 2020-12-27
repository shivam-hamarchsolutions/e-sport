package app.puretech.e_sport.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.model.TrainerPostSchoolActivityDTO;

public class TrainerPostSchoolActivityAdapter extends RecyclerView.Adapter<TrainerPostSchoolActivityAdapter.ViewHolder> {

    private List<TrainerPostSchoolActivityDTO> array_list;
    private Activity activity;

    public TrainerPostSchoolActivityAdapter(List<TrainerPostSchoolActivityDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public TrainerPostSchoolActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trainer_post_school_activity_item, viewGroup, false);

        return new TrainerPostSchoolActivityAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerPostSchoolActivityAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_grade.setText(array_list.get(position).getStr_grade());
        viewHolder.tv_sport_name.setText(array_list.get(position).getStr_sport_name());
        viewHolder.tv_sessions.setText(array_list.get(position).getStr_sessions());
        viewHolder.tv_start_Date.setText(array_list.get(position).getStr_start_date());
        viewHolder.tv_end_date.setText(array_list.get(position).getStr_end_date());
        viewHolder.tv_days.setText(array_list.get(position).getStr_days());
        viewHolder.tv_time.setText(array_list.get(position).getStr_time());
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_grade, tv_sport_name, tv_sessions, tv_time, tv_start_Date, tv_end_date, tv_days;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_grade = itemView.findViewById(R.id.tv_trainer_post_activity_grade);
            tv_sport_name = itemView.findViewById(R.id.tv_sport_name);
            tv_sessions = itemView.findViewById(R.id.tv_session);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_start_Date = itemView.findViewById(R.id.tv_start_date);
            tv_end_date = itemView.findViewById(R.id.tv_end_date);
            tv_days = itemView.findViewById(R.id.tv_days);

        }
    }
}
