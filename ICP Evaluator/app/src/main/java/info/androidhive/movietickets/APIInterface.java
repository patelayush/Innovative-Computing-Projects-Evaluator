package info.androidhive.movietickets;


import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
 * Created by ayush on 7/13/2018.
 */

public interface APIInterface {


    @POST("user_login")
    Call<UserResponse> getUser(@Body User user,@Header("Authorization") String auth);

    @GET("get_all_questions")
    Call<ArrayList<QuestionsAPI>> getAllQuestions();


    @GET("get_teams")
    Call<ArrayList<Team>> getAllTeams();

    @POST("add_score")
    Call<ResponseBody> submitresponse(@Body SubmittingResponse res);

    @POST("get_evaluator_response")
    Call<ArrayList<SubmittingResponse>> getpastresponse(@Body Singltonvar res);

    @GET("get_all_scores")
    Call<ArrayList<SubmittingResponse>> getallscores();

}
