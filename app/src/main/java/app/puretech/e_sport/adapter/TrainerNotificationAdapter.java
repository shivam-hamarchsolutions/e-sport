package app.puretech.e_sport.adapter;

import android.app.Activity;
import android.app.Dialog;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import app.puretech.e_sport.R;
import app.puretech.e_sport.model.TrainerNotifactionDTO;

public class TrainerNotificationAdapter extends RecyclerView.Adapter<TrainerNotificationAdapter.ViewHolder> {

    private List<TrainerNotifactionDTO> array_list;
    private Activity activity;

    public TrainerNotificationAdapter(List<TrainerNotifactionDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public TrainerNotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trainer_notification_item, viewGroup, false);

        return new TrainerNotificationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerNotificationAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_notification.setText(array_list.get(position).getStr_notifaction_name());
        viewHolder.tv_date.setText(array_list.get(position).getStr_date());
        viewHolder.tv_time.setText(array_list.get(position).getStr_time());
        viewHolder.tv_grade.setText(array_list.get(position).getStr_grade());
        viewHolder.tv_more.setOnClickListener(v -> {
            Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.notifications_dialog);
            final ImageView iv_close = dialog.findViewById(R.id.iv_close);
            final TextView et_comment = dialog.findViewById(R.id.et_comment);
            et_comment.setText(array_list.get(position).getSir_description());
            iv_close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_notification,tv_grade,tv_date,tv_time,tv_more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_notification = itemView.findViewById(R.id.tv_Trainer_Notification_name);
            tv_grade = itemView.findViewById(R.id.tv_grade);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_more = itemView.findViewById(R.id.tv_more);

        }
    }
}
