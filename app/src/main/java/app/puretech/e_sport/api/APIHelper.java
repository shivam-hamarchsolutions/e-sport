package app.puretech.e_sport.api;


import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import app.puretech.e_sport.App;
import app.puretech.e_sport.model.UserDTO;
import app.puretech.e_sport.utill.DataManager;
import app.puretech.e_sport.utill.Logger;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Dinesh on 16-02-2018.
 */

public class APIHelper {
    public interface OnRequestComplete<T> {
        void onSuccess(T object);
        void onFailure(APIResponse.Error error);
    }
    private static APIHelper instance;
    private APIService apiService;
    private App app;
    private UserDTO userDTO;
    private APIResponse.Error getServerError() {
        return new APIResponse.Error(-1, "We apologize for the inconvenience. Please try again later.");
    }
    public static synchronized APIHelper init(App app) {
        if (null == instance) {
            instance = new APIHelper();
            instance.setApplication(app);
            instance.initAPIService();
        }
        return instance;
    }
    public void setApplication(App application) {
        this.app = application;
    }
    private void initAPIService() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        //loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // Define the interceptor, add authentication headers
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(loggingInterceptor);
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                requestBuilder.addHeader("content-type", "application/json");
                requestBuilder.method(original.method(), original.body());
                userDTO=app.getPreferences().getUser();
                if (userDTO != null && userDTO.getTokenId() != null) {
                    requestBuilder.addHeader("Authorization", userDTO.getTokenId());
                }
                Request request = requestBuilder.build();
                if (Logger.isDebugEnabled()) {
                    app.getLogger().info("Request URL >> " + request.urlString());
                }
                return chain.proceed(request);
            }
        });
        client.setReadTimeout(5, TimeUnit.SECONDS);
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(DataManager.url)
                .build();
        apiService = retrofit.create(APIService.class);
    }
    public APIService getApiService() {
        return apiService;
    }
}