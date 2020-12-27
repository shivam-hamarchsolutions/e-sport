package app.puretech.e_sport.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.SchoolStudentActivityAssessmentActivity;
import app.puretech.e_sport.activity.TrainerStudentActivityAssessmentActivity;
import app.puretech.e_sport.model.TrainerSchoolAssessmentListDTO;
import app.puretech.e_sport.utill.AppPreferences;

public class SchoolStudentAssessmentLlistAdapter extends RecyclerView.Adapter<SchoolStudentAssessmentLlistAdapter.ViewHolder> {

    private List<TrainerSchoolAssessmentListDTO> array_list;
    private Activity activity;

    public SchoolStudentAssessmentLlistAdapter(List<TrainerSchoolAssessmentListDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public SchoolStudentAssessmentLlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trainer_student_assessment_list_item, viewGroup, false);

        return new SchoolStudentAssessmentLlistAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolStudentAssessmentLlistAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_name.setText(array_list.get(position).getStr_name());
        viewHolder.tv_grad.setText(array_list.get(position).getStr_grade());
        viewHolder.tv_view_assessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("school_id",array_list.get(position).getStr_school_id());
                AppPreferences.init(activity).setString("student_id",array_list.get(position).getStr_student_id());
                AppPreferences.init(activity).setString("student_name",array_list.get(position).getStr_name());
                Intent i_next = new Intent(activity, SchoolStudentActivityAssessmentActivity.class);
                activity.startActivity(i_next);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name,tv_grad,tv_view_assessment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_grad = itemView.findViewById(R.id.tv_grade);
            tv_view_assessment = itemView.findViewById(R.id.tv_view_assessment);
        }
    }
}

