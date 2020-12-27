package app.puretech.e_sport.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentFeedbackFragment extends Fragment {


    private View view;
    private App app;
    private Activity activity;
    private EditText et_feedback;
    private Dialog dialog;
    private Button btn_Submit;

    public ParentFeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_feedback, container, false);
        doInit(view);
        return view;
    }

    private void doInit(View view) {
        app = (App) getActivity().getApplication();
        activity = getActivity();
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        et_feedback = view.findViewById(R.id.et_feed_back);
        btn_Submit = view.findViewById(R.id.btn_Submit);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_feedback.getText().toString().isEmpty()){
                    et_feedback.setError("Feedback Required!!");
                }else {
                    Log.d("xxxbb", "stude id :"+AppPreferences.init(activity).getString("student_id",""));
                    Log.d("xxxbb", "stude id :"+ AppPreferences.init(activity).getString("school_id", ""));

                    doFeedback(et_feedback.getText().toString());
                }
            }
        });

    }
    private void doFeedback(String feedback) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("student_id", AppPreferences.init(activity).getString("student_id",""))
                    .addFormDataPart("school_id", AppPreferences.init(activity).getString("school_id", ""))
                    .addFormDataPart("feedback", feedback)
                    .build();
            app.getApiService().doFeedback(requestBody).enqueue(new Callback<Map<String, Object>>() {
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
                                et_feedback.setText("");
                                app.showSnackBar(activity, message);
                            } else if (status.equals("1")) {
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

}
