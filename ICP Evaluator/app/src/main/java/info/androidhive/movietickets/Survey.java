package info.androidhive.movietickets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Survey.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Survey#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Survey extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static ArrayList<String> positions = new ArrayList<>();
    APIInterface restapi =
            APIClient.getClient().create(APIInterface.class);
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView textView;
    private Button button;
    private RecyclerView recyclerView;
    ArrayList<String> ques;
    private MoviesAdapter mAdapter;
    private OnFragmentInteractionListener mListener;

    public Survey() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Survey.
     */
    // TODO: Rename and change types and number of parameters
    public static Survey newInstance(String param1, String param2) {
        Survey fragment = new Survey();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_survey_fragment, container, false);

        LinearLayout llayout = view.findViewById(R.id.llayout);
        RelativeLayout teamlayout = view.findViewById(R.id.teamlayout);
        TextView evalname = view.findViewById(R.id.evalname);
        TextView teamname = view.findViewById(R.id.teamname);


        Button submitbutton = view.findViewById(R.id.submitresponse);
        button = view.findViewById(R.id.btn_scan);

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SubmittingResponse res = new SubmittingResponse();
                String pos[] = new String[positions.size()];
                for(int i =0; i<ques.size();i++){
                    pos[i] = positions.get(i);
                }
                res.evaluator = ScanActivity.evaluatorname;
                res.exhibit = ScanActivity.teamname;
                res.scores = pos;

                System.out.println("response:" + res.scores.toString());
                Call<ResponseBody> responseBodyCall = restapi.submitresponse(res);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()  && response.body()!=null){
                            Toast.makeText(getContext(),"submitted response",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(),"Couldn't submit response",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println(t.getLocalizedMessage());
                    }
                });

                Toast.makeText(getContext(), "responses : ", Toast.LENGTH_SHORT).show();
                for(String r:pos){
                    System.out.println(r);
                }
                button.setVisibility(View.VISIBLE);
                llayout.setVisibility(View.VISIBLE);
                teamlayout.setVisibility(View.GONE);
                BottomNav.flag = false;
                evalname.setText("Welcome "+ScanActivity.evaluatorname);
            }
        });
        //System.out.println("yaha aaya "+ mParam1);
     if(mParam1.equals("team")){
         if(BottomNav.flag) {
             Call<ArrayList<QuestionsAPI>> call = restapi.getAllQuestions();
             call.enqueue(new Callback<ArrayList<QuestionsAPI>>() {
                 @Override
                 public void onResponse(Call<ArrayList<QuestionsAPI>> call, Response<ArrayList<QuestionsAPI>> response) {
                     if (response.isSuccessful() && response.body() != null) {
                         button.setVisibility(View.GONE);
                         llayout.setVisibility(View.GONE);
                         teamlayout.setVisibility(View.VISIBLE);
                         teamname.setText("Questions for " + mParam2);

                         BottomNav.flag = true;
                         ArrayList<QuestionsAPI> questionsAPIS = response.body();
                         ques = new ArrayList<>();
                         for (QuestionsAPI q : questionsAPIS) {
                             ques.add(q.getQuestion());
                         }
                         recyclerView = view.findViewById(R.id.recycler_view);
                         mAdapter = new MoviesAdapter(ques);


                         positions = new ArrayList<>();
                         for (int i = 0; i < ques.size(); i++) {
                             Survey.positions.add(i,"4");
                         }

                         RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
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
            }
            else{
            button.setVisibility(View.VISIBLE);
            llayout.setVisibility(View.VISIBLE);
            teamlayout.setVisibility(View.GONE);
            evalname.setText("Welcome "+mParam2);

        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScanActivity.class);
                intent.putExtra("team",true);
                startActivityForResult(intent,101);

            }
        });

        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.team_menu, menu);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
