package app.puretech.e_sport.webservices;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.activity.ParentHomeActivity;
import app.puretech.e_sport.activity.SchoolHomeActivity;
import app.puretech.e_sport.activity.TrainerHomeActivity;
import app.puretech.e_sport.model.UserDTO;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.CommonUtil;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignInAPI {
    //Check login
    public void doSignIn(App app, Activity activity, Dialog dialog, String str_user, String str_mobile, String str_password, String fcm_token) {
        dialog.show();
        RequestBody requestLogin = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("email", str_mobile)
                .addFormDataPart("password", str_password)
                .addFormDataPart("role", str_user)
                .addFormDataPart("fcm_token",fcm_token)
                .build();
        app.getApiService().doSignIn(requestLogin).enqueue(new Callback<Map<String, Object>>() {
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
                            String token = jobj.getString("token");
                            JSONObject jsonObject = jobj.getJSONObject("data");

                            UserDTO userDTO = new UserDTO();
                            userDTO.setTokenId("bearer " + token);
                            app.getPreferences().setUser(userDTO);
                            String app_user = AppPreferences.init(activity).getString("user", "");
                            switch (app_user) {
                                case "Trainer":

                                    AppPreferences.init(activity).setString("trainer_id", jsonObject.getString("trainer_id"));
                                    AppPreferences.init(activity).setString("user_name", jsonObject.getString("name"));
                                    AppPreferences.init(activity).setString("pic", jsonObject.getString("profile_photo"));
                                    AppPreferences.init(activity).setString("experience", jsonObject.getString("experience"));
                                    AppPreferences.init(activity).setString("primary_game", jsonObject.getString("primary_game"));
                                    AppPreferences.init(activity).setString("sport_achievement", jsonObject.getString("sport_achievement"));
                                    AppPreferences.init(activity).setString("education", jsonObject.getString("education"));
                                    AppPreferences.init(activity).setString("email", jsonObject.getString("email"));
                                    AppPreferences.init(activity).setString("contact", jsonObject.getString("contact"));
                                    AppPreferences.init(activity).setString("address", jsonObject.getString("address"));
                                    AppPreferences.init(activity).setString("school_name", jsonObject.getString("school_name"));

                                    Intent iTrainer = new Intent(activity, TrainerHomeActivity.class);
                                    activity.startActivity(iTrainer);
                                    activity.finish();
                                    break;
                                case "School":
                                    //School
                                    AppPreferences.init(activity).setString("school_id", jsonObject.getString("school_id"));
                                    AppPreferences.init(activity).setString("user_name", jsonObject.getString("school_name"));
                                    AppPreferences.init(activity).setString("school_name", jsonObject.getString("school_name"));
                                    AppPreferences.init(activity).setString("school_email", jsonObject.getString("school_email"));
                                    AppPreferences.init(activity).setString("school_contact", jsonObject.getString("school_contact"));
                                    AppPreferences.init(activity).setString("school_pic", jsonObject.getString("school_pic"));
                                    AppPreferences.init(activity).setString("school_address", jsonObject.getString("school_address"));
                                    Intent i_School = new Intent(activity, SchoolHomeActivity.class);
                                    activity.startActivity(i_School);
                                    activity.finish();
                                    break;
                                case "Parent":
                                    AppPreferences.init(activity).setString("name", jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"));
                                    AppPreferences.init(activity).setString("email", jsonObject.getString("unique_id"));
                                    AppPreferences.init(activity).setString("address", jsonObject.getString("address"));
                                    /*  AppPreferences.init(activity).setString("mobile", jsonObject.getString("phone"));*/
                                    AppPreferences.init(activity).setString("school_id", jsonObject.getString("school_id"));
                                    AppPreferences.init(activity).setString("class", jsonObject.getString("class"));
                                    AppPreferences.init(activity).setString("parent_image", jsonObject.getString("image"));
                                    AppPreferences.init(activity).setString("student_id", jsonObject.getString("student_id"));


                                    Intent i_Parent = new Intent(activity, ParentHomeActivity.class);
                                    activity.startActivity(i_Parent);
                                    activity.finish();
                                    break;
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
                dialog.dismiss();
                CommonUtil.showBar(activity, ".activity.SignInActivity");
            }
        });
    }

}
