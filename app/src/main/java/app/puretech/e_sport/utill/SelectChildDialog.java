package app.puretech.e_sport.utill;

import android.app.Activity;
import android.app.Dialog;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.SelectChildAdapter;
import app.puretech.e_sport.model.SelectChildDTO;

public class SelectChildDialog {
    public void selectChildDialog(Activity activity, ArrayList<SelectChildDTO> arrayList) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.parent_child_dialog);
        final SelectChildAdapter selectChildAdapter;
        final RecyclerView rv_no_of_child = dialog.findViewById(R.id.rv_no_of_child);
        selectChildAdapter = new SelectChildAdapter(arrayList, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_no_of_child.setLayoutManager(mLayoutManager);
        rv_no_of_child.setItemAnimator(new DefaultItemAnimator());
        rv_no_of_child.setAdapter(selectChildAdapter);
        dialog.show();
    }

}
