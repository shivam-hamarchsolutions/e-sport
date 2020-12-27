package app.puretech.e_sport.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.model.SelectChildDTO;

public class SelectChildAdapter extends RecyclerView.Adapter<SelectChildAdapter.ViewHolder> {

    private List<SelectChildDTO> array_list;
    private Activity activity;



    public SelectChildAdapter(List<SelectChildDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;


    }

    @NonNull
    @Override
    public SelectChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.select_child_item, viewGroup, false);

        return new SelectChildAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectChildAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_Name.setText(array_list.get(position).getStudent_name());
        viewHolder.tv_school_name.setText(array_list.get(position).getSchool_name());
        viewHolder.tv_class.setText(array_list.get(position).getStr_class());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.cb_select.setChecked(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Name, tv_school_name, tv_class;
        private CheckBox cb_select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_name);
            tv_school_name = itemView.findViewById(R.id.tv_school_name);
            tv_class = itemView.findViewById(R.id.tv_class);
            cb_select = itemView.findViewById(R.id.cb_select);
        }
    }
}

