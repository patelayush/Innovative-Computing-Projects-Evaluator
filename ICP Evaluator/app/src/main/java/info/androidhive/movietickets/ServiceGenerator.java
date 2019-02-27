package info.androidhive.movietickets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mattbryant on 6/8/17.
 */

public class ServiceGenerator {

    // BASE URL Constants
/*
    private static final String BASE_URL = "https://www.timetap.com/businessWeb/rest/"; // Production Server
    private static final String BASE_URL_BO = "https://bo.checkappointments.com/businessWeb/rest/"; // Test Server
    private static final String BASE_URL_DEV = "https://backoffice.devtap.us/businessWeb/rest/"; // Dev Environment
*/

    private static final String BASE_URL = "https://api.timetap.com/bolive/"; // Production Server
    private static final String BASE_URL_BO = "http://ec2-13-58-41-200.us-east-2.compute.amazonaws.com:3000/"; // Test Server
    private static final String BASE_URL_DEV = "https://api.timetap.com/bodev/"; // Dev Environment


    //Should not be referenced as static. Might cause memory leak.
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY); // TODO: Set to Level.NONE before release -- highest priority
    private static Dispatcher dispatcher = new Dispatcher();
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();
                Response response = chain.proceed(request);

                //Unauthorised access. Indicates expired token.
                if (response.code() == 401) {
                    Intent intent = new Intent(getmContext(), MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("login", true);
                    getmContext().startActivity(intent);
                    ((Activity) getmContext()).finish();
                    return response;
                }

                return response;
            })
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .dispatcher(dispatcher);

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            //.baseUrl(BASE_URL_DEV) // Pointing to Dev
            .baseUrl(BASE_URL_BO) // Pointing to BO
            //.baseUrl(BASE_URL) // TODO: Set URL to Production before release -- highest priority
            .addConverterFactory(GsonConverterFactory.create(gson));
    private static Retrofit retrofit = builder.client(httpClient.build()).build();

    private static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        ServiceGenerator.mContext = mContext;
    }

    /**
     * Function to get dispatcher.
     *
     * @return Dispatcher, helps to cancel calls when fragment ends.
     */
    public static Dispatcher getDispatcher() {
        return dispatcher;
    }


    /* package */
    static Retrofit getRetrofit() {
        return retrofit;
    }

    public static <S> S createService(Class<S> retrofitAPI) {
        return retrofit.create(retrofitAPI);
    }



}
