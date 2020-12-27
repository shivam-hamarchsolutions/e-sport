package app.puretech.e_sport.api;

import com.squareup.okhttp.RequestBody;

import java.util.Map;

import app.puretech.e_sport.model.ParentProfiledModel;
import app.puretech.e_sport.model.TrainerGallaryUploadModel;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by dinesh on 16-02-2018.
 */
public interface APIService {
    //versionControl
    @POST("versionControl")
    Call<Map<String, Object>> doVersionControl(@Body RequestBody requestBody);

    @POST("login")
    Call<Map<String, Object>> doSignIn(@Body RequestBody requestBody);

    //daily time table
    @POST("dailyTimeTable")
    Call<Map<String, Object>> getDailyTimeTable(@Body RequestBody requestBody);

    //post Class Comment
    @POST("postClassComment")
    Call<Map<String, Object>> doComment(@Body RequestBody requestBody);

    //get student list
    @POST("studentList")
    Call<Map<String, Object>> getStudentList(@Body RequestBody requestBody);

    //get student list
    @POST("studentAttendance")
    Call<Map<String, Object>> doStudentAttendance(@Body RequestBody requestBody);

    //getMonthlyPlannerClass
    @GET("getClassData")
    Call<Map<String, Object>> getMonthlyPlannerClass();

    //get Monthly planner data
    //get student list
    @POST("monthlyTimeTable")
    Call<Map<String, Object>> getMonthlyPlanner(@Body RequestBody requestBody);
    //auth
    //getYearlyPlannerClass
    @GET("getClassData")
    Call<Map<String, Object>> getYearlyPlannerClass();

    //getYearlyPlannerMonth
    @GET("getTimetableMonth")
    Call<Map<String, Object>> getYearlyPlannerMonth();

    //get student list
    @POST("yearlyTimeTable")
    Call<Map<String, Object>> getYearlyPlanner(@Body RequestBody requestBody);

    //getYearlyEvent
    @GET("yearlyEvent")
    Call<Map<String, Object>> getYearlyEvents();

    //get notification
    @GET("notification")
    Call<Map<String, Object>> getNotification();

    //get notification
    @GET("achievement")
    Call<Map<String, Object>> getAchievements();

    //get notification
    @GET("studentAssessment")
    Call<Map<String, Object>> getClassDivision();

    @POST("assessmentStudentList")
    Call<Map<String, Object>> doAssessmentList(@Body RequestBody requestBody);

    @POST("assessmentStudentActivity")
    Call<Map<String, Object>> doAssessmentActivity(@Body RequestBody requestBody);

    @POST("assessmentGraph")
    Call<Map<String, Object>> doAssessmentBarchart(@Body RequestBody requestBody);

    @GET("equipmentList")
    Call<Map<String, Object>> getEquipment();

    @GET("sportItemList")
    Call<Map<String, Object>> getEquipmentList();

    @POST("addEquipmentData")
    Call<Map<String, Object>> doAddEquipment(@Body RequestBody requestBody);

    @GET("postSchoolActivity")
    Call<Map<String, Object>> getSchoolPostActivity();

    @GET("photoGallery")
    Call<Map<String, Object>> getTrainerGallery();

    @POST("leaveApplication")
    Call<Map<String, Object>> doTrainerLeave(@Body RequestBody requestBody);

    @GET("trainerLeaveApplication")
    Call<Map<String, Object>> getTrainerLeaveApplication();

    @GET("trainerList")
    Call<Map<String, Object>> getTrainerList();

    @POST("trainerAttendanceAllMonth")
    Call<Map<String, Object>> getTrainerAttendanceBar(@Body RequestBody requestBody);

    @POST("studentAttendanceAllMonth")
    Call<Map<String, Object>> getStudentAttendanceBar(@Body RequestBody requestBody);

    @POST("trainerAttendancePerMonth")
    Call<Map<String, Object>> getTrainerAttendanceCal(@Body RequestBody requestBody);

    //daily time table
    @POST("trainerDailyDetails")
    Call<Map<String, Object>> getTrainerDailyReport(@Body RequestBody requestBody);

    @POST("studentAttendancePerMonth")
    Call<Map<String, Object>> getStudentAttendanceCal(@Body RequestBody requestBody);

    @POST("studentSessionList")
    Call<Map<String, Object>> getSession(@Body RequestBody requestBody);

    //daily time table
    @POST("parentVerification")
    Call<Map<String, Object>> doVerify(@Body RequestBody requestBody);

    @POST("parentRegistration")
    Call<Map<String, Object>> doReg(@Body RequestBody requestBody);

    @GET("getChildList")
    Call<Map<String, Object>> getChildList();

    @GET("parentGallery")
    Call<Map<String, Object>> getParentGallery();

    @GET("parentYearlyEvent")
    Call<Map<String, Object>> getParentEvent();

    @GET("parentNotification")
    Call<Map<String, Object>> getParentNotification();

    @GET("parentAchievement")
    Call<Map<String, Object>> getParentAchievements();


    @GET("childAttendanceAllMonth")
    Call<Map<String, Object>> getParentStudentAttendanceBar();

    @POST("childAttendancePerMonth")
    Call<Map<String, Object>> getParentAttendanceCal(@Body RequestBody requestBody);

    @POST("childSessionList")
    Call<Map<String, Object>> getParentSession(@Body RequestBody requestBody);

    @POST("parentFeedback")
    Call<Map<String, Object>> doFeedback(@Body RequestBody requestBody);

    @POST("authuser")
    Call<Map<String, Object>> authuser();


    /* add photo gallary for trainer */
    @POST("addPhotoGallery")
    Call<TrainerGallaryUploadModel> uploadTrainerGallary(@Body RequestBody requestBody);

    /* upload parent profile */
    @POST("addParentImage")
    Call<ParentProfiledModel> uploadParnetProfile(@Body RequestBody requestBody);


}