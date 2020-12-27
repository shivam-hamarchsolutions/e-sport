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
import app.puretech.e_sport.model.SessionDTO;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

    private List<SessionDTO> array_list;
    private Activity activity;

    public SessionAdapter(List<SessionDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.session_item, viewGroup, false);
        return new SessionAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.tv_sport_name.setText(array_list.get(position).getStr_sport_name());
        viewHolder.tv_time.setText(array_list.get(position).getStr_time());

    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_sport_name, tv_time;

        public ViewHolder(@NonNull View view) {
            super(view);
            tv_sport_name = view.findViewById(R.id.tv_sport_name);
            tv_time = view.findViewById(R.id.tv_time);
        }
    }
}
