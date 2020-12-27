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
import app.puretech.e_sport.model.ParentNotificationDTO;

public class ParentNotifactionAdapter extends RecyclerView.Adapter<ParentNotifactionAdapter.ViewHolder> {

    private List<ParentNotificationDTO> array_list;
    private Activity activity;

    public ParentNotifactionAdapter(List<ParentNotificationDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ParentNotifactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.parent_notifaction_item, viewGroup, false);

        return new ParentNotifactionAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentNotifactionAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_notification.setText(array_list.get(position).getStr_notifaction_name());
        viewHolder.tv_time.setText(array_list.get(position).getStr_time());
        viewHolder.tv_date.setText(array_list.get(position).getStr_date());
        viewHolder.tv_grade.setText(array_list.get(position).getStr_grade());    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_notification,tv_grade,tv_date,tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_notification = itemView.findViewById(R.id.tv_Trainer_Notification_name);
            tv_grade = itemView.findViewById(R.id.tv_grade);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
