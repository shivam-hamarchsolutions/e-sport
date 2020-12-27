package app.puretech.e_sport.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.SchoolHomeActivity;
import app.puretech.e_sport.adapter.ParentDailyTimeTableAdapter;
import app.puretech.e_sport.model.ParentDailyTimeTableDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentDailyTimeTableFragment extends Fragment implements View.OnClickListener {

    private View view;
    private App app;
    private Activity activity;
    private RecyclerView rv_show;
    private List<ParentDailyTimeTableDTO> array_list = new ArrayList<>();
    private ParentDailyTimeTableAdapter parentDailyTimeTableAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private RelativeLayout rl_month_next, rl_month_back;
    private Calendar c;
    private SimpleDateFormat df_daily;
    private String[] fd_daily;
    private TextView tv_trainer_monthly_planner_month;
    private Dialog dialog;

    public ParentDailyTimeTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_daily_time_table, container, false);
        doInit(view);
        return view;
    }

    @SuppressLint("SimpleDateFormat")
    private void doInit(View view) {
        app = (App) Objects.requireNonNull(getActivity()).getApplication();
        activity = getActivity();
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_show = view.findViewById(R.id.rv_parent_daily_time_table);
        ll_hide = view.findViewById(R.id.ll_Hide);
        s_Refresh = view.findViewById(R.id.s_refresh);
        rl_month_back = view.findViewById(R.id.rl_month_back);
        rl_month_next = view.findViewById(R.id.rl_month_next);
        tv_trainer_monthly_planner_month = view.findViewById(R.id.tv_trainer_monthly_planner_month);
        Intent i_back = new Intent(activity, SchoolHomeActivity.class);
        rl_month_next.setOnClickListener(this);
        rl_month_back.setOnClickListener(this);
        //Get current month
        c = Calendar.getInstance();
        df_daily = new SimpleDateFormat("dd MMM yyyy");
        fd_daily = new String[]{df_daily.format(c.getTime())};
        tv_trainer_monthly_planner_month.setText(fd_daily[0]);
        swipRefresh(s_Refresh);
        getDailyTimeTable(CommonUtil.convertDate(tv_trainer_monthly_planner_month.getText().toString(), "dd MMM yyyy", "yyyy-MM-dd"));


    }

    private void swipRefresh(SwipeRefreshLayout view) {
        view.setOnRefreshListener(() -> s_Refresh.setRefreshing(false));
    }

    private void getDailyTimeTable(String str_date) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("current_date", str_date)
                    .addFormDataPart("school_id", AppPreferences.init(activity).getString("school_id", ""))
                    .addFormDataPart("class", AppPreferences.init(activity).getString("class", ""))
                    .build();
            app.getApiService().getDailyTimeTable(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    ParentDailyTimeTableDTO parentDailyTimeTableDTO = new ParentDailyTimeTableDTO();
                                    parentDailyTimeTableDTO.setStr_grade(c.getString("day"));
                                    parentDailyTimeTableDTO.setStr_time(c.getString("time"));
                                    parentDailyTimeTableDTO.setStr_class(c.getString("class"));
                                    parentDailyTimeTableDTO.setStr_session_name(c.getString("session_name") + " " + c.getString("day"));
                                    parentDailyTimeTableDTO.setStr_school_name(c.getString("school_name"));
                                    parentDailyTimeTableDTO.setStr_image(c.getString("session_pic"));
                                    array_list.add(parentDailyTimeTableDTO);
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
            CommonUtil.showBar(activity, ".activity.TrainerDailyTimeTableActivity");
        }

    }

    private void setAdapter() {
        parentDailyTimeTableAdapter = new ParentDailyTimeTableAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_show.setLayoutManager(mLayoutManager);
        rv_show.setItemAnimator(new DefaultItemAnimator());
        rv_show.setAdapter(parentDailyTimeTableAdapter);
        if (parentDailyTimeTableAdapter.getItemCount() == 0) {
            ll_hide.setVisibility(View.VISIBLE);
        } else {
            ll_hide.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_month_back:
                c.add(Calendar.DAY_OF_MONTH, -1);
                fd_daily[0] = df_daily.format(c.getTime());
                tv_trainer_monthly_planner_month.setText(fd_daily[0]);
                getDailyTimeTable(CommonUtil.convertDate(tv_trainer_monthly_planner_month.getText().toString(), "dd MMM yyyy", "yyyy-MM-dd"));

                break;
            case R.id.rl_month_next:
                c.add(Calendar.DAY_OF_MONTH, 1);
                fd_daily[0] = df_daily.format(c.getTime());
                tv_trainer_monthly_planner_month.setText(fd_daily[0]);
                getDailyTimeTable(CommonUtil.convertDate(tv_trainer_monthly_planner_month.getText().toString(), "dd MMM yyyy", "yyyy-MM-dd"));

                break;
        }
    }
}

