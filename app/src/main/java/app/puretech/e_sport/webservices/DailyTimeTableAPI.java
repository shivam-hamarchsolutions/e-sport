package app.puretech.e_sport.webservices;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.model.TrainerDailyTimeTableDTO;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DailyTimeTableAPI {
    //Check login

    public ArrayList<TrainerDailyTimeTableDTO> getDailyTimeTable(ArrayList<TrainerDailyTimeTableDTO>arrayList,App app, Activity activity, Dialog dialog, String str_date) {

        dialog.show();
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("current_date", str_date)
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
                                TrainerDailyTimeTableDTO trainerDailyTimeTableDTO = new TrainerDailyTimeTableDTO();
                                trainerDailyTimeTableDTO.setStr_school_name(c.getString("school_name"));
                                arrayList.add(trainerDailyTimeTableDTO);
                            }

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
            }
        });
        return (ArrayList<TrainerDailyTimeTableDTO>) arrayList;
    }

}
