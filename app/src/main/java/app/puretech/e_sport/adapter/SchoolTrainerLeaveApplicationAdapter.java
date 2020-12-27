package app.puretech.e_sport.adapter;

import android.app.Activity;
import android.app.Dialog;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.model.SchoolTrainerLeaveApplicationDTO;
import app.puretech.e_sport.utill.ImageUtil;
import de.hdodenhof.circleimageview.CircleImageView;

public class SchoolTrainerLeaveApplicationAdapter extends RecyclerView.Adapter<SchoolTrainerLeaveApplicationAdapter.ViewHolder> {

    private List<SchoolTrainerLeaveApplicationDTO> array_list;
    private Activity activity;

    public SchoolTrainerLeaveApplicationAdapter(List<SchoolTrainerLeaveApplicationDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public SchoolTrainerLeaveApplicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.school_trainer_leave_application_item, viewGroup, false);

        return new SchoolTrainerLeaveApplicationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolTrainerLeaveApplicationAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_Name.setText(array_list.get(position).getStr_name());
        viewHolder.tv_sport_name.setText(array_list.get(position).getStr_sport_name());
        viewHolder.tv_subject.setText(array_list.get(position).getStr_subject());
        viewHolder.tv_start_date.setText(array_list.get(position).getStr_start_date());
        viewHolder.tv_end_date.setText(array_list.get(position).getStr_end_date());
        ImageUtil.displayImage(viewHolder.cv_image,array_list.get(position).getStr_image(),null);
        viewHolder.btn_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.school_trainer_leave_application_reason_dialog);
                final TextView tv_name,tv_subject,tv_ok,tv_note;
                final ImageView iv_close;
                tv_name = dialog.findViewById(R.id.tv_name);
                tv_name.setText(array_list.get(position).getStr_name());
                tv_subject = dialog.findViewById(R.id.tv_subject);
                tv_subject.setText(array_list.get(position).getStr_subject());
                tv_note = dialog.findViewById(R.id.tv_note);
                tv_note.setText(array_list.get(position).getStr_note());
                tv_ok = dialog.findViewById(R.id.tv_ok);
                iv_close = dialog.findViewById(R.id.iv_close);
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Name,tv_sport_name,tv_subject,tv_start_date,tv_end_date;
        private Button btn_reason;
        private CircleImageView cv_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_school_trainer_leave_application_title);
            btn_reason = itemView.findViewById(R.id.btn_reason);
            tv_sport_name = itemView.findViewById(R.id.tv_sport_name);
            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_start_date = itemView.findViewById(R.id.tv_start_date);
            tv_end_date = itemView.findViewById(R.id.tv_end_date);
            cv_image = itemView.findViewById(R.id.cv_image);

        }
    }
}

