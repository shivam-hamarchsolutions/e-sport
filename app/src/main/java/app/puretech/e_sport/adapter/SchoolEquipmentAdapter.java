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
import app.puretech.e_sport.model.SchoolEquipmentDTO;

public class SchoolEquipmentAdapter extends RecyclerView.Adapter<SchoolEquipmentAdapter.ViewHolder> {

    private List<SchoolEquipmentDTO> array_list;
    private Activity activity;

    public SchoolEquipmentAdapter(List<SchoolEquipmentDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public SchoolEquipmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.school_equipment_stock_item, viewGroup, false);

        return new SchoolEquipmentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolEquipmentAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_equipment_name.setText(array_list.get(position).getStr_equipment_name());

        if (array_list.get(position).getStr_specifiction().equals("null")) {

        } else {
            viewHolder.tv_standard.setText(array_list.get(position).getStr_specifiction());
        }
        viewHolder.tv_opending_balance.setText(array_list.get(position).getStr_opening_balance());
        viewHolder.tv_inward.setText(array_list.get(position).getStr_inword_for_month());
        viewHolder.tv_ouward.setText(array_list.get(position).getStr_outward_for_month());
        viewHolder.tv_discrepancy.setText(array_list.get(position).getStr_discre_for_month());
        viewHolder.tv_physical.setText(array_list.get(position).getStr_physical_stock());
    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_equipment_name, tv_standard, tv_opending_balance, tv_inward, tv_ouward, tv_discrepancy, tv_physical;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_equipment_name = itemView.findViewById(R.id.tv_trainer_equipment_name);
            tv_standard = itemView.findViewById(R.id.tv_standard);
            tv_opending_balance = itemView.findViewById(R.id.tv_opening_balance);
            tv_inward = itemView.findViewById(R.id.tv_invward);
            tv_ouward = itemView.findViewById(R.id.tv_outward);
            tv_discrepancy = itemView.findViewById(R.id.tv_discre_for_month);
            tv_physical = itemView.findViewById(R.id.tv_physical_stock);
        }
    }
}