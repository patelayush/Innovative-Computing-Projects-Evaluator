package info.androidhive.movietickets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.MyViewHolder> {

    private List<ScoreboardData> moviesList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView avg;
        public View itemView;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvTitle);

            avg = (TextView) view.findViewById(R.id.tvAvg);
            itemView = view;

        }
    }


    public ScoreAdapter(List<ScoreboardData> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scoreboard_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String movie = moviesList.get(position).getTeamname();
        float avg = moviesList.get(position).getAverage();
        holder.title.setText(movie);
        holder.avg.setText(Math.round(avg) +"%");

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
