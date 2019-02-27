package info.androidhive.movietickets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

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


    public MoviesAdapter(List<String> moviesList) {
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

        holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radio_button_1:
                        Survey.positions.set(position,"4");
                        break;

                    case R.id.radio_button_2:
                        Survey.positions.set(position,"3");
                        break;

                    case R.id.radio_button_3:
                        Survey.positions.set(position,"2");
                        break;

                    case R.id.radio_button_4:
                        Survey.positions.set(position,"1");
                        break;

                    case R.id.radio_button_5:
                        Survey.positions.set(position,"0");
                        break;

                }
            }
        });
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
