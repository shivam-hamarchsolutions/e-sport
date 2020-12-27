package app.puretech.e_sport.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.SchoolGalleryActivity;
import app.puretech.e_sport.adapter.ParentGalleryAdapter;
import app.puretech.e_sport.model.ParentGalleryDTO;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static app.puretech.e_sport.utill.DataManager.urlTrainerGallary;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentGalleryFragment extends Fragment {

    View view;
    App app;
    Activity activity;
    private GridView rv_gallery;
    private List<ParentGalleryDTO> array_list = new ArrayList<>();
    private ParentGalleryAdapter parentGalleryAdapter;
    private LinearLayout ll_hide;
    private Dialog dialog;
    private SwipeRefreshLayout s_Refresh;

    public ParentGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_gallery, container, false);
        doInit(view);

        rv_gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("cccccc", "onItemClick: " + array_list.get(position).getStr_image());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.fullscreen_img, null);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.show();
                ImageView image = (ImageView) dialog.findViewById(R.id.goProDialogImage);
                ImageView imageClose = (ImageView) dialog.findViewById(R.id.close_zoom);

                Picasso.with(activity).load(urlTrainerGallary + array_list.get(position).getStr_image())
                        .error(R.drawable.no_gallery)
                        .into(image);

                imageClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        return  view;
    }
    private void doInit(View view) {
        app = (App) getActivity().getApplication();
        activity = getActivity();
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_gallery = view.findViewById(R.id.rv_Parent_gallery);
        ll_hide = view.findViewById(R.id.ll_Hide);
        s_Refresh =view. findViewById(R.id.s_refresh);
        swipRefresh(s_Refresh);
        getGallery();

    }
    private void getGallery() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
//            RequestBody requestBody = new MultipartBuilder()
//                    .type(MultipartBuilder.FORM)
//                    .addFormDataPart("student_id", AppPreferences.init(activity).getString("student_id",""))
//                    .addFormDataPart("school_id", AppPreferences.init(activity).getString("school_id", ""))
//                    .addFormDataPart("class", AppPreferences.init(activity).getString("class", ""))
//                    .build();
            app.getApiService().getParentGallery().enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("gallery_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    ParentGalleryDTO parentGalleryDTO = new ParentGalleryDTO();
                                    parentGalleryDTO.setStr_image(c.getString("photo"));
                                    array_list.add(parentGalleryDTO);
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
        parentGalleryAdapter = new ParentGalleryAdapter(array_list, activity);
        rv_gallery.setAdapter(parentGalleryAdapter);
        if (parentGalleryAdapter.getCount() == 0) {
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
