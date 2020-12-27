package app.puretech.e_sport.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.EquipmentListAdapter;
import app.puretech.e_sport.model.EquipmentListDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TrainerAddEquipmentActivity extends AppCompatActivity {

    private App app;
    private Activity activity;
    private Toolbar toolbar;
    private Dialog dialog;
    private Spinner sp_equipment;
    private EquipmentListAdapter equipmentListAdapter;
    private ArrayList<EquipmentListDTO> arrayList;
    private EditText et_equipment_size, et_opening_bal, et_inward, et_outward, et_discrepancy, et_closing_stock;
    private Button btn_submit;
    private String str_item_id;
    private Intent i_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_trainer_add_equipment);
        doInit();
        getEquipmentList();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        arrayList = new ArrayList<>();
        i_back = new Intent(activity, TrainerEquipmentManagementActivity.class);
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        toolbar = findViewById(R.id.toolbar_home);
        sp_equipment = findViewById(R.id.sp_equipment);
        et_equipment_size = findViewById(R.id.tv_equipment_size);
        et_opening_bal = findViewById(R.id.et_opening_balance);
        et_inward = findViewById(R.id.et_inward_for_month);
        et_outward = findViewById(R.id.et_outward_of_month);
        et_discrepancy = findViewById(R.id.et_discrepancey);
        et_closing_stock = findViewById(R.id.et_closing_stock);
        btn_submit = findViewById(R.id.btn_submit);
        sp_equipment = findViewById(R.id.sp_equipment);
        doBack(toolbar);
        sp_equipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_item_id = arrayList.get(position).getItem_id();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_equipment_size.getText().toString().isEmpty()) {
                    et_equipment_size.setError("Required !");
                } else if (et_opening_bal.getText().toString().isEmpty()) {
                    et_opening_bal.setError("Required !");
                } else if (et_inward.getText().toString().isEmpty()) {
                    et_inward.setError("Required !");
                } else if (et_outward.getText().toString().isEmpty()) {
                    et_outward.setError("Required !");
                } else if (et_discrepancy.getText().toString().isEmpty()) {
                    et_discrepancy.setError("Required !");
                } else if (et_closing_stock.getText().toString().isEmpty()) {
                    et_closing_stock.setError("Required !");
                } else {
                    doAddEquipment(AppPreferences.init(activity).getString("school_id", ""), str_item_id, et_equipment_size.getText().toString(), et_opening_bal.getText().toString(), et_inward.getText().toString(), et_outward.getText().toString(), et_discrepancy.getText().toString(), et_closing_stock.getText().toString());
                }
            }
        });
    }

    private void getEquipmentList() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            dialog.show();
            app.getApiService().getEquipmentList().enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Response<Map<String, Object>> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        app.getLogger().error("success");
                        String status;
                        String message;
                        JSONObject jobj;
                        try {
                            jobj = new JSONObject(response.body());
                            status = jobj.getString("success");
                            message = jobj.getString("message");
                            if (status.equals("0")) {
                                JSONArray jsonArray = jobj.getJSONArray("item_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    EquipmentListDTO equipmentListDTO = new EquipmentListDTO();
                                    equipmentListDTO.setEquipment_name(c.getString("equip_name"));
                                    equipmentListDTO.setItem_id(c.getString("id"));
                                    arrayList.add(equipmentListDTO);
                                }
                                setAdapter();
                            } else if (status.equals("1")) {
                                setAdapter();
                                app.showSnackBar(activity, message);
                            } else {
                                app.showSnackBar(activity, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        int a = response.code();
                        if (a == 401) {
                            app.showSnackBar(activity, "" + a);
                        } else {
                            app.showSnackBar(activity, "" + a);
                        }
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Throwable t) {
                    app.getLogger().error("failure");
                    dialog.dismiss();
                }
            });
        } else {
            CommonUtil.showBar(activity, ".activity.TrainerNotificationActivity");
        }
    }

    private void setAdapter() {
        equipmentListAdapter = new EquipmentListAdapter(arrayList, activity);
        sp_equipment.setAdapter(equipmentListAdapter);
    }

    private void doAddEquipment(String str_school_id, String str_item_id, String str_specifiction, String str_opening_balance, String str_inword_for_month, String str_outward_for_month, String str_discre_for_month, String str_physical_stock) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("school_id", str_school_id)
                    .addFormDataPart("item_id", str_item_id)
                    .addFormDataPart("specifiction", str_specifiction)
                    .addFormDataPart("opening_balance", str_opening_balance)
                    .addFormDataPart("inword_for_month", str_inword_for_month)
                    .addFormDataPart("outward_for_month", str_outward_for_month)
                    .addFormDataPart("discre_for_month", str_discre_for_month)
                    .addFormDataPart("physical_stock", str_physical_stock)
                    .build();
            app.getApiService().doAddEquipment(requestBody).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Response<Map<String, Object>> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        app.getLogger().error("success");
                        String status;
                        String message;
                        JSONObject jobj;
                        try {
                            jobj = new JSONObject(response.body());
                            status = jobj.getString("success");
                            message = jobj.getString("message");
                            if (status.equals("0")) {
                                app.showSnackBar(activity, message);
                                activity.startActivity(i_back);
                                activity.finish();
                            } else if (status.equals("1")) {
                                setAdapter();
                                app.showSnackBar(activity, message);
                            } else {
                                app.showSnackBar(activity, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        int a = response.code();
                        if (a == 401) {
                            app.showSnackBar(activity, "" + a);
                        } else {
                            app.showSnackBar(activity, "" + a);
                        }
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Throwable t) {
                    app.getLogger().error("failure");
                    dialog.dismiss();
                }
            });
        } else {
            CommonUtil.showBar(activity, ".activity.TrainerDailyTimeTableActivity");
        }

    }


    private void doBack(Toolbar toolbar) {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(i_back);
                activity.finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        activity.startActivity(i_back);
        activity.finish();
    }
}
