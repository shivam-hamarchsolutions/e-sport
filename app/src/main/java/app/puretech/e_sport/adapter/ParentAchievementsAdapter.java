package app.puretech.e_sport.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.model.ParentAchievementsDTO;
import app.puretech.e_sport.utill.ImageUtil;
import de.hdodenhof.circleimageview.CircleImageView;

public class ParentAchievementsAdapter extends RecyclerView.Adapter<ParentAchievementsAdapter.ViewHolder> {

    private List<ParentAchievementsDTO> array_list;
    private Activity activity;

    public ParentAchievementsAdapter(List<ParentAchievementsDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ParentAchievementsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.parent_achievement_item, viewGroup, false);

        return new ParentAchievementsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentAchievementsAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_Name.setText(array_list.get(position).getStr_full_name());
        viewHolder.tv_sport_name.setText(array_list.get(position).getStr_competition_name());
        viewHolder.tv_rank.setText(array_list.get(position).getStr_rank());
        viewHolder.tv_level.setText(array_list.get(position).getStr_competition_level());
        viewHolder.tv_year.setText(array_list.get(position).getStr_competition_year());
        ImageUtil.displayImage(viewHolder.cv_image,array_list.get(position).getSet_image(),null);
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Name,tv_sport_name,tv_level,tv_year,tv_rank;
        private CircleImageView cv_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_Trainer_Achivements_name);
            tv_sport_name = itemView.findViewById(R.id.tv_sport_name);
            tv_level = itemView.findViewById(R.id.tv_level);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            cv_image = itemView.findViewById(R.id.cv_image);

        }
    }
}
