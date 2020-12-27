package app.puretech.e_sport.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.ParentYearlyEventAdapter;
import app.puretech.e_sport.model.ParentYearlyEventDTO;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentEventFragment extends Fragment {


    View view;
    App app;
    Activity activity;
    private RecyclerView rv_trainer_yearly_event;
    private List<ParentYearlyEventDTO> array_list = new ArrayList<>();
    private ParentYearlyEventAdapter parentYearlyEventAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Dialog dialog;

    public ParentEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_event, container, false);
        doInit(view);
        return view;
    }

    private void doInit(View view) {
        app = (App) getActivity().getApplication();
        activity = getActivity();
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);

        rv_trainer_yearly_event = view.findViewById(R.id.rv_Parent_yearly_events);
        ll_hide = view.findViewById(R.id.ll_Hide);
        s_Refresh = view.findViewById(R.id.s_refresh);
        swipRefresh(s_Refresh);
        getEvent();
    }

    private void getEvent() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
//            RequestBody requestBody = new MultipartBuilder()
//                    .type(MultipartBuilder.FORM)
//                    .addFormDataPart("student_id", AppPreferences.init(activity).getString("student_id", ""))
//                    .addFormDataPart("school_id", AppPreferences.init(activity).getString("school_id", ""))
//                    .addFormDataPart("class", AppPreferences.init(activity).getString("class", ""))
//                    .build();
            app.getApiService().getParentEvent().enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("event_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    ParentYearlyEventDTO parentYearlyEventDTO = new ParentYearlyEventDTO();
                                    parentYearlyEventDTO.setStr_sub_event_name(c.getString("sub_event_name"));
                                    parentYearlyEventDTO.setStr_day(c.getString("day"));
                                    parentYearlyEventDTO.setStr_date(c.getString("date"));
                                    parentYearlyEventDTO.setStr_timing(c.getString("timing"));
                                    parentYearlyEventDTO.setStr_session(c.getString("session"));
                                    parentYearlyEventDTO.setStr_venue(c.getString("venue"));
                                    parentYearlyEventDTO.setStr_grade(c.getString("grade"));
                                    array_list.add(parentYearlyEventDTO);
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
        parentYearlyEventAdapter = new ParentYearlyEventAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_trainer_yearly_event.setLayoutManager(mLayoutManager);
        rv_trainer_yearly_event.setItemAnimator(new DefaultItemAnimator());
        rv_trainer_yearly_event.setAdapter(parentYearlyEventAdapter);
        if (parentYearlyEventAdapter.getItemCount() == 0) {
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

}
