package app.puretech.e_sport.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.TrainerPDFActivity;
import app.puretech.e_sport.activity.TrainerStudentAttendanceActivity;
import app.puretech.e_sport.model.TrainerDailyTimeTableDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import app.puretech.e_sport.utill.ImageUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TrainerDailyTimeTableAdapter extends RecyclerView.Adapter<TrainerDailyTimeTableAdapter.ViewHolder> {

    private List<TrainerDailyTimeTableDTO> trainer_daily_time_table_list;
    private Activity activity;
    private App app;
    private Dialog dialog;
    String user;

    public TrainerDailyTimeTableAdapter(List<TrainerDailyTimeTableDTO> trainer_daily_time_table_list, Activity activity, App app, Dialog dialog, String trainer) {
        this.trainer_daily_time_table_list = trainer_daily_time_table_list;
        this.activity = activity;
        this.app = app;
        this.dialog = dialog;
        this.user = trainer;

    }

    @NonNull
    @Override
    public TrainerDailyTimeTableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trainer_daily_time_table_item, viewGroup, false);

        return new TrainerDailyTimeTableAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerDailyTimeTableAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_school_name.setText(trainer_daily_time_table_list.get(position).getStr_school_name());
        viewHolder.tv_time.setText(trainer_daily_time_table_list.get(position).getStr_time());
        viewHolder.tv_class.setText(trainer_daily_time_table_list.get(position).getStr_class());
        viewHolder.tv_sport_name.setText(trainer_daily_time_table_list.get(position).getStr_session());
        viewHolder.tv_sport_unit.setText(trainer_daily_time_table_list.get(position).getStr_days());
        viewHolder.tv_attendance.setText("Attendance : "+trainer_daily_time_table_list.get(position).getPresetStu()+"/"+trainer_daily_time_table_list.get(position).getTotalStu());
       // ImageUtil.loadImage(trainer_daily_time_table_list.get(position).getStr_image(),null);
        ImageUtil.displayImage(viewHolder.iv_image,trainer_daily_time_table_list.get(position).getStr_image(),null);

        if(user.equals("parent")){
            viewHolder.btn_attendance.setVisibility(View.GONE);
            viewHolder.btn_comment_box.setVisibility(View.GONE);
            viewHolder.btn_PDF.setVisibility(View.GONE);
        }
        commentBox(viewHolder.btn_comment_box,trainer_daily_time_table_list.get(position).getStr_class_session_id());

        pdfRead(viewHolder.btn_PDF,trainer_daily_time_table_list.get(position).getStr_session_pdf());
        doAttendance(viewHolder.btn_attendance,trainer_daily_time_table_list.get(position).getStr_class(),trainer_daily_time_table_list.get(position).getStr_school_id(),
                trainer_daily_time_table_list.get(position).getStr_class_session_id(),
                trainer_daily_time_table_list.get(position).getStr_status());
    }

    @Override
    public int getItemCount() {
        return trainer_daily_time_table_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_school_name, tv_time, tv_class, tv_sport_name, tv_attendance,tv_sport_unit;
        private Button btn_comment_box, btn_PDF, btn_attendance;
        private ImageView iv_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_school_name = itemView.findViewById(R.id.tv_school_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_attendance = itemView.findViewById(R.id.tv_attendance);
            tv_class = itemView.findViewById(R.id.tv_class);
            tv_sport_name = itemView.findViewById(R.id.tv_sport_name);
            btn_comment_box = itemView.findViewById(R.id.btn_Trainer_comment_box);
            btn_PDF = itemView.findViewById(R.id.btn_Trainer_PDF);
            btn_attendance = itemView.findViewById(R.id.btn_Trainer_Attendance);
            iv_image = itemView.findViewById(R.id.iv_image);
            tv_sport_unit = itemView.findViewById(R.id.tv_sport_unit);

        }
    }

    private void commentBox(Button button,String str_comment_id) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog comment_box_dialog = new Dialog(activity);
                comment_box_dialog.setContentView(R.layout.trainer_comment_box);
                comment_box_dialog.getWindow().setGravity(Gravity.CENTER);
                final EditText et_comment = comment_box_dialog.findViewById(R.id.et_comment);
                final Button btn_submit = comment_box_dialog.findViewById(R.id.btn_submit);
                final Button btn_cancel = comment_box_dialog.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        comment_box_dialog.dismiss();
                    }
                });
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_comment.getText().toString().isEmpty()){
                            et_comment.setError("Comment Required!!");
                        }else {
                            doComment(str_comment_id,et_comment.getText().toString());
                            comment_box_dialog.dismiss();
                        }
                    }
                });
                comment_box_dialog.show();
            }
        });
    }

    private void pdfRead(Button button,String str_url) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("url",str_url);
                Intent i_PDF = new Intent(activity, TrainerPDFActivity.class);
                activity.startActivity(i_PDF);
            }
        });
    }

    private void doAttendance(Button button, String class_name, String school_id, String cls_session_id, int str_status) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("class_name",class_name);
                AppPreferences.init(activity).setString("school_id",school_id);
                AppPreferences.init(activity).setString("cls_session_id",cls_session_id);
                AppPreferences.init(activity).setString("attendence_status", String.valueOf(str_status));
                Intent i_Attendance = new Intent(activity, TrainerStudentAttendanceActivity.class);
                activity.startActivity(i_Attendance);
            }
        });
    }

    public void doComment(String str_comment_id, String str_comment) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("cls_session_id", str_comment_id)
                    .addFormDataPart("comment", str_comment)
                    .build();
            app.getApiService().doComment(requestBody).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Response<Map<String, Object>> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        app.getLogger().error("success");
                        String status;
                        String message;
                        JSONObject jobj;
                        try {
                            jobj = new JSONObject(response.body());
                            status = jobj.getString("success");
                            message = jobj.getString("message");
                            if (status.equals("0")) {
                                app.showSnackBar(activity, message);
                            } else if (status.equals("1")) {
                                app.showSnackBar(activity, message);
                            } else {
                                app.showSnackBar(activity, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        int a = response.code();
                        if (a == 401) {
                            app.showSnackBar(activity, "" + a);
                        } else {
                            app.showSnackBar(activity, "" + a);
                        }
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Throwable t) {
                    app.getLogger().error("failure");
                }
            });
        } else {
            Toast.makeText(activity, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
        }
    }
}
