package app.puretech.e_sport.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.puretech.e_sport.R;
import app.puretech.e_sport.model.SchoolStudentAttendanceListDTO;

public class SchoolStudentAttendanceListAdapter extends BaseAdapter {
    private Activity context; //context
    private ArrayList<SchoolStudentAttendanceListDTO> items; //data source of the list adapter

    //public constructor
    public SchoolStudentAttendanceListAdapter(Activity context, ArrayList<SchoolStudentAttendanceListDTO> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.school_student_attendance_item, parent, false);
        }

        // get current item to be displayed

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.tv_one);


        //sets the text for item name and item description from the current item object
        textViewItemName.setText(items.get(position).getDay());

        // returns the view for the current row
        return convertView;
    }
}