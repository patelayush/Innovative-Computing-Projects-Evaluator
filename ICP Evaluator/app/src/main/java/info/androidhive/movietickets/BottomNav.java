package info.androidhive.movietickets;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomNav extends AppCompatActivity implements Survey.OnFragmentInteractionListener, Responses.OnFragmentInteractionListener, Scoreboard.OnFragmentInteractionListener {
    APIInterface restapi =
            APIClient.getClient().create(APIInterface.class);
    private TextView mTextMessage;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private int currentBottomMenuID;
    protected static boolean flag = false;
    Fragment fragment = null;

    boolean isSurvey = false;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Disables reloading of same fragment.

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            String TAG = null;
            boolean isNew = false;

            if (item.getItemId() == currentBottomMenuID) return true;
            currentBottomMenuID = item.getItemId();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //toolbar.setTitle(R.string.title_appointments);
                    TAG = "Survey";
                    invalidateOptionsMenu();
                    fragment = getSupportFragmentManager().findFragmentByTag(TAG);
                    if(fragment!=null) isSurvey = true;
                    if (fragment == null) {
                        isSurvey = true;
                        fragment = Survey.newInstance("survey1","survey1");
                        isNew = true;
                    }
                    break;
                case R.id.navigation_dashboard:
                    TAG = "Responses";
                    isSurvey = false;
                    invalidateOptionsMenu();
                    fragment = getSupportFragmentManager().findFragmentByTag(TAG);
                    if (fragment == null) {
                        fragment = Responses.newInstance("Responses","Responses");
                        isNew = true;
                    }
                    break;
                case R.id.navigation_notifications:
                    TAG = "Scoreboard";
                    isSurvey = false;
                    invalidateOptionsMenu();
                    fragment = getSupportFragmentManager().findFragmentByTag(TAG);
                    if (fragment == null) {
                        fragment = Scoreboard.newInstance("Scoreboard","Scoreboard");
                        isNew = true;
                    }
                    break;
            }
            if(!flag) {
                if (isNew)
                    fragmentManager.beginTransaction()
                            .replace(R.id.bottom_nav_container, fragment, TAG)
                            .addToBackStack(BACK_STACK_ROOT_TAG)
                            .commit();
                else fragmentManager.beginTransaction()
                        .replace(R.id.bottom_nav_container, fragment, TAG)
                        .disallowAddToBackStack()
                        .commit();
            }
            return true;



        }


    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isSurvey) {
            getMenuInflater().inflate(R.menu.team_menu, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.null_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(isSurvey) {
            showteams();
        }
        return true;
    }

    private void showteams() {
        Call<ArrayList<Team>> call = restapi.getAllTeams();
        ArrayList<String> teamnames = new ArrayList<>();
        call.enqueue(new Callback<ArrayList<Team>>() {
            @Override
            public void onResponse(Call<ArrayList<Team>> call, Response<ArrayList<Team>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    for(Team t : response.body()){
                        teamnames.add(t.getUsername());
                    }
                    new MaterialDialog.Builder(BottomNav.this)
                            .title("Teams List")
                            .titleColorRes(R.color.colorPrimaryDark)
                            .dividerColorRes(R.color.colorAccent)
                            .items(teamnames)
                            .itemsCallbackSingleChoice(-1,(dialog, view, which, text) -> {
                                if (!flag) {


                                    fragment = Survey.newInstance("team", teamnames.get(which));
                                    isSurvey = true;
                                    flag = true;
                                    ScanActivity.teamname = teamnames.get(which);
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.bottom_nav_container, fragment, "Survey")
                                            .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                            .addToBackStack(BACK_STACK_ROOT_TAG)
                                            .commit();
                                }
                                return true;
                            }).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Wrror fetching teams",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Team>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment fragment = null;
        if(getIntent().getStringExtra("team")!=null) {
            fragment = Survey.newInstance("team",getIntent().getStringExtra("team") );
        }else {
            fragment = Survey.newInstance("evaluator", getIntent().getStringExtra("evaluator"));
        }

        isSurvey = true;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_nav_container, fragment, "Survey")
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();




        currentBottomMenuID = navigation.getSelectedItemId();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 101 && resultCode ==102){
            Toast.makeText(getApplicationContext(),"finisih me aaya",Toast.LENGTH_SHORT).show();
            System.out.println("yaha aaya");
        }
        else if(requestCode == 101 && resultCode ==103){
            System.out.println("yaha aaya finsh me");
            Toast.makeText(getApplicationContext(),"Invalid Qr code. Scan Again",Toast.LENGTH_SHORT).show();
           }
    }

    @Override
    public void onBackPressed() {

    }
}
