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
import app.puretech.e_sport.activity.SchoolTrainerAttendanceBarActivity;
import app.puretech.e_sport.model.SchoolTrainerAttendanceDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.ImageUtil;
import de.hdodenhof.circleimageview.CircleImageView;

public class SchoolTrainerAttendanceAdapter extends RecyclerView.Adapter<SchoolTrainerAttendanceAdapter.ViewHolder> {

    private List<SchoolTrainerAttendanceDTO> array_list;
    private Activity activity;

    public SchoolTrainerAttendanceAdapter(List<SchoolTrainerAttendanceDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public SchoolTrainerAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.school_trainer_attendance_item, viewGroup, false);

        return new SchoolTrainerAttendanceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolTrainerAttendanceAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_Name.setText(array_list.get(position).getStr_name());
        viewHolder.tv_Sport_name.setText(array_list.get(position).getStr_primary_game());
        ImageUtil.displayImage(viewHolder.cv_image,array_list.get(position).getStr_profile_photo(),null);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("trainer_id",array_list.get(position).getStr_trainer_id());
                Intent i_next = new Intent(activity, SchoolTrainerAttendanceBarActivity.class);
                activity.startActivity(i_next);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Name, tv_Sport_name;
        private CircleImageView cv_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_name);
            tv_Sport_name = itemView.findViewById(R.id.tv_sport_name);
            cv_image = itemView.findViewById(R.id.cv_image);
        }
    }
}

