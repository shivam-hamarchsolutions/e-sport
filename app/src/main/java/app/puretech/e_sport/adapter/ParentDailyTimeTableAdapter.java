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
import app.puretech.e_sport.model.ParentDailyTimeTableDTO;
import app.puretech.e_sport.utill.ImageUtil;

public class ParentDailyTimeTableAdapter extends RecyclerView.Adapter<ParentDailyTimeTableAdapter.ViewHolder> {

    private List<ParentDailyTimeTableDTO> array_list;
    private Activity activity;

    public ParentDailyTimeTableAdapter(List<ParentDailyTimeTableDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ParentDailyTimeTableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.parent_daily_time_table_item, viewGroup, false);

        return new ParentDailyTimeTableAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentDailyTimeTableAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_school_name.setText(array_list.get(position).getStr_school_name());
        viewHolder.tv_class.setText(array_list.get(position).getStr_class());
        viewHolder.tv_sport_name.setText(array_list.get(position).getStr_session_name());
        viewHolder.tv_time.setText(array_list.get(position).getStr_time());
        ImageUtil.displayImage(viewHolder.iv_image,array_list.get(position).getStr_image(),null);    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_school_name,tv_time,tv_class,tv_sport_name;
        private ImageView iv_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_school_name = itemView.findViewById(R.id.tv_school_name);
            iv_image = itemView.findViewById(R.id.iv_image);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_class = itemView.findViewById(R.id.tv_class);
            tv_sport_name = itemView.findViewById(R.id.tv_sport_name);
        }
    }
}

