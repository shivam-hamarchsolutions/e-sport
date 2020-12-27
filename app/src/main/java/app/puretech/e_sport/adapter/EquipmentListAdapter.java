package app.puretech.e_sport.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.model.EquipmentListDTO;

public class EquipmentListAdapter extends BaseAdapter {

    private List<EquipmentListDTO> array_list;
    private Activity activity;

    public EquipmentListAdapter(List<EquipmentListDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return array_list.size();
    }

    @Override
    public Object getItem(int position) {
        return array_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.equipment_list_item, parent, false);
        }
        //do init
        TextView tv_title = convertView.findViewById(R.id.tv_equipment_name);
        //set init
        tv_title.setText(array_list.get(position).getEquipment_name());

        return convertView;
    }
}
