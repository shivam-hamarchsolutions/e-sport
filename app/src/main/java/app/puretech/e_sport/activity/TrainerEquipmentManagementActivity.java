package app.puretech.e_sport.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.TrainerEquipmentAdapter;
import app.puretech.e_sport.model.TrainerEquipmentDTO;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TrainerEquipmentManagementActivity extends AppCompatActivity {

    App app;
    Activity activity;
    private RecyclerView rv_equipment;
    private List<TrainerEquipmentDTO> array_list = new ArrayList<>();
    private TrainerEquipmentAdapter trainerEquipmentAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private FloatingActionButton fab_add_Equipment;
    private Toolbar toolbar;
    private Dialog dialog;
    Intent iBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_trainer_equipment_management);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_equipment = findViewById(R.id.rv_Trainer_equipment);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        fab_add_Equipment = findViewById(R.id.fab_trainer_add_equipment);
        toolbar = findViewById(R.id.toolbar_home);
        iBack = new Intent(activity, TrainerHomeActivity.class);
        swipRefresh(s_Refresh);
        addEquipment(fab_add_Equipment);
        doBack(toolbar);
        getEquipment();
    }
    private void getEquipment() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            app.getApiService().getEquipment().enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("equipment_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    TrainerEquipmentDTO trainerEquipmentDTO = new TrainerEquipmentDTO();
                                    trainerEquipmentDTO.setStr_equipment_name(c.getString("equip_name"));
                                    trainerEquipmentDTO.setStr_specifiction(c.getString("specifiction"));
                                    trainerEquipmentDTO.setStr_inword_for_month(c.getString("inword_for_month"));
                                    trainerEquipmentDTO.setStr_outward_for_month(c.getString("outward_for_month"));
                                    trainerEquipmentDTO.setStr_discre_for_month(c.getString("discre_for_month"));
                                    trainerEquipmentDTO.setStr_physical_stock(c.getString("physical_stock"));
                                    trainerEquipmentDTO.setStr_opening_balance(c.getString("opening_balance"));
                                    array_list.add(trainerEquipmentDTO);
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
    private void setAdapter(){
        trainerEquipmentAdapter = new TrainerEquipmentAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_equipment.setLayoutManager(mLayoutManager);
        rv_equipment.setItemAnimator(new DefaultItemAnimator());
        rv_equipment.setAdapter(trainerEquipmentAdapter);
        if (trainerEquipmentAdapter.getItemCount() == 0) {
            ll_hide.setVisibility(View.VISIBLE);
        } else {
            ll_hide.setVisibility(View.GONE);
        }
    }

    private void swipRefresh(SwipeRefreshLayout view) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                s_Refresh.setRefreshing(false);
            }
        });
    }

    private void addEquipment(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_Add_Equipment = new Intent(activity, TrainerAddEquipmentActivity.class);
                activity.startActivity(i_Add_Equipment);
                activity.finish();
            }
        });
    }

    private void doBack(Toolbar toolbar) {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(iBack);
                activity.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        activity.startActivity(iBack);
        activity.finish();
    }
}
