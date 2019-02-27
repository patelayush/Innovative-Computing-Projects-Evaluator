package info.androidhive.movietickets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class PastAdapter extends RecyclerView.Adapter<PastAdapter.MyViewHolder> {

    private List<String> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RadioGroup rg ;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.ques_title);
            rg = (RadioGroup) view.findViewById(R.id.radio_group_1);

        }
    }


    public PastAdapter(List<String> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ques_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String movie = moviesList.get(position);
        holder.title.setText(movie);
        for(int i = 0; i < holder.rg.getChildCount(); i++){
            ((RadioButton)holder.rg.getChildAt(i)).setEnabled(false);
        }

        String[] scors = ShowResponses.past_response.getScores();
        switch(scors[position]){
            case "4":holder.rg.check(R.id.radio_button_1);
                    break;
            case "3":holder.rg.check(R.id.radio_button_2);
                break;
            case "2":holder.rg.check(R.id.radio_button_3);
                break;
            case "1":holder.rg.check(R.id.radio_button_4);
                break;
            case "0":holder.rg.check(R.id.radio_button_5);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
