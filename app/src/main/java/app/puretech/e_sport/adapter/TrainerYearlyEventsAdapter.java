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
import app.puretech.e_sport.model.TrainerYearlyEventsDTO;

public class TrainerYearlyEventsAdapter extends RecyclerView.Adapter<TrainerYearlyEventsAdapter.ViewHolder> {

    private List<TrainerYearlyEventsDTO> array_list;
    private Activity activity;

    public TrainerYearlyEventsAdapter(List<TrainerYearlyEventsDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TrainerYearlyEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trainer_yearly_events_item, viewGroup, false);
        return new TrainerYearlyEventsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerYearlyEventsAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_date.setText(array_list.get(position).getStr_date());
        viewHolder.tv_sub_event_name.setText(array_list.get(position).getStr_sub_event_name());
        viewHolder.tv_session.setText(array_list.get(position).getStr_session());
        viewHolder.tv_grade.setText(array_list.get(position).getStr_grade());
        viewHolder.tv_day.setText("Grade:"+array_list.get(position).getStr_day());
        viewHolder.tv_time.setText(array_list.get(position).getStr_timing());
        viewHolder.tv_venue.setText(array_list.get(position).getStr_venue());
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_date, tv_sub_event_name, tv_session, tv_grade, tv_day, tv_venue, tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_trainer_yearly_event_date);
            tv_sub_event_name = itemView.findViewById(R.id.tv_sub_event_name);
            tv_session = itemView.findViewById(R.id.tv_session);
            tv_grade = itemView.findViewById(R.id.tv_grade);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_venue = itemView.findViewById(R.id.tv_venue);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
