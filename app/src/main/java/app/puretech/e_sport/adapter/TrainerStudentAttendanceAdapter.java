package app.puretech.e_sport.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.TrainerStudentAttendanceActivity;
import app.puretech.e_sport.model.TrainerStudentAttendanceDTO;

public class TrainerStudentAttendanceAdapter extends RecyclerView.Adapter<TrainerStudentAttendanceAdapter.ViewHolder> {

    private List<TrainerStudentAttendanceDTO> array_list;
    private Activity activity;

    public TrainerStudentAttendanceAdapter(List<TrainerStudentAttendanceDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public TrainerStudentAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trainer_student_attendance_item, viewGroup, false);

        return new TrainerStudentAttendanceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerStudentAttendanceAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_student_name.setText(array_list.get(position).getStr_student_name());
        viewHolder.tv_sr_no.setText(array_list.get(position).getStr_serial_number());
        viewHolder.cb_check_in.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TrainerStudentAttendanceDTO trainerStudentAttendanceDTO = new TrainerStudentAttendanceDTO();
                if (isChecked) {
                    ((TrainerStudentAttendanceActivity) activity).dopresent("1");
                    viewHolder.tv_status.setText("1");
                    trainerStudentAttendanceDTO.setStr_status("1");
                    trainerStudentAttendanceDTO.setStr_serial_number(array_list.get(position).getStr_serial_number());
                    trainerStudentAttendanceDTO.setStr_student_name(array_list.get(position).getStr_student_name());
                    trainerStudentAttendanceDTO.setStr_id(array_list.get(position).getStr_id());
                    array_list.set(position, trainerStudentAttendanceDTO);

                } else {
                    ((TrainerStudentAttendanceActivity) activity).dopresent("0");
                    viewHolder.tv_status.setText("0");
                    trainerStudentAttendanceDTO.setStr_serial_number(array_list.get(position).getStr_serial_number());
                    trainerStudentAttendanceDTO.setStr_student_name(array_list.get(position).getStr_student_name());
                    trainerStudentAttendanceDTO.setStr_id(array_list.get(position).getStr_id());
                    trainerStudentAttendanceDTO.setStr_status("0");
                    array_list.set(position, trainerStudentAttendanceDTO);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_sr_no, tv_student_name, tv_status;
        private CheckBox cb_check_in;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_student_name = itemView.findViewById(R.id.tv_Trainer_student_Name);
            tv_sr_no = itemView.findViewById(R.id.tv_sn);
            cb_check_in = itemView.findViewById(R.id.cb_check_in);
            tv_status = itemView.findViewById(R.id.tv_status);

        }
    }

    public void addItem(int position, TrainerStudentAttendanceDTO viewModel) {
        array_list.add(position, viewModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        array_list.remove(position);
        notifyItemRemoved(position);
    }
}
