package app.puretech.e_sport.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.TrainerStudentAttendanceBarActivity;
import app.puretech.e_sport.model.SchoolStudentListDTO;
import app.puretech.e_sport.utill.AppPreferences;

public class TrainerStudentListAdapter extends RecyclerView.Adapter<TrainerStudentListAdapter.ViewHolder> {

    private List<SchoolStudentListDTO> array_list;
    private Activity activity;

    public TrainerStudentListAdapter(List<SchoolStudentListDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public TrainerStudentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.school_student_list_item, viewGroup, false);

        return new TrainerStudentListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerStudentListAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_Name.setText(array_list.get(position).getStr_name());
        viewHolder.tv_class.setText(array_list.get(position).getStr_grade());
        viewHolder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("student_id",array_list.get(position).getStr_student_id());
                Intent i_view = new Intent(activity, TrainerStudentAttendanceBarActivity.class);
                activity.startActivity(i_view);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Name,tv_class;
        private Button btn_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_school_list_title);
            btn_view = itemView.findViewById(R.id.btn_view);
            tv_class = itemView.findViewById(R.id.tv_class);

        }
    }
}

