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
import app.puretech.e_sport.model.SchoolTrainerDailyReportSubDTO;
import app.puretech.e_sport.utill.ImageUtil;

public class SchoolTrainerDailyReportSubAdapter extends RecyclerView.Adapter<SchoolTrainerDailyReportSubAdapter.ViewHolder> {

    private List<SchoolTrainerDailyReportSubDTO> array_list;
    private Activity activity;

    public SchoolTrainerDailyReportSubAdapter(List<SchoolTrainerDailyReportSubDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SchoolTrainerDailyReportSubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.school_trainer_daily_report_sub_item, viewGroup, false);
        return new SchoolTrainerDailyReportSubAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolTrainerDailyReportSubAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_Name.setText(array_list.get(position).getStr_school_name());
        viewHolder.tv_time.setText(array_list.get(position).getStr_time());
        viewHolder.tv_class.setText(array_list.get(position).getStr_class());
        viewHolder.tv_session.setText(array_list.get(position).getStr_session_name());
        viewHolder.tv_subject.setText(array_list.get(position).getStr_day());
        if (!array_list.get(position).getStr_attendance_status().equals("0")){
            viewHolder.tv_attendance.setVisibility(View.VISIBLE);
            viewHolder.tv_attendance.setText("Attendance : ["+array_list.get(position).getPresentStu()+" / "+array_list.get(position).getToatlStu()+"]");
        }else {
            viewHolder.tv_attendance.setVisibility(View.GONE);
        }
        ImageUtil.displayImage(viewHolder.iv_image,array_list.get(position).getStr_session_pic(),null);
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Name,tv_time,tv_class,tv_session,tv_attendance,tv_subject;
        private ImageView iv_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_school_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_class = itemView.findViewById(R.id.tv_class);
            tv_session = itemView.findViewById(R.id.tv_session);
            tv_attendance = itemView.findViewById(R.id.tv_attendance);
            iv_image = itemView.findViewById(R.id.iv_image);
            tv_subject = itemView.findViewById(R.id.tv_subject);
        }
    }
}

