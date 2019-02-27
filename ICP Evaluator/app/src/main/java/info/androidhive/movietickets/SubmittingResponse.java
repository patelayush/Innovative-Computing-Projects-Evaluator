package info.androidhive.movietickets;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SubmittingResponse implements Serializable {
    String[] scores;
    String evaluator;
    String exhibit;
    String _id;
    String __V;

    public String get_id() {
        return _id;
    }



    public String get__V() {
        return __V;
    }



    public String[] getScores() {
        return scores;
    }

    public void setScores(String[] scores) {
        this.scores = scores;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    public String getExhibit() {
        return exhibit;
    }

    public void setExhibit(String exhibit) {
        this.exhibit = exhibit;
    }

    public float getAverage(){
        float sum = 0.0f;
        for(int i =0; i<this.scores.length;i++){
            sum+=Integer.parseInt(this.scores[i]);
        }
        return sum/this.scores.length;
    }


}
