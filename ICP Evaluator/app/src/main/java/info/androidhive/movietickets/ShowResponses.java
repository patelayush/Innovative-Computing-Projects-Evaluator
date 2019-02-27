package info.androidhive.movietickets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.awt.font.TextAttribute;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowResponses extends AppCompatActivity {

    public static SubmittingResponse past_response;
    APIInterface restapi =
            APIClient.getClient().create(APIInterface.class);
    private Button button;
    private RecyclerView recyclerView;
    ArrayList<String> ques;
    private PastAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_responses);

        if(getIntent().getSerializableExtra("team")!=null){
            past_response = (SubmittingResponse) getIntent().getSerializableExtra("team");
        }

        Call<ArrayList<QuestionsAPI>> call = restapi.getAllQuestions();
        call.enqueue(new Callback<ArrayList<QuestionsAPI>>() {
            @Override
            public void onResponse(Call<ArrayList<QuestionsAPI>> call, Response<ArrayList<QuestionsAPI>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    BottomNav.flag = true;
                    ArrayList<QuestionsAPI> questionsAPIS = response.body();
                    ques = new ArrayList<>();
                    for (QuestionsAPI q : questionsAPIS) {
                        ques.add(q.getQuestion());
                    }
                    recyclerView = findViewById(R.id.recycler_view);
                    mAdapter = new PastAdapter(ques);
                    TextView tv = findViewById(R.id.teamname);
                    tv.setText("Survey Response for "+ past_response.getExhibit());

                    for (int i = 0; i < ques.size(); i++) {
                        Survey.positions.add(i,"1");
                    }

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<QuestionsAPI>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        BottomNav.flag=false;
        finish();
    }
}
