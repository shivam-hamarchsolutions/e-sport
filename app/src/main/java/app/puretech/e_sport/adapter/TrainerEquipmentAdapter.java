package app.puretech.e_sport.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.model.TrainerEquipmentDTO;

public class TrainerEquipmentAdapter extends RecyclerView.Adapter<TrainerEquipmentAdapter.ViewHolder> {

    private List<TrainerEquipmentDTO> array_list;
    private Activity activity;

    public TrainerEquipmentAdapter(List<TrainerEquipmentDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public TrainerEquipmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trainer_equipment_management_item, viewGroup, false);

        return new TrainerEquipmentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerEquipmentAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_equipment_name.setText(array_list.get(position).getStr_equipment_name());
        if (array_list.get(position).getStr_specifiction().equals("null")) {

        }else {

            viewHolder.tv_specifiction.setText(array_list.get(position).getStr_specifiction());
        }
        Log.d("llllllllll", "onBindViewHolder: "+array_list.get(position).getStr_specifiction());
        viewHolder.tv_opening_balance.setText(array_list.get(position).getStr_opening_balance());
        viewHolder.tv_inword_for_month.setText(array_list.get(position).getStr_inword_for_month());
        viewHolder.tv_outward_for_month.setText(array_list.get(position).getStr_outward_for_month());
        viewHolder.tv_discre_for_month.setText(array_list.get(position).getStr_discre_for_month());
        viewHolder.tv_physical_stock.setText(array_list.get(position).getStr_physical_stock());
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_equipment_name, tv_specifiction, tv_opening_balance, tv_inword_for_month, tv_outward_for_month, tv_discre_for_month, tv_physical_stock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_equipment_name = itemView.findViewById(R.id.tv_trainer_equipment_name);
            tv_specifiction = itemView.findViewById(R.id.tv_specifiction);
            tv_opening_balance = itemView.findViewById(R.id.tv_opening_balance);
            tv_inword_for_month = itemView.findViewById(R.id.tv_invward);
            tv_outward_for_month = itemView.findViewById(R.id.tv_outward);
            tv_discre_for_month = itemView.findViewById(R.id.tv_discre_for_month);
            tv_physical_stock = itemView.findViewById(R.id.tv_physical_stock);

        }
    }
}