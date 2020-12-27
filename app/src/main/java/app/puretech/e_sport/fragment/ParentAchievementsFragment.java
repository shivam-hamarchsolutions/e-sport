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
import app.puretech.e_sport.adapter.ParentAchievementsAdapter;
import app.puretech.e_sport.model.ParentAchievementsDTO;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentAchievementsFragment extends Fragment {


    View view;
    App app;
    Activity activity;
    private RecyclerView rv_show;
    private List<ParentAchievementsDTO> array_list = new ArrayList<>();
    private ParentAchievementsAdapter parentAchievementsAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private Dialog dialog;

    public ParentAchievementsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_achievements, container, false);
        doInit(view);
        return  view;
    }
    private void doInit(View view) {
        app = (App) getActivity().getApplication();
        activity = getActivity();
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);

        rv_show = view.findViewById(R.id.rv_parent_achievements);
        ll_hide = view.findViewById(R.id.ll_Hide);
        s_Refresh = view.findViewById(R.id.s_refresh);
        swipRefresh(s_Refresh);
        getAchievements();


    }
    private void getAchievements() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
//            RequestBody requestBody = new MultipartBuilder()
//                    .type(MultipartBuilder.FORM)
//                    .addFormDataPart("student_id", AppPreferences.init(activity).getString("student_id", ""))
//                    .addFormDataPart("school_id", AppPreferences.init(activity).getString("school_id", ""))
//                    .addFormDataPart("class", AppPreferences.init(activity).getString("class", ""))
//                    .build();
            app.getApiService().getParentAchievements().enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("achievement_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    ParentAchievementsDTO parentAchievementsDTO = new ParentAchievementsDTO();
                                    parentAchievementsDTO.setStr_competition_name(c.getString("competition_name"));
                                    parentAchievementsDTO.setStr_rank(c.getString("rank"));
                                    parentAchievementsDTO.setStr_full_name(c.getString("student_name"));
                                    parentAchievementsDTO.setStr_competition_level(c.getString("competation_level"));
                                    parentAchievementsDTO.setStr_competition_year(c.getString("year"));
                                    parentAchievementsDTO.setSet_image(c.getString("competation_pic"));
                                    array_list.add(parentAchievementsDTO);
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

    private void setAdapter(){
        parentAchievementsAdapter = new ParentAchievementsAdapter(array_list, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_show.setLayoutManager(mLayoutManager);
        rv_show.setItemAnimator(new DefaultItemAnimator());
        rv_show.setAdapter(parentAchievementsAdapter);
        if (parentAchievementsAdapter.getItemCount() == 0) {
            ll_hide.setVisibility(View.VISIBLE);
        } else {
            ll_hide.setVisibility(View.GONE);
        }
    }
    //add refresh
    private void swipRefresh(SwipeRefreshLayout view) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                s_Refresh.setRefreshing(false);
            }
        });
    }

}
