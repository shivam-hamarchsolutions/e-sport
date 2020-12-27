package app.puretech.e_sport.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;

public class ParentStudentAssessmentActivity extends AppCompatActivity {

    App app;
    Activity activity;
    private Button btn_next;
    private Spinner spinner_term;
    private Toolbar toolbar;
    private Intent i_back;
    private TextView txtClass, txtDiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_student_assessment);
        doInit();


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.init(activity).setString("class_name", txtClass.getText().toString());
                AppPreferences.init(activity).setString("division_name", txtDiv.getText().toString());
                AppPreferences.init(activity).setString("term", spinner_term.getSelectedItem().toString());
                Intent i_next = new Intent(activity, ParentStudentAssessmentDataActivity.class);
                activity.startActivity(i_next);
                activity.finish();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(i_back);
                activity.finish();
            }
        });
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        i_back = new Intent(activity, ParentHomeActivity.class);
        btn_next = findViewById(R.id.btn_next);
        txtClass = findViewById(R.id.txt_class);
        txtDiv = findViewById(R.id.txt_division);
        spinner_term = findViewById(R.id.tv_spinner_term);
        toolbar = findViewById(R.id.toolbar_home);
        String str = AppPreferences.init(activity).getString("class", "");
        String[] splited = str.split("\\s+");
        txtClass.setText("" + splited[0]);
        txtDiv.setText("" + splited[1]);
    }


    @Override
    public void onBackPressed() {
        activity.startActivity(i_back);
        activity.finish();
    }
}
