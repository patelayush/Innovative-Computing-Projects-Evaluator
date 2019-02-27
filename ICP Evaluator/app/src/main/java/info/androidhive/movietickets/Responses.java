package info.androidhive.movietickets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Responses.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Responses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Responses extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    APIInterface restapi =
            APIClient.getClient().create(APIInterface.class);
    static ArrayList<SubmittingResponse> responses;
    ArrayList<String> teamnames;
    static Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    static void showResponse(SubmittingResponse response){
        Intent intent = new Intent(context,ShowResponses.class);
        intent.putExtra("team",response);
        context.startActivity(intent);

    }
    public Responses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Responses.
     */
    // TODO: Rename and change types and number of parameters
    public static Responses newInstance(String param1, String param2) {
        Responses fragment = new Responses();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_responses, container, false);
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerview);
        context = this.getContext();
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        Singltonvar var = new Singltonvar();
        var.setEvaluator(ScanActivity.evaluatorname);
        Call<ArrayList<SubmittingResponse>> res = restapi.getpastresponse(var);
        res.enqueue(new Callback<ArrayList<SubmittingResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SubmittingResponse>> call, Response<ArrayList<SubmittingResponse>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    responses = response.body();
                    teamnames = new ArrayList<>();
                    for(SubmittingResponse r :responses){
                        teamnames.add(r.getExhibit());
                    }
                    ResponseAdapter myAdapter = new ResponseAdapter(teamnames);
                    mRecyclerView.setAdapter(myAdapter);


                }
            }

            @Override
            public void onFailure(Call<ArrayList<SubmittingResponse>> call, Throwable t) {

            }
        });
        return view;
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
