package app.puretech.e_sport.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.SchoolTrainerDailyReportSubActivity;
import app.puretech.e_sport.model.SchoolTrainerDailyReportDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.ImageUtil;
import de.hdodenhof.circleimageview.CircleImageView;

public class SchoolTrainerDailyReportAdapter extends RecyclerView.Adapter<SchoolTrainerDailyReportAdapter.ViewHolder> {

    private List<SchoolTrainerDailyReportDTO> array_list;
    private Activity activity;

    public SchoolTrainerDailyReportAdapter(List<SchoolTrainerDailyReportDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public SchoolTrainerDailyReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.school_trainer_daily_report_item, viewGroup, false);

        return new SchoolTrainerDailyReportAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolTrainerDailyReportAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_Name.setText(array_list.get(position).getName());
        viewHolder.tv_sport_name.setText(array_list.get(position).getPrimary_game());
        ImageUtil.displayImage(viewHolder.cv_image,array_list.get(position).getProfile_photo(),null);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("trainer_id",array_list.get(position).getTrainer_id());
                Intent intent = new Intent(activity, SchoolTrainerDailyReportSubActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Name, tv_sport_name;
        private CircleImageView cv_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_name);
            tv_sport_name = itemView.findViewById(R.id.tv_sport_name);
            cv_image = itemView.findViewById(R.id.cv_image);
        }
    }
}

